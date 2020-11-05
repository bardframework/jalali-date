package org.bardframework.time.format;

import org.bardframework.time.LocaleDateTimeJalali;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.Date;

class LocaleDateTimeJalaliFormatterTest {

    public static Date getDateOfJalaliString(String jalaliString, String pattern, ZoneId zoneId) {
        DateTimeFormatterJalali formatter = DateTimeFormatterJalali.ofPattern(pattern);
        LocaleDateTimeJalali jalaliDateTime = LocaleDateTimeJalali.parse(jalaliString, formatter);
        LocalDateTime localDateTime = jalaliDateTime.toLocalDateTime();
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        return Date.from(zonedDateTime.toInstant());
    }

    public static String formatJalali(LocalDateTime dateTime, String pattern) {
        DateTimeFormatterJalali formatter = DateTimeFormatterJalali.ofPattern(pattern);
        return formatter.format(LocaleDateTimeJalali.of(dateTime));
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