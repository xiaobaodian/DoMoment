package threecats.zhang.domoment.DataStructures;

import android.text.format.DateFormat;

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

    private DateTimeHelper DateTime = App.getDateTime();

    private TaskItem taskItem = null;
    private TaskDate extStartDay = new TaskDate();
    private TaskDate extDueDay = new TaskDate();
    private TaskTime extBeginTime = new TaskTime();
    private TaskTime extEndTime = new TaskTime();
    private Calendar extCreateDateTime;
    private Calendar extStartDateTime;
    private Calendar extDueDateTime;
    private Calendar extCompleteDateTime;
    private TaskPriority extPriority;

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
            saveToTaskItem();
        }
        this.taskItem = taskItem;

        extCreateDateTime = Calendar.getInstance();
        extCreateDateTime.setTime(taskItem.getCreateDateTime());

        extStartDateTime = Calendar.getInstance();
        extStartDateTime.setTime(taskItem.getStartDateTime());

        extDueDateTime = Calendar.getInstance();
        extDueDateTime.setTime(taskItem.getDueDateTime());

        extCompleteDateTime = Calendar.getInstance();
        extCompleteDateTime.setTime(taskItem.getCompleteDateTime());

        setStartDateAndTime(extStartDateTime);
        setDueDateAndTime(extDueDateTime);

        extPriority = TaskPriority.values()[taskItem.getPriority()];
    }
    public TaskItem getTaskItem(){
        return this.taskItem;
    }

    public void setExtCreateDateTime(Calendar extCreateDateTime){
        this.extCreateDateTime = extCreateDateTime;
        taskItem.setCreateDateTime(extCreateDateTime.getTime());
    }
    public Calendar getExtCreateDateTime(){
        //taskItem应用可能在别处被修改，所以在调用的时候需要从taskItem实例中重新获取数值
        extCreateDateTime.setTime(taskItem.getCreateDateTime());
        return this.extCreateDateTime;
    }
    public void setExtStartDateTime(Calendar extStartDateTime){
        this.extStartDateTime = extStartDateTime;
        taskItem.setIsNoDate(false);
        setStartDateAndTime(extStartDateTime);
        taskItem.setStartDateTime(extStartDateTime.getTime());
    }
    public void setLongStartDateTime(long longStartDateTime){
        extStartDateTime.setTimeInMillis(longStartDateTime);
        taskItem.setIsNoDate(false);
        setStartDateAndTime(extStartDateTime);
        taskItem.setStartDateTime(extStartDateTime.getTime());
    }
    public Calendar getExtStartDateTime(){
        extStartDateTime.setTime(taskItem.getStartDateTime());
        return this.extStartDateTime;
    }
    public long getLongStartDateTime(){
        //taskItem应用可能在别处被修改，所以在调用的时候需要从taskItem实例中重新获取数值
        extStartDateTime.setTime(taskItem.getStartDateTime());
        return this.extStartDateTime.getTimeInMillis();
    }

    public void setExtDueDateTime(Calendar extDueDateTime){
        this.extDueDateTime = extDueDateTime;
        taskItem.setIsNoDate(false);
        setDueDateAndTime(extDueDateTime);
        taskItem.setDueDateTime(extDueDateTime.getTime());
    }
    public void setLongDueDateTime(long longDueDateTime){
        this.extDueDateTime.setTimeInMillis(longDueDateTime);
        taskItem.setIsNoDate(false);
        setDueDateAndTime(extDueDateTime);
        taskItem.setDueDateTime(extDueDateTime.getTime());
    }
    public Calendar getExtDueDateTime(){
        extDueDateTime.setTime(taskItem.getDueDateTime());
        return extDueDateTime;
    }
    public long getLongDueDateTime(){
        extDueDateTime.setTime(taskItem.getDueDateTime());
        return extDueDateTime.getTimeInMillis();
    }

    public void setExtCompleteDateTime(Calendar extCompleteDateTime){
        this.extCompleteDateTime = extCompleteDateTime;
        taskItem.setCompleteDateTime(extCompleteDateTime.getTime());
    }
    public Calendar getExtCompleteDateTime(){
        extCompleteDateTime.setTime(taskItem.getCompleteDateTime());
        return extCompleteDateTime;
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
        setAllDay(true);
        taskItem.setIsNoDate(true);
        extDueDateTime = (Calendar) extStartDateTime.clone();
        taskItem.setDueDateTime(extDueDateTime.getTime());
    }
    public boolean IsNoDate(){
        return taskItem.getIsNoDate();
    }
    public void setExtPriority(TaskPriority extPriority){
        taskItem.setPriority(extPriority.ordinal());
        this.extPriority = extPriority;
    }
    public TaskPriority getExtPriority(){
        return extPriority;
    }
    public void setComplete(boolean isComplete){
        if (isComplete) {
            extCompleteDateTime = Calendar.getInstance();
            taskItem.setCompleteDateTime(extCompleteDateTime.getTime());
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
        return extStartDay.Year == extDueDay.Year && extStartDay.Month == extDueDay.Month && extStartDay.Day == extDueDay.Day;
    }
    public boolean IsOneTime(){
        return extBeginTime.Hour == extEndTime.Hour && extBeginTime.Minute == extEndTime.Minute;
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
        extStartDay.setDate(year, month, day);
        extStartDateTime = extStartDay.getCalendar(extStartDateTime);
        taskItem.setStartDateTime(extStartDateTime.getTime());

        Date e = extStartDateTime.getTime();
        Date s = taskItem.getStartDateTime();
        Date d = taskItem.getDueDateTime();

    }
    public void setDueDate(int year, int month, int day){
        extDueDay.setDate(year, month, day);
        extDueDateTime = extDueDay.getCalendar(extDueDateTime);
        taskItem.setDueDateTime(extDueDateTime.getTime());
    }

    public void setBeginTime(int hour, int minute){
        taskItem.setIsAllDay(false);
        if (IsOneTime()) {
            setEndTime(hour, minute);
        }
        extBeginTime.setTime(hour, minute);
        extStartDateTime = extBeginTime.getCalendar(extStartDateTime);
        taskItem.setStartDateTime(extStartDateTime.getTime());
    }
    public void setEndTime(int hour, int minute){
        taskItem.setIsAllDay(false);
        extEndTime.setTime(hour, minute);
        extDueDateTime = extEndTime.getCalendar(extDueDateTime);
        taskItem.setDueDateTime(extDueDateTime.getTime());
    }
    public void setOneDay(OneDayBase base){
        if (base == OneDayBase.Start) {
            setDueDate(extStartDay.Year, extStartDay.Month, extStartDay.Day);
        } else {
            setStartDate(extDueDay.Year, extDueDay.Month, extDueDay.Day);
        }
    }
    public void setOneTime(OneDayBase base){
        if (base == OneDayBase.Start) {
            setEndTime(extBeginTime.Hour, extBeginTime.Minute);
        } else {
            setBeginTime(extEndTime.Hour, extEndTime.Minute);
        }
    }

    public String getBeginTimeStr(){
        final String timeFormatString = "H:mm";
        return (String) DateFormat.format(timeFormatString, extStartDateTime);
    }
    public String getEndTimeStr(){
        final String timeFormatString = "H:mm";
        return (String) DateFormat.format(timeFormatString, extDueDateTime);
    }

    public String getCreatedDateTimeStr(){
        String year = DateTime.IsCurrentYear(extCreateDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m", Locale.CHINA);
        return createDateTimeFStr.format(taskItem.getCreateDateTime());
    }

    public String getCompleteDateTimeStr(){
        String year = DateTime.IsCurrentYear(extCreateDateTime) ? "" : "yyyy ";
        SimpleDateFormat createDateTimeFStr = new SimpleDateFormat(year + "MMMd  H:m");
        return createDateTimeFStr.format(taskItem.getCompleteDateTime());
    }

    public String getBeginDateStr(){
        String year = DateTime.IsCurrentYear(extStartDateTime) ? "" : "yyyy ";
        SimpleDateFormat beginDateTimeFStr = new SimpleDateFormat(year + "MMM  dd");
        return beginDateTimeFStr.format(taskItem.getStartDateTime());
    }
    public String getEndDateStr(){
        if (IsOneDay()) return "";
        String year = DateTime.IsCurrentYear(extDueDateTime) ? "" : "yyyy ";
        String month = DateTime.SameYearMonth(extStartDateTime, extDueDateTime) ? "" : "MMM  ";
        SimpleDateFormat dueDateTimeFStr = new SimpleDateFormat(year + month + "dd");
        return dueDateTimeFStr.format(taskItem.getDueDateTime());
    }
    public String getDateRangeStr(){
        if (IsNoDate()) return "DoDate";
        String beginDateFormatStr = "";
        String endDateFormatStr = "";
        if (DateTime.SameYearMonth(extStartDateTime, extDueDateTime)) {
            beginDateFormatStr = DateTime.IsCurrentYear(extStartDateTime) ? "MMM d" : "yyyy MMM d";
            endDateFormatStr = " - d";
        } else {
            beginDateFormatStr = DateTime.IsCurrentYear(extStartDateTime) ? "MMM d" : "yyyy MMM d";
            endDateFormatStr = DateTime.IsCurrentYear(extStartDateTime) ? " - MMM d" : " - yyyy MMM d";
        }
        SimpleDateFormat beginDate = new SimpleDateFormat(beginDateFormatStr);
        SimpleDateFormat endDate = new SimpleDateFormat(endDateFormatStr);
        String endDateStr = IsOneDay() ? "" : endDate.format(extDueDateTime.getTime());
        return beginDate.format(extStartDateTime.getTime()) + endDateStr;
    }

    public String getTimeRangeStr(){
        if (IsAllDay()) return "";
        String timeFormatStr = "H:mm";
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat(timeFormatStr);
        String endTime = IsOneTime() ? "" : " - " + simpleTimeFormat.format(extDueDateTime.getTime());
        return simpleTimeFormat.format(extStartDateTime.getTime()) + endTime;
    }

    public void saveToTaskItem(){
        taskItem.setCreateDateTime(extCreateDateTime.getTime());
        taskItem.setStartDateTime(extStartDateTime.getTime());
        taskItem.setDueDateTime(extDueDateTime.getTime());
        taskItem.setCompleteDateTime(extCompleteDateTime.getTime());
    }

    private void setStartDateAndTime(Calendar date){
        extStartDay.setDate(date.getTime());
        extBeginTime.setTime(date.getTime());
    }

    private void setDueDateAndTime(Calendar date){
        extDueDay.setDate(date.getTime());
        extEndTime.setTime(date.getTime());
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
