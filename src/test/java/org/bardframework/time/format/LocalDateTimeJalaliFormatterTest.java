package org.bardframework.time.format;

import org.bardframework.time.LocalDateTimeJalali;
import org.bardframework.time.ZonedDateTimeJalali;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

class LocalDateTimeJalaliFormatterTest {

    public static Date getDateOfJalaliString(String jalaliString, String pattern, ZoneId zoneId) {
        DateTimeFormatterJalali formatter = DateTimeFormatterJalali.ofPattern(pattern);
        LocalDateTimeJalali dateTimeJalali = LocalDateTimeJalali.parse(jalaliString, formatter);
        LocalDateTime localDateTime = dateTimeJalali.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatJalali(LocalDateTime dateTime, String pattern) {
        DateTimeFormatterJalali formatter = DateTimeFormatterJalali.ofPattern(pattern);
        return formatter.format(LocalDateTimeJalali.of(dateTime));
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        Locale locale = new Locale("en");
        String pattern = "yyyy MMM dd HH:mm";
        LocalDateTimeJalali localDateTimeJalali = LocalDateTimeJalali.of(localDateTime);
        ZonedDateTimeJalali zonedDateTimeJalali = localDateTimeJalali.atZone(zoneId);
        System.out.println(DateTimeFormatterJalali.ofPattern(pattern).withLocale(locale).withZone(zoneId).format(zonedDateTimeJalali));
        System.out.println(DateTimeFormatter.ofPattern(pattern).withLocale(locale).withZone(zoneId).format(localDateTime));
    }

    @Test
    void test() {
        Date generatedDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        String pattern = "yyyy MM dd HH:mm";
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = Instant.ofEpochMilli(generatedDate.getTime()).atZone(zoneId).toLocalDateTime();
        String jalaliString = formatJalali(localDateTime, pattern);
        Date revertedDate = getDateOfJalaliString(jalaliString, pattern, zoneId);
        System.out.println(generatedDate + " -> " + jalaliString + " -> " + " -> " + revertedDate);
    }
}