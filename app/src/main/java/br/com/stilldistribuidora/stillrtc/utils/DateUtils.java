package br.com.stilldistribuidora.stillrtc.utils;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ack Lay (Cleidimar Viana) on 11/19/2016.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class DateUtils {

    public static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";


    public static final String FORMAT_DATE_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

    public static final String FORMAT_DATE_TIME_ZONE_PICTURE = "yyyyMMdd'T'HHmmssZZZZZ";

    public static String formateDate(String dateString) {
        Date date;
        String day = "";

        int monthNumber = 0;
        try {
            date = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).parse(dateString);
            day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);

            monthNumber = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String monthText = new DateFormatSymbols(Locale.getDefault()).getMonths()[monthNumber - 1];

        return day.concat(" " + monthText.substring(0, 3).toUpperCase());
    }

    public static String formateDateAdapter(String dateString) {
        Date date;
        String day = "";
        String year = "";
        int monthNumber = 0;
        try {
            date = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).parse(dateString);
            day = new SimpleDateFormat("dd", Locale.getDefault()).format(date);
            year = new SimpleDateFormat("yy", Locale.getDefault()).format(date);
            monthNumber = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(date));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String monthText = new DateFormatSymbols(Locale.getDefault()).getMonths()[monthNumber - 1];

        return day.concat(" " + monthText.substring(0, 3).toUpperCase()).concat(" " + year);
    }

    public static String convertDatetimeStringInDate(String dateString) {

        String strDte = "";
        try {
            return DateFormat.getDateInstance().format(new SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDte;
    }

    public static String formateHour(String dateString) {
        Date date;
        String hour = "";
        try {
            date = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).parse(dateString);
            hour = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return hour;
    }

    public static String currentDate() {
        return DateFormat.getDateInstance().format(new Date());
    }

    public static String currentHour() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        String strMinute;
        if (minute < 10) {
            strMinute = ":0" + minute;
        } else {
            strMinute = ":" + minute;
        }

        String strHour;

        if (hour < 10) {
            strHour = "0" + hour;
        } else {
            strHour = "" + hour;
        }
        return strHour + strMinute;
    }

    public static String currentDateOnly() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return df.format(c.getTime());
    }

    public static int[] currentHourInt() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new int[]{hour, minute};
    }

    public static int[] currentDateInt() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new int[]{year, month, day};
    }

    public static String recuperarDateTimeAtual() {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME_ZONE, Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }

    public static String recuperarDateTimeAtualPicture() {
        DateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE_TIME_ZONE_PICTURE, Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }


    public static int compareDate(String date) {
        int days = 0;
        SimpleDateFormat myFormat = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault());

        try {
            Date date1 = myFormat.parse(date);
            Date date2 = myFormat.parse(recuperarDateTimeAtual());
            long diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    public static String formateT() {

        final Calendar c = Calendar.getInstance();

        String day = "" + c.get(Calendar.DAY_OF_MONTH);
        String monthText = new DateFormatSymbols(Locale.getDefault()).getMonths()[c.get(Calendar.MONTH)];

        return day.concat(" " + monthText.substring(0, 3)).concat(" " + c.get(Calendar.YEAR));
    }

    public static String compararDataRetornarMinutos(String dtInicio, String dtFim) {

        SimpleDateFormat myFormat = new SimpleDateFormat(FORMAT_DATE_TIME_ZONE, Locale.getDefault());

        try {
            Date date1 = myFormat.parse(dtInicio);
            Date date2 = myFormat.parse(dtFim);
            long diff = date2.getTime() - date1.getTime();

            long minutes = ((diff / 1000) / 60) % 60;
            long seconds = (diff / 1000) % 60;

            return TimeUnit.MILLISECONDS.toHours(diff) + "h " + minutes + "m " + seconds + "s";
        } catch (ParseException e) {
            e.printStackTrace();
            return "0h 0m 0s";
        }
    }

    public static long calcDiff(String dtStart, String dtEnd) {
        SimpleDateFormat myFormat = new SimpleDateFormat(FORMAT_DATE, Locale.getDefault());
        long diff = 0;
        try {
            Date date1 = myFormat.parse(dtStart);
            Date date2 = myFormat.parse(dtEnd);

            return date2.getTime() - date1.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return diff;
        }
    }

    public static String currentDatetimeUTC() throws ParseException {
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));

        //Time in GMT
        return df.format(new Date());
    }


}