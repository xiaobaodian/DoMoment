package DataStructures;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import ENUM.DisplayState;
import ENUM.GroupType;
import ENUM.ItemType;
import ENUM.TimeSeries;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;

/**
 * Created by zhang on 2017/8/3.
 */

public abstract class GroupBase extends ListItemBase {

    protected DateTimeHelper DateTime = DoMoment.getDateTime();

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
    private List<Task> tasks;

    public GroupBase(GroupListBase parent){
        super();
        //super.setItemType(ItemType.Group);
        itemType = ItemType.Group;
        this.SiteID = -1;
        this.State = DisplayState.Hide;
        this.tasks = new ArrayList<>();
        this.context = DoMoment.getContext();
        this.parentGroupList = parent;
        BuildTimePoint();
    }

    //抽象方法，必须在子类中实现
    protected abstract boolean isGroupMember(Calendar timePoint, @Nullable Calendar secTimePoint);
    public abstract void BuildTimePoint();

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

    public boolean InGroup(Task task){
        Calendar timePoint, secTimePoint;
        timePoint = null;
        //secTimePoint = task.IsOneDay() ? null : task.getDueDateTime();
        secTimePoint = task.getDueDateTime();
        switch (parentGroupList.taskBasePoint){
            case BeginDate:
                timePoint = task.getStartDateTime();
                break;
            case EndDate:
                timePoint = task.getDueDateTime();
                break;
            case CreateDate:
                timePoint = task.getCreateDateTime();
                break;
            case CompleteDate:
                timePoint = task.getCompleteDateTime();
                break;
        }
        return isEmpty ? false : isGroupMember(timePoint, secTimePoint);
    }

    public void setPreviousGroup(GroupBase group){
        this.previousGroup = group;

    }
    public GroupBase getPreviousGroup(){
        return previousGroup;
    }
    public void setNextGroup(GroupBase group){
        this.nextGroup = group;
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
    public int AddTask(Task task){
        task.addParentGroup(this);
        int site = 0;
        if (tasks.size() == 0) {
            tasks.add(task);
            site = tasks.size();
        } else if(task.compareTo(tasks.get(tasks.size() - 1)) >= 0) {
            tasks.add(task);
            site = tasks.size();
        } else {
            for (int i = 0; i <tasks.size(); i++) {
                if (task.compareTo(tasks.get(i)) < 0) {
                    site = i + 1;
                    break;
                }
            }
            tasks.add(site, task);
        }
        if (State == DisplayState.Hide) State = DisplayState.Show;
        return site;
    }

    public int InsertTask(Task task){
        //task.addParentGroup(this);  不能在此加入ParentGroup，要是执行了AddTask就会重复加入了
        int site = 0;
        if (tasks.size() == 0) {
            site = AddTask(task);
        } else if(task.compareTo(tasks.get(tasks.size() - 1)) >= 0) {
            site = AddTask(task);
        } else {
            for (int i = 0; i <tasks.size(); i++) {
                if (task.compareTo(tasks.get(i)) < 0) {
                    site = i + 1;
                    break;
                }
            }
            task.addParentGroup(this); // 放在这里加入ParentGroup才是正确的
            tasks.add(site, task);
            if (State == DisplayState.Hide) State = DisplayState.Show;
        }
        return site;
    }
    
    public int RemoveTask(Task task){
        int site = tasks.indexOf(task);
        if(site >= 0) tasks.remove(site);
        return site;
    }
    public int getTaskCount(){
        return tasks.size();
    }
    public List<Task> getTasks(){
        return tasks;
    }
    public void Sort(){
        Collections.sort(tasks);
    }

}
