package threecats.zhang.domoment.DataStructures;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    private DateTimeHelper DateTime = App.getDateTime();

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
    public TaskExt(int categoryID, Calendar beginDate){
        super();
        taskItem = new TaskItem();
        setCategoryID(categoryID);
        setStartDate(beginDate.get(Calendar.YEAR), beginDate.get(Calendar.MONTH)+1, beginDate.get(Calendar.DAY_OF_MONTH));
    }

    public void setTaskItem(TaskItem taskItem){

        if (this.taskItem != null) {
            finish();
        }
        this.taskItem = taskItem;

        startDay.setDate(taskItem.getStartDateTime());
        dueDay.setDate(taskItem.getDueDateTime());
        beginTime.setTime(taskItem.getStartDateTime());
        endTime.setTime(taskItem.getDueDateTime());

        createDateTime = Calendar.getInstance();
        createDateTime.setTime(taskItem.getCreateDateTime());

        startDateTime = Calendar.getInstance();
        startDateTime.setTime(taskItem.getStartDateTime());

        dueDateTime = Calendar.getInstance();
        dueDateTime.setTime(taskItem.getDueDateTime());

        completeDateTime = Calendar.getInstance();
        completeDateTime.setTime(taskItem.getCompleteDateTime());

        priority = TaskPriority.values()[taskItem.getPriority()];
    }
    public TaskItem getTaskItem(){
        return this.taskItem;
    }

    public void setCreateDateTime(Calendar createDateTime){
        this.createDateTime = createDateTime;
    }
    public Calendar getCreateDateTime(){
        return this.createDateTime;
    }
    public void setStartDateTime(Calendar startDateTime){
        this.startDateTime = startDateTime;
        taskItem.setIsNoDate(false);
        setStartDateAndTime(startDateTime);
    }
    public Calendar getStartDateTime(){
        return this.startDateTime;
    }

    public void setDueDateTime(Calendar dueDateTime){
        this.dueDateTime = dueDateTime;
        taskItem.setIsNoDate(false);
        setDueDateAndTime(dueDateTime);
    }
    public Calendar getDueDateTime(){
        return dueDateTime;
    }

    public void setCompleteDateTime(Calendar completeDateTime){
        this.completeDateTime = completeDateTime;
    }
    public Calendar getCompleteDateTime(){
        return completeDateTime;
    }

    // 设置全天属性时，将开始时间设为当天的零点零分，这样就可以在排序时排在最前面。
    // 全天属性设定后，可以利用任务的完成日期和时间设定独立闹钟
    public boolean IsAllDay() {
        return taskItem.getIsAllDay();
    }

    public void setAllDay(boolean isAllDay){
        if (isAllDay) setBeginTime(0,0);
        taskItem.setIsAllDay(isAllDay);
    }

    public void setNoDate(){
        taskItem.setIsNoDate(true);
        setAllDay(true);
        dueDateTime = (Calendar) startDateTime.clone();
    }
    public boolean IsNoDate(){
        return taskItem.getIsNoDate();
    }
    public void setPriority(TaskPriority priority){
        taskItem.setPriority(priority.ordinal());
        this.priority = priority;
    }
    public TaskPriority getPriority(){
        return priority;
    }
    public void setComplete(boolean isComplete){
        if (isComplete) {
            completeDateTime = Calendar.getInstance();
        }
        taskItem.setIsComplete(isComplete);
    }
    public void setCategoryID(int categoryID){
        taskItem.setCategoryID(categoryID);
    }
    public int getCategoryID(){
        return taskItem.getCategoryID();
    }

    public boolean IsComplete(){
        return taskItem.getIsComplete();
    }
    public boolean IsOneDay(){
        return startDay.Year == dueDay.Year && startDay.Month == dueDay.Month && startDay.Day == dueDay.Day;
    }
    public boolean IsOneTime(){
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
        if (IsOneDay()) {
            setDueDate(year, month, day);
        }
        startDay.setDate(year, month, day);
        startDateTime = startDay.getCalendar(startDateTime);
    }
    public void setDueDate(int year, int month, int day){
        dueDay.setDate(year, month, day);
        dueDateTime = dueDay.getCalendar(dueDateTime);
    }

    public void setBeginTime(int hour, int minute){
        taskItem.setIsAllDay(false);
        if (IsOneTime()) {
            setEndTime(hour, minute);
        }
        beginTime.setTime(hour, minute);
        startDateTime = beginTime.getCalendar(startDateTime);
    }
    public void setEndTime(int hour, int minute){
        taskItem.setIsAllDay(false);
        endTime.setTime(hour, minute);
        dueDateTime = endTime.getCalendar(dueDateTime);
    }
    public void setOneDay(OneDayBase base){
        if (base == OneDayBase.Start) {
            dueDay.setDate(startDay.Year, startDay.Month, startDay.Day);
        } else {
            startDay.setDate(dueDay.Year, dueDay.Month, dueDay.Day);
        }
    }
    public void setOneTime(OneDayBase base){
        if (base == OneDayBase.Start) {
            endTime.setTime(beginTime.Hour, beginTime.Minute);
        } else {
            beginTime.setTime(endTime.Hour, endTime.Minute);
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
        String year = DateTime.IsCurrentYear(createDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m", Locale.CHINA);
        return createDateTimeFStr.format(createDateTime.getTime());
    }

    public String getCompleteDateTimeStr(){
        String year = DateTime.IsCurrentYear(createDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m", Locale.CHINA);
        return createDateTimeFStr.format(completeDateTime.getTime());
    }

    public String getBeginDateStr(){
        String year = DateTime.IsCurrentYear(startDateTime) ? "" : "yyyy ";
        SimpleDateFormat beginDateTimeFStr = new SimpleDateFormat(year + "MMM  dd", Locale.CHINA);
        return beginDateTimeFStr.format(startDateTime.getTime());
    }
    public String getEndDateStr(){
        if (IsOneDay()) return "";
        String year = DateTime.IsCurrentYear(dueDateTime) ? "" : "yyyy ";
        String month = DateTime.SameYearMonth(startDateTime, dueDateTime) ? "" : "MMM  ";
        SimpleDateFormat dueDateTimeFStr = new SimpleDateFormat(year + month + "dd", Locale.CHINA);
        return dueDateTimeFStr.format(dueDateTime.getTime());
    }
    public String getDateRangeStr(){
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
        SimpleDateFormat beginDate = new SimpleDateFormat(beginDateFormatStr, Locale.CHINA);
        SimpleDateFormat endDate = new SimpleDateFormat(endDateFormatStr, Locale.CHINA);
        String endDateStr = IsOneDay() ? "" : endDate.format(dueDateTime.getTime());
        return beginDate.format(startDateTime.getTime()) + endDateStr;
    }

    public String getTimeRangeStr(){
        if (IsAllDay()) return "";
        String timeFormatStr = "H:mm";
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormatStr, Locale.CHINA);
        String endTime = IsOneTime() ? "" : " - " + simpleTimeFormat.format(dueDateTime.getTime());
        return simpleTimeFormat.format(startDateTime.getTime()) + endTime;
    }

    public void finish(){
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
