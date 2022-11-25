package org.bardframework.time;

import org.bardframework.time.format.DateTimeFormatterJalali;
import org.bardframework.time.temporal.IsoFieldsJalali;

import java.io.Serializable;
import java.time.*;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.time.zone.ZoneRules;
import java.util.Objects;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.FOREVER;
import static java.time.temporal.ChronoUnit.NANOS;

/**
 * A date-time without a time-zone in the ISO-8601 calendar system,
 * such as {@code 1367-08-12T10:15:30}.
 * <p>
 * {@code LocalDateTimeJalali} is an immutable date-time object that represents a date-time,
 * often viewed as year-month-day-hour-minute-second. Other date and time fields,
 * such as day-of-year, day-of-week and week-of-year, can also be accessed.
 * Time is represented to nanosecond precision.
 * For example, the value "2nd October 2007 at 13:45.30.123456789" can be
 * stored in a {@code LocalDateTimeJalali}.
 * <p>
 * This class does not store or represent a time-zone.
 * Instead, it is a description of the date, as used for birthdays, combined with
 * the local time as seen on a wall clock.
 * It cannot represent an instant on the time-line without additional information
 * such as an offset or time-zone.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 * <p>
 * This is a <a href="{@docRoot}/java/lang/doc-files/ValueBased.html">value-based</a>
 * class; use of identity-sensitive operations (including reference equality
 * ({@code ==}), identity hash code, or synchronization) on instances of
 * {@code LocalDateTimeJalali} may have unpredictable results and should be avoided.
 * The {@code equals} method should be used for comparisons.
 *
 * @author Vahid Zafari
 * This class is immutable and thread-safe.
 */
public final class LocalDateTimeJalali implements Temporal, TemporalAdjuster, ChronoLocalDateTime<LocalDateJalali>, Serializable {


    /**
     * The minimum supported {@code LocalDateTimeJalali}, '-999999999-01-01T00:00:00'.
     * This is the local date-time of midnight at the start of the minimum date.
     * This combines {@link LocalDateJalali#MIN} and {@link LocalTime#MIN}.
     * This could be used by an application as a "far past" date-time.
     */
    public static final LocalDateTimeJalali MIN = LocalDateTimeJalali.of(LocalDateJalali.MIN, LocalTime.MIN);
    /**
     * The maximum supported {@code LocalDateTimeJalali}, '+999999999-12-31T23:59:59.999999999'.
     * This is the local date-time just before midnight at the end of the maximum date.
     * This combines {@link LocalDateJalali#MAX} and {@link LocalTime#MAX}.
     * This could be used by an application as a "far future" date-time.
     */
    public static final LocalDateTimeJalali MAX = LocalDateTimeJalali.of(LocalDateJalali.MAX, LocalTime.MAX);
    /**
     * Hours per day.
     */
    static final long HOURS_PER_DAY = 24;
    /**
     * Minutes per hour.
     */
    static final int MINUTES_PER_HOUR = 60;
    /**
     * Minutes per day.
     */
    static final long MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
    /**
     * Seconds per minute.
     */
    static final int SECONDS_PER_MINUTE = 60;
    /**
     * Seconds per hour.
     */
    static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    /**
     * Seconds per day.
     */
    static final long SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    /**
     * Milliseconds per day.
     */
    static final long MILLIS_PER_DAY = SECONDS_PER_DAY * 1000L;
    /**
     * Microseconds per day.
     */
    static final long MICROS_PER_DAY = SECONDS_PER_DAY * 1000_000L;
    /**
     * Nanos per second.
     */
    static final long NANOS_PER_SECOND = 1000_000_000L;
    /**
     * Nanos per minute.
     */
    static final long NANOS_PER_MINUTE = NANOS_PER_SECOND * SECONDS_PER_MINUTE;
    /**
     * Nanos per hour.
     */
    static final long NANOS_PER_HOUR = NANOS_PER_MINUTE * MINUTES_PER_HOUR;
    /**
     * Nanos per day.
     */
    static final long NANOS_PER_DAY = NANOS_PER_HOUR * HOURS_PER_DAY;
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 6207766400415563566L;

    /**
     * The date part.
     */
    private final LocalDateJalali date;
    /**
     * The time part.
     */
    private final LocalTime time;

