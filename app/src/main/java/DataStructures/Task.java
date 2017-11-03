package DataStructures;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import threecats.zhang.domoment.DoMoment;

/**
 * Created by zhang on 2017/7/26.
 */

public class Task extends TaskBase{

    private int categoryID;
    private List<GroupBase> parentGroup;
    private String taskContext;
    private String taskTags;
    private boolean intoMoment;

    public Task(){
        super();
        parentGroup = new ArrayList<>(3);
    }

    public Task(String title, String place, Calendar createDate){
        super();
        super.setTitle(title);
        super.setPlace(place);
        super.setCreateDateTime(createDate);
        parentGroup = new ArrayList<>(3);
    }

    public Task(String title, String place){
        super();
        super.setTitle(title);
        super.setPlace(place);
        parentGroup = new ArrayList<>(3);
    }

    public Task(String title, String place, int categoryID){
        super();
        super.setTitle(title);
        super.setPlace(place);
        this.categoryID = categoryID;
        parentGroup = new ArrayList<>(3);
    }

    public void addParentGroup(GroupBase group){
        //DoMoment.Toast("Add Parent :" + group.getTitle()+"/"+group.getParent().getTitle());
        //DoMoment.Toast("Wait...");
        if (!parentGroup.contains(group)) {
            // 如果不判断group是不是已经存在parentGroup里，将会重复加入，原因待查
            parentGroup.add(group);
        }
    }
    public void clearParentGroup(){
        DoMoment.Toast("Parent Size : " + parentGroup.size());
        parentGroup.clear();
        DoMoment.Toast("Parent Size : " + parentGroup.size());
    }
    public List<GroupBase> getParentGroup(){
        return parentGroup;
    }
    public int getCategoryID(){
        return categoryID;
    }
    public void setCategoryID(int ID){
        this.categoryID = ID;
    }

    public @Nullable GroupBase getCurrentGroup(GroupListBase parentGroupList){
        GroupBase currentGroup = null;
        for (GroupBase group : parentGroup) {
            if (group.getParent() == parentGroupList){
                currentGroup = group;
                break;
            }
        }
        return currentGroup;
    }

}
