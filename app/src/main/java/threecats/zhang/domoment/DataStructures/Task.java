package threecats.zhang.domoment.DataStructures;

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
        categoryID = 1;
        parentGroup = new ArrayList<>(3);
    }

    public Task(int categoryID, Calendar beginDate){
        super();
        setCategoryID(categoryID);
        setStartDate(beginDate.get(Calendar.YEAR), beginDate.get(Calendar.MONTH)+1, beginDate.get(Calendar.DAY_OF_MONTH));
        parentGroup = new ArrayList<>(3);
    }

    public Task(String title, String place, Calendar createDate){
        super();
        super.setTitle(title);
        super.setPlace(place);
        super.setCreateDateTime(createDate);
        categoryID = 1;
        parentGroup = new ArrayList<>(3);
    }

    public Task(String title, String place){
        super();
        super.setTitle(title);
        super.setPlace(place);
        categoryID = 1;
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
        if (!parentGroup.contains(group)) {
            parentGroup.add(group);
        } else {
            DoMoment.Toast("有重复的ParentGroup加入");
        }
    }
    public void clearParentGroup(){
        parentGroup.clear();
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
