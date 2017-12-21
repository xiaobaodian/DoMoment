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

    public static void SetCalendarDate(Calendar date, int year, int month, int day){
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);
    }

    public static Calendar getTomorrow(){
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, 1);
        return day;
    }

    public static Calendar getAfterTomorrow(){
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, 2);
        return day;
    }

    public static boolean IsCurrentYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
    }
    public static boolean IsCurrentYear(Calendar calendar){
        return calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
    }

    public static boolean IsCurrentMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean currentMonth;
        if (IsCurrentYear(date)) {
            currentMonth = calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH);
        } else {
            currentMonth = false;
        }
        return currentMonth;
    }

    public static void  BlendCalendar(Calendar date, DatePicker datePicker){
        date.set(Calendar.YEAR, datePicker.getYear());
        date.set(Calendar.MONTH, datePicker.getMonth());
        date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
    }

    public static boolean SameYearMonth(Calendar c1, Calendar c2){
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    public static boolean SameYMD(Calendar c1, Calendar c2){
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
               c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
               c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    public static String getyearFormatStr(Date date){
        return IsCurrentYear(date) ? "" : "yyyy ";
    }
    public static String getyearFormatStr(Calendar date){
        return IsCurrentYear(date) ? "" : "yyyy ";
    }

    public static String getDateWeekStr(Calendar date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMdd  E");
        return dateFormat.format(date.getTime());
    }

    public static Calendar BuildTimePoint(int days){
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

    public static int GetDayOfWeek(Calendar day){
        int Site = day.get(Calendar.DAY_OF_WEEK);
        if (Site == 1) Site = 8;
        return --Site;
    }

    public static int getDayOfWeek(LocalDate day){
        //DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        return day.getDayOfWeek().ordinal();
    }

    public static boolean IsCurrentDateChanged(){
        return ! Calendar.getInstance().before(BuildTimePoint(1));
    }

    public static void MarkToday(){
        //currentDate = BuildTimePoint(1);
    }
}
