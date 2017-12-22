package threecats.zhang.domoment.DataStructures;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import threecats.zhang.domoment.ENUM.DisplayState;
import threecats.zhang.domoment.ENUM.TaskBasePoint;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.ENUM.GroupListDisplayType;
import threecats.zhang.domoment.ENUM.GroupListType;
import threecats.zhang.domoment.Helper.UIHelper;
import threecats.zhang.domoment.adapter.RecyclerViewAdapterBase;

/**
 * 由 zhang 于 2017/8/3 创建
 */

public abstract class GroupListBase {

    private String title = "";
    protected RecyclerView GroupListRecyclerView;
    protected RecyclerViewAdapterBase GroupListAdapter;
    public TimeSeries timeSeries;
    public TaskBasePoint taskBasePoint;
    protected GroupListType selfType ;
    protected CategoryBase parent;
    protected Calendar timePoint;
    protected GroupListDisplayType displayType;
    protected boolean isItemChecked;

    private boolean isInit;

    //用于RecyclerView使用的条目列表
    private List<RecyclerViewItem> recyclerViewItems;
    //分组中每个组项的列表
    private List<GroupBase> groups;

    public GroupListBase(){
        isInit = false;
        isItemChecked = false;
        GroupListAdapter = null;
        displayType = GroupListDisplayType.Simple;
        recyclerViewItems = new ArrayList<>();
        groups = new ArrayList<>();
        buildTimePoint();
    }
    public abstract boolean inGroupList(TaskItem task);
    public abstract void buildTimePoint();
    protected abstract void buildGroups();

