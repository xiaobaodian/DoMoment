package threecats.zhang.domoment.DataStructures;

import java.util.ArrayList;
import java.util.List;

import threecats.zhang.domoment.ENUM.CategoryType;
import threecats.zhang.domoment.ENUM.GroupListType;

/**
 * Created by zhang on 2017/8/14.
 */

public abstract class CategoryBase extends ItemBase {
    private List<GroupListBase> GroupLists;
    protected int orderID;
    protected int categoryID;
    protected CategoryType categoryType;
    protected int iconId;
    protected int themeBackgroundID;
    protected int themeColorID;

    public CategoryBase(){
        GroupLists = new ArrayList<>();
    }

    public abstract boolean InCategory(Task task);
    protected abstract void BuildGroupLists();
    public int getTaskCount(){
        int count = 0;
        for (GroupListBase groupList : GroupLists) {
            count += groupList.getTaskCount();
        }
        return count;
    }
    public List<Task> getAllTasks(){
        List<Task> allTasks = new ArrayList<>();
        for (GroupListBase groupList : GroupLists) {
            for (GroupBase group : groupList.getGroups()) {
                for (Task task : group.getTasks()) {
                    allTasks.add(task);
                }
            }
        }
        return allTasks;
    }

    public void AddGroupList(GroupListBase groupList){
        GroupLists.add(groupList);
    }

    public GroupListBase getGroupList(GroupListType type){
        GroupListBase vGroup = null;
        for (GroupListBase groupList : GroupLists) {
            if (groupList.selfType == type) {
                vGroup = groupList;
                break;
            }
        }
        return vGroup;
    }

    public void setOrderID(int orderID){
        this.orderID = orderID;
    }
    public int getOrderID(){
        return orderID;
    }
    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public void setThemeColorID(int themeColorID){
        this.themeColorID = themeColorID;
    }
    public int getThemeColorID(){
        return themeColorID;
    }
    public void setIconId(int iconId){
        this.iconId = iconId;
    }
    public int getIconId(){
        return iconId;
    }
    public void setThemeBackgroundID(int themeBackgroundID){
        this.themeBackgroundID = themeBackgroundID;
    }
    public int getThemeBackgroundID(){
        return themeBackgroundID;
    }

    public void AddTask(Task task){
        if ( ! InCategory(task)) return;
        for (GroupListBase groupList : GroupLists) {
            if (groupList.InGroupList(task)) {
                groupList.AddTask(task);
                break;
            }
        }
    }

    public void InsertTask(Task task){
        if ( ! InCategory(task)) return;
        for (GroupListBase groupList : GroupLists) {
            if (groupList.InGroupList(task)){
                groupList.InsertTask(task);
                break;
            }
        }
    }

    public List<GroupListBase> getGroupLists(){
        return GroupLists;
    }


    public void UnBind(){
        for (GroupListBase groupList : GroupLists){
            groupList.UnBind();
        }
    }


}
