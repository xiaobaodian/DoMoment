package threecats.zhang.domoment.DataStructures;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import threecats.zhang.domoment.App;
import threecats.zhang.domoment.ENUM.OneDayBase;
import threecats.zhang.domoment.ENUM.TaskPriority;
import threecats.zhang.domoment.Helper.DateTimeHelper;

/**
 * Created by zhang on 2017/8/3.
 */

public class TaskExt {

    private TaskItem taskItem = null;
    private TaskDate startDay = new TaskDate();
    private TaskDate dueDay = new TaskDate();
    private TaskTime beginTime = new TaskTime();
    private TaskTime endTime = new TaskTime();
    private Calendar createDateTime;
    private Calendar startDateTime;
    private Calendar dueDateTime;
    private Calendar completeDateTime;
    private TaskPriority priority;

    public TaskExt(){

    }
    public TaskExt(TaskItem taskItem){
        setTaskItem(taskItem);
    }
    public TaskExt(int categoryID, Calendar startDateTime){
        super();
        setTaskItem(new TaskItem());
        setCategoryID(categoryID);
        setStartDate(startDateTime);
        setBeginTime(0,0);
        setAllDay(true);
    }

    public void setTaskItem(TaskItem taskItem){

        if (this.taskItem != null) {
            saveToTaskItem();
        }
        this.taskItem = taskItem;

        createDateTime = Calendar.getInstance();
        createDateTime.setTime(taskItem.getCreateDateTime());

        startDateTime = Calendar.getInstance();
        startDateTime.setTime(taskItem.getStartDateTime());

        dueDateTime = Calendar.getInstance();
        dueDateTime.setTime(taskItem.getDueDateTime());

        completeDateTime = Calendar.getInstance();
        completeDateTime.setTime(taskItem.getCompleteDateTime());

        setStartDateAndTime(startDateTime);
        setDueDateAndTime(dueDateTime);

        priority = TaskPriority.values()[taskItem.getPriorityID()];
    }
    public TaskItem getTaskItem(){
        return this.taskItem;
    }

    public void setCreateDateTime(Calendar createDateTime){
        this.createDateTime = createDateTime;
        taskItem.setCreateDateTime(createDateTime.getTime());
    }
    public Calendar getCreateDateTime(){
        //taskItem应用可能在别处被修改，所以在调用的时候需要从taskItem实例中重新获取数值
        createDateTime.setTime(taskItem.getCreateDateTime());
        return this.createDateTime;
    }
    public void setStartDateTime(Calendar startDateTime){
        this.startDateTime = startDateTime;
        taskItem.setIsNoDate(false);
        setStartDateAndTime(startDateTime);
        taskItem.setStartDateTime(startDateTime.getTime());
    }
    public void setLongStartDateTime(long longStartDateTime){
        startDateTime.setTimeInMillis(longStartDateTime);
        taskItem.setIsNoDate(false);
        setStartDateAndTime(startDateTime);
        taskItem.setStartDateTime(startDateTime.getTime());
    }
    public Calendar getStartDateTime(){
        startDateTime.setTime(taskItem.getStartDateTime());
        return this.startDateTime;
    }
    public long getLongStartDateTime(){
        //taskItem应用可能在别处被修改，所以在调用的时候需要从taskItem实例中重新获取数值
        startDateTime.setTime(taskItem.getStartDateTime());
        return this.startDateTime.getTimeInMillis();
    }

    public void setDueDateTime(Calendar dueDateTime){
        this.dueDateTime = dueDateTime;
        taskItem.setIsNoDate(false);
        setDueDateAndTime(dueDateTime);
        taskItem.setDueDateTime(dueDateTime.getTime());
    }
    public void setLongDueDateTime(long longDueDateTime){
        this.dueDateTime.setTimeInMillis(longDueDateTime);
        taskItem.setIsNoDate(false);
        setDueDateAndTime(dueDateTime);
        taskItem.setDueDateTime(dueDateTime.getTime());
    }
    public Calendar getDueDateTime(){
        dueDateTime.setTime(taskItem.getDueDateTime());
        return dueDateTime;
    }
    public long getLongDueDateTime(){
        dueDateTime.setTime(taskItem.getDueDateTime());
        return dueDateTime.getTimeInMillis();
    }

    public void setCompleteDateTime(Calendar completeDateTime){
        this.completeDateTime = completeDateTime;
        taskItem.setCompleteDateTime(completeDateTime.getTime());
    }
    public Calendar getCompleteDateTime(){
        completeDateTime.setTime(taskItem.getCompleteDateTime());
        return completeDateTime;
    }

    // 设置全天属性时，将开始时间设为当天的零点零分，这样就可以在排序时排在最前面。
    // 全天属性设定后，可以利用任务的完成日期和时间设定独立闹钟
    public boolean isAllDay() {
        return taskItem.getIsAllDay();
    }

    public void setAllDay(boolean isAllDay){
        if (isAllDay) setBeginTime(0,0);
        taskItem.setIsAllDay(isAllDay);
    }

