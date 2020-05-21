package org.bardframework.time.utils;

import org.bardframework.time.JalaliDate;
import org.bardframework.time.JalaliDateTime;
import org.bardframework.time.format.JalaliDateTimeFormatter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Vahid Zafari on 8/12/2016.
 */
public final class DateTimeUtils {

    private DateTimeUtils() {
    }

    /**
     * convert date to persian date string divided by '/'
     *
     * @param date the date
     * @return null if date is null, else persian date with 'yyyy/mm/dd' format
     */
    public static String toPersian(LocalDate date) {
        return null == date ? "" : JalaliDate.of(date).format(DateTimeFormatter.ofPattern("yyy/MM/dd"));
    }

    /**
     * convert date to persian date string divided by '/'
     *
     * @param dateTime the dateTime
     * @return null if date is null, else persian date with 'yyyy/MM/dd HH:mm' format
     */
    public static String toPersian(LocalDateTime dateTime) {
        return null == dateTime ? "" : JalaliDateTime.of(dateTime).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    }


    public static Date getDateOfJalaliString(String jalaliString, String pattern, ZoneId zoneId) {
        JalaliDateTimeFormatter formatter = JalaliDateTimeFormatter.ofPattern(pattern);
        JalaliDateTime jalaliDateTime = JalaliDateTime.parse(jalaliString, formatter);
        LocalDateTime localDateTime = jalaliDateTime.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date getDateOfJalaliString(String jalaliString, ZoneId zoneId) {
        JalaliDateTime jalaliDateTime = JalaliDateTime.of(jalaliString);
        LocalDateTime localDateTime = jalaliDateTime.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatJalali(LocalDateTime dateTime, String pattern) {
        JalaliDateTimeFormatter formatter = JalaliDateTimeFormatter.ofPattern(pattern);
        return formatter.format(JalaliDateTime.of(dateTime));
    }

    public static Date parseJalali(String jalaliDateTime) {
        LocalDateTime ldt = JalaliDateTime.of(jalaliDateTime).toLocalDateTime();
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
        return Date.from(zdt.toInstant());
    }
}
