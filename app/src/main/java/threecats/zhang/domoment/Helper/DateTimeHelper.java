package threecats.zhang.domoment.Helper;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * 由 zhang 于 2017/8/15 创建
 */

public class DateTimeHelper {

    public DateTimeHelper(){
    }

    public static void setCalendarDate(Calendar date, int year, int month, int day){
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

    public static boolean isCurrentYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
    }
    public static boolean isCurrentYear(Calendar calendar){
        return calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
    }

    public static boolean isCurrentMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        boolean currentMonth;
        if (isCurrentYear(date)) {
            currentMonth = calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH);
        } else {
            currentMonth = false;
        }
        return currentMonth;
    }

    public static void setCalendarFromDatePicker(Calendar date, DatePicker datePicker){
        date.set(Calendar.YEAR, datePicker.getYear());
        date.set(Calendar.MONTH, datePicker.getMonth());
        date.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
    }

    public static boolean sameYearMonth(Calendar c1, Calendar c2){
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
    }

    public static boolean sameYMD(Calendar c1, Calendar c2){
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
               c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
               c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    public static String getYearFormatStr(Date date){
        return isCurrentYear(date) ? "" : "yyyy ";
    }
    public static String getYearFormatStr(Calendar date){
        return isCurrentYear(date) ? "" : "yyyy ";
    }

    public static String getDateWeekStr(Calendar date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMdd  E");
        return dateFormat.format(date.getTime());
    }

    public static Calendar buildTimePoint(int days){
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

    public static int getDayOfWeek(Calendar day){
        int Site = day.get(Calendar.DAY_OF_WEEK);
        if (Site == 1) Site = 8;
        return --Site;
    }

    public static int getDayOfWeek(LocalDate day){
        //DayOfWeek dayOfWeek = DayOfWeek.MONDAY;
        return day.getDayOfWeek().ordinal();
    }

    public static boolean isCurrentDateChanged(){
        return ! Calendar.getInstance().before(buildTimePoint(1));
    }

}