    public void setNoDate(){
        taskItem.setIsNoDate(true);
        taskItem.setIsAllDay(true);
        dueDateTime = (Calendar) startDateTime.clone();
        taskItem.setDueDateTime(dueDateTime.getTime());
        Log.d(App.TAG,"taskExt is NoDate : "+ isNoDate());
        Log.d(App.TAG,"taskItem is NoDate : "+taskItem.getIsNoDate());
    }
    public boolean isNoDate(){
        return taskItem.getIsNoDate();
    }
    public void setPriority(TaskPriority priority){
        taskItem.setPriorityID(priority.ordinal());
        this.priority = priority;
    }
    public TaskPriority getPriority(){
        return priority;
    }
    public void setComplete(boolean isComplete){
        if (isComplete) {
            completeDateTime = Calendar.getInstance();
            taskItem.setCompleteDateTime(completeDateTime.getTime());
        }
        taskItem.setIsComplete(isComplete);
    }
    public void setCategoryID(int categoryID){
        taskItem.setCategoryID(categoryID);
    }
    public int getCategoryID(){
        return taskItem.getCategoryID();
    }

    public boolean isComplete(){
        return taskItem.getIsComplete();
    }
    public boolean isOneDay(){
        return startDay.Year == dueDay.Year && startDay.Month == dueDay.Month && startDay.Day == dueDay.Day;
    }
    public boolean isOneTime(){
        return beginTime.Hour == endTime.Hour && beginTime.Minute == endTime.Minute;
    }

    public void setTitle(String title){
        taskItem.setTitle(title);
    }
    public String getTitle(){
        return taskItem.getTitle();
    }
    public void setPlace(String place){
        taskItem.setPlace(place);
    }
    public String getPlace(){
        return taskItem.getPlace();
    }

    //辅助日期属性
    public void setStartDate(int year, int month, int day){
        if (isOneDay()) {
            setDueDate(year, month, day);
        }
        taskItem.setIsNoDate(false);
        startDay.setDate(year, month, day);
        startDateTime = startDay.getCalendar(startDateTime);
        taskItem.setStartDateTime(startDateTime.getTime());
    }
    public void setStartDate(Calendar startDate){
        TaskDate day = new TaskDate();
        day.setDate(startDate.getTime());
        setStartDate(day.Year, day.Month, day.Day);
    }
    public void setShiftStartDate(int year, int month, int day){
        //dueDateTime().clone()是为了在if (date.after(dueDateTime())判断
        //里对齐小时分钟，即只计算年月日的部分
        Calendar date = (Calendar)dueDateTime.clone();
        date.set(year, month, day);
        if (date.after(dueDateTime)) {
            long timeSpan = getLongDueDateTime() - getLongStartDateTime();
            setStartDate(year, month, day);
            setLongDueDateTime(getLongStartDateTime() + timeSpan);
        } else {
            setStartDate(year, month, day);
        }
    }

    public void setDueDate(int year, int month, int day){
        dueDay.setDate(year, month, day);
        dueDateTime = dueDay.getCalendar(dueDateTime);
        taskItem.setDueDateTime(dueDateTime.getTime());
    }
    public void setDueDate(Calendar dueDate){
        TaskDate day = new TaskDate();
        day.setDate(dueDate.getTime());
        setDueDate(day.Year, day.Month, day.Day);
    }
    public void setShiftDueDate(int year, int month, int day){
        Calendar date = (Calendar)startDateTime.clone();
        date.set(year, month, day);
        Calendar tmpStartDate = (Calendar)startDateTime.clone();
        if (date.before(startDateTime)) {
            if (isOneDay()) {
                setStartDate(year, month, day);
                setDueDate(startDay.Year, startDay.Month, startDay.Day);
                //setDueDate(tmpStartDate.get(Calendar.YEAR),tmpStartDate.get(Calendar.MONTH),tmpStartDate.get(Calendar.DAY_OF_MONTH));
                //setDueDate(year, month, day);
            } else {
                long timeSpan = getLongDueDateTime() - getLongStartDateTime();
                setDueDate(year, month, day);
                setLongStartDateTime(getLongDueDateTime()-timeSpan);
            }
        } else {
            setDueDate(year, month, day);
        }
    }

    public void setBeginTime(int hour, int minute){
        taskItem.setIsAllDay(false);
        if (isOneTime()) {
            setEndTime(hour, minute);
        }
        beginTime.setTime(hour, minute);
        startDateTime = beginTime.getCalendar(startDateTime);
        taskItem.setStartDateTime(startDateTime.getTime());
    }
    public void setEndTime(int hour, int minute){
        taskItem.setIsAllDay(false);
        endTime.setTime(hour, minute);
        dueDateTime = endTime.getCalendar(dueDateTime);
        taskItem.setDueDateTime(dueDateTime.getTime());
    }
    public void setOneDay(OneDayBase base){
        if (base == OneDayBase.Start) {
            setDueDate(startDay.Year, startDay.Month, startDay.Day);
        } else {
            setStartDate(dueDay.Year, dueDay.Month, dueDay.Day);
        }
    }
    public void setOneTime(OneDayBase base){
        if (base == OneDayBase.Start) {
            setEndTime(beginTime.Hour, beginTime.Minute);
        } else {
            setBeginTime(endTime.Hour, endTime.Minute);
        }
    }