    //-----------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param date the date part of the date-time, validated not null
     * @param time the time part of the date-time, validated not null
     */
    private LocalDateTimeJalali(LocalDateJalali date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    /**
     * Obtains the current date-time from the system clock.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current date-time.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current date-time using the system clock and default time-zone, not null
     */
    public static LocalDateTimeJalali now() {
        return now(Clock.system(ZoneOffset.UTC));
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains the current date-time from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current date-time.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@link Clock dependency injection}.
     *
     * @param clock the clock to use, not null
     * @return the current date-time, not null
     */
    public static LocalDateTimeJalali now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        final Instant now = clock.instant();  // called once
        return ofEpochSecond(now.getEpochSecond(), now.getNano());
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from year, month,
     * day, hour and minute, setting the second and nanosecond to zero.
     * <p>
     * This returns a {@code LocalDateTimeJalali} with the specified year, month,
     * day-of-month, hour and minute.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     * The second and nanosecond fields will be set to zero.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, not null
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @param hour       the hour-of-day to represent, from 0 to 23
     * @param minute     the minute-of-hour to represent, from 0 to 59
     * @return the local date-time, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateTimeJalali of(int year, MonthJalali month, int dayOfMonth, int hour, int minute) {
        LocalDateJalali date = LocalDateJalali.of(year, month, dayOfMonth);
        LocalTime time = LocalTime.of(hour, minute);
        return new LocalDateTimeJalali(date, time);
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from year, month,
     * day, hour, minute and second, setting the nanosecond to zero.
     * <p>
     * This returns a {@code LocalDateTimeJalali} with the specified year, month,
     * day-of-month, hour, minute and second.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     * The nanosecond field will be set to zero.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, not null
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @param hour       the hour-of-day to represent, from 0 to 23
     * @param minute     the minute-of-hour to represent, from 0 to 59
     * @param second     the second-of-minute to represent, from 0 to 59
     * @return the local date-time, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateTimeJalali of(int year, MonthJalali month, int dayOfMonth, int hour, int minute, int second) {
        LocalDateJalali date = LocalDateJalali.of(year, month, dayOfMonth);
        LocalTime time = LocalTime.of(hour, minute, second);
        return new LocalDateTimeJalali(date, time);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from year, month,
     * day, hour, minute, second and nanosecond.
     * <p>
     * This returns a {@code LocalDateTimeJalali} with the specified year, month,
     * day-of-month, hour, minute, second and nanosecond.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     *
     * @param year         the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month        the month-of-year to represent, not null
     * @param dayOfMonth   the day-of-month to represent, from 1 to 31
     * @param hour         the hour-of-day to represent, from 0 to 23
     * @param minute       the minute-of-hour to represent, from 0 to 59
     * @param second       the second-of-minute to represent, from 0 to 59
     * @param nanoOfSecond the nano-of-second to represent, from 0 to 999,999,999
     * @return the local date-time, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateTimeJalali of(int year, MonthJalali month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        LocalDateJalali date = LocalDateJalali.of(year, month, dayOfMonth);
        LocalTime time = LocalTime.of(hour, minute, second, nanoOfSecond);
        return new LocalDateTimeJalali(date, time);
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from year, month,
     * day, hour and minute, setting the second and nanosecond to zero.
     * <p>
     * This returns a {@code LocalDateTimeJalali} with the specified year, month,
     * day-of-month, hour and minute.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     * The second and nanosecond fields will be set to zero.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @param hour       the hour-of-day to represent, from 0 to 23
     * @param minute     the minute-of-hour to represent, from 0 to 59
     * @return the local date-time, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateTimeJalali of(int year, int month, int dayOfMonth, int hour, int minute)
            throws DateTimeException {
        LocalDateJalali date = LocalDateJalali.of(year, month, dayOfMonth);
        LocalTime time = LocalTime.of(hour, minute);
        return new LocalDateTimeJalali(date, time);
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from year, month,
     * day, hour, minute and second, setting the nanosecond to zero.
     * <p>
     * This returns a {@code LocalDateTimeJalali} with the specified year, month,
     * day-of-month, hour, minute and second.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     * The nanosecond field will be set to zero.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @param hour       the hour-of-day to represent, from 0 to 23
     * @param minute     the minute-of-hour to represent, from 0 to 59
     * @param second     the second-of-minute to represent, from 0 to 59
     * @return the local date-time, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateTimeJalali of(int year, int month, int dayOfMonth, int hour, int minute, int second) {
        LocalDateJalali date = LocalDateJalali.of(year, month, dayOfMonth);
        LocalTime time = LocalTime.of(hour, minute, second);
        return new LocalDateTimeJalali(date, time);
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from year, month,
     * day, hour, minute, second and nanosecond.
     * <p>
     * This returns a {@code LocalDateTimeJalali} with the specified year, month,
     * day-of-month, hour, minute, second and nanosecond.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     *
     * @param year         the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month        the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth   the day-of-month to represent, from 1 to 31
     * @param hour         the hour-of-day to represent, from 0 to 23
     * @param minute       the minute-of-hour to represent, from 0 to 59
     * @param second       the second-of-minute to represent, from 0 to 59
     * @param nanoOfSecond the nano-of-second to represent, from 0 to 999,999,999
     * @return the local date-time, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateTimeJalali of(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond) {
        LocalDateJalali date = LocalDateJalali.of(year, month, dayOfMonth);
        LocalTime time = LocalTime.of(hour, minute, second, nanoOfSecond);
        return new LocalDateTimeJalali(date, time);
    }

    //-------------------------------------------------------------------------

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from a date and time.
     *
     * @param date the local date, not null
     * @param time the local time, not null
     * @return the local date-time, not null
     */
    public static LocalDateTimeJalali of(LocalDateJalali date, LocalTime time) {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(time, "time");
        return new LocalDateTimeJalali(date, time);
    }

    public static LocalDateTimeJalali of(LocalDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime");
        return new LocalDateTimeJalali(LocalDateJalali.of(dateTime.toLocalDate()), dateTime.toLocalTime());
    }

    public static LocalDateTimeJalali of(String localDateTimeJalaliString) {
        if (null == localDateTimeJalaliString) {
            throw new IllegalArgumentException("invalid localDateTimeJalaliString.");
        }
        String digit = localDateTimeJalaliString.replaceAll("[^\\d]", "");
        if (8 > digit.length() || 23 < digit.length()) {
            throw new IllegalArgumentException("cant obtain date value from: " + digit + " min digit count is 8 (yyyymmdd) and max is 23 (yyyymmddhhmmssnnnnnnnnnn) ");
        }
        LocalDateJalali localDateJalali = LocalDateJalali.of(Integer.parseInt(digit.substring(0, 4)), Integer.parseInt(digit.substring(4, 6)), Integer.parseInt(digit.substring(6, 8)));
        int hour = 0, minute = 0, second = 0, nanoOfSecond = 0;
        if (8 < digit.length()) {
            hour = Integer.parseInt(digit.substring(8, (9 < digit.length() ? 10 : 9)));
        }
        if (10 < digit.length()) {
            minute = Integer.parseInt(digit.substring(10, (11 < digit.length() ? 12 : 11)));
        }
        if (12 < digit.length()) {
            second = Integer.parseInt(digit.substring(12, (13 < digit.length() ? 14 : 13)));
        }
        if (14 < digit.length()) {
            nanoOfSecond = Integer.parseInt(digit.substring(14));
        }
        return LocalDateTimeJalali.of(localDateJalali, LocalTime.of(hour, minute, second, nanoOfSecond));
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali}.
     * <p>
     * This creates a local date-time based on the specified instant.
     *
     * @param instant the instant to create the date-time from, not null
     * @return the local date-time, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    public static LocalDateTimeJalali ofInstant(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return ofEpochSecond(instant.getEpochSecond(), instant.getNano());
    }

    /**
     * Obtains an instance of {@code LocalDateTime} using seconds from the
     * epoch of 1970-01-01T00:00:00Z.
     * <p>
     * This allows the {@link ChronoField#INSTANT_SECONDS epoch-second} field
     * to be converted to a local date-time. This is primarily intended for
     * low-level conversions rather than general application usage.
     *
     * @param epochSecond  the number of seconds from the epoch of 1970-01-01T00:00:00Z
     * @param nanoOfSecond the nanosecond within the second, from 0 to 999,999,999
     * @param offset       the zone offset, not null
     * @return the local date-time, not null
     * @throws DateTimeException if the result exceeds the supported range,
     *                           or if the nano-of-second is invalid
     */
    public static LocalDateTimeJalali ofEpochSecond(long epochSecond, int nanoOfSecond, ZoneOffset offset) {
        Objects.requireNonNull(offset, "offset");
        NANO_OF_SECOND.checkValidValue(nanoOfSecond);
        long localSecond = epochSecond + offset.getTotalSeconds();  // overflow caught later
        long localEpochDay = Math.floorDiv(localSecond, SECONDS_PER_DAY);
        long secsOfDay = Math.floorMod(localSecond, SECONDS_PER_DAY);
        LocalDateJalali date = LocalDateJalali.ofEpochDay(localEpochDay);
        LocalTime time = LocalTime.ofNanoOfDay(secsOfDay * NANOS_PER_SECOND + nanoOfSecond);
        return new LocalDateTimeJalali(date, time);
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} using seconds from the
     * epoch of 1970-01-01T00:00:00Z.
     * <p>
     * This allows the {@link ChronoField#INSTANT_SECONDS epoch-second} field
     * to be converted to a local date-time. This is primarily intended for
     * low-level conversions rather than general application usage.
     *
     * @param epochSecond  the number of seconds from the epoch of 1970-01-01T00:00:00Z
     * @param nanoOfSecond the nanosecond within the second, from 0 to 999,999,999
     * @return the local date-time, not null
     * @throws DateTimeException if the result exceeds the supported range,
     *                           or if the nano-of-second is invalid
     */
    public static LocalDateTimeJalali ofEpochSecond(long epochSecond, int nanoOfSecond) {
        NANO_OF_SECOND.checkValidValue(nanoOfSecond);
        long localEpochDay = Math.floorDiv(epochSecond, SECONDS_PER_DAY);
        long secsOfDay = Math.floorMod(epochSecond, SECONDS_PER_DAY);
        LocalDateJalali date = LocalDateJalali.ofEpochDay(localEpochDay);
        LocalTime time = LocalTime.ofNanoOfDay(secsOfDay * NANOS_PER_SECOND + nanoOfSecond);
        return new LocalDateTimeJalali(date, time);
    }

    /**
     * Obtains an instance of {@code LocalDateTime} from a temporal object.
     * <p>
     * This obtains a local date-time based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code LocalDateTime}.
     * <p>
     * The conversion extracts and combines the {@code LocalDateJalali} and the
     * {@code LocalTime} from the temporal object.
     * Implementations are permitted to perform optimizations such as accessing
     * those fields that are equivalent to the relevant objects.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code LocalDateTime::from}.
     *
     * @param temporal the temporal object to toModel, not null
     * @return the local date-time, not null
     * @throws DateTimeException if unable to toModel to a {@code LocalDateTime}
     */
    public static LocalDateTimeJalali from(TemporalAccessor temporal) {
        if (temporal instanceof LocalDateTimeJalali) {
            return (LocalDateTimeJalali) temporal;
        }
        try {
            LocalDateJalali date = LocalDateJalali.from(temporal);
            LocalTime time = LocalTime.from(temporal);
            return new LocalDateTimeJalali(date, time);
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain LocalDateTime from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from a text string such as {@code 1367-08-12T10:15:30}.
     * <p>
     * The string must represent a valid date-time and is parsed using
     * {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
     *
     * @param text the text to parse such as "1367-08-12T10:15:30", not null
     * @return the parsed local date-time, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateTimeJalali parse(CharSequence text) {
        return parse(text, DateTimeFormatterJalali.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Obtains an instance of {@code LocalDateTimeJalali} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a date-time.
     *
     * @param text      the text to parse, not null
     * @param formatter the formatter to use, not null
     * @return the parsed jalali date-time, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateTimeJalali parse(CharSequence text, DateTimeFormatterJalali formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.withChronology(ChronologyJalali.INSTANCE).parse(text, LocalDateTimeJalali::from);
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(this.toLocalDate().toLocalDate(), this.toLocalTime());
    }

    //-----------------------------------------------------------------------

    @Override
    public LocalDateJalali toLocalDate() {
        return date;
    }

    /**
     * Returns a copy of this date-time with the new date and time, checking
     * to see if a new object is in fact required.
     *
     * @param newDate the date of the new date-time, not null
     * @param newTime the time of the new date-time, not null
     * @return the date-time, not null
     */
    private LocalDateTimeJalali with(LocalDateJalali newDate, LocalTime newTime) {
        if (date == newDate && time == newTime) {
            return this;
        }
        return new LocalDateTimeJalali(newDate, newTime);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this date-time can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range},
     * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
     * methods will throw an exception.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The supported fields are:
     * <ul>
     * <li>{@code NANO_OF_SECOND}
     * <li>{@code NANO_OF_DAY}
     * <li>{@code MICRO_OF_SECOND}
     * <li>{@code MICRO_OF_DAY}
     * <li>{@code MILLI_OF_SECOND}
     * <li>{@code MILLI_OF_DAY}
     * <li>{@code SECOND_OF_MINUTE}
     * <li>{@code SECOND_OF_DAY}
     * <li>{@code MINUTE_OF_HOUR}
     * <li>{@code MINUTE_OF_DAY}
     * <li>{@code HOUR_OF_AMPM}
     * <li>{@code CLOCK_HOUR_OF_AMPM}
     * <li>{@code HOUR_OF_DAY}
     * <li>{@code CLOCK_HOUR_OF_DAY}
     * <li>{@code AMPM_OF_DAY}
     * <li>{@code DAY_OF_WEEK}
     * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH}
     * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR}
     * <li>{@code DAY_OF_MONTH}
     * <li>{@code DAY_OF_YEAR}
     * <li>{@code EPOCH_DAY}
     * <li>{@code ALIGNED_WEEK_OF_MONTH}
     * <li>{@code ALIGNED_WEEK_OF_YEAR}
     * <li>{@code MONTH_OF_YEAR}
     * <li>{@code PROLEPTIC_MONTH}
     * <li>{@code YEAR_OF_ERA}
     * <li>{@code YEAR}
     * <li>{@code ERA}
     * </ul>
     * All other {@code ChronoField} instances will return false.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the field is supported is determined by the field.
     *
     * @param field the field to check, null returns false
     * @return true if the field is supported on this date-time, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField f) {
            return f.isDateBased() || f.isTimeBased();
        }
        return field != null && field.isSupportedBy(this);
    }

    /**
     * Checks if the specified unit is supported.
     * <p>
     * This checks if the specified unit can be added to, or subtracted from, this date-time.
     * If false, then calling the {@link #plus(long, TemporalUnit)} and
     * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
     * <p>
     * If the unit is a {@link ChronoUnit} then the query is implemented here.
     * The supported units are:
     * <ul>
     * <li>{@code NANOS}
     * <li>{@code MICROS}
     * <li>{@code MILLIS}
     * <li>{@code SECONDS}
     * <li>{@code MINUTES}
     * <li>{@code HOURS}
     * <li>{@code HALF_DAYS}
     * <li>{@code DAYS}
     * <li>{@code WEEKS}
     * <li>{@code MONTHS}
     * <li>{@code YEARS}
     * <li>{@code DECADES}
     * <li>{@code CENTURIES}
     * <li>{@code MILLENNIA}
     * <li>{@code ERAS}
     * </ul>
     * All other {@code ChronoUnit} instances will return false.
     * <p>
     * If the unit is not a {@code ChronoUnit}, then the result of this method
     * is obtained by invoking {@code TemporalUnit.isSupportedBy(Temporal)}
     * passing {@code this} as the argument.
     * Whether the unit is supported is determined by the unit.
     *
     * @param unit the unit to check, null returns false
     * @return true if the unit can be added/subtracted, false if not
     */
    @Override  // override for Javadoc
    public boolean isSupported(TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            return unit != FOREVER;
        }
        return unit != null && unit.isSupportedBy(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This date-time is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return
     * appropriate range instances.
     * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the range can be obtained is determined by the field.
     *
     * @param field the field to query the range for, not null
     * @return the range of valid values for the field, not null
     * @throws DateTimeException                if the range for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     */
    @Override
    public ValueRange range(TemporalField field) {
        if (field instanceof ChronoField f) {
            return (f.isTimeBased() ? time.range(field) : date.range(field));
        }
        return field.rangeRefinedBy(this);
    }

    /**
     * Gets the value of the specified field from this date-time as an {@code int}.
     * <p>
     * This queries this date-time for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this date-time, except {@code NANO_OF_DAY}, {@code MICRO_OF_DAY},
     * {@code EPOCH_DAY} and {@code PROLEPTIC_MONTH} which are too large to fit in
     * an {@code int} and throw a {@code DateTimeException}.
     * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
     * passing {@code this} as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field the field to get, not null
     * @return the value for the field
     * @throws DateTimeException                if a value for the field cannot be obtained or
     *                                          the value is outside the range of valid values for the field
     * @throws UnsupportedTemporalTypeException if the field is not supported or
     *                                          the range of values exceeds an {@code int}
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public int get(TemporalField field) {
        if (field instanceof ChronoField f) {
            return (f.isTimeBased() ? time.get(field) : date.get(field));
        }
        ValueRange range = range(field);
        if (!range.isIntValue()) {
            throw new UnsupportedTemporalTypeException("Invalid field " + field + " for get() method, use getLong() instead");
        }
        long value = getLong(field);
        if (!range.isValidValue(value)) {
            throw new DateTimeException("Invalid value for " + field + " (valid values " + range + "): " + value);
        }
        return (int) value;
    }

    /**
     * Gets the value of the specified field from this date-time as a {@code long}.
     * <p>
     * This queries this date-time for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this date-time.
     * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
     * passing {@code this} as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field the field to get, not null
     * @return the value for the field
     * @throws DateTimeException                if a value for the field cannot be obtained
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public long getLong(TemporalField field) {
        if (field instanceof ChronoField f) {
            return (f.isTimeBased() ? time.getLong(field) : date.getLong(field));
        }
        if (WeekFields.WEEK_BASED_YEARS.equals(field)) {
            return IsoFieldsJalali.getWeekBasedYear(date);
        }
        if (IsoFields.WEEK_OF_WEEK_BASED_YEAR.equals(field)) {
            return IsoFieldsJalali.getWeek(date);
        }
        return field.getFrom(this);
    }

    /**
     * Gets the year field.
     * <p>
     * This method returns the primitive {@code int} value for the year.
     * <p>
     * The year returned by this method is proleptic as per {@code get(YEAR)}.
     * To obtain the year-of-era, use {@code get(YEAR_OF_ERA)}.
     *
     * @return the year, from MIN_YEAR to MAX_YEAR
     */
    public int getYear() {
        return date.getYear();
    }

    /**
     * Gets the month-of-year field from 1 to 12.
     * <p>
     * This method returns the month as an {@code int} from 1 to 12.
     * Application code is frequently clearer if the enum {@link MonthJalali}
     * is used by calling {@link #getMonth()}.
     *
     * @return the month-of-year, from 1 to 12
     * @see #getMonth()
     */
    public int getMonthValue() {
        return date.getMonthValue();
    }

    /**
     * Gets the month-of-year field using the {@code JalaliMonth} enum.
     * <p>
     * This method returns the enum {@link MonthJalali} for the month.
     * This avoids confusion as to what {@code int} values mean.
     * If you need access to the primitive {@code int} value then the enum
     * provides the {@link MonthJalali#getValue() int value}.
     *
     * @return the month-of-year, not null
     * @see #getMonthValue()
     */
    public MonthJalali getMonth() {
        return date.getMonth();
    }

    /**
     * Gets the day-of-month field.
     * <p>
     * This method returns the primitive {@code int} value for the day-of-month.
     *
     * @return the day-of-month, from 1 to 31
     */
    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    /**
     * Gets the day-of-year field.
     * <p>
     * This method returns the primitive {@code int} value for the day-of-year.
     *
     * @return the day-of-year, from 1 to 365, or 366 in a leap year
     */
    public int getDayOfYear() {
        return date.getDayOfYear();
    }

    /**
     * Gets the day-of-week field, which is an enum {@code DayOfWeek}.
     * <p>
     * This method returns the enum {@link DayOfWeek} for the day-of-week.
     * This avoids confusion as to what {@code int} values mean.
     * If you need access to the primitive {@code int} value then the enum
     * provides the {@link DayOfWeek#getValue() int value}.
     * <p>
     * Additional information can be obtained from the {@code DayOfWeek}.
     * This includes textual names of the values.
     *
     * @return the day-of-week, not null
     */
    public DayOfWeek getDayOfWeek() {
        return date.getDayOfWeek();
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the {@code LocalTime} part of this date-time.
     * <p>
     * This returns a {@code LocalTime} with the same hour, minute, second and
     * nanosecond as this date-time.
     *
     * @return the time part of this date-time, not null
     */
    @Override
    public LocalTime toLocalTime() {
        return time;
    }

    /**
     * Gets the hour-of-day field.
     *
     * @return the hour-of-day, from 0 to 23
     */
    public int getHour() {
        return time.getHour();
    }

    /**
     * Gets the minute-of-hour field.
     *
     * @return the minute-of-hour, from 0 to 59
     */
    public int getMinute() {
        return time.getMinute();
    }

    /**
     * Gets the second-of-minute field.
     *
     * @return the second-of-minute, from 0 to 59
     */
    public int getSecond() {
        return time.getSecond();
    }

    /**
     * Gets the nano-of-second field.
     *
     * @return the nano-of-second, from 0 to 999,999,999
     */
    public int getNano() {
        return time.getNano();
    }

    //-----------------------------------------------------------------------

    /**
     * Returns an adjusted copy of this date-time.
     * <p>
     * This returns a {@code LocalDateTimeJalali}, based on this one, with the date-time adjusted.
     * The adjustment takes place using the specified adjuster strategy object.
     * Read the documentation of the adjuster to understand what adjustment will be made.
     * <p>
     * A simple adjuster might simply set the one of the fields, such as the year field.
     * A more complex adjuster might set the date to the last day of the month.
     * <p>
     * A selection of common adjustments is provided in
     * {@link TemporalAdjusters TemporalAdjusters}.
     * These include finding the "last day of the month" and "next Wednesday".
     * Key date-time classes also implement the {@code TemporalAdjuster} interface,
     * such as {@link MonthJalali} and {@link MonthDay MonthDay}.
     * The adjuster is responsible for handling special cases, such as the varying
     * lengths of month and leap years.
     * <p>
     * For example this code returns a date on the last day of July:
     * <pre>
     *  import static java.time.JalaliMonth.*;
     *  import static java.time.temporal.TemporalAdjusters.*;
     *
     *  result = LocalDateTimeJalali.with(JULY).with(lastDayOfMonth());
     * </pre>
     * <p>
     * The classes {@link LocalDateJalali} and {@link LocalTime} implement {@code TemporalAdjuster},
     * thus this method can be used to change the date, time or offset:
     * <pre>
     *  result = LocalDateTimeJalali.with(date);
     *  result = LocalDateTimeJalali.with(time);
     * </pre>
     * <p>
     * The result of this method is obtained by invoking the
     * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
     * specified adjuster passing {@code this} as the argument.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param adjuster the adjuster to use, not null
     * @return a {@code LocalDateTimeJalali} based on {@code this} with the adjustment made, not null
     * @throws DateTimeException   if the adjustment cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public LocalDateTimeJalali with(TemporalAdjuster adjuster) {
        // optimizations
        if (adjuster instanceof LocalDateJalali) {
            return with((LocalDateJalali) adjuster, time);
        } else if (adjuster instanceof LocalTime) {
            return with(date, (LocalTime) adjuster);
        } else if (adjuster instanceof LocalDateTimeJalali) {
            return (LocalDateTimeJalali) adjuster;
        }
        return (LocalDateTimeJalali) adjuster.adjustInto(this);
    }

    /**
     * Returns a copy of this date-time with the specified field set to a new value.
     * <p>
     * This returns a {@code LocalDateTimeJalali}, based on this one, with the value
     * for the specified field changed.
     * This can be used to change any supported field, such as the year, month or day-of-month.
     * If it is not possible to set the value, because the field is not supported or for
     * some other reason, an exception is thrown.
     * <p>
     * In some cases, changing the specified field can cause the resulting date-time to become invalid,
     * such as changing the month from 31st January to February would make the day-of-month invalid.
     * In cases like this, the field is responsible for resolving the date. Typically it will choose
     * the previous valid date, which would be the last valid day of February in this example.
     * <p>
     * If the field is a {@link ChronoField} then the adjustment is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will behave as per
     * the matching method on {@link LocalDateJalali#with(TemporalField, long) LocalDateJalali}
     * or {@link LocalTime#with(TemporalField, long) LocalTime}.
     * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
     * passing {@code this} as the argument. In this case, the field determines
     * whether and how to adjust the instant.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param field    the field to set in the result, not null
     * @param newValue the new value of the field in the result
     * @return a {@code LocalDateTimeJalali} based on {@code this} with the specified field set, not null
     * @throws DateTimeException                if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public LocalDateTimeJalali with(TemporalField field, long newValue) {
        if (field instanceof ChronoField f) {
            if (f.isTimeBased()) {
                return with(date, time.with(field, newValue));
            } else {
                return with(date.with(field, newValue), time);
            }
        }
        return field.adjustInto(this, newValue);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the year altered.
     * <p>
     * The time does not affect the calculation and will be the same in the result.
     * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year the year to set in the result, from MIN_YEAR to MAX_YEAR
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested year, not null
     * @throws DateTimeException if the year value is invalid
     */
    public LocalDateTimeJalali withYear(int year) {
        return with(date.withYear(year), time);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the month-of-year altered.
     * <p>
     * The time does not affect the calculation and will be the same in the result.
     * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param month the month-of-year to set in the result, from 1 (January) to 12 (December)
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested month, not null
     * @throws DateTimeException if the month-of-year value is invalid
     */
    public LocalDateTimeJalali withMonth(int month) {
        return with(date.withMonth(month), time);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the day-of-month altered.
     * <p>
     * If the resulting date-time is invalid, an exception is thrown.
     * The time does not affect the calculation and will be the same in the result.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfMonth the day-of-month to set in the result, from 1 to 28-31
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested day, not null
     * @throws DateTimeException if the day-of-month value is invalid,
     *                           or if the day-of-month is invalid for the month-year
     */
    public LocalDateTimeJalali withDayOfMonth(int dayOfMonth) {
        return with(date.withDayOfMonth(dayOfMonth), time);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the day-of-year altered.
     * <p>
     * If the resulting date-time is invalid, an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfYear the day-of-year to set in the result, from 1 to 365-366
     * @return a {@code LocalDateTimeJalali} based on this date with the requested day, not null
     * @throws DateTimeException if the day-of-year value is invalid,
     *                           or if the day-of-year is invalid for the year
     */
    public LocalDateTimeJalali withDayOfYear(int dayOfYear) {
        return with(date.withDayOfYear(dayOfYear), time);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the hour-of-day altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param hour the hour-of-day to set in the result, from 0 to 23
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested hour, not null
     * @throws DateTimeException if the hour value is invalid
     */
    public LocalDateTimeJalali withHour(int hour) {
        LocalTime newTime = time.withHour(hour);
        return with(date, newTime);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the minute-of-hour altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param minute the minute-of-hour to set in the result, from 0 to 59
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested minute, not null
     * @throws DateTimeException if the minute value is invalid
     */
    public LocalDateTimeJalali withMinute(int minute) {
        LocalTime newTime = time.withMinute(minute);
        return with(date, newTime);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the second-of-minute altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param second the second-of-minute to set in the result, from 0 to 59
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested second, not null
     * @throws DateTimeException if the second value is invalid
     */
    public LocalDateTimeJalali withSecond(int second) {
        LocalTime newTime = time.withSecond(second);
        return with(date, newTime);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the nano-of-second altered.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param nanoOfSecond the nano-of-second to set in the result, from 0 to 999,999,999
     * @return a {@code LocalDateTimeJalali} based on this date-time with the requested nanosecond, not null
     * @throws DateTimeException if the nano value is invalid
     */
    public LocalDateTimeJalali withNano(int nanoOfSecond) {
        LocalTime newTime = time.withNano(nanoOfSecond);
        return with(date, newTime);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the time truncated.
     * <p>
     * Truncation returns a copy of the original date-time with fields
     * smaller than the specified unit set to zero.
     * For example, truncating with the {@link ChronoUnit#MINUTES minutes} unit
     * will set the second-of-minute and nano-of-second field to zero.
     * <p>
     * The unit must have a {@linkplain TemporalUnit#getDuration() duration}
     * that divides into the length of a standard day without remainder.
     * This includes all supplied time units on {@link ChronoUnit} and
     * {@link ChronoUnit#DAYS DAYS}. Other units throw an exception.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param unit the unit to truncate to, not null
     * @return a {@code LocalDateTimeJalali} based on this date-time with the time truncated, not null
     * @throws DateTimeException                if unable to truncate
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     */
    public LocalDateTimeJalali truncatedTo(TemporalUnit unit) {
        return with(date, time.truncatedTo(unit));
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this date-time with the specified amount added.
     * <p>
     * This returns a {@code LocalDateTimeJalali}, based on this one, with the specified amount added.
     * The amount is typically {@link Period} or {@link Duration} but may be
     * any other type implementing the {@link TemporalAmount} interface.
     * <p>
     * The calculation is delegated to the amount object by calling
     * {@link TemporalAmount#addTo(Temporal)}. The amount implementation is free
     * to implement the addition in any way it wishes, however it typically
     * calls back to {@link #plus(long, TemporalUnit)}. Consult the documentation
     * of the amount implementation to determine if it can be successfully added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToAdd the amount to add, not null
     * @return a {@code LocalDateTimeJalali} based on this date-time with the addition made, not null
     * @throws DateTimeException   if the addition cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public LocalDateTimeJalali plus(TemporalAmount amountToAdd) {
        if (amountToAdd instanceof Period periodToAdd) {
            return with(date.plus(periodToAdd), time);
        }
        Objects.requireNonNull(amountToAdd, "amountToAdd");
        return (LocalDateTimeJalali) amountToAdd.addTo(this);
    }

    /**
     * Returns a copy of this date-time with the specified amount added.
     * <p>
     * This returns a {@code LocalDateTimeJalali}, based on this one, with the amount
     * in terms of the unit added. If it is not possible to add the amount, because the
     * unit is not supported or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoUnit} then the addition is implemented here.
     * Date units are added as per {@link LocalDateJalali#plus(long, TemporalUnit)}.
     * Time units are added as per {@link LocalTime#plus(long, TemporalUnit)} with
     * any overflow in days added equivalent to using {@link #plusDays(long)}.
     * <p>
     * If the field is not a {@code ChronoUnit}, then the result of this method
     * is obtained by invoking {@code TemporalUnit.addTo(Temporal, long)}
     * passing {@code this} as the argument. In this case, the unit determines
     * whether and how to perform the addition.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToAdd the amount of the unit to add to the result, may be negative
     * @param unit        the unit of the amount to add, not null
     * @return a {@code LocalDateTimeJalali} based on this date-time with the specified amount added, not null
     * @throws DateTimeException                if the addition cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public LocalDateTimeJalali plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit f) {
            switch (f) {
                case NANOS:
                    return plusNanos(amountToAdd);
                case MICROS:
                    return plusDays(amountToAdd / MICROS_PER_DAY).plusNanos((amountToAdd % MICROS_PER_DAY) * 1000);
                case MILLIS:
                    return plusDays(amountToAdd / MILLIS_PER_DAY).plusNanos((amountToAdd % MILLIS_PER_DAY) * 1000_000);
                case SECONDS:
                    return plusSeconds(amountToAdd);
                case MINUTES:
                    return plusMinutes(amountToAdd);
                case HOURS:
                    return plusHours(amountToAdd);
                case HALF_DAYS:
                    return plusDays(amountToAdd / 256).plusHours((amountToAdd % 256) * 12);  // no overflow (256 is multiple of 2)
            }
            return with(date.plus(amountToAdd, unit), time);
        }
        return unit.addTo(this, amountToAdd);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of years added.
     * <p>
     * This method adds the specified amount to the years field in three steps:
     * <ol>
     * <li>Add the input years to the year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day-of-month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2008-02-29 (leap year) plus one year would result in the
     * invalid date 2009-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2009-02-28, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years the years to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the years added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusYears(long years) {
        LocalDateJalali newDate = date.plusYears(years);
        return with(newDate, time);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of months added.
     * <p>
     * This method adds the specified amount to the months field in three steps:
     * <ol>
     * <li>Add the input months to the month-of-year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day-of-month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2007-03-31 plus one month would result in the invalid date
     * 1367-08-12. Instead of returning an invalid result, the last valid day
     * of the month, 1367-08-12, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months the months to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the months added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusMonths(long months) {
        LocalDateJalali newDate = date.plusMonths(months);
        return with(newDate, time);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of weeks added.
     * <p>
     * This method adds the specified amount in weeks to the days field incrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one week would result in 2009-01-07.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks the weeks to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the weeks added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusWeeks(long weeks) {
        LocalDateJalali newDate = date.plusWeeks(weeks);
        return with(newDate, time);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of days added.
     * <p>
     * This method adds the specified amount to the days field incrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one day would result in 2009-01-01.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days the days to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the days added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusDays(long days) {
        LocalDateJalali newDate = date.plusDays(days);
        return with(newDate, time);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of hours added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param hours the hours to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the hours added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusHours(long hours) {
        return plusWithOverflow(date, hours, 0, 0, 0, 1);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of minutes added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param minutes the minutes to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the minutes added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusMinutes(long minutes) {
        return plusWithOverflow(date, 0, minutes, 0, 0, 1);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of seconds added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param seconds the seconds to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the seconds added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusSeconds(long seconds) {
        return plusWithOverflow(date, 0, 0, seconds, 0, 1);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of nanoseconds added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param nanos the nanos to add, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the nanoseconds added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali plusNanos(long nanos) {
        return plusWithOverflow(date, 0, 0, 0, nanos, 1);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this date-time with the specified amount subtracted.
     * <p>
     * This returns a {@code LocalDateTimeJalali}, based on this one, with the specified amount subtracted.
     * The amount is typically {@link Period} or {@link Duration} but may be
     * any other type implementing the {@link TemporalAmount} interface.
     * <p>
     * The calculation is delegated to the amount object by calling
     * {@link TemporalAmount#subtractFrom(Temporal)}. The amount implementation is free
     * to implement the subtraction in any way it wishes, however it typically
     * calls back to {@link #minus(long, TemporalUnit)}. Consult the documentation
     * of the amount implementation to determine if it can be successfully subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToSubtract the amount to subtract, not null
     * @return a {@code LocalDateTimeJalali} based on this date-time with the subtraction made, not null
     * @throws DateTimeException   if the subtraction cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public LocalDateTimeJalali minus(TemporalAmount amountToSubtract) {
        if (amountToSubtract instanceof Period periodToSubtract) {
            return with(date.minus(periodToSubtract), time);
        }
        Objects.requireNonNull(amountToSubtract, "amountToSubtract");
        return (LocalDateTimeJalali) amountToSubtract.subtractFrom(this);
    }

    /**
     * Returns a copy of this date-time with the specified amount subtracted.
     * <p>
     * This returns a {@code LocalDateTimeJalali}, based on this one, with the amount
     * in terms of the unit subtracted. If it is not possible to subtract the amount,
     * because the unit is not supported or for some other reason, an exception is thrown.
     * <p>
     * This method is equivalent to {@link #plus(long, TemporalUnit)} with the amount negated.
     * See that method for a full description of how addition, and thus subtraction, works.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amountToSubtract the amount of the unit to subtract from the result, may be negative
     * @param unit             the unit of the amount to subtract, not null
     * @return a {@code LocalDateTimeJalali} based on this date-time with the specified amount subtracted, not null
     * @throws DateTimeException                if the subtraction cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public LocalDateTimeJalali minus(long amountToSubtract, TemporalUnit unit) {
        return (amountToSubtract == Long.MIN_VALUE ? plus(Long.MAX_VALUE, unit).plus(1, unit) : plus(-amountToSubtract, unit));
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of years subtracted.
     * <p>
     * This method subtracts the specified amount from the years field in three steps:
     * <ol>
     * <li>Subtract the input years from the year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day-of-month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2008-02-29 (leap year) minus one year would result in the
     * invalid date 2009-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2009-02-28, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param years the years to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the years subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusYears(long years) {
        return (years == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-years));
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of months subtracted.
     * <p>
     * This method subtracts the specified amount from the months field in three steps:
     * <ol>
     * <li>Subtract the input months from the month-of-year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day-of-month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 1367-01-31 minus one month would result in the invalid date
     * 1366-12-31. Instead of returning an invalid result, the last valid day
     * of the month, 1367-08-12, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months the months to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the months subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusMonths(long months) {
        return (months == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-months));
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of weeks subtracted.
     * <p>
     * This method subtracts the specified amount in weeks from the days field decrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2009-01-07 minus one week would result in 2008-12-31.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeks the weeks to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the weeks subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusWeeks(long weeks) {
        return (weeks == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-weeks));
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of days subtracted.
     * <p>
     * This method subtracts the specified amount from the days field decrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2009-01-01 minus one day would result in 2008-12-31.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param days the days to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the days subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusDays(long days) {
        return (days == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-days));
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of hours subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param hours the hours to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the hours subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusHours(long hours) {
        return plusWithOverflow(date, hours, 0, 0, 0, -1);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of minutes subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param minutes the minutes to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the minutes subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusMinutes(long minutes) {
        return plusWithOverflow(date, 0, minutes, 0, 0, -1);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of seconds subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param seconds the seconds to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the seconds subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusSeconds(long seconds) {
        return plusWithOverflow(date, 0, 0, seconds, 0, -1);
    }

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified number of nanoseconds subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param nanos the nanos to subtract, may be negative
     * @return a {@code LocalDateTimeJalali} based on this date-time with the nanoseconds subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateTimeJalali minusNanos(long nanos) {
        return plusWithOverflow(date, 0, 0, 0, nanos, -1);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateTimeJalali} with the specified period added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param newDate the new date to base the calculation on, not null
     * @param hours   the hours to add, may be negative
     * @param minutes the minutes to add, may be negative
     * @param seconds the seconds to add, may be negative
     * @param nanos   the nanos to add, may be negative
     * @param sign    the sign to determine add or subtract
     * @return the combined result, not null
     */
    private LocalDateTimeJalali plusWithOverflow(LocalDateJalali newDate, long hours, long minutes, long seconds, long nanos, int sign) {
        // 9223372036854775808 long, 2147483648 int
        if ((hours | minutes | seconds | nanos) == 0) {
            return with(newDate, time);
        }
        long totDays = nanos / NANOS_PER_DAY +             //   max/24*60*60*1B
                seconds / SECONDS_PER_DAY +                //   max/24*60*60
                minutes / MINUTES_PER_DAY +                //   max/24*60
                hours / HOURS_PER_DAY;                     //   max/24
        totDays *= sign;                                   // total max*0.4237...
        long totNanos = nanos % NANOS_PER_DAY +                    //   max  86400000000000
                (seconds % SECONDS_PER_DAY) * NANOS_PER_SECOND +   //   max  86400000000000
                (minutes % MINUTES_PER_DAY) * NANOS_PER_MINUTE +   //   max  86400000000000
                (hours % HOURS_PER_DAY) * NANOS_PER_HOUR;          //   max  86400000000000
        long curNoD = time.toNanoOfDay();                       //   max  86400000000000
        totNanos = totNanos * sign + curNoD;                    // total 432000000000000
        totDays += Math.floorDiv(totNanos, NANOS_PER_DAY);
        long newNoD = Math.floorMod(totNanos, NANOS_PER_DAY);
        LocalTime newTime = (newNoD == curNoD ? time : LocalTime.ofNanoOfDay(newNoD));
        return with(newDate.plusDays(totDays), newTime);
    }

    //-----------------------------------------------------------------------

    /**
     * Queries this date-time using the specified query.
     * <p>
     * This queries this date-time using the specified query strategy object.
     * The {@code TemporalQuery} object defines the logic to be used to
     * obtain the result. Read the documentation of the query to understand
     * what the result of this method will be.
     * <p>
     * The result of this method is obtained by invoking the
     * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
     * specified query passing {@code this} as the argument.
     *
     * @param <R>   the type of the result
     * @param query the query to invoke, not null
     * @return the query result, null may be returned (defined by the query)
     * @throws DateTimeException   if unable to query (defined by the query)
     * @throws ArithmeticException if numeric overflow occurs (defined by the query)
     */
    @SuppressWarnings("unchecked")
    @Override  // override for Javadoc
    public <R> R query(TemporalQuery<R> query) {
        if (query == TemporalQueries.localDate()) {
            return (R) date;
        }
        if (query == TemporalQueries.zoneId() || query == TemporalQueries.zone() || query == TemporalQueries.offset()) {
            return null;
        } else if (query == TemporalQueries.localTime()) {
            return (R) toLocalTime();
        } else if (query == TemporalQueries.chronology()) {
            return (R) getChronology();
        } else if (query == TemporalQueries.precision()) {
            return (R) NANOS;
        }
        // inline TemporalAccessor.super.query(query) as an optimization
        // non-JDK classes are not permitted to make this optimization
        return query.queryFrom(this);
    }

    /**
     * Adjusts the specified temporal object to have the same date and time as this object.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the date and time changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * twice, passing {@link ChronoField#EPOCH_DAY} and
     * {@link ChronoField#NANO_OF_DAY} as the fields.
     * <p>
     * In most cases, it is clearer to reverse the calling pattern by using
     * {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   // these two lines are equivalent, but the second approach is recommended
     *   temporal = thisLocalDateTimeJalali.adjustInto(temporal);
     *   temporal = temporal.with(thisLocalDateTimeJalali);
     * </pre>
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param temporal the target object to be adjusted, not null
     * @return the adjusted object, not null
     * @throws DateTimeException   if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override  // override for Javadoc
    public Temporal adjustInto(Temporal temporal) {
        return temporal
                .with(EPOCH_DAY, toLocalDate().toEpochDay())
                .with(NANO_OF_DAY, toLocalTime().toNanoOfDay());
    }

    /**
     * Calculates the amount of time until another date-time in terms of the specified unit.
     * <p>
     * This calculates the amount of time between two {@code LocalDateTimeJalali}
     * objects in terms of a single {@code TemporalUnit}.
     * The start and end points are {@code this} and the specified date-time.
     * The result will be negative if the end is before the start.
     * The {@code Temporal} passed to this method is converted to a
     * {@code LocalDateTimeJalali} using {@link #from(TemporalAccessor)}.
     * For example, the amount in days between two date-times can be calculated
     * using {@code startDateTime.until(endDateTime, DAYS)}.
     * <p>
     * The calculation returns a whole number, representing the number of
     * complete units between the two date-times.
     * For example, the amount in months between 2012-06-15T00:00 and 2012-08-14T23:59
     * will only be one month as it is one minute short of two months.
     * <p>
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
     * <pre>
     *   // these two lines are equivalent
     *   amount = start.until(end, MONTHS);
     *   amount = MONTHS.between(start, end);
     * </pre>
     * The choice should be made based on which makes the code more readable.
     * <p>
     * The calculation is implemented in this method for {@link ChronoUnit}.
     * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
     * {@code MINUTES}, {@code HOURS} and {@code HALF_DAYS}, {@code DAYS},
     * {@code WEEKS}, {@code MONTHS}, {@code YEARS}, {@code DECADES},
     * {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS} are supported.
     * Other {@code ChronoUnit} values will throw an exception.
     * <p>
     * If the unit is not a {@code ChronoUnit}, then the result of this method
     * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
     * passing {@code this} as the first argument and the converted input temporal
     * as the second argument.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param endExclusive the end date, exclusive, which is converted to a {@code LocalDateTimeJalali}, not null
     * @param unit         the unit to measure the amount in, not null
     * @return the amount of time between this date-time and the end date-time
     * @throws DateTimeException                if the amount cannot be calculated, or the end
     *                                          temporal cannot be converted to a {@code LocalDateTimeJalali}
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        LocalDateTimeJalali end = LocalDateTimeJalali.from(endExclusive);
        if (unit instanceof ChronoUnit) {
            if (unit.isTimeBased()) {
                long amount = date.daysUntil(end.date);
                if (amount == 0) {
                    return time.until(end.time, unit);
                }
                long timePart = end.time.toNanoOfDay() - time.toNanoOfDay();
                if (amount > 0) {
                    amount--;  // safe
                    timePart += NANOS_PER_DAY;  // safe
                } else {
                    amount++;  // safe
                    timePart -= NANOS_PER_DAY;  // safe
                }
                switch ((ChronoUnit) unit) {
                    case NANOS:
                        amount = Math.multiplyExact(amount, NANOS_PER_DAY);
                        break;
                    case MICROS:
                        amount = Math.multiplyExact(amount, MICROS_PER_DAY);
                        timePart = timePart / 1000;
                        break;
                    case MILLIS:
                        amount = Math.multiplyExact(amount, MILLIS_PER_DAY);
                        timePart = timePart / 1_000_000;
                        break;
                    case SECONDS:
                        amount = Math.multiplyExact(amount, SECONDS_PER_DAY);
                        timePart = timePart / NANOS_PER_SECOND;
                        break;
                    case MINUTES:
                        amount = Math.multiplyExact(amount, MINUTES_PER_DAY);
                        timePart = timePart / NANOS_PER_MINUTE;
                        break;
                    case HOURS:
                        amount = Math.multiplyExact(amount, HOURS_PER_DAY);
                        timePart = timePart / NANOS_PER_HOUR;
                        break;
                    case HALF_DAYS:
                        amount = Math.multiplyExact(amount, 2L);
                        timePart = timePart / (NANOS_PER_HOUR * 12);
                        break;
                }
                return Math.addExact(amount, timePart);
            }
            LocalDateJalali endDate = end.date;
            if (endDate.isAfter(date) && end.time.isBefore(time)) {
                endDate = endDate.minusDays(1);
            } else if (endDate.isBefore(date) && end.time.isAfter(time)) {
                endDate = endDate.plusDays(1);
            }
            return date.until(endDate, unit);
        }
        return unit.between(this, end);
    }

    /**
     * Formats this date-time using the specified formatter.
     * <p>
     * This date-time will be passed to the formatter to produce a string.
     *
     * @param formatter the formatter to use, not null
     * @return the formatted date-time string, not null
     * @throws DateTimeException if an error occurs during printing
     */
    @Override  // override for Javadoc and performance
    public String format(DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.withChronology(getChronology()).format(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Combines this date-time with an offset to create an {@code OffsetDateTime}.
     * <p>
     * This returns an {@code OffsetDateTime} formed from this date-time at the specified offset.
     * All possible combinations of date-time and offset are valid.
     *
     * @param offset the offset to combine with, not null
     * @return the offset date-time formed from this date-time and the specified offset, not null
     */
    public OffsetDateTimeJalali atOffset(ZoneOffset offset) {
        return OffsetDateTimeJalali.of(this, offset);
    }

    /**
     * Combines this date-time with a time-zone to create a {@code ZonedDateTime}.
     * <p>
     * This returns a {@code ZonedDateTime} formed from this date-time at the
     * specified time-zone. The result will match this date-time as closely as possible.
     * Time-zone rules, such as daylight savings, mean that not every local date-time
     * is valid for the specified zone, thus the local date-time may be adjusted.
     * <p>
     * The local date-time is resolved to a single instant on the time-line.
     * This is achieved by finding a valid offset from UTC/Greenwich for the local
     * date-time as defined by the {@link ZoneRules rules} of the zone ID.
     * <p>
     * In most cases, there is only one valid offset for a local date-time.
     * In the case of an overlap, where clocks are set back, there are two valid offsets.
     * This method uses the earlier offset typically corresponding to "summer".
     * <p>
     * In the case of a gap, where clocks jump forward, there is no valid offset.
     * Instead, the local date-time is adjusted to be later by the length of the gap.
     * For a typical one hour daylight savings change, the local date-time will be
     * moved one hour later into the offset typically corresponding to "summer".
     * <p>
     * To obtain the later offset during an overlap, call
     * {@link ZonedDateTimeJalali#withLaterOffsetAtOverlap()} on the result of this method.
     * To throw an exception when there is a gap or overlap
     *
     * @param zone the time-zone to use, not null
     * @return the zoned date-time formed from this date-time, not null
     */
    @Override
    public ZonedDateTimeJalali atZone(ZoneId zone) {
        return ZonedDateTimeJalali.of(this, zone);
    }

    /**
     * Compares this date-time to another date-time.
     * <p>
     * The comparison is primarily based on the date-time, from earliest to latest.
     * It is "consistent with equals", as defined by {@link Comparable}.
     * <p>
     * If all the date-times being compared are instances of {@code LocalDateTimeJalali},
     * then the comparison will be entirely based on the date-time.
     * If some dates being compared are in different chronologies, then the
     * chronology is also considered, see {@link ChronoLocalDateTime#compareTo}.
     *
     * @param other the other date-time to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override  // override for Javadoc and performance
    public int compareTo(ChronoLocalDateTime<?> other) {
        if (other instanceof LocalDateTimeJalali) {
            return compareTo0((LocalDateTimeJalali) other);
        }
        return ChronoLocalDateTime.super.compareTo(other);
    }

    private int compareTo0(LocalDateTimeJalali other) {
        int cmp = date.compareTo0(other.toLocalDate());
        if (cmp == 0) {
            cmp = time.compareTo(other.toLocalTime());
        }
        return cmp;
    }

    /**
     * Checks if this date-time is after the specified date-time.
     * <p>
     * This checks to see if this date-time represents a point on the
     * local time-line after the other date-time.
     * <pre>
     *   LocalDateTimeJalali a = LocalDateTimeJalali.of(2012, 6, 30, 12, 00);
     *   LocalDateTimeJalali b = LocalDateTimeJalali.of(2012, 7, 1, 12, 00);
     *   a.isAfter(b) == false
     *   a.isAfter(a) == false
     *   b.isAfter(a) == true
     * </pre>
     * <p>
     * This method only considers the position of the two date-times on the local time-line.
     * It does not take into user the chronology, or calendar system.
     * This is different from the comparison in {@link #compareTo(ChronoLocalDateTime)},
     * but is the same approach as {@link ChronoLocalDateTime#timeLineOrder()}.
     *
     * @param other the other date-time to compare to, not null
     * @return true if this date-time is after the specified date-time
     */
    @Override  // override for Javadoc and performance
    public boolean isAfter(ChronoLocalDateTime<?> other) {
        if (other instanceof LocalDateTimeJalali) {
            return compareTo0((LocalDateTimeJalali) other) > 0;
        }
        return ChronoLocalDateTime.super.isAfter(other);
    }

    /**
     * Checks if this date-time is before the specified date-time.
     * <p>
     * This checks to see if this date-time represents a point on the
     * local time-line before the other date-time.
     * <pre>
     *   LocalDateTimeJalali a = LocalDateTimeJalali.of(2012, 6, 30, 12, 00);
     *   LocalDateTimeJalali b = LocalDateTimeJalali.of(2012, 7, 1, 12, 00);
     *   a.isBefore(b) == true
     *   a.isBefore(a) == false
     *   b.isBefore(a) == false
     * </pre>
     * <p>
     * This method only considers the position of the two date-times on the local time-line.
     * It does not take into user the chronology, or calendar system.
     * This is different from the comparison in {@link #compareTo(ChronoLocalDateTime)},
     * but is the same approach as {@link ChronoLocalDateTime#timeLineOrder()}.
     *
     * @param other the other date-time to compare to, not null
     * @return true if this date-time is before the specified date-time
     */
    @Override  // override for Javadoc and performance
    public boolean isBefore(ChronoLocalDateTime<?> other) {
        if (other instanceof LocalDateTimeJalali) {
            return compareTo0((LocalDateTimeJalali) other) < 0;
        }
        return ChronoLocalDateTime.super.isBefore(other);
    }

    /**
     * Checks if this date-time is equal to the specified date-time.
     * <p>
     * This checks to see if this date-time represents the same point on the
     * local time-line as the other date-time.
     * <pre>
     *   LocalDateTimeJalali a = LocalDateTimeJalali.of(2012, 6, 30, 12, 00);
     *   LocalDateTimeJalali b = LocalDateTimeJalali.of(2012, 7, 1, 12, 00);
     *   a.isEqual(b) == false
     *   a.isEqual(a) == true
     *   b.isEqual(a) == false
     * </pre>
     * <p>
     * This method only considers the position of the two date-times on the local time-line.
     * It does not take into user the chronology, or calendar system.
     * This is different from the comparison in {@link #compareTo(ChronoLocalDateTime)},
     * but is the same approach as {@link ChronoLocalDateTime#timeLineOrder()}.
     *
     * @param other the other date-time to compare to, not null
     * @return true if this date-time is equal to the specified date-time
     */
    @Override  // override for Javadoc and performance
    public boolean isEqual(ChronoLocalDateTime<?> other) {
        if (other instanceof LocalDateTimeJalali) {
            return compareTo0((LocalDateTimeJalali) other) == 0;
        }
        return ChronoLocalDateTime.super.isEqual(other);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this date-time is equal to another date-time.
     * <p>
     * Compares this {@code LocalDateTimeJalali} with another ensuring that the date-time is the same.
     * Only objects of type {@code LocalDateTimeJalali} are compared, other types return false.
     *
     * @param obj the object to check, null returns false
     * @return true if this is equal to the other date-time
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LocalDateTimeJalali other) {
            return date.equals(other.date) && time.equals(other.time);
        }
        return false;
    }

    /**
     * A hash code for this date-time.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return date.hashCode() ^ time.hashCode();
    }

    //-----------------------------------------------------------------------

    /**
     * Outputs this date-time as a {@code String}, such as {@code 1367-08-12T10:15:30}.
     * <p>
     * The output will be one of the following ISO-8601 formats:
     * <ul>
     * <li>{@code uuuu-MM-dd'T'HH:mm}</li>
     * <li>{@code uuuu-MM-dd'T'HH:mm:ss}</li>
     * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSS}</li>
     * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSS}</li>
     * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS}</li>
     * </ul>
     * The format used will be the shortest that outputs the full value of
     * the time where the omitted parts are implied to be zero.
     *
     * @return a string representation of this date-time, not null
     */
    @Override
    public String toString() {
        return date.toString() + 'T' + time.toString();
    }

}
