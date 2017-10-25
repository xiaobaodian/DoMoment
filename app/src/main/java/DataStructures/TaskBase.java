package DataStructures;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ENUM.DATEFORMAT;
import ENUM.ItemType;
import ENUM.OneDayBase;
import ENUM.TaskPriority;
import threecats.zhang.domoment.DateTimeHelper;
import threecats.zhang.domoment.DoMoment;

/**
 * Created by zhang on 2017/8/3.
 */

public class TaskBase extends ListItemBase implements Comparable{

    private DateTimeHelper DateTime = DoMoment.getDateTime();

    private String place;
    private TaskPriority priority;
    private Calendar createDateTime;
    private Calendar startDateTime;
    private Calendar dueDateTime;
    private Calendar completeDateTime;
    private boolean isAllDay;
    private boolean isNoDate;
    private boolean isComplete;

    public TaskBase(){
        itemType = ItemType.Item;

        startDateTime = Calendar.getInstance();
        dueDateTime = (Calendar) startDateTime.clone();
        createDateTime = Calendar.getInstance();
        completeDateTime = Calendar.getInstance();

        isNoDate = true;
        isAllDay = true;
        isComplete = false;
    }

    public void setCreateDateTime(Calendar createDateTime){
        this.createDateTime = createDateTime;
    }
    public Calendar getCreateDateTime(){
        return createDateTime;
    }
    public void setCreateDateTimeDB(Long millis){
        createDateTime.setTimeInMillis(millis);
    }
    public long getCreateDateTimeDB(){
        return createDateTime.getTimeInMillis();
    }

    // 设置全天属性时，将开始时间设为当天的零点零分，这样就可以在排序时排在最前面。
    // 全天属性设定后，可以利用任务的完成日期和时间设定独立闹钟
    public void setAllDay(boolean isAllDay){
        if (isAllDay) setStartTime(0,0);
        this.isAllDay = isAllDay;
    }
    public boolean IsAllDay() {
        return isAllDay;
    }
    public void setNoDate(){
        isNoDate = true;
        isAllDay = true;
        Calendar tmpDateTime = (Calendar)startDateTime.clone();
        dueDateTime = tmpDateTime;
    }
    public void setNoDateDB(boolean noDateDB){
        this.isNoDate = noDateDB;
    }
    public boolean IsNoDate(){
        return isNoDate;
    }
    public void setPriority(TaskPriority priority){
        this.priority = priority;
    }
    public TaskPriority getPriority(){
        return priority;
    }
    public void setComplete(boolean isComplete){
        this.isComplete = isComplete;
    }
    public boolean getComplete(){
        return isComplete;
    }
    public boolean IsOneDay(){
        return DateTime.SameYMD(startDateTime, dueDateTime);
//        return startDateTime.get(Calendar.YEAR) == dueDateTime.get(Calendar.YEAR) &&
//               startDateTime.get(Calendar.MONTH) == dueDateTime.get(Calendar.MONTH) &&
//               startDateTime.get(Calendar.DAY_OF_MONTH) == dueDateTime.get(Calendar.DAY_OF_MONTH);
    }
    public boolean IsOneTime(){
        return startDateTime.get(Calendar.HOUR_OF_DAY) == dueDateTime.get(Calendar.HOUR_OF_DAY) &&
               startDateTime.get(Calendar.MINUTE) == dueDateTime.get(Calendar.MINUTE);
    }

    public void setStartDateTime(Calendar startDateTime){
        isNoDate = false;
        this.startDateTime = startDateTime;
    }
    public Calendar getStartDateTime(){
        return startDateTime;
    }
    public void setStartDateTimeDB(long millis){
        startDateTime.setTimeInMillis(millis);
    }
    public long getStartDateTimeDB(){
        return startDateTime.getTimeInMillis();
    }

    public void setDueDateTime(Calendar dueDateTime){
        isNoDate = false;
        this.dueDateTime = dueDateTime;
    }
    public Calendar getDueDateTime(){
        return dueDateTime;
    }
    public void setDueDateTimeDB(long millis){
        dueDateTime.setTimeInMillis(millis);
    }
    public long getDueDateTimeDB(){
        return dueDateTime.getTimeInMillis();
    }

    public void setCompleteDateTime(Calendar completeDateTime){
        this.completeDateTime = completeDateTime;
    }
    public Calendar getCompleteDateTime(){
        return completeDateTime;
    }
    public void setCompleteDateTimeDB(Long millis){
        completeDateTime.setTimeInMillis(millis);
    }
    public long getCompleteDateTimeDB(){
        return completeDateTime.getTimeInMillis();
    }

    public void setPlace(String place){
        this.place = place;
    }
    public String getPlace(){
        return place;
    }

    public int compareTo(Object task) {// 实现Comparator接口的方法
        TaskBase taskBase = (TaskBase)task;
        return this.startDateTime.compareTo(taskBase.startDateTime);
    }