    public String getBeginTimeStr(){
        final String timeFormatString = "H:mm";
        return (String) DateFormat.format(timeFormatString, startDateTime);
    }
    public String getEndTimeStr(){
        final String timeFormatString = "H:mm";
        return (String) DateFormat.format(timeFormatString, dueDateTime);
    }

    public String getCreatedDateTimeStr(){
        String year = DateTimeHelper.IsCurrentYear(createDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m", Locale.CHINA);
        return createDateTimeFStr.format(taskItem.getCreateDateTime());
    }

    public String getCompleteDateTimeStr(){
        String year = DateTimeHelper.IsCurrentYear(createDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m");
        return createDateTimeFStr.format(taskItem.getCompleteDateTime());
    }

    public String getBeginDateStr(){
        String year = DateTimeHelper.IsCurrentYear(startDateTime) ? "" : "yyyy ";
        SimpleDateFormat beginDateTimeFStr = new SimpleDateFormat(year + "MMM  dd");
        return beginDateTimeFStr.format(taskItem.getStartDateTime());
    }
    public String getEndDateStr(){
        if (isOneDay()) return "";
        String year = DateTimeHelper.IsCurrentYear(dueDateTime) ? "" : "yyyy ";
        String month = DateTimeHelper.SameYearMonth(startDateTime, dueDateTime) ? "" : "MMM  ";
        SimpleDateFormat dueDateTimeFStr = new SimpleDateFormat(year + month + "dd");
        return dueDateTimeFStr.format(taskItem.getDueDateTime());
    }
    public String getDateRangeStr(){
        if (isNoDate()) return "DoDate";
        String beginDateFormatStr = "";
        String endDateFormatStr = "";
        if (DateTimeHelper.SameYearMonth(startDateTime, dueDateTime)) {
            beginDateFormatStr = DateTimeHelper.IsCurrentYear(startDateTime) ? "MMM d" : "yyyy MMM d";
            endDateFormatStr = " - d";
        } else {
            beginDateFormatStr = DateTimeHelper.IsCurrentYear(startDateTime) ? "MMM d" : "yyyy MMM d";
            endDateFormatStr = DateTimeHelper.IsCurrentYear(startDateTime) ? " - MMM d" : " - yyyy MMM d";
        }
        SimpleDateFormat beginDate = new SimpleDateFormat(beginDateFormatStr);
        SimpleDateFormat endDate = new SimpleDateFormat(endDateFormatStr);
        String endDateStr = isOneDay() ? "" : endDate.format(dueDateTime.getTime());
        return beginDate.format(startDateTime.getTime()) + endDateStr;
    }

    public String getTimeRangeStr(){
        if (isAllDay()) return "";
        String timeFormatStr = "H:mm";
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormatStr);
        String endTime = isOneTime() ? "" : " - " + simpleTimeFormat.format(dueDateTime.getTime());
        return simpleTimeFormat.format(startDateTime.getTime()) + endTime;
    }

    public void saveToTaskItem(){
        taskItem.setCreateDateTime(createDateTime.getTime());
        taskItem.setStartDateTime(startDateTime.getTime());
        taskItem.setDueDateTime(dueDateTime.getTime());
        taskItem.setCompleteDateTime(completeDateTime.getTime());
    }

    private void setStartDateAndTime(Calendar date){
        startDay.setDate(date.getTime());
        beginTime.setTime(date.getTime());
    }

    private void setDueDateAndTime(Calendar date){
        dueDay.setDate(date.getTime());
        endTime.setTime(date.getTime());
    }

    class TaskDate {
        int Year;
        int Month;
        int Day;

        void setDate(Date date){
            Calendar d = Calendar.getInstance();
            d.setTime(date);
            this.Year = d.get(Calendar.YEAR);
            this.Month = d.get(Calendar.MONTH);
            this.Day = d.get(Calendar.DAY_OF_MONTH);
        }
        void setDate(int year, int month, int day){
            this.Year = year;
            this.Month = month;
            this.Day = day;
        }
        Calendar getCalendar(Calendar day){
            Calendar cDay = (Calendar) day.clone();
            cDay.set(Year, Month, Day);
            return cDay;
        }
    }

    class TaskTime {
        int Hour;
        int Minute;

        void setTime(Date date){
            Calendar d = Calendar.getInstance();
            d.setTime(date);
            this.Hour = d.get(Calendar.HOUR_OF_DAY);
            this.Minute = d.get(Calendar.MINUTE);
        }
        void setTime(int hour, int minute){
            this.Hour = hour;
            this.Minute = minute;
        }
        Calendar getCalendar(Calendar day){
            Calendar cDay = (Calendar)day.clone();
            cDay.set(Calendar.HOUR_OF_DAY, Hour);
            cDay.set(Calendar.MINUTE, Minute);
            return cDay;
        }
    }

}
