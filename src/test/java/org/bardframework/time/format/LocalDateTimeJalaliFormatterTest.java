package org.bardframework.time.format;

import org.bardframework.time.LocalDateJalali;
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
        LocalDateTimeJalali jalaliDateTime = LocalDateTimeJalali.parse(jalaliString, formatter);
        LocalDateTime localDateTime = jalaliDateTime.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatJalali(LocalDateTime dateTime, String pattern) {
        DateTimeFormatterJalali formatter = DateTimeFormatterJalali.ofPattern(pattern);
        return formatter.format(LocalDateTimeJalali.of(dateTime));
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

    @Test
    void testJalali() {
        String date = "1398/06/01 12:00:00";
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatterJalali fmt = DateTimeFormatterJalali.ofPattern("yyyy/MM/dd HH:mm:ss");
        Instant first = LocalDateTimeJalali.from(fmt.parse(date)).atZone(zoneId).toInstant();
        Instant second = LocalDateTimeJalali.from(fmt.parse(date)).toLocalDateTime().atZone(zoneId).toInstant();

        System.out.println(date + " -> " + first + " & " + second);
    }

    @Test
    void testJalali2() {
        LocalDateJalali first = LocalDateJalali.of(1400, 12, 29);
        LocalDateJalali second = first.plusDays(1);

        System.out.println(first + " -> " + second);
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
}