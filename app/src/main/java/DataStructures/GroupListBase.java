package DataStructures;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ENUM.DisplayState;
import ENUM.TaskBasePoint;
import ENUM.TimeSeries;
import ENUM.GroupListDisplayType;
import ENUM.GroupListType;
import adapter.RecyclerViewAdapterBase;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;

/**
 * Created by zhang on 2017/8/3.
 */

public abstract class GroupListBase {

    protected DateTimeHelper DateTime = DoMoment.getDateTime();
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
    private List<ListItemBase> recyclerViewItems;
    //分组中每个组项的列表
    private List<GroupBase> groups;

    public GroupListBase(){
        isInit = false;
        isItemChecked = false;
        GroupListAdapter = null;
        displayType = GroupListDisplayType.Simple;
        recyclerViewItems = new ArrayList<>();
        groups = new ArrayList<>();
        BuildTimePoint();
    }
    public abstract boolean InGroupList(Task task);
    public abstract void BuildTimePoint();
    protected abstract void BuildGroups();

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
    public void AddGroup(GroupBase group){
        if (groups.size() > 0) {
            GroupBase lastGroup = groups.get(groups.size() - 1);
            lastGroup.setNextGroup(group);
            group.setPreviousGroup(lastGroup);
        }
        groups.add(group);
        AddGroupInListItems(group);
        CalculatorTitleSite();
    }
    public void CheckArea(){
        for (GroupBase group : groups) {
            group.CheckArea();
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
    public void SortGroup(GroupBase group){
        group.Sort();
        int I = group.SiteID +1;
        for (Task task : group.getTasks()) {
            recyclerViewItems.set(I++,task);
        }
        if (isBindRecyclerView()) GroupListAdapter.notifyItemRangeChanged(group.SiteID + 1,group.getTaskCount());
        //GroupListAdapter.notifyDataSetChanged();
    }
    public void SortAllGroup(){
        for (GroupBase group : groups) {
            SortGroup(group);
        }
    }
    private void HideGroup(GroupBase group){
//        if (group.getTaskCount() > 0) return;
//        int site = recyclerViewItems.indexOf(group);
//        if (site >=0 ) {
//            recyclerViewItems.remove(site);
//            if (isBindRecyclerView()) GroupListAdapter.notifyItemRemoved(site);
//        }

        if (group.SiteID > recyclerViewItems.size() - 1) {
            DoMoment.Toast("Hide Group : group.siteID > size()");
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
            DoMoment.Toast("HideGroup出现错误");
        }
        UpdateTitleMessage();
    }

    private void ActiveGroup(GroupBase group){
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
    public boolean AddTask(Task task){
        boolean isOK = false;
        for(GroupBase group : groups){
            if (group.InGroup(task)){
                if (group.State == DisplayState.Hide) {
                    ActiveGroup(group);
                }
                int site = group.AddTask(task);
                AddTaskToListItems(group, site, task);  //在RecyclerView列表中加入条目
                isOK = true;
            }
        }
        return isOK;
    }
    public boolean InsertTask(Task task){
        boolean isOK = false;
        for(GroupBase group : groups){
            if (group.InGroup(task)){
                if (group.State == DisplayState.Hide) {
                    ActiveGroup(group);
                }
                int site = group.InsertTask(task);
                //int site = group.AddTask(task);
                AddTaskToListItems(group, site, task);  //在RecyclerView列表中加入条目
                //SortGroup(group);
                isOK = true;
            }
        }
        return isOK;
    }
    public void RemoveTask(Task task){
        for (GroupBase group : groups) {
            if (group.InGroup(task)) {
                RemoveTaskFromGroup(group, task);
                break;
            }
        }
    }

    public void RemoveTask(GroupBase group, Task task){
        RemoveTaskFromGroup(group, task);
    }

    private void RemoveTaskFromGroup(GroupBase group, Task task){
        //task.getParentGroup().remove(group);  这里不能删除父类group的引用，不然无法调用遍历寻找下一个父类引用
        int site = group.RemoveTask(task);
        if (site >= 0){
            RemoveTaskFromListItems(group, site, task);
            CalculatorTitleSite();
        }
        if (group.getTaskCount() == 0) {
            HideGroup(group);
            CalculatorTitleSite();
        }
    }

    //下面是供RecycleView使用的列表(displayItems)的管理
    public List<ListItemBase> getRecyclerViewItems(){
        return recyclerViewItems;
    }
    private void AddGroupInListItems(GroupBase group){
        if (group.State == DisplayState.Hide) return;
        recyclerViewItems.add(group);
        recyclerViewItems.addAll(group.getTasks());
    }
    private void AddTaskToListItems(GroupBase group, int position, Task task){
        if (getNextGroupSite(group) >= 0) {
            //根据任务在分组中的位置计算出任务在RecyclerView列表中的位置
            int site = group.SiteID + position;
            //根据上面计算的位置执行插入操作
            recyclerViewItems.add(site, task);
            //执行插入动画
            if (isBindRecyclerView()) GroupListAdapter.notifyItemInserted(site);
            CalculatorTitleSite();
        } else {
            recyclerViewItems.add(task);
            if (isBindRecyclerView()) GroupListAdapter.notifyDataSetChanged();
        }
        //测试用，更新显示组头的位置序号
        UpdateTitleMessage();
    }
    private void RemoveTaskFromListItems(GroupBase group, int position, Task task){
        int site = recyclerViewItems.indexOf(task);
        if (site>=0) {
            recyclerViewItems.remove(site);
            if (isBindRecyclerView()) GroupListAdapter.notifyItemRemoved(site);
        }
//        CalculatorTitleSite();
//        int site = group.SiteID + position + 1;  //从分组中返回的位置是不包括组头的，就是说分组中列表是从0算起的所以+1
//        Task ntask = (Task) recyclerViewItems.get(site);
//        if (ntask == task) {
//            recyclerViewItems.remove(site);
//            if (isBindRecyclerView()) GroupListAdapter.notifyItemRemoved(site);
//            CalculatorTitleSite();
//        } else {

//            DoMoment.Toast(group.getParent().getParent().getTitle()+"Task与ListItems不符");
//        }


        //测试用，更新显示组头的位置序号
        UpdateTitleMessage();
    }

    //测试用，更新显示组头的位置序号
    public void UpdateTitleMessage(){
        for (GroupBase g : groups) {
            if (g.State == DisplayState.Show) {
                if (isBindRecyclerView()) GroupListAdapter.notifyItemChanged(g.SiteID);
            }
        }
    }

    public void UpdateTaskDisplay(GroupBase group, Task task){
        int site = group.getTasks().indexOf(task) + group.SiteID + 1;
        if (isBindRecyclerView()) GroupListAdapter.notifyItemChanged(site);
    }

    public void BindRecyclerView(RecyclerView recyclerView, RecyclerViewAdapterBase adapter, View view){
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

    public void UnBind(){
        GroupListAdapter = null;
        GroupListRecyclerView = null;
    }

    public void BindDatas(){
        if (GroupListRecyclerView != null) {
            GroupListRecyclerView.setAdapter(GroupListAdapter);
        }
        isInit = true;
    }

    public void setItemChecked(boolean checked){
        isItemChecked = checked;
        GroupListAdapter.isChecked = checked;
        GroupListAdapter.notifyDataSetChanged();
        //Toast.makeText(DoMoment.getContext(),"<"+className+">",Toast.LENGTH_SHORT).show();
    }

    public boolean isItemChecked(){
        return isItemChecked;
    }

    private boolean isBindRecyclerView(){
        return GroupListAdapter != null;
    }
}
