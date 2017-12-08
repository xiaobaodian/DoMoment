package threecats.zhang.domoment.DataStructures;

import android.content.Context;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import threecats.zhang.domoment.ENUM.DisplayState;
import threecats.zhang.domoment.ENUM.GroupType;
import threecats.zhang.domoment.ENUM.ItemType;
import threecats.zhang.domoment.ENUM.TimeSeries;
import threecats.zhang.domoment.Helper.DateTimeHelper;
import threecats.zhang.domoment.App;

/**
 * 由 zhang 于 2017/8/3 创建
 */

public abstract class GroupBase extends RecyclerViewItem {

    protected DateTimeHelper DateTime = App.getDateTime();

    protected long ID;
    protected String title;
    protected String note;
    protected Context context;
    protected GroupType groupType;
    private GroupListBase parentGroupList;
    private GroupBase previousGroup;
    private GroupBase nextGroup;
    protected Calendar timePoint;
    private boolean isEmpty = false;

    private String flag;
    public int SiteID;
    public DisplayState State;
    private int imageID;
    private List<TaskItem> tasks;

    public GroupBase(GroupListBase parent){
        super();
        //super.setItemType(ItemType.Group);
        itemType = ItemType.Group;
        this.SiteID = -1;
        this.State = DisplayState.Hide;
        this.tasks = new ArrayList<>();
        this.context = App.getContext();
        this.parentGroupList = parent;
        BuildTimePoint();
    }

    //抽象方法，必须在子类中实现
    protected abstract boolean isGroupMember(Calendar timePoint, @Nullable Calendar secTimePoint);
    public abstract void BuildTimePoint();

