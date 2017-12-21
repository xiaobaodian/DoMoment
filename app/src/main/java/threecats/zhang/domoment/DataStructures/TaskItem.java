package threecats.zhang.domoment.DataStructures;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToMany;
import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.ItemType;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.Helper.UIHelper;

/**
 * 由 zhang 于 2017/8/3 创建
 */

@Entity
public class TaskItem extends RecyclerViewItem implements Comparable{

    @Id
    long id;

    private String title = "";
    private String note = "";
    private String place = "";
    private String taskContext = "";
    private String taskTags = "";
    private int categoryID = 1;
    private int priorityID = TaskPriority.None.ordinal();
    private Date createDateTime;
    private Date startDateTime;
    private Date dueDateTime;
    private Date completeDateTime;
    private boolean isAllDay = true;
    private boolean isNoDate = true;
    private boolean isComplete = false;
    private boolean isMoment = true;
    @Backlink
    ToMany<CheckItem> checkList;

    @Transient
    private List<GroupBase> parentGroups = new ArrayList<>();


    public TaskItem(){

        itemType = ItemType.Item;

        startDateTime = new Date();
        dueDateTime = (Date)startDateTime.clone();
        createDateTime = new Date();
        completeDateTime = new Date();

    }

    public void setID(int ID){
        this.id = ID;
    }
    public long getID(){
        return id;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setNote(String note){ this.note = note; }
    public String getNote(){ return note; }

    public void setPlace(String place){
        this.place = place;
    }
    public String getPlace(){
        return place;
    }

    public void setTaskContext(String taskContext){
        this.taskContext = taskContext;
    }
    public String getTaskContext(){
        return taskContext;
    }

    public void setTaskTags(String taskTags){
        this.taskTags = taskTags;
    }
    public String getTaskTags(){
        return taskTags;
    }

    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }
    public int getCategoryID(){
        return categoryID;
    }

    public void setPriorityID(int priorityID){
        this.priorityID = priorityID;
    }
    public int getPriorityID(){
        return priorityID;
    }

    public void setCreateDateTime(Date createDateTime){
        this.createDateTime = createDateTime;
    }
    public Date getCreateDateTime(){
        return createDateTime;
    }

    public void setStartDateTime(Date startDateTime){
        this.startDateTime = startDateTime;
    }
    public Date getStartDateTime(){
        return startDateTime;
    }

    public void setDueDateTime(Date dueDateTime){
        this.dueDateTime = dueDateTime;
    }
    public Date getDueDateTime(){
        return dueDateTime;
    }

    public void setCompleteDateTime(Date completeDateTime){
        this.completeDateTime = completeDateTime;
    }
    public Date getCompleteDateTime(){
        return completeDateTime;
    }

    public void setIsAllDay(boolean isAllDay){
        this.isAllDay = isAllDay;
    }
    public boolean getIsAllDay() {
        return isAllDay;
    }
    public void setIsNoDate(boolean isNoDate){
        this.isNoDate = isNoDate;
    }
    public boolean getIsNoDate(){
        return isNoDate;
    }

    public void setIsComplete(boolean isComplete){
        this.isComplete = isComplete;
    }
    public boolean getIsComplete(){
        return isComplete;
    }

    public void setMoment(boolean isMoment){
        this.isMoment = isMoment;
    }
    public boolean isMoment(){
        return isMoment;
    }

    public int compareTo(@NonNull Object task) {  // 实现Comparator接口的方法
        TaskItem taskItem = (TaskItem)task;
        if (!this.getIsAllDay() && taskItem.getIsAllDay()) return 1;
        if (this.getIsAllDay() && !taskItem.getIsAllDay()) return -1;
        if (this.getIsAllDay() && taskItem.getIsAllDay()) return 0;
        return this.startDateTime.compareTo(taskItem.startDateTime);
    }

    public void addParentGroup(GroupBase group){
        if (!parentGroups.contains(group)) {
            parentGroups.add(group);
        } else {
            UIHelper.Toast("有重复的ParentGroup加入");
        }
    }
    public List<GroupBase> getParentGroups(){
        return parentGroups;
    }

    public @Nullable GroupBase getCurrentGroup(GroupListBase parentGroupList){
        GroupBase currentGroup = null;
        for (GroupBase group : parentGroups) {
            if (group.getParent() == parentGroupList){
                currentGroup = group;
                break;
            }
        }
        return currentGroup;
    }


}