    public GroupListType getType(){
        return selfType;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public CategoryBase getParent(){
        return parent;
    }
    public List<GroupBase> getGroups(){
        return groups;
    }
    protected GroupListType getSelfType(){
        return selfType;
    }
    public TimeSeries getTimeSeries(){
        return timeSeries;
    }

    //下面是分类列表的管理
    public void addGroup(GroupBase group){
        if (groups.size() > 0) {
            GroupBase lastGroup = groups.get(groups.size() - 1);
            lastGroup.setNextGroup(group);
            group.setPreviousGroup(lastGroup);
        }
        groups.add(group);
        addGroupInListItems(group);
        CalculatorTitleSite();
    }
    public void checkArea(){
        for (GroupBase group : groups) {
            group.checkArea();
        }
    }
    private void CalculatorTitleSite(){
        int site = 0;
        for(GroupBase group : groups){
            if (group.State == DisplayState.Show) {
                group.SiteID = site;
                site += group.getTaskCount()+1;
            }
        }
    }
    public int getGroupCount(){
        return groups.size();
    }
    public int getTaskCount(){
        int count = 0;
        for (GroupBase group : groups) {
            count += group.getTaskCount();
        }
        return count;
    }
    public void sortGroup(GroupBase group){
        group.sort();
        int I = group.SiteID +1;
        for (TaskItem task : group.getTasks()) {
            recyclerViewItems.set(I++,task);
        }
        if (isBindRecyclerView()) GroupListAdapter.notifyItemRangeChanged(group.SiteID + 1,group.getTaskCount());
        //GroupListAdapter.notifyDataSetChanged();
    }
    public void sortAllGroup(){
        for (GroupBase group : groups) {
            sortGroup(group);
        }
    }
    private void hideGroup(GroupBase group){
        if (group.getTaskCount() > 0) return;
        if (group.SiteID > recyclerViewItems.size() - 1) {
            UIHelper.Toast("Hide Group : group.siteID > size()");
            return;
        }
        if (group == recyclerViewItems.get(group.SiteID)) {
            recyclerViewItems.remove(group.SiteID);
            if (isBindRecyclerView()) GroupListAdapter.notifyItemRemoved(group.SiteID);
            group.State = DisplayState.Hide;
            CalculatorTitleSite();
        } else {
            int a= group.SiteID;
            GroupBase g = (GroupBase) recyclerViewItems.get(group.SiteID);
            UIHelper.Toast("HideGroup出现错误");
        }
        updateTitleMessage();
    }

    private void activeGroup(GroupBase group){
        if (group.State == DisplayState.Show || group.State == DisplayState.Fold) return;
        int nextGroupSite = getNextGroupSite(group);
        if (nextGroupSite >= 0) {
            group.SiteID = nextGroupSite;
            recyclerViewItems.add(nextGroupSite, group);
            if (isBindRecyclerView()) GroupListAdapter.notifyItemInserted(nextGroupSite);
        } else {
            //如果没有下一组，就表明当前组就是最后一组，不用插入，只需要添加
            recyclerViewItems.add(group);
            group.SiteID = recyclerViewItems.size() - 1;
            if (isBindRecyclerView()) GroupListAdapter.notifyDataSetChanged();
        }
        group.State = DisplayState.Show;
    }

    private int getNextGroupSite(GroupBase group){
        int nextGroupSite = -1;
        boolean beforeGroup = true;
        for (GroupBase currentGroup : groups) {
            if (beforeGroup) {
                if (currentGroup == group) beforeGroup = false;
                continue;
            }
            if (currentGroup.State == DisplayState.Show) {
                nextGroupSite = currentGroup.SiteID;
                break;
            }
        }
        return nextGroupSite;
    }

    //分组列表的任务项管理，在这里管理分组项里的任务可以执行自动进入分组、重新调整分组、从分组中删除的操作
    public void addTask(TaskItem task){
        for(GroupBase group : groups){
            if (group.inGroup(task)){
                if (group.State == DisplayState.Hide) {
                    activeGroup(group);
                }
                int site = group.addTask(task);
                addTaskToListItems(group, site, task);  //在RecyclerView列表中加入条目
            }
        }
        CalculatorTitleSite();
    }

    public void removeTask(TaskItem task){
        for (GroupBase group : groups) {
            if (group.inGroup(task)) {
                removeTaskFromGroup(group, task);
                break;
            }
        }
    }

    public void removeTask(GroupBase group, TaskItem task){
        removeTaskFromGroup(group, task);
    }

    private void removeTaskFromGroup(GroupBase group, TaskItem task){
        //task.getParentGroup().remove(group);
        //如果采用for(GroupBase group : Groups) 遍历调用
        //这里不能删除父类group的引用，不然无法调用遍历寻找下一个父类引用
        //目前采用的方法是另外new一个group的ArrayList,通过for(GroupBase group : Groups)将group加入进去
        //然后遍历新建的ArrayList操作
        task.getParentGroups().remove(group);
        int site = group.removeTask(task);
        if (site >= 0){
            removeTaskFromListItems(group, site, task);
            if (group.getTaskCount() == 0) {
                hideGroup(group);
            }
            CalculatorTitleSite();
        }
    }

    //下面是供RecycleView使用的列表(displayItems)的管理
    public List<RecyclerViewItem> getRecyclerViewItems(){
        return recyclerViewItems;
    }
    private void addGroupInListItems(GroupBase group){
        if (group.State == DisplayState.Hide) return;
        recyclerViewItems.add(group);
        recyclerViewItems.addAll(group.getTasks());
    }
    private void addTaskToListItems(GroupBase group, int position, TaskItem task){
        //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
        int site = group.SiteID + position + 1;
        //根据上面计算的位置执行插入操作
        recyclerViewItems.add(site, task);
        //执行插入动画
        if (isBindRecyclerView()) GroupListAdapter.notifyItemInserted(site);
        //测试用，更新显示组头的位置序号
        //updateTitleMessage();
    }
    private void removeTaskFromListItems(GroupBase group, int position, TaskItem task){

        int site = group.SiteID + position + 1;  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
        TaskItem ntask = (TaskItem) recyclerViewItems.get(site);
        if (ntask == task) {
            recyclerViewItems.remove(site);
            if (isBindRecyclerView()) GroupListAdapter.notifyItemRemoved(site);
            //CalculatorTitleSite();
        } else {
            UIHelper.Toast(group.getParent().getParent().getTitle()+"Task与ListItems不符");
        }
        //测试用，更新显示组头的位置序号
        //updateTitleMessage();
    }

    //测试用，更新显示组头的位置序号
    public void updateTitleMessage(){
        for (GroupBase g : groups) {
            if (g.State == DisplayState.Show) {
                if (isBindRecyclerView()) GroupListAdapter.notifyItemChanged(g.SiteID);
            }
        }
    }

    public void updateTaskDisplay(GroupBase group, TaskItem task){
        int site = group.getTasks().indexOf(task) + group.SiteID + 1;
        if (isBindRecyclerView()) GroupListAdapter.notifyItemChanged(site);
    }

    public void bindRecyclerView(RecyclerView recyclerView, RecyclerViewAdapterBase adapter, View view){
        //String className = adapter.getClass().getSimpleName();
        //if (className.contains("NoDateAdapter")) viewAdapterBase = (RecyclerViewAdapterBase)adapter;

        adapter.setGroupList(this);
        GroupListAdapter = adapter;
        GroupListRecyclerView = recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setAdapter(GroupListAdapter);
        //if (isInit) recyclerView.setAdapter(GroupListAdapter);
        recyclerView.setAdapter(GroupListAdapter);
    }

    public void unBind(){
        GroupListAdapter = null;
        GroupListRecyclerView = null;
    }

    public void bindDatas(){
        if (GroupListRecyclerView != null) {
            GroupListRecyclerView.setAdapter(GroupListAdapter);
        }
        isInit = true;
    }

    public void setItemChecked(boolean checked){
        isItemChecked = checked;
        GroupListAdapter.isChecked = checked;
        GroupListAdapter.notifyDataSetChanged();
        //Toast.makeText(App.getContext(),"<"+className+">",Toast.LENGTH_SHORT).show();
    }

    public boolean isItemChecked(){
        return isItemChecked;
    }

    private boolean isBindRecyclerView(){
        return GroupListAdapter != null;
    }
}
