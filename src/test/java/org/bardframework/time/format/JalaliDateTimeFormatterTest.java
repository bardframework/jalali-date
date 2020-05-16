package org.bardframework.time.format;

import org.bardframework.time.utils.DateTimeUtils;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

class JalaliDateTimeFormatterTest {

    @Test
    void test() throws Exception {
        Date generatedDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        String pattern = "yyyy MM dd HH";
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = Instant.ofEpochMilli(generatedDate.getTime()).atZone(zoneId).toLocalDateTime();
        String jalaliString = DateTimeUtils.formatJalali(localDateTime, pattern);
        Date revertedDate = DateTimeUtils.getDateOfJalaliString(jalaliString, pattern, zoneId);
        System.out.println(generatedDate + " -> " + jalaliString + " -> " + " -> " + revertedDate);
    }
}