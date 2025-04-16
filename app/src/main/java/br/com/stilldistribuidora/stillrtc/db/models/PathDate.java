package br.com.stilldistribuidora.stillrtc.db.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.stilldistribuidora.stillrtc.utils.DateUtils;

/**
 * Created by Still Technology and Development Team on 06/11/2017.
 */

public class PathDate {
    private static final ThreadLocal<DateFormat> DF = new ThreadLocal<DateFormat>() {
        @Override public DateFormat initialValue() {
            return new SimpleDateFormat(DateUtils.FORMAT_DATE_TIME_ZONE, Locale.ENGLISH);
        }
    };

    private final Date date;

    public PathDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return DF.get().format(date);
    }
}