    public void setID(long ID){
        this.ID = ID;
    }
    public long getID(){
        return ID;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setNote(String note){ this.note = note; }
    public String getNote(){ return note; }

    public void setFlag(String type){
        this.flag = type;
    }
    public String getFlag(){
        return flag;
    }

    public void setImageID(int imageID){
        this.imageID = imageID;
    }
    public int getImageID(){
        return imageID;
    }

    public GroupListBase getParent(){
        return parentGroupList;
    }

    public Calendar getTimePoint(){
        return timePoint;
    }
    public Calendar getNextTimePint(){
        if (nextGroup == null) return null;
        return nextGroup.timePoint;
    }

    public boolean InGroup(TaskItem task){
        TaskExt taskExt = new TaskExt(task);
        Calendar timePoint, secTimePoint;
        timePoint = null;
        //secTimePoint = task.IsOneDay() ? null : task.getExtDueDateTime();
        secTimePoint = taskExt.getExtDueDateTime();
        switch (parentGroupList.taskBasePoint){
            case BeginDate:
                timePoint = taskExt.getExtStartDateTime();
                break;
            case EndDate:
                timePoint = taskExt.getExtDueDateTime();
                break;
            case CreateDate:
                timePoint = taskExt.getExtCreateDateTime();
                break;
            case CompleteDate:
                timePoint = taskExt.getExtCompleteDateTime();
                //timePoint = task.getExtDueDateTime();
                break;
        }
        return !isEmpty && isGroupMember(timePoint, secTimePoint);
    }

    void setPreviousGroup(GroupBase group){
        this.previousGroup = group;
    }
    void setNextGroup(GroupBase group){
        this.nextGroup = group;
    }

    public GroupBase getPreviousGroup(){
        return previousGroup;
    }
    public GroupBase getNextGroup(){
        return nextGroup;
    }
    public GroupType getGroupType(){
        return groupType;
    }

    public void CheckArea(){
        if (previousGroup == null || nextGroup == null) return;
        if (getParent().timeSeries == TimeSeries.Forward) {
            // 区间判断：如果前一组的TimePoint不在当前组的TimePoint日期以前，说明前一组
            // 的时间区有一部分与当前组重叠，就将当前组的timepoint设置成前一组的timePoint
            // 如果下一组的timePoint不在当前组以后，说明本组与下一组重叠，就将isEmpty标志设为真

            //if (!previousTimePoint.before(timePoint) ) { timePoint = previousTimePoint; }
            //isEmpty = nextTimePoint.after(timePoint) ? false : true;

            if ( !previousGroup.timePoint.before(timePoint)) {
                timePoint = previousGroup.timePoint;
                //isEmpty = ! timePoint.before(nextGroup.timePoint);
            }
            isEmpty = ! timePoint.before(nextGroup.timePoint);
        } else {
            if ( ! previousGroup.timePoint.after(timePoint)) {
                timePoint = previousGroup.timePoint;;
                //isEmpty = !timePoint.after(nextGroup.timePoint);
            }
            isEmpty = !timePoint.after(nextGroup.timePoint);
        }

    }

    public String getDateLabel(String formatStr){
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        return dateFormat.format(timePoint.getTime());
    }

    public String getDateArea(){
        if (parentGroupList == null) return "No Parent";

        SimpleDateFormat date1Format = new SimpleDateFormat("MMMdd  E");
        String d = "", reg = "";

        if (parentGroupList.timeSeries == TimeSeries.Forward) {
            if (nextGroup == null) {
                d = " —— 将来";
            } else {
                Calendar endDate = (Calendar)nextGroup.timePoint.clone();
                endDate.add(Calendar.DATE, -1);
                if (!DateTime.SameYMD(timePoint, endDate)) {
                    String s = DateTime.getyearFormatStr(endDate.getTime());
                    SimpleDateFormat date2Format = new SimpleDateFormat("  / "+ s +" MMMd  E");
                    d = date2Format.format(endDate.getTime());
                }
            }
            reg = date1Format.format(timePoint.getTime())+d;
        } else {
            Calendar endDate = (Calendar)timePoint.clone();
            endDate.add(Calendar.DATE, -1);
            if (nextGroup == null) {
                d = "很久以前 —— ";
            } else {
                Calendar beginDate = (Calendar)nextGroup.timePoint.clone();
                if (!DateTime.SameYMD(beginDate, endDate)) {
                    String s = DateTime.getyearFormatStr(beginDate.getTime());
                    SimpleDateFormat date2Format = new SimpleDateFormat(s + "MMMd  E" + "  / ");
                    d = date2Format.format(beginDate.getTime());
                }
            }
            reg = d + date1Format.format(endDate.getTime());
        }
        return reg;
    }

    //以下是分组里面的条目管理
//    public int AddTask(Task task){
//        task.addParentGroup(this);
//        tasks.add(task);
//        if (State == DisplayState.Hide) State = DisplayState.Show;
//        return tasks.size();      //返回加入的任务的位置序号，便于组列表处理（0位是组标题）
//    }
    public int AddTask(TaskItem task){
        task.addParentGroup(this);
        int site = 0;
        if (tasks.size() == 0) {
            tasks.add(task);
            site = 0;
        } else if (task.compareTo(tasks.get(tasks.size() - 1)) >= 0) {
            tasks.add(task);
            site = tasks.size() - 1;
        } else {
            for (int i = 0; i <tasks.size(); i++) {
                if (task.compareTo(tasks.get(i)) < 0) {
                    site = i;
                    break;
                }
            }
            tasks.add(site, task);
        }
        if (State == DisplayState.Hide) State = DisplayState.Show;
        return site;
    }
    
    public int RemoveTask(TaskItem task){
        int site = tasks.indexOf(task);
        if(site >= 0) tasks.remove(site);
        return site;
    }

    public boolean needChangedPosition(TaskItem task){
        int pose = tasks.indexOf(task);
        if (pose < 0) return false;
        TaskItem prevTask = pose == 0 ? null : tasks.get(pose - 1);
        TaskItem nextTask = pose == tasks.size() - 1 ? null : tasks.get(pose + 1);
        boolean needChange = false;
        if (prevTask == null && nextTask == null) {
            needChange = false;
        } else if (prevTask == null) {
            if (task.compareTo(nextTask) > 0) needChange = true;
        } else if (nextTask == null) {
            if (task.compareTo(prevTask) < 0) needChange = true;
        } else if (task.compareTo(prevTask) < 0 || task.compareTo(nextTask) > 0) {
            needChange = true;
        }
        return needChange;
    }

    public int getTaskCount(){
        return tasks.size();
    }
    public List<TaskItem> getTasks(){
        return tasks;
    }
    public void Sort(){
        Collections.sort(tasks);
    }

}