    //辅助日期属性
    public void setStartDate(int year, int month, int day){
        if (IsOneDay()) {
            setDueDate(year, month, day);
        }
        Calendar tmpDateTime = (Calendar)startDateTime.clone();
        tmpDateTime.set(year, month - 1, day);
        setStartDateTime(tmpDateTime);
    }
    public void setDueDate(int year, int month, int day){
        Calendar tmpDateTime = (Calendar)dueDateTime.clone();
        tmpDateTime.set(year, month - 1, day);
        setDueDateTime(tmpDateTime);
    }
    public void setStartTime(int hour, int minute){
        if (IsOneTime()) setDueTime(hour, minute);
        startDateTime.set(Calendar.HOUR_OF_DAY, hour);
        startDateTime.set(Calendar.MINUTE, minute);
        isAllDay = false;
    }
    public void setDueTime(int hour, int minute){
        dueDateTime.set(Calendar.HOUR_OF_DAY, hour);
        dueDateTime.set(Calendar.MINUTE, minute);
        isAllDay = false;
    }
    public String getStartTime(){
        final String timeFormatString = "H:mm";
        return (String) DateFormat.format(timeFormatString, startDateTime);
    }
    public String getDueTime(){
        final String timeFormatString = "H:mm";
        return (String) DateFormat.format(timeFormatString, dueDateTime);
    }

    public void setOneDay(OneDayBase base){
        //isSingleTime = true;
        if (base == OneDayBase.Start) {
            //Calendar tmpDateTime = (Calendar)dueDateTime.clone();
            //tmpDateTime.set(startDateTime.get(Calendar.YEAR), startDateTime.get(Calendar.MONTH), startDateTime.get(Calendar.DAY_OF_MONTH));
            dueDateTime.set(startDateTime.get(Calendar.YEAR), startDateTime.get(Calendar.MONTH), startDateTime.get(Calendar.DAY_OF_MONTH));
            //setDueDateTime(tmpDateTime);
        } else {
            //Calendar tmpDateTime = (Calendar)startDateTime.clone();
            //tmpDateTime.set(dueDateTime.get(Calendar.YEAR), dueDateTime.get(Calendar.MONTH), dueDateTime.get(Calendar.DAY_OF_MONTH));
            startDateTime.set(dueDateTime.get(Calendar.YEAR), dueDateTime.get(Calendar.MONTH), dueDateTime.get(Calendar.DAY_OF_MONTH));
            //setStartDateTime(tmpDateTime);
        }
        int sy = startDateTime.get(Calendar.YEAR);
        int dy = dueDateTime.get(Calendar.YEAR);
        int sm = startDateTime.get(Calendar.MONTH);
        int dm = dueDateTime.get(Calendar.MONTH);
        int sd = startDateTime.get(Calendar.DAY_OF_MONTH);
        int dd = dueDateTime.get(Calendar.DAY_OF_MONTH);
    }
    public void setOneTime(OneDayBase base){
        if (base == OneDayBase.Start) {
            setDueTime(startDateTime.get(Calendar.HOUR_OF_DAY), startDateTime.get(Calendar.MINUTE));
        } else {
            setStartTime(dueDateTime.get(Calendar.HOUR_OF_DAY), dueDateTime.get(Calendar.MINUTE));
        }
    }

    public String getCreatedDateTimeStr(){
        String year = DateTime.IsCurrentYear(createDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m");
        return createDateTimeFStr.format(createDateTime.getTime());
    }

    public String getBeginDateString(){
        String year = DateTime.IsCurrentYear(startDateTime) ? "" : "yyyy ";
        SimpleDateFormat beginDateTimeFStr = new SimpleDateFormat(year + "MMM  dd");
        return beginDateTimeFStr.format(startDateTime.getTime());
    }
    public String getEndDateString(){
        if (IsOneDay()) return "";
        String year = DateTime.IsCurrentYear(dueDateTime) ? "" : "yyyy ";
        String month = DateTime.SameYearMonth(startDateTime, dueDateTime) ? "" : "MMM  ";
        SimpleDateFormat dueDateTimeFStr = new SimpleDateFormat(year + month + "dd");
        return dueDateTimeFStr.format(dueDateTime.getTime());
    }
    public String getDateRange(){
        if (IsNoDate()) return "DoDate";
        String beginDateFormatStr = "";
        String endDateFormatStr = "";
        if (DateTime.SameYearMonth(startDateTime, dueDateTime)) {
            beginDateFormatStr = DateTime.IsCurrentYear(startDateTime) ? "MMM d" : "yyyy MMM d";
            endDateFormatStr = " - d";
        } else {
            beginDateFormatStr = DateTime.IsCurrentYear(startDateTime) ? "MMM d" : "yyyy MMM d";
            endDateFormatStr = DateTime.IsCurrentYear(startDateTime) ? " - MMM d" : " - yyyy MMM d";
        }
        SimpleDateFormat beginDate = new SimpleDateFormat(beginDateFormatStr);
        SimpleDateFormat endDate = new SimpleDateFormat(endDateFormatStr);
        String endDateStr = IsOneDay() ? "" : endDate.format(dueDateTime.getTime());
        return beginDate.format(startDateTime.getTime()) + endDateStr;
    }

    public  String getTimeRange(){
        if (IsAllDay()) return "";
        String timeFormatStr = "H:mm";
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormatStr);
        String endTime = IsOneTime() ? "" : " - " + simpleTimeFormat.format(dueDateTime.getTime());
        return simpleTimeFormat.format(startDateTime.getTime()) + endTime;
    }

}
