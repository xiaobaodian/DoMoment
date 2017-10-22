package DataStructures;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zhang on 2017/7/26.
 */

public class Task extends TaskBase{

    private int categoryID;
    private List<GroupBase> parentGroups;
    private String context;
    private String tags;

    public Task(){
        super();
        parentGroups = new ArrayList<>(3);
    }

    public Task(String title, String place, Calendar createDate){
        super();
        super.setTitle(title);
        super.setPlace(place);
        super.setCreateDateTime(createDate);
        parentGroups = new ArrayList<>(3);
    }

    public Task(String title, String place){
        super();
        super.setTitle(title);
        super.setPlace(place);
        parentGroups = new ArrayList<>(3);
    }

    public Task(String title, String place, int categoryID){
        super();
        super.setTitle(title);
        super.setPlace(place);
        this.categoryID = categoryID;
        parentGroups = new ArrayList<>(3);
    }

    public void setParentGroups(GroupBase group){
        parentGroups.add(group);
    }
    public List<GroupBase> getParentGroups(){
        return parentGroups;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public void setCategoryID(int ID){
        this.categoryID = ID;
    }

    public @Nullable GroupBase getCurrentGroup(GroupListBase groupList){
        GroupBase currentGroup = null;
        for (GroupBase group : parentGroups) {
            if (group.getParent() == groupList){
                currentGroup = group;
                break;
            }
        }
        return currentGroup;
    }

}
