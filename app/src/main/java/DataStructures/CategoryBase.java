package DataStructures;

import java.util.ArrayList;
import java.util.List;

import ENUM.CategoryType;
import ENUM.GroupListType;

/**
 * Created by zhang on 2017/8/14.
 */

public abstract class CategoryBase extends ItemBase {
    private List<GroupListBase> GroupLists;
    protected int orderID;
    protected int categoryID;
    protected CategoryType categoryType;
    protected int themeBackground;
    protected int themeColor;

    public CategoryBase(){
        GroupLists = new ArrayList<>();
    }

    public abstract boolean InCategory(Task task);
    protected abstract void BuildGroupLists();

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
    public void setThemeColor(int themeColor){
        this.themeColor = themeColor;
    }
    public int getThemeColor(){
        return themeColor;
    }

    public void setThemeBackground(int themeBackground){
        this.themeBackground = themeBackground;
    }
    public int getThemeBackground(){
        return themeBackground;
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
