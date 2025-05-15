package Utilities.DateUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static String setDateTimeFormat1() {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HHmmss");
        formatter.setTimeZone(timeZone);
        String today = formatter.format(date);
        return today;
    }

    public static String setDateTimeTestRail() {
        TimeZone timeZone = TimeZone.getTimeZone("CST");
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH:mm");
        formatter.setTimeZone(timeZone);
        String today = formatter.format(date);
        return today;
    }

    public static String setDateTimeFormat2() {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("ddMMyyHHmmss");
        formatter.setTimeZone(timeZone);
        String today = formatter.format(date);
        return today;
    }

    public static String setDateTimeFormat3() {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        formatter.setTimeZone(timeZone);
        String today = formatter.format(date);
        return today;
    }

    public static String setDateTimeFormat4() {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        formatter.setTimeZone(timeZone);
        String today = formatter.format(date);
        return today;
    }

    public static String setNowAndThenDateTime() {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date myDate = new Date(System.currentTimeMillis());
        System.out.println("Now "+ dateFormat.format(myDate));
        Calendar cal = Calendar.getInstance();
        dateFormat.setTimeZone(timeZone);
        cal.setTime(myDate);
        cal.add(Calendar.DATE, -7);
        return dateFormat.format(cal.getTime());
    }

    public static String setNowAndFutureDateTime(int howManyDays) {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date myDate = new Date(System.currentTimeMillis());
        System.out.println("Now "+ dateFormat.format(myDate));
        Calendar cal = Calendar.getInstance();
        dateFormat.setTimeZone(timeZone);
        cal.setTime(myDate);
        cal.add(Calendar.DATE, +howManyDays);
        return dateFormat.format(cal.getTime());
    }

    public static String setNowAndBackDateTime(int howManyDays) {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        Date myDate = new Date(System.currentTimeMillis());
        System.out.println("Now "+ dateFormat.format(myDate));
        Calendar cal = Calendar.getInstance();
        dateFormat.setTimeZone(timeZone);
        cal.setTime(myDate);
        cal.add(Calendar.DATE, -howManyDays);
        return dateFormat.format(cal.getTime());
    }


    public static String setNowAndThenDateTime1(int howManyDays) {
        TimeZone timeZone = TimeZone.getTimeZone("IST");
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date myDate = new Date(System.currentTimeMillis());
        System.out.println("Now "+ dateFormat.format(myDate));
        Calendar cal = Calendar.getInstance();
        dateFormat.setTimeZone(timeZone);
        cal.setTime(myDate);
        cal.add(Calendar.DATE, -howManyDays);
        return dateFormat.format(cal.getTime());
    }
}
