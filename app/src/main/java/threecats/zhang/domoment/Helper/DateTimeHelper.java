package threecats.zhang.domoment.Helper;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhang on 2017/8/15.
 */

public class DateTimeHelper {
    private Calendar tempCalendarDay;  //用于日期计算的临时变量，多处使用，状态不稳定。使用前一定需要重新设置里面的字段
    private Calendar currentDate;

    public DateTimeHelper(){
        tempCalendarDay = Calendar.getInstance();
        MarkToday();
    }

    public Date Now(){
        tempCalendarDay = Calendar.getInstance();
        return tempCalendarDay.getTime();
    }

    public void SetDate(Calendar date, int year, int month, int day){
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
    }

    public boolean IsCurrentYear(Date date){
        tempCalendarDay.setTime(date);
        return tempCalendarDay.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
    }
    public boolean IsCurrentYear(Calendar calendar){
        return calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
    }

    public boolean IsCurrentMonth(Date date){
        tempCalendarDay.setTime(date);
        boolean currentMonth;
        if (IsCurrentYear(date)) {
            currentMonth = tempCalendarDay.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH);
        } else {
            currentMonth = false;
        }
        return currentMonth;
    }

    public void  BlendCalendar(Calendar date, DatePicker datePicker){
        date.set(Calendar.YEAR, datePicker.getYear());
        date.set(Calendar.MONTH, datePicker.getMonth());
        date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
    }

    public boolean SameYearMonth(Calendar c1, Calendar c2){
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    public boolean SameYMD(Calendar c1, Calendar c2){
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
               c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
               c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    public Date Clone(Date date){
        if (date == null) {
            date = Now();
        }
        tempCalendarDay.setTime(date);
        return tempCalendarDay.getTime();
    }

    public String getyearFormatStr(Date date){
        return IsCurrentYear(date) ? "" : "yyyy ";
    }
    public String getyearFormatStr(Calendar date){
        return IsCurrentYear(date) ? "" : "yyyy ";
    }

    public String getDateWeekStr(Calendar date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMdd  E");
        return dateFormat.format(date.getTime());
    }

    public Calendar BuildTimePoint(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,days);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public LocalDate makeTimePoint(long days){
        LocalDate timePoint = LocalDate.now();
        timePoint.plusDays(days);
        return timePoint;
    }

    public int GetDayOfWeek(Calendar day){
        int Site = day.get(Calendar.DAY_OF_WEEK);
        if (Site == 1) Site = 8;
        return --Site;
    }

    public int getDayOfWeek(LocalDate day){
        //DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        return day.getDayOfWeek().ordinal();
    }

    public boolean IsCurrentDateChanged(){
        return ! Calendar.getInstance().before(currentDate);
    }

    public void MarkToday(){
        currentDate = BuildTimePoint(1);
    }
}
