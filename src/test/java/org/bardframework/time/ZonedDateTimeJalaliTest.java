package org.bardframework.time;

import org.bardframework.time.format.DateTimeFormatterJalali;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;

public class ZonedDateTimeJalaliTest {
    public static void main(String[] args) {

        String pattern = "dd MMM yyyy H:mm";
//        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.parse("2020-08-05T00:00:11");
        ZoneId zoneId = ZoneId.of("Asia/Tehran");
        TimeZone timeZone = TimeZone.getTimeZone(zoneId);
        System.out.println(localDateTime);
        localDateTime = localDateTime.plus(timeZone.getRawOffset(), ChronoUnit.MILLIS);
        ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
        System.out.println(DateTimeFormatter.ofPattern(pattern).format(zonedDateTime));

        LocaleDateTimeJalali localeDateTimeJalali = LocaleDateTimeJalali.of(localDateTime);
        ZonedDateTimeJalali zonedDateTimeJalali = localeDateTimeJalali.atZone(zoneId);
        System.out.println(DateTimeFormatterJalali.ofPattern(pattern).withZone(ZoneOffset.UTC).format(zonedDateTimeJalali));
        System.out.println(DateTimeFormatterJalali.ofPattern(pattern).format(zonedDateTimeJalali));
        zonedDateTimeJalali = ZonedDateTimeJalali.of(1399, 5, 20, 3, 30, 0, 0, zoneId);
        System.out.println(zonedDateTimeJalali);
    }
}
