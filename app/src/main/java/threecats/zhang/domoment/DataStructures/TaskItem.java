package threecats.zhang.domoment.DataStructures;

import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.ItemType;
import threecats.zhang.domoment.ENUM.OneDayBase;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.Helper.DateTimeHelper;

/**
 * Created by zhang on 2017/8/3.
 */

@Entity
public class TaskItem extends RecyclerViewItem implements Comparable{

    @Id
    long id;

    private String title;
    private String note;
    private String place;
    private int priority;
    private Date createDateTime;
    private Date startDateTime;
    private Date dueDateTime;
    private Date completeDateTime;
    private boolean isAllDay;
    private boolean isNoDate;
    private boolean isComplete;

    //@Backlink
    ToMany<CheckItem> checkList;

    public TaskItem(){

        itemType = ItemType.Item;

        startDateTime = new Date();
        dueDateTime = (Date)startDateTime.clone();
        createDateTime = new Date();
        completeDateTime = new Date();

        priority = TaskPriority.None.ordinal();

        isNoDate = true;
        isAllDay = true;
        isComplete = false;
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

    public void setPriority(int priority){
        this.priority = priority;
    }
    public int getPriority(){
        return priority;
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
        isNoDate = false;
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

    public int compareTo(@NonNull Object task) {  // 实现Comparator接口的方法
        TaskItem taskItem = (TaskItem)task;
        if (!this.getIsAllDay() && taskItem.getIsAllDay()) return 1;
        if (this.getIsAllDay() && !taskItem.getIsAllDay()) return -1;
        if (this.getIsAllDay() && taskItem.getIsAllDay()) return 0;
        return this.startDateTime.compareTo(taskItem.startDateTime);
    }


}
