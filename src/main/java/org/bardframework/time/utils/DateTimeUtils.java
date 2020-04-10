package org.bardframework.time.utils;

import org.bardframework.time.JalaliDate;
import org.bardframework.time.JalaliDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vahid Zafari on 8/12/2016.
 */
public final class DateTimeUtils {

    public static final long YEAR_DURATION_MILLS = 365 * 24 * 3600 * 1000L;
    private static final Logger LOGGER = LoggerFactory.getLogger(DateTimeUtils.class);
    private static final ZoneId UTCZone = ZoneOffset.UTC;

    private DateTimeUtils() {
    }

    /**
     * calculate milliseconds past from the epoch of 1970-01-01T00:00:00Z.
     * <p>
     *
     * @param date an instance of (@code JalaliDate)
     * @return past milliseconds from 1970-01-01T00:00:00Z according (!@code date) at 23:59:59:99:999.
     * @throws IllegalArgumentException if null date passed to method.
     */
    public static long toEpochMills(LocalDate date) {
        if (null == date) {
            throw new IllegalArgumentException("null date not acceptable.");
        }
        return date.toEpochDay() * 24 * 60 * 60 * 1000L;
    }


    public static LocalDate fromEpochMills(long dateAsMills) {
        return fromEpochMills(dateAsMills, UTCZone).toLocalDate();
    }

    /**
     * Obtains an instance of {@code LocalDateTime} using milliseconds from the
     * epoch of 1970-01-01T00:00:00Z.
     * <p>
     * The seconds and nanoseconds are extracted from the specified milliseconds.
     *
     * @param dateTimeAsMills the number of milliseconds after 1970-01-01T00:00:00Z
     * @return a LocalDateTime, not null
     * @throws DateTimeException if the instant exceeds the maximum or minimum instant
     */
    public static LocalDateTime dateTimeFromEpochMills(long dateTimeAsMills) {
        return fromEpochMills(dateTimeAsMills, UTCZone);
    }

    /**
     * Obtains an instance of {@code LocalDateTime} using milliseconds from the
     * epoch of 1970-01-01T00:00:00Z.
     * <p>
     * The seconds and nanoseconds are extracted from the specified milliseconds.
     *
     * @param dateTimeAsMills the number of milliseconds from 1970-01-01T00:00:00Z.
     * @param zone            an isntance of (@code ZoneId) specify zone id for calculating LocalDateTime
     * @return a LocalDateTime, not null
     * @throws DateTimeException        if the instant exceeds the maximum or minimum instant
     * @throws IllegalArgumentException if null zone passed to method.
     */
    public static LocalDateTime fromEpochMills(long dateTimeAsMills, ZoneId zone) {
        if (null == zone) {
            throw new IllegalArgumentException("null zone not acceptable.");
        }
        return Instant.ofEpochMilli(dateTimeAsMills).atZone(zone).toLocalDateTime();
    }

    /**
     * calculatee milliseconds past from the epoch of 1970-01-01T00:00:00Z.
     * <p>
     *
     * @param dateTime an instance of (@code LocalDateTime)
     * @return past milliseconds from 1970-01-01T00:00:00Z.
     * @throws IllegalArgumentException if null dateTime passed to method.
     */
    public static long toEpochMills(LocalDateTime dateTime) {
        if (null == dateTime) {
            throw new IllegalArgumentException("null dateTime not acceptable.");
        }
        return toEpochMills(dateTime.toLocalDate())
            + (dateTime.getHour() * 60 * 60 * 1000L)
            + (dateTime.getMinute() * 60 * 1000L)
            + (dateTime.getSecond() * 1000L)
            + dateTime.getNano() / 1000000;
    }

    public static long toEpochMills(OffsetDateTime dateTime) {
        if (null == dateTime) {
            throw new IllegalArgumentException("null dateTime not acceptable.");
        }
        return toEpochMills(dateTime.toLocalDate())
            + (dateTime.getHour() * 60 * 60 * 1000L)
            + (dateTime.getMinute() * 60 * 1000L)
            + (dateTime.getSecond() * 1000L)
            + dateTime.getNano() / 1000000;
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

    /**
     * convert date to string divided by '/'
     *
     * @param dateTime the dateTime
     * @return null if date is null, else date with 'yyyy/MM/dd HH:mm' format
     */
    public static String toLocalString(LocalDateTime dateTime) {
        return null == dateTime ? "" : dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    }

    /**
     * convert date to string divided by '/'
     *
     * @param date the dateTime
     * @return null if date is null, else date with 'yyyy/MM/dd' format
     */
    public static String toLocalString(LocalDate date) {
        return null == date ? "" : date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public static TimeUnit toTimeUnit(ChronoUnit unit) {
        if (unit == null) {
            return null;
        }
        switch (unit) {
            case DAYS:
                return TimeUnit.DAYS;
            case HOURS:
                return TimeUnit.HOURS;
            case MINUTES:
                return TimeUnit.MINUTES;
            case SECONDS:
                return TimeUnit.SECONDS;
            case MICROS:
                return TimeUnit.MICROSECONDS;
            case MILLIS:
                return TimeUnit.MILLISECONDS;
            case NANOS:
                return TimeUnit.NANOSECONDS;
            default:
                //TODO support the rest
                throw new UnsupportedOperationException("Man, use a real temporal unit");
        }
    }

    public static ChronoUnit toChronoUnit(TimeUnit unit) {
        if (unit == null) {
            return null;
        }
        switch (unit) {
            case DAYS:
                return ChronoUnit.DAYS;
            case HOURS:
                return ChronoUnit.HOURS;
            case MINUTES:
                return ChronoUnit.MINUTES;
            case SECONDS:
                return ChronoUnit.SECONDS;
            case MICROSECONDS:
                return ChronoUnit.MICROS;
            case MILLISECONDS:
                return ChronoUnit.MILLIS;
            case NANOSECONDS:
                return ChronoUnit.NANOS;
            default:
                assert false : "there are no other TimeUnit ordinal values";
                return null;
        }
    }

    public static long getNowUtcMills() {
        return DateTimeUtils.toEpochMills(LocalDateTime.now(ZoneOffset.UTC));
    }
}
