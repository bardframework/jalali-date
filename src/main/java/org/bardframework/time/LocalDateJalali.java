package org.bardframework.time;

import java.io.Serializable;
import java.time.*;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Era;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoField.*;

/**
 * A date without a time-zone in the ISO-8601 calendar system,
 * such as {@code 1367-08-12}.
 * <p>
 * {@code LocalDateJalali} is an immutable date-time object that represents a date,
 * often viewed as year-month-day. Other date fields, such as day-of-year,
 * day-of-week and week-of-year, can also be accessed.
 * For example, the value "2nd October 2007" can be stored in a {@code LocalDateJalali}.
 * <p>
 * This class does not store or represent a time or time-zone.
 * Instead, it is a description of the date, as used for birthdays.
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
 * {@code LocalDateJalali} may have unpredictable results and should be avoided.
 * The {@code equals} method should be used for comparisons.
 *
 * @author Vahid Zafari
 * This class is immutable and thread-safe.
 */
public final class LocalDateJalali implements ChronoLocalDate, Serializable {

    /**
     * The minimum supported {@code LocalDateJalali}, '-999999999-01-01'.
     * This could be used by an application as a "far past" date.
     */
    public static final LocalDateJalali MIN = LocalDateJalali.of(Year.MIN_VALUE, 1, 1);
    /**
     * The maximum supported {@code LocalDateJalali}, '+999999999-12-31'.
     * This could be used by an application as a "far future" date.
     */
    public static final LocalDateJalali MAX = LocalDateJalali.of(Year.MAX_VALUE, 12, 30);
    public static final LocalDateJalali LOCAL_1970_01_01 = of(1348, 10, 11);
    public static final int DAYS_LEFT_IN_1970_01_01 = 492_634;
    public static final LocalDate JALALI_0001_01_01 = LocalDate.of(622, Month.MARCH, 22);
    /**
     * Hours per day.
     */
    static final int HOURS_PER_DAY = 24;
    /**
     * Minutes per hour.
     */
    static final int MINUTES_PER_HOUR = 60;
    /**
     * Minutes per day.
     */
    static final int MINUTES_PER_DAY = MINUTES_PER_HOUR * HOURS_PER_DAY;
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

    static final TemporalQuery<LocalDateJalali> LOCAL_DATE = (temporal) -> {
        if (temporal.isSupported(EPOCH_DAY)) {
            return LocalDateJalali.ofEpochDay(temporal.getLong(EPOCH_DAY));
        }
        return null;
    };
    /**
     * The number of days in a 400 year cycle.
     */
    private static final int DAYS_PER_CYCLE = 146097;
    /**
     * The number of days from year zero to year 1970.
     * There are five 400 year cycles from year zero to 2000.
     * There are 7 leap years from 1970 to 2000.
     */
    static final long DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5L) - (30L * 365L + 7L);
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 2942565459149668126L;
    //From 1722 (1100 jalali) to 2122 (1500 jalali)
    private static final List<Integer> _10 = List.of(1804, 1808, 1812, 1816, 1820, 1824, 1828,
        1903, 1904, 1907, 1908, 1911, 1912, 1915, 1916, 1919, 1920, 1923, 1924, 1927, 1928, 1932, 1936, 1940, 1944,
        1948, 1952, 1956, 1960);
    //From 1722 (1100 jalali) to 2122 (1500 jalali
    private static final List<Integer> _12 = List.of(1733, 1737, 1741, 1745, 1749, 1753, 1757, 1761, 1765, 1766,
        1769, 1770, 1773, 1774, 1777, 1778, 1781, 1782, 1785, 1786, 1789, 1790, 1793, 1794, 1797, 1798, 1865, 1869,
        1873, 1877, 1881, 1885, 1889, 1893, 1897, 1898,
        1997, 2001, 2005, 2009, 2013, 2017, 2021, 2025, 2029, 2030, 2033, 2034, 2037, 2038, 2041, 2042, 2045, 2046,
        2049, 2050, 2053, 2054, 2057, 2058, 2061, 2062, 2063, 2065, 2066, 2067, 2069, 2070, 2071, 2073, 2074, 2075,
        2077, 2078, 2079, 2081, 2082, 2083, 2085, 2086, 2087, 2089, 2090, 2091, 2093, 2094, 2095, 2096, 2097, 2098,
        2099, 2100);
    private static final int[] jalaliDaysInMonth = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};

    //-----------------------------------------------------------------------
    private static final int[] gregorianDaysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    /**
     * The year.
     */
    private final int year;
    /**
     * The month-of-year.
     */
    private final short month;
    /**
     * The day-of-month.
     */
    private final short day;

    //-----------------------------------------------------------------------

    /**
     * Constructor, previously validated.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, not null
     * @param dayOfMonth the day-of-month to represent, valid for year-month, from 1 to 31
     */
    private LocalDateJalali(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = (short) month;
        this.day = (short) dayOfMonth;
    }

    /**
     * Obtains the current date from the system clock in the default time-zone.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current date.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current date using the system clock and default time-zone, not null
     */
    public static LocalDateJalali now() {
        return now(Clock.systemDefaultZone());
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains the current date from the system clock in the specified time-zone.
     * <p>
     * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
     * Specifying the time-zone avoids dependence on the default time-zone.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @param zone the zone ID to use, not null
     * @return the current date using the system clock, not null
     */
    public static LocalDateJalali now(ZoneId zone) {
        return now(Clock.system(zone));
    }

    /**
     * Obtains the current date from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current date - today.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@link Clock dependency injection}.
     *
     * @param clock the clock to use, not null
     * @return the current date, not null
     */
    public static LocalDateJalali now(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        // inline to avoid creating object and Instant checks
        final Instant now = clock.instant();  // called once
        ZoneOffset offset = clock.getZone().getRules().getOffset(now);
        long epochSec = now.getEpochSecond() + offset.getTotalSeconds();  // overflow caught later
        long epochDay = Math.floorDiv(epochSec, SECONDS_PER_DAY);
        return LocalDateJalali.ofEpochDay(epochDay);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code LocalDateJalali} from a year, month and day.
     * <p>
     * This returns a {@code LocalDateJalali} with the specified year, month and day-of-month.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, not null
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return the local date, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateJalali of(int year, MonthJalali month, int dayOfMonth) {
        YEAR.checkValidValue(year);
        Objects.requireNonNull(month, "month");
        DAY_OF_MONTH.checkValidValue(dayOfMonth);
        return create(year, month.getValue(), dayOfMonth);
    }

    /**
     * Obtains an instance of {@code LocalDateJalali} from a year, month and day.
     * <p>
     * This returns a {@code LocalDateJalali} with the specified year, month and day-of-month.
     * The day must be valid for the year and month, otherwise an exception will be thrown.
     *
     * @param year       the year to represent, from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return the local date, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month-year
     */
    public static LocalDateJalali of(int year, int month, int dayOfMonth) {
        YEAR.checkValidValue(year);
        MONTH_OF_YEAR.checkValidValue(month);
        DAY_OF_MONTH.checkValidValue(dayOfMonth);
        return create(year, month, dayOfMonth);
    }

    public static LocalDateJalali of(String localDateJalaliString) {
        if (null == localDateJalaliString) {
            throw new IllegalArgumentException("invalid date: " + null);
        }
        String digit = localDateJalaliString.replaceAll("[^\\d]", "");
        if (8 != digit.length()) {
            throw new IllegalArgumentException("cant obtain date value from: " + localDateJalaliString);
        }
        return LocalDateJalali.of(Integer.parseInt(digit.substring(0, 4)), Integer.parseInt(digit.substring(4, 6)), Integer.parseInt(digit.substring(6, 8)));
    }

    /**
     * Obtains an instance of {@code LocalDateJalali} from a year and day-of-year.
     * <p>
     * This returns a {@code LocalDateJalali} with the specified year and day-of-year.
     * The day-of-year must be valid for the year, otherwise an exception will be thrown.
     *
     * @param year      the year to represent, from MIN_YEAR to MAX_YEAR
     * @param dayOfYear the day-of-year to represent, from 1 to 366
     * @return the local date, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-year is invalid for the year
     */
    public static LocalDateJalali ofYearDay(int year, int dayOfYear) {
        YEAR.checkValidValue(year);
        DAY_OF_YEAR.checkValidValue(dayOfYear);
        boolean leap = ChronologyJalali.INSTANCE.isLeapYear(year);
        if (dayOfYear == 366 && !leap) {
            throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + year + "' is not a leap year");
        }
        MonthJalali moy = MonthJalali.of((dayOfYear - 1) / 31 + 1);
        int monthEnd = moy.firstDayOfYear(leap) + moy.length(leap) - 1;
        if (dayOfYear > monthEnd) {
            moy = moy.plus(1);
        }
        int dom = dayOfYear - moy.firstDayOfYear(leap) + 1;
        return new LocalDateJalali(year, moy.getValue(), dom);
    }

    public static LocalDateJalali of(LocalDate localDate) {
        return getFirstDayOfGregorianYearInJalaliCalendar(localDate.getYear()).plusDays(localDate.getDayOfYear() - 1);
//        return ofEpochDay(localDate.toEpochDay());
    }

    /**
     * Obtains an instance of {@code LocalDateJalali} from the epoch day count.
     * <p>
     * This returns a {@code LocalDateJalali} with the specified epoch-day.
     * The {@link ChronoField#EPOCH_DAY EPOCH_DAY} is a simple incrementing count
     * of days where day 0 is 1970-01-01. Negative numbers represent earlier days.
     *
     * @param epochDay the Epoch Day to toModel, based on the epoch 1970-01-01
     * @return the local date, not null
     * @throws DateTimeException if the epoch day exceeds the supported date range
     */
    public static LocalDateJalali ofEpochDay(long epochDay) {
        YearJalali year = YearJalali.of(LOCAL_1970_01_01.getYear());
        MonthJalali month = LOCAL_1970_01_01.getMonth();
        short day = LOCAL_1970_01_01.day;
        if (epochDay < 0) {
            LOCAL_1970_01_01.minusDays(10);
            epochDay = Math.abs(epochDay);
            YearJalali previousYear = year.minusYears(1);
            while (epochDay >= previousYear.length()) {
                epochDay -= previousYear.length();
                year = previousYear;
                previousYear = year.minusYears(1);
            }
            MonthJalali previousMonth = month.minus(1);
            while (epochDay >= previousMonth.length(year.isLeap())) {
                epochDay -= previousMonth.length(year.isLeap());
                month = previousMonth;
                if (MonthJalali.ESFAND == month) {
                    year = year.minusYears(1);
                }
                previousMonth = month.minus(1);
            }
            day -= epochDay;
            if (day < 0) {
                month = month.minus(1);
                day += month.length(year.isLeap());
                if (MonthJalali.ESFAND == month) {
                    year = year.minusYears(1);
                }
            }
        } else {
            while (epochDay >= year.length()) {
                epochDay -= year.length();
                year = year.plusYears(1);
            }
            while (epochDay >= month.length(year.isLeap())) {
                epochDay -= month.length(year.isLeap());
                month = month.plus(1);
                if (MonthJalali.FARVARDIN == month) {
                    year = year.plusYears(1);
                }
            }
            day += epochDay;
            if (day > month.length(year.isLeap())) {
                day -= month.length(year.isLeap());
                month = month.plus(1);
                if (MonthJalali.FARVARDIN == month) {
                    year = year.plusYears(1);
                }
            }
        }
        return of(year.getValue(), month, day);
    }

    private static LocalDateJalali getFirstDayOfGregorianYearInJalaliCalendar(int gregorianYear) {
        int day;
        if (_10.contains(gregorianYear)) {
            day = 10;
        } else if (_12.contains(gregorianYear)) {
            day = 12;
        } else {
            day = 11;
        }
        return LocalDateJalali.of(gregorianYear - 622, 10, day);
    }

    /**
     * Obtains an instance of {@code LocalDateJalali} from a temporal object.
     * <p>
     * This obtains a local date based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code LocalDateJalali}.
     * <p>
     * The conversion uses the {@link TemporalQueries#localDate()} query, which relies
     * on extracting the {@link ChronoField#EPOCH_DAY EPOCH_DAY} field.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code LocalDateJalali::from}.
     *
     * @param temporal the temporal object to toModel, not null
     * @return the local date, not null
     * @throws DateTimeException if unable to toModel to a {@code LocalDateJalali}
     */
    public static LocalDateJalali from(TemporalAccessor temporal) {
        Objects.requireNonNull(temporal, "temporal");
        LocalDateJalali date = temporal.query(LOCAL_DATE);
        if (date == null) {
            throw new DateTimeException("Unable to obtain LocalDateJalali from TemporalAccessor: " + temporal + " of type " + temporal.getClass().getName());
        }
        return date;
    }

    /**
     * Obtains an instance of {@code LocalDateJalali} from a text string such as {@code 1367-08-12}.
     * <p>
     * The string must represent a valid date and is parsed using
     * {@link DateTimeFormatter#ISO_LOCAL_DATE}.
     *
     * @param text the text to parse such as "1367-08-12", not null
     * @return the parsed local date, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateJalali parse(CharSequence text) {
        return parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Obtains an instance of {@code LocalDateJalali} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a date.
     *
     * @param text      the text to parse, not null
     * @param formatter the formatter to use, not null
     * @return the parsed local date, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static LocalDateJalali parse(CharSequence text, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.parse(text, LocalDateJalali::from);
    }

    //-----------------------------------------------------------------------

    /**
     * Creates a local date from the year, month and day fields.
     *
     * @param year       the year to represent, validated from MIN_YEAR to MAX_YEAR
     * @param month      the month-of-year to represent, from 1 to 12, validated
     * @param dayOfMonth the day-of-month to represent, validated from 1 to 31
     * @return the local date, not null
     * @throws DateTimeException if the day-of-month is invalid for the month-year
     */
    private static LocalDateJalali create(int year, int month, int dayOfMonth) {
        if (dayOfMonth > 29) {
            int dom = 31;
            switch (month) {
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                    dom = 30;
                    break;
                case 12:
                    dom = YearJalali.isLeap(year) ? 30 : 29;
                    break;
            }
            if (dayOfMonth > dom) {
                if (12 == month) {
                    throw new DateTimeException("Invalid date '30 Esfand' as '" + year + "' is not a leap year");
                } else {
                    throw new DateTimeException("Invalid date '" + MonthJalali.of(month).name() + " " + dayOfMonth + "'");
                }
            }
        }
        return new LocalDateJalali(year, month, dayOfMonth);
    }

    /**
     * Resolves the date, resolving days past the end of month.
     *
     * @param year  the year to represent, validated from MIN_YEAR to MAX_YEAR
     * @param month the month-of-year to represent, validated from 1 to 12
     * @param day   the day-of-month to represent, validated from 1 to 31
     * @return the resolved date, not null
     */
    private static LocalDateJalali resolvePreviousValid(int year, int month, int day) {
        switch (month) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                day = Math.min(day, 31);
                break;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                day = Math.min(day, 30);
                break;
            case 12:
                day = Math.min(day, ChronologyJalali.INSTANCE.isLeapYear(year) ? 30 : 29);
                break;
        }
        return new LocalDateJalali(year, month, day);
    }

    public LocalDate toLocalDate() {
        int gregorianYear = this.getYear() + 622;
        LocalDate firstDayOfGregorianYearInGregorian = LocalDate.of(gregorianYear, 1, 1);
        LocalDateJalali firstDayOfGregorianYearInJalali = getFirstDayOfGregorianYearInJalaliCalendar(gregorianYear);
        int dif = this.getPositiveDistance(firstDayOfGregorianYearInJalali);
        return this.isBefore(firstDayOfGregorianYearInJalali) ? firstDayOfGregorianYearInGregorian.minusDays(dif) : firstDayOfGregorianYearInGregorian.plusDays(dif);
    }

    public int getPositiveDistance(LocalDateJalali other) {
        if (other == null) {
            return 0;
        }
        if (this.isAfter(other)) {
            return other.getPositiveDistance(this);
        }
        if (this.getYear() == other.getYear()) {
            return other.getDayOfYear() - this.getDayOfYear();
        }
        if (this.getYear() + 1 == other.getYear()) {
            return this.getRemainDayOfYear() + other.getDayOfYear();
        }
        int current = this.getYear();
        int diff = this.getRemainDayOfYear() + other.getDayOfYear();
        while (current + 1 != other.getYear()) {
            diff += YearJalali.of(current).length();
            current += 1;
        }
        return diff;
    }

    /**
     * @return the number of remaining days of the current year
     */
    public int getRemainDayOfYear() {
        return (isLeapYear() ? 366 : 365) - getDayOfYear();
    }


    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this date can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range},
     * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
     * methods will throw an exception.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The supported fields are:
     * <ul>
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
     * @return true if the field is supported on this date, false if not
     */
    @Override  // override for Javadoc
    public boolean isSupported(TemporalField field) {
        return ChronoLocalDate.super.isSupported(field);
    }

    /**
     * Checks if the specified unit is supported.
     * <p>
     * This checks if the specified unit can be added to, or subtracted from, this date.
     * If false, then calling the {@link #plus(long, TemporalUnit)} and
     * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
     * <p>
     * If the unit is a {@link ChronoUnit} then the query is implemented here.
     * The supported units are:
     * <ul>
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
        return ChronoLocalDate.super.isSupported(unit);
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This date is used to enhance the accuracy of the returned range.
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
            if (f.isDateBased()) {
                switch (f) {
                    case DAY_OF_MONTH:
                        return ValueRange.of(1, lengthOfMonth());
                    case DAY_OF_YEAR:
                        return ValueRange.of(1, lengthOfYear());
                    case ALIGNED_WEEK_OF_MONTH:
                        //TODO check this line again
//                        return ValueRange.of(1, getMonth() == JalaliMonth.FEBRUARY && isLeapYear() == false ? 4 : 5);
                        return ValueRange.of(1, getMonth() == MonthJalali.ESFAND && !isLeapYear() ? 4 : 5);
                    case YEAR_OF_ERA:
                        return (getYear() <= 0 ? ValueRange.of(1, Year.MAX_VALUE + 1) : ValueRange.of(1, Year.MAX_VALUE));
                }
                return field.range();
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.rangeRefinedBy(this);
    }

    /**
     * Gets the value of the specified field from this date as an {@code int}.
     * <p>
     * This queries this date for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this date, except {@code EPOCH_DAY} and {@code PROLEPTIC_MONTH}
     * which are too large to fit in an {@code int} and throw a {@code DateTimeException}.
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
    @Override  // override for Javadoc and performance
    public int get(TemporalField field) {
        if (field instanceof ChronoField) {
            return get0(field);
        }
        return ChronoLocalDate.super.get(field);
    }

    /**
     * Gets the value of the specified field from this date as a {@code long}.
     * <p>
     * This queries this date for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this date.
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
        if (field instanceof ChronoField) {
            if (field == EPOCH_DAY) {
                return toEpochDay();
            }
            if (field == PROLEPTIC_MONTH) {
                return getProlepticMonth();
            }
            return get0(field);
        }
        return field.getFrom(this);
    }

    private int get0(TemporalField field) {
        switch ((ChronoField) field) {
            case DAY_OF_WEEK:
                return getDayOfWeek().getValue();
            case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                return ((day - 1) % 7) + 1;
            case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                return ((getDayOfYear() - 1) % 7) + 1;
            case DAY_OF_MONTH:
                return day;
            case DAY_OF_YEAR:
                return getDayOfYear();
            case EPOCH_DAY:
                throw new UnsupportedTemporalTypeException("Invalid field 'EpochDay' for get() method, use getLong() instead");
            case ALIGNED_WEEK_OF_MONTH:
                return ((day - 1) / 7) + 1;
            case ALIGNED_WEEK_OF_YEAR:
                return ((getDayOfYear() - 1) / 7) + 1;
            case MONTH_OF_YEAR:
                return month;
            case PROLEPTIC_MONTH:
                throw new UnsupportedTemporalTypeException("Invalid field 'ProlepticMonth' for get() method, use getLong() instead");
            case YEAR_OF_ERA:
                return (year >= 1 ? year : 1 - year);
            case YEAR:
                return year;
            case ERA:
                return (year >= 1 ? 1 : 0);
        }
        throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
    }

    private long getProlepticMonth() {
        return (year * 12L + month - 1);
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the chronology of this date, which is the ISO calendar system.
     * <p>
     * The {@code Chronology} represents the calendar system in use.
     * The ISO-8601 calendar system is the modern civil calendar system used today
     * in most of the world. It is equivalent to the proleptic Gregorian calendar
     * system, in which today's rules for leap years are applied for all time.
     *
     * @return the ISO chronology, not null
     */
    @Override
    public ChronologyJalali getChronology() {
        return ChronologyJalali.INSTANCE;
    }

    /**
     * Gets the era applicable at this date.
     * <p>
     * The official ISO-8601 standard does not define eras, however {@code JalaliChronology} does.
     * It defines two eras, 'CE' from year one onwards and 'BCE' from year zero backwards.
     * Since dates before the Julian-Gregorian cutover are not in line with history,
     * the cutover between 'BCE' and 'CE' is also not aligned with the commonly used
     * eras, often referred to using 'BC' and 'AD'.
     * <p>
     * Users of this class should typically ignore this method as it exists primarily
     * to fulfill the {@link ChronoLocalDate} contract where it is necessary to support
     * the Japanese calendar system.
     * <p>
     * The returned era will be a singleton capable of being compared with the constants
     * in {@link ChronologyJalali} using the {@code ==} operator.
     *
     * @return the {@code JalaliChronology} era constant applicable at this date, not null
     */
    @Override // override for Javadoc
    public Era getEra() {
        return ChronoLocalDate.super.getEra();
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
        return year;
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
        return month;
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
        return MonthJalali.of(month);
    }

    /**
     * Gets the day-of-month field.
     * <p>
     * This method returns the primitive {@code int} value for the day-of-month.
     *
     * @return the day-of-month, from 1 to 31
     */
    public int getDayOfMonth() {
        return day;
    }

    /**
     * Gets the day-of-year field.
     * <p>
     * This method returns the primitive {@code int} value for the day-of-year.
     *
     * @return the day-of-year, from 1 to 365, or 366 in a leap year
     */
    public int getDayOfYear() {
        return getMonth().firstDayOfYear(isLeapYear()) + day - 1;
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
        int dow0 = (int) Math.floorMod(toEpochDay() + 3, 7L);
        return DayOfWeek.of(dow0 + 1);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the year is a leap year, according to the ISO proleptic
     * calendar system rules.
     * <p>
     * This method applies the current rules for leap years across the whole time-line.
     * In general, a year is a leap year if it is divisible by four without
     * remainder. However, years divisible by 100, are not leap years, with
     * the exception of years divisible by 400 which are.
     * <p>
     * For example, 1904 is a leap year it is divisible by 4.
     * 1900 was not a leap year as it is divisible by 100, however 2000 was a
     * leap year as it is divisible by 400.
     * <p>
     * The calculation is proleptic - applying the same rules into the far future and far past.
     * This is historically inaccurate, but is correct for the ISO-8601 standard.
     *
     * @return true if the year is leap, false otherwise
     */
    @Override // override for Javadoc and performance
    public boolean isLeapYear() {
        return ChronologyJalali.INSTANCE.isLeapYear(year);
    }

    /**
     * Returns the length of the month represented by this date.
     * <p>
     * This returns the length of the month in days.
     * For example, a date in January would return 31.
     *
     * @return the length of the month in days
     */
    @Override
    public int lengthOfMonth() {
        switch (month) {
            case 2:
                return (isLeapYear() ? 29 : 28);
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    /**
     * Returns the length of the year represented by this date.
     * <p>
     * This returns the length of the year in days, either 365 or 366.
     *
     * @return 366 if the year is leap, 365 otherwise
     */
    @Override // override for Javadoc and performance
    public int lengthOfYear() {
        return (isLeapYear() ? 366 : 365);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns an adjusted copy of this date.
     * <p>
     * This returns a {@code LocalDateJalali}, based on this one, with the date adjusted.
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
     *  result = localDateJalali.with(FARVARDIN).with(lastDayOfMonth());
     * </pre>
     * <p>
     * The result of this method is obtained by invoking the
     * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
     * specified adjuster passing {@code this} as the argument.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param adjuster the adjuster to use, not null
     * @return a {@code LocalDateJalali} based on {@code this} with the adjustment made, not null
     * @throws DateTimeException   if the adjustment cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public LocalDateJalali with(TemporalAdjuster adjuster) {
        // optimizations
        if (adjuster instanceof LocalDateJalali) {
            return (LocalDateJalali) adjuster;
        }
        return (LocalDateJalali) adjuster.adjustInto(this);
    }

    /**
     * Returns a copy of this date with the specified field set to a new value.
     * <p>
     * This returns a {@code LocalDateJalali}, based on this one, with the value
     * for the specified field changed.
     * This can be used to change any supported field, such as the year, month or day-of-month.
     * If it is not possible to set the value, because the field is not supported or for
     * some other reason, an exception is thrown.
     * <p>
     * In some cases, changing the specified field can cause the resulting date to become invalid,
     * such as changing the month from 31st January to February would make the day-of-month invalid.
     * In cases like this, the field is responsible for resolving the date. Typically it will choose
     * the previous valid date, which would be the last valid day of February in this example.
     * <p>
     * If the field is a {@link ChronoField} then the adjustment is implemented here.
     * The supported fields behave as follows:
     * <ul>
     * <li>{@code DAY_OF_WEEK} -
     * Returns a {@code LocalDateJalali} with the specified day-of-week.
     * The date is adjusted up to 6 days forward or backward within the boundary
     * of a Monday to Sunday week.
     * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH} -
     * Returns a {@code LocalDateJalali} with the specified aligned-day-of-week.
     * The date is adjusted to the specified month-based aligned-day-of-week.
     * Aligned weeks are counted such that the first week of a given month starts
     * on the first day of that month.
     * This may cause the date to be moved up to 6 days into the following month.
     * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR} -
     * Returns a {@code LocalDateJalali} with the specified aligned-day-of-week.
     * The date is adjusted to the specified year-based aligned-day-of-week.
     * Aligned weeks are counted such that the first week of a given year starts
     * on the first day of that year.
     * This may cause the date to be moved up to 6 days into the following year.
     * <li>{@code DAY_OF_MONTH} -
     * Returns a {@code LocalDateJalali} with the specified day-of-month.
     * The month and year will be unchanged. If the day-of-month is invalid for the
     * year and month, then a {@code DateTimeException} is thrown.
     * <li>{@code DAY_OF_YEAR} -
     * Returns a {@code LocalDateJalali} with the specified day-of-year.
     * The year will be unchanged. If the day-of-year is invalid for the
     * year, then a {@code DateTimeException} is thrown.
     * <li>{@code EPOCH_DAY} -
     * Returns a {@code LocalDateJalali} with the specified epoch-day.
     * This completely replaces the date and is equivalent to {@link #ofEpochDay(long)}.
     * <li>{@code ALIGNED_WEEK_OF_MONTH} -
     * Returns a {@code LocalDateJalali} with the specified aligned-week-of-month.
     * Aligned weeks are counted such that the first week of a given month starts
     * on the first day of that month.
     * This adjustment moves the date in whole week chunks to match the specified week.
     * The result will have the same day-of-week as this date.
     * This may cause the date to be moved into the following month.
     * <li>{@code ALIGNED_WEEK_OF_YEAR} -
     * Returns a {@code LocalDateJalali} with the specified aligned-week-of-year.
     * Aligned weeks are counted such that the first week of a given year starts
     * on the first day of that year.
     * This adjustment moves the date in whole week chunks to match the specified week.
     * The result will have the same day-of-week as this date.
     * This may cause the date to be moved into the following year.
     * <li>{@code MONTH_OF_YEAR} -
     * Returns a {@code LocalDateJalali} with the specified month-of-year.
     * The year will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     * <li>{@code PROLEPTIC_MONTH} -
     * Returns a {@code LocalDateJalali} with the specified proleptic-month.
     * The day-of-month will be unchanged, unless it would be invalid for the new month
     * and year. In that case, the day-of-month is adjusted to the maximum valid value
     * for the new month and year.
     * <li>{@code YEAR_OF_ERA} -
     * Returns a {@code LocalDateJalali} with the specified year-of-era.
     * The era and month will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     * <li>{@code YEAR} -
     * Returns a {@code LocalDateJalali} with the specified year.
     * The month will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     * <li>{@code ERA} -
     * Returns a {@code LocalDateJalali} with the specified era.
     * The year-of-era and month will be unchanged. The day-of-month will also be unchanged,
     * unless it would be invalid for the new month and year. In that case, the
     * day-of-month is adjusted to the maximum valid value for the new month and year.
     * </ul>
     * <p>
     * In all cases, if the new value is outside the valid range of values for the field
     * then a {@code DateTimeException} will be thrown.
     * <p>
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
     * @return a {@code LocalDateJalali} based on {@code this} with the specified field set, not null
     * @throws DateTimeException                if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public LocalDateJalali with(TemporalField field, long newValue) {
        if (field instanceof ChronoField f) {
            f.checkValidValue(newValue);
            switch (f) {
                case DAY_OF_WEEK:
                    return plusDays(newValue - getDayOfWeek().getValue());
                case ALIGNED_DAY_OF_WEEK_IN_MONTH:
                    return plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_MONTH));
                case ALIGNED_DAY_OF_WEEK_IN_YEAR:
                    return plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_YEAR));
                case DAY_OF_MONTH:
                    return withDayOfMonth((int) newValue);
                case DAY_OF_YEAR:
                    return withDayOfYear((int) newValue);
                case EPOCH_DAY:
                    return LocalDateJalali.ofEpochDay(newValue);
                case ALIGNED_WEEK_OF_MONTH:
                    return plusWeeks(newValue - getLong(ALIGNED_WEEK_OF_MONTH));
                case ALIGNED_WEEK_OF_YEAR:
                    return plusWeeks(newValue - getLong(ALIGNED_WEEK_OF_YEAR));
                case MONTH_OF_YEAR:
                    return withMonth((int) newValue);
                case PROLEPTIC_MONTH:
                    return plusMonths(newValue - getProlepticMonth());
                case YEAR_OF_ERA:
                    return withYear((int) (year >= 1 ? newValue : 1 - newValue));
                case YEAR:
                    return withYear((int) newValue);
                case ERA:
                    return (getLong(ERA) == newValue ? this : withYear(1 - year));
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.adjustInto(this, newValue);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateJalali} with the year altered.
     * <p>
     * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year the year to set in the result, from MIN_YEAR to MAX_YEAR
     * @return a {@code LocalDateJalali} based on this date with the requested year, not null
     * @throws DateTimeException if the year value is invalid
     */
    public LocalDateJalali withYear(int year) {
        if (this.year == year) {
            return this;
        }
        YEAR.checkValidValue(year);
        return resolvePreviousValid(year, month, day);
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the month-of-year altered.
     * <p>
     * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param month the month-of-year to set in the result, from 1 (January) to 12 (December)
     * @return a {@code LocalDateJalali} based on this date with the requested month, not null
     * @throws DateTimeException if the month-of-year value is invalid
     */
    public LocalDateJalali withMonth(int month) {
        if (this.month == month) {
            return this;
        }
        MONTH_OF_YEAR.checkValidValue(month);
        return resolvePreviousValid(year, month, day);
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the day-of-month altered.
     * <p>
     * If the resulting date is invalid, an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfMonth the day-of-month to set in the result, from 1 to 28-31
     * @return a {@code LocalDateJalali} based on this date with the requested day, not null
     * @throws DateTimeException if the day-of-month value is invalid,
     *                           or if the day-of-month is invalid for the month-year
     */
    public LocalDateJalali withDayOfMonth(int dayOfMonth) {
        if (this.day == dayOfMonth) {
            return this;
        }
        return of(year, month, dayOfMonth);
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the day-of-year altered.
     * <p>
     * If the resulting date is invalid, an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfYear the day-of-year to set in the result, from 1 to 365-366
     * @return a {@code LocalDateJalali} based on this date with the requested day, not null
     * @throws DateTimeException if the day-of-year value is invalid,
     *                           or if the day-of-year is invalid for the year
     */
    public LocalDateJalali withDayOfYear(int dayOfYear) {
        if (this.getDayOfYear() == dayOfYear) {
            return this;
        }
        return ofYearDay(year, dayOfYear);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this date with the specified amount added.
     * <p>
     * This returns a {@code LocalDateJalali}, based on this one, with the specified amount added.
     * The amount is typically {@link Period} but may be any other type implementing
     * the {@link TemporalAmount} interface.
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
     * @return a {@code LocalDateJalali} based on this date with the addition made, not null
     * @throws DateTimeException   if the addition cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public LocalDateJalali plus(TemporalAmount amountToAdd) {
        if (amountToAdd instanceof Period periodToAdd) {
            return plusMonths(periodToAdd.toTotalMonths()).plusDays(periodToAdd.getDays());
        }
        Objects.requireNonNull(amountToAdd, "amountToAdd");
        return (LocalDateJalali) amountToAdd.addTo(this);
    }

    /**
     * Returns a copy of this date with the specified amount added.
     * <p>
     * This returns a {@code LocalDateJalali}, based on this one, with the amount
     * in terms of the unit added. If it is not possible to add the amount, because the
     * unit is not supported or for some other reason, an exception is thrown.
     * <p>
     * In some cases, adding the amount can cause the resulting date to become invalid.
     * For example, adding one month to 31st January would result in 31st February.
     * In cases like this, the unit is responsible for resolving the date.
     * Typically it will choose the previous valid date, which would be the last valid
     * day of February in this example.
     * <p>
     * If the field is a {@link ChronoUnit} then the addition is implemented here.
     * The supported fields behave as follows:
     * <ul>
     * <li>{@code DAYS} -
     * Returns a {@code LocalDateJalali} with the specified number of days added.
     * This is equivalent to {@link #plusDays(long)}.
     * <li>{@code WEEKS} -
     * Returns a {@code LocalDateJalali} with the specified number of weeks added.
     * This is equivalent to {@link #plusWeeks(long)} and uses a 7 day week.
     * <li>{@code MONTHS} -
     * Returns a {@code LocalDateJalali} with the specified number of months added.
     * This is equivalent to {@link #plusMonths(long)}.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     * <li>{@code YEARS} -
     * Returns a {@code LocalDateJalali} with the specified number of years added.
     * This is equivalent to {@link #plusYears(long)}.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     * <li>{@code DECADES} -
     * Returns a {@code LocalDateJalali} with the specified number of decades added.
     * This is equivalent to calling {@link #plusYears(long)} with the amount
     * multiplied by 10.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     * <li>{@code CENTURIES} -
     * Returns a {@code LocalDateJalali} with the specified number of centuries added.
     * This is equivalent to calling {@link #plusYears(long)} with the amount
     * multiplied by 100.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     * <li>{@code MILLENNIA} -
     * Returns a {@code LocalDateJalali} with the specified number of millennia added.
     * This is equivalent to calling {@link #plusYears(long)} with the amount
     * multiplied by 1,000.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     * <li>{@code ERAS} -
     * Returns a {@code LocalDateJalali} with the specified number of eras added.
     * Only two eras are supported so the amount must be one, zero or minus one.
     * If the amount is non-zero then the year is changed such that the year-of-era
     * is unchanged.
     * The day-of-month will be unchanged unless it would be invalid for the new
     * month and year. In that case, the day-of-month is adjusted to the maximum
     * valid value for the new month and year.
     * </ul>
     * <p>
     * All other {@code ChronoUnit} instances will throw an {@code UnsupportedTemporalTypeException}.
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
     * @return a {@code LocalDateJalali} based on this date with the specified amount added, not null
     * @throws DateTimeException                if the addition cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public LocalDateJalali plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit f) {
            switch (f) {
                case DAYS:
                    return plusDays(amountToAdd);
                case WEEKS:
                    return plusWeeks(amountToAdd);
                case MONTHS:
                    return plusMonths(amountToAdd);
                case YEARS:
                    return plusYears(amountToAdd);
                case DECADES:
                    return plusYears(Math.multiplyExact(amountToAdd, 10L));
                case CENTURIES:
                    return plusYears(Math.multiplyExact(amountToAdd, 100L));
                case MILLENNIA:
                    return plusYears(Math.multiplyExact(amountToAdd, 1000L));
                case ERAS:
                    return with(ERA, Math.addExact(getLong(ERA), amountToAdd));
            }
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
        return unit.addTo(this, amountToAdd);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of years added.
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
     * @param yearsToAdd the years to add, may be negative
     * @return a {@code LocalDateJalali} based on this date with the years added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali plusYears(long yearsToAdd) {
        if (yearsToAdd == 0) {
            return this;
        }
        int newYear = YEAR.checkValidIntValue(year + yearsToAdd);  // safe overflow
        return resolvePreviousValid(newYear, month, day);
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of months added.
     * <p>
     * This method adds the specified amount to the months field in three steps:
     * <ol>
     * <li>Add the input months to the month-of-year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day-of-month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 1401-06-31 plus one month would result in the invalid date
     * 1401-07-31. Instead of returning an invalid result, the last valid day
     * of the month, 1401-07-30, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToAdd the months to add, may be negative
     * @return a {@code LocalDateJalali} based on this date with the months added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali plusMonths(long monthsToAdd) {
        if (monthsToAdd == 0) {
            return this;
        }
        long monthCount = year * 12L + (month - 1);
        long calcMonths = monthCount + monthsToAdd;  // safe overflow
        int newYear = YEAR.checkValidIntValue(Math.floorDiv(calcMonths, 12L));
        int newMonth = (int) (Math.floorMod(calcMonths, 12L) + 1);
        return resolvePreviousValid(newYear, newMonth, day);
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of weeks added.
     * <p>
     * This method adds the specified amount in weeks to the days field incrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one week would result in 2009-01-07.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeksToAdd the weeks to add, may be negative
     * @return a {@code LocalDateJalali} based on this date with the weeks added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali plusWeeks(long weeksToAdd) {
        return plusDays(Math.multiplyExact(weeksToAdd, 7L));
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of days added.
     * <p>
     * This method adds the specified amount to the days field incrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2008-12-31 plus one day would result in 2009-01-01.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param daysToAdd the days to add, may be negative
     * @return a {@code LocalDateJalali} based on this date with the days added, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali plusDays(long daysToAdd) {
        if (daysToAdd == 0) {
            return this;
        }
        YearJalali year = YearJalali.of(getYear());
        MonthJalali month = getMonth();
        int dayOfMonth = getDayOfMonth();
        while (daysToAdd >= year.length()) {
            daysToAdd -= year.length();
            year = year.plusYears(1);
        }
        while (daysToAdd >= month.length(year.isLeap())) {
            daysToAdd -= month.length(year.isLeap());
            month = month.plus(1);
            if (month == MonthJalali.FARVARDIN) {
                year = year.plusYears(1);
            }
        }
        dayOfMonth += daysToAdd;
        if (dayOfMonth > month.length(year.isLeap())) {
            dayOfMonth -= month.length(year.isLeap());
            month = month.plus(1);
            if (month == MonthJalali.FARVARDIN) {
                year = year.plusYears(1);
            }
        }
        return of(year.getValue(), month, dayOfMonth);
//        long mjDay = Math.addExact(toEpochDay(), daysToAdd);
//        return LocalDateJalali.ofEpochDay(mjDay);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this date with the specified amount subtracted.
     * <p>
     * This returns a {@code LocalDateJalali}, based on this one, with the specified amount subtracted.
     * The amount is typically {@link Period} but may be any other type implementing
     * the {@link TemporalAmount} interface.
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
     * @return a {@code LocalDateJalali} based on this date with the subtraction made, not null
     * @throws DateTimeException   if the subtraction cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public LocalDateJalali minus(TemporalAmount amountToSubtract) {
        if (amountToSubtract instanceof Period periodToSubtract) {
            return minusMonths(periodToSubtract.toTotalMonths()).minusDays(periodToSubtract.getDays());
        }
        Objects.requireNonNull(amountToSubtract, "amountToSubtract");
        return (LocalDateJalali) amountToSubtract.subtractFrom(this);
    }

    /**
     * Returns a copy of this date with the specified amount subtracted.
     * <p>
     * This returns a {@code LocalDateJalali}, based on this one, with the amount
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
     * @return a {@code LocalDateJalali} based on this date with the specified amount subtracted, not null
     * @throws DateTimeException                if the subtraction cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public LocalDateJalali minus(long amountToSubtract, TemporalUnit unit) {
        return (amountToSubtract == Long.MIN_VALUE ? plus(Long.MAX_VALUE, unit).plus(1, unit) : plus(-amountToSubtract, unit));
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of years subtracted.
     * <p>
     * This method subtracts the specified amount from the years field in three steps:
     * <ol>
     * <li>Subtract the input years from the year field</li>
     * <li>Check if the resulting date would be invalid</li>
     * <li>Adjust the day-of-month to the last valid day if necessary</li>
     * </ol>
     * <p>
     * For example, 2008-02-29 (leap year) minus one year would result in the
     * invalid date 2007-02-29 (standard year). Instead of returning an invalid
     * result, the last valid day of the month, 2007-02-28, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToSubtract the years to subtract, may be negative
     * @return a {@code LocalDateJalali} based on this date with the years subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali minusYears(long yearsToSubtract) {
        return (yearsToSubtract == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-yearsToSubtract));
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of months subtracted.
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
     * of the month, 2007-02-28, is selected instead.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param monthsToSubtract the months to subtract, may be negative
     * @return a {@code LocalDateJalali} based on this date with the months subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali minusMonths(long monthsToSubtract) {
        return (monthsToSubtract == Long.MIN_VALUE ? plusMonths(Long.MAX_VALUE).plusMonths(1) : plusMonths(-monthsToSubtract));
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of weeks subtracted.
     * <p>
     * This method subtracts the specified amount in weeks from the days field decrementing
     * the month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2009-01-07 minus one week would result in 2008-12-31.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param weeksToSubtract the weeks to subtract, may be negative
     * @return a {@code LocalDateJalali} based on this date with the weeks subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali minusWeeks(long weeksToSubtract) {
        return (weeksToSubtract == Long.MIN_VALUE ? plusWeeks(Long.MAX_VALUE).plusWeeks(1) : plusWeeks(-weeksToSubtract));
    }

    /**
     * Returns a copy of this {@code LocalDateJalali} with the specified number of days subtracted.
     * <p>
     * This method subtracts the specified amount from the days field decrementing the
     * month and year fields as necessary to ensure the result remains valid.
     * The result is only invalid if the maximum/minimum year is exceeded.
     * <p>
     * For example, 2009-01-01 minus one day would result in 2008-12-31.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param daysToSubtract the days to subtract, may be negative
     * @return a {@code LocalDateJalali} based on this date with the days subtracted, not null
     * @throws DateTimeException if the result exceeds the supported date range
     */
    public LocalDateJalali minusDays(long daysToSubtract) {
        return (daysToSubtract == Long.MIN_VALUE ? plusDays(Long.MAX_VALUE).plusDays(1) : plusDays(-daysToSubtract));
    }

    //-----------------------------------------------------------------------

    /**
     * Queries this date using the specified query.
     * <p>
     * This queries this date using the specified query strategy object.
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
    @Override
    public <R> R query(TemporalQuery<R> query) {
        if (query == TemporalQueries.localDate()) {
            return (R) this;
        }
        return ChronoLocalDate.super.query(query);
    }

    /**
     * Adjusts the specified temporal object to have the same date as this object.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the date changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * passing {@link ChronoField#EPOCH_DAY} as the field.
     * <p>
     * In most cases, it is clearer to reverse the calling pattern by using
     * {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   // these two lines are equivalent, but the second approach is recommended
     *   temporal = thisLocalDate.adjustInto(temporal);
     *   temporal = temporal.with(thisLocalDate);
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
        return ChronoLocalDate.super.adjustInto(temporal);
    }

    /**
     * Calculates the amount of time until another date in terms of the specified unit.
     * <p>
     * This calculates the amount of time between two {@code LocalDateJalali}
     * objects in terms of a single {@code TemporalUnit}.
     * The start and end points are {@code this} and the specified date.
     * The result will be negative if the end is before the start.
     * The {@code Temporal} passed to this method is converted to a
     * {@code LocalDateJalali} using {@link #from(TemporalAccessor)}.
     * For example, the amount in days between two dates can be calculated
     * using {@code startDate.until(endDate, DAYS)}.
     * <p>
     * The calculation returns a whole number, representing the number of
     * complete units between the two dates.
     * For example, the amount in months between 2012-06-15 and 2012-08-14
     * will only be one month as it is one day short of two months.
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
     * The units {@code DAYS}, {@code WEEKS}, {@code MONTHS}, {@code YEARS},
     * {@code DECADES}, {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS}
     * are supported. Other {@code ChronoUnit} values will throw an exception.
     * <p>
     * If the unit is not a {@code ChronoUnit}, then the result of this method
     * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
     * passing {@code this} as the first argument and the converted input temporal
     * as the second argument.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param endExclusive the end date, exclusive, which is converted to a {@code LocalDateJalali}, not null
     * @param unit         the unit to measure the amount in, not null
     * @return the amount of time between this date and the end date
     * @throws DateTimeException                if the amount cannot be calculated, or the end
     *                                          temporal cannot be converted to a {@code LocalDateJalali}
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        LocalDateJalali end = LocalDateJalali.from(endExclusive);
        if (unit instanceof ChronoUnit) {
            switch ((ChronoUnit) unit) {
                case DAYS:
                    return daysUntil(end);
                case WEEKS:
                    return daysUntil(end) / 7;
                case MONTHS:
                    return monthsUntil(end);
                case YEARS:
                    return monthsUntil(end) / 12;
                case DECADES:
                    return monthsUntil(end) / 120;
                case CENTURIES:
                    return monthsUntil(end) / 1200;
                case MILLENNIA:
                    return monthsUntil(end) / 12000;
                case ERAS:
                    return end.getLong(ERA) - getLong(ERA);
            }
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
        return unit.between(this, end);
    }

    long daysUntil(LocalDateJalali end) {
        return end.toEpochDay() - toEpochDay();  // no overflow
    }

    private long monthsUntil(LocalDateJalali end) {
        long packed1 = getProlepticMonth() * 32L + getDayOfMonth();  // no overflow
        long packed2 = end.getProlepticMonth() * 32L + end.getDayOfMonth();  // no overflow
        return (packed2 - packed1) / 32;
    }

    /**
     * Calculates the period between this date and another date as a {@code Period}.
     * <p>
     * This calculates the period between two dates in terms of years, months and days.
     * The start and end points are {@code this} and the specified date.
     * The result will be negative if the end is before the start.
     * The negative sign will be the same in each of year, month and day.
     * <p>
     * The calculation is performed using the ISO calendar system.
     * If necessary, the input date will be converted to ISO.
     * <p>
     * The start date is included, but the end date is not.
     * The period is calculated by removing complete months, then calculating
     * the remaining number of days, adjusting to ensure that both have the same sign.
     * The number of months is then normalized into years and months based on a 12 month year.
     * A month is considered to be complete if the end day-of-month is greater
     * than or equal to the start day-of-month.
     * For example, from {@code 2010-01-15} to {@code 2011-03-18} is "1 year, 2 months and 3 days".
     * <p>
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use {@link Period#between(ChronoLocalDate, ChronoLocalDate)}:
     * <pre>
     *   // these two lines are equivalent
     *   period = start.until(end);
     *   period = Period.between(start, end);
     * </pre>
     * The choice should be made based on which makes the code more readable.
     *
     * @param endDateExclusive the end date, exclusive, which may be in any chronology, not null
     * @return the period between this date and the end date, not null
     */
    @Override
    public Period until(ChronoLocalDate endDateExclusive) {
        LocalDateJalali end = LocalDateJalali.from(endDateExclusive);
        long totalMonths = end.getProlepticMonth() - this.getProlepticMonth();  // safe
        int days = end.day - this.day;
        if (totalMonths > 0 && days < 0) {
            totalMonths--;
            LocalDateJalali calcDate = this.plusMonths(totalMonths);
            days = (int) (end.toEpochDay() - calcDate.toEpochDay());  // safe
        } else if (totalMonths < 0 && days > 0) {
            totalMonths++;
            days -= end.lengthOfMonth();
        }
        long years = totalMonths / 12;  // safe
        int months = (int) (totalMonths % 12);  // safe
        return Period.of(Math.toIntExact(years), months, days);
    }

    /**
     * Formats this date using the specified formatter.
     * <p>
     * This date will be passed to the formatter to produce a string.
     *
     * @param formatter the formatter to use, not null
     * @return the formatted date string, not null
     * @throws DateTimeException if an error occurs during printing
     */
    @Override  // override for Javadoc and performance
    public String format(DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.withChronology(getChronology()).format(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Combines this date with a time to create a {@code LocalDateTimeJalali}.
     * <p>
     * This returns a {@code LocalDateTimeJalali} formed from this date at the specified time.
     * All possible combinations of date and time are valid.
     *
     * @param time the time to combine with, not null
     * @return the local date-time formed from this date and the specified time, not null
     */
    @Override
    public LocalDateTimeJalali atTime(LocalTime time) {
        return LocalDateTimeJalali.of(this, time);
    }

    /**
     * Combines this date with a time to create a {@code LocalDateTimeJalali}.
     * <p>
     * This returns a {@code LocalDateTimeJalali} formed from this date at the
     * specified hour and minute.
     * The seconds and nanosecond fields will be set to zero.
     * The individual time fields must be within their valid range.
     * All possible combinations of date and time are valid.
     *
     * @param hour   the hour-of-day to use, from 0 to 23
     * @param minute the minute-of-hour to use, from 0 to 59
     * @return the local date-time formed from this date and the specified time, not null
     * @throws DateTimeException if the value of any field is out of range
     */
    public LocalDateTimeJalali atTime(int hour, int minute) {
        return atTime(LocalTime.of(hour, minute));
    }

    /**
     * Combines this date with a time to create a {@code LocalDateTimeJalali}.
     * <p>
     * This returns a {@code LocalDateTimeJalali} formed from this date at the
     * specified hour, minute and second.
     * The nanosecond field will be set to zero.
     * The individual time fields must be within their valid range.
     * All possible combinations of date and time are valid.
     *
     * @param hour   the hour-of-day to use, from 0 to 23
     * @param minute the minute-of-hour to use, from 0 to 59
     * @param second the second-of-minute to represent, from 0 to 59
     * @return the local date-time formed from this date and the specified time, not null
     * @throws DateTimeException if the value of any field is out of range
     */
    public LocalDateTimeJalali atTime(int hour, int minute, int second) {
        return atTime(LocalTime.of(hour, minute, second));
    }

    /**
     * Combines this date with a time to create a {@code LocalDateTimeJalali}.
     * <p>
     * This returns a {@code LocalDateTimeJalali} formed from this date at the
     * specified hour, minute, second and nanosecond.
     * The individual time fields must be within their valid range.
     * All possible combinations of date and time are valid.
     *
     * @param hour         the hour-of-day to use, from 0 to 23
     * @param minute       the minute-of-hour to use, from 0 to 59
     * @param second       the second-of-minute to represent, from 0 to 59
     * @param nanoOfSecond the nano-of-second to represent, from 0 to 999,999,999
     * @return the local date-time formed from this date and the specified time, not null
     * @throws DateTimeException if the value of any field is out of range
     */
    public LocalDateTimeJalali atTime(int hour, int minute, int second, int nanoOfSecond) {
        return atTime(LocalTime.of(hour, minute, second, nanoOfSecond));
    }

    /**
     * Combines this date with the time of midnight to create a {@code LocalDateTimeJalali}
     * at the start of this date.
     * <p>
     * This returns a {@code LocalDateTimeJalali} formed from this date at the time of
     * midnight, 00:00, at the start of this date.
     *
     * @return the local date-time of midnight at the start of this date, not null
     */
    public LocalDateTimeJalali atStartOfDay() {
        return LocalDateTimeJalali.of(this, LocalTime.MIDNIGHT);
    }

    //-----------------------------------------------------------------------
    @Override
    public long toEpochDay() {
        long total = year * 365L;
        for (int i = 0; i < year; i++) {
            if (YearJalali.isLeap(i)) {
                total++;
            }
        }
        total += this.getDayOfYear();
        return total - DAYS_LEFT_IN_1970_01_01;
    }

    //-----------------------------------------------------------------------

    /**
     * Compares this date to another date.
     * <p>
     * The comparison is primarily based on the date, from earliest to latest.
     * It is "consistent with equals", as defined by {@link Comparable}.
     * <p>
     * If all the dates being compared are instances of {@code LocalDateJalali},
     * then the comparison will be entirely based on the date.
     * If some dates being compared are in different chronologies, then the
     * chronology is also considered, see {@link ChronoLocalDate#compareTo}.
     *
     * @param other the other date to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override  // override for Javadoc and performance
    public int compareTo(ChronoLocalDate other) {
        if (other instanceof LocalDateJalali) {
            return compareTo0((LocalDateJalali) other);
        }
        return ChronoLocalDate.super.compareTo(other);
    }

    int compareTo0(LocalDateJalali otherDate) {
        int cmp = (year - otherDate.year);
        if (cmp == 0) {
            cmp = (month - otherDate.month);
            if (cmp == 0) {
                cmp = (day - otherDate.day);
            }
        }
        return cmp;
    }

    /**
     * Checks if this date is after the specified date.
     * <p>
     * This checks to see if this date represents a point on the
     * local time-line after the other date.
     * <pre>
     *   LocalDateJalali a = LocalDateJalali.of(2012, 6, 30);
     *   LocalDateJalali b = LocalDateJalali.of(2012, 7, 1);
     *   a.isAfter(b) == false
     *   a.isAfter(a) == false
     *   b.isAfter(a) == true
     * </pre>
     * <p>
     * This method only considers the position of the two dates on the local time-line.
     * It does not take into user the chronology, or calendar system.
     * This is different from the comparison in {@link #compareTo(ChronoLocalDate)},
     * but is the same approach as {@link ChronoLocalDate#timeLineOrder()}.
     *
     * @param other the other date to compare to, not null
     * @return true if this date is after the specified date
     */
    @Override  // override for Javadoc and performance
    public boolean isAfter(ChronoLocalDate other) {
        if (other instanceof LocalDateJalali) {
            return compareTo0((LocalDateJalali) other) > 0;
        }
        return ChronoLocalDate.super.isAfter(other);
    }

    /**
     * Checks if this date is before the specified date.
     * <p>
     * This checks to see if this date represents a point on the
     * local time-line before the other date.
     * <pre>
     *   LocalDateJalali a = LocalDateJalali.of(2012, 6, 30);
     *   LocalDateJalali b = LocalDateJalali.of(2012, 7, 1);
     *   a.isBefore(b) == true
     *   a.isBefore(a) == false
     *   b.isBefore(a) == false
     * </pre>
     * <p>
     * This method only considers the position of the two dates on the local time-line.
     * It does not take into user the chronology, or calendar system.
     * This is different from the comparison in {@link #compareTo(ChronoLocalDate)},
     * but is the same approach as {@link ChronoLocalDate#timeLineOrder()}.
     *
     * @param other the other date to compare to, not null
     * @return true if this date is before the specified date
     */
    @Override  // override for Javadoc and performance
    public boolean isBefore(ChronoLocalDate other) {
        if (other instanceof LocalDateJalali) {
            return compareTo0((LocalDateJalali) other) < 0;
        }
        return ChronoLocalDate.super.isBefore(other);
    }

    /**
     * Checks if this date is equal to the specified date.
     * <p>
     * This checks to see if this date represents the same point on the
     * local time-line as the other date.
     * <pre>
     *   LocalDateJalali a = LocalDateJalali.of(2012, 6, 30);
     *   LocalDateJalali b = LocalDateJalali.of(2012, 7, 1);
     *   a.isEqual(b) == false
     *   a.isEqual(a) == true
     *   b.isEqual(a) == false
     * </pre>
     * <p>
     * This method only considers the position of the two dates on the local time-line.
     * It does not take into user the chronology, or calendar system.
     * This is different from the comparison in {@link #compareTo(ChronoLocalDate)}
     * but is the same approach as {@link ChronoLocalDate#timeLineOrder()}.
     *
     * @param other the other date to compare to, not null
     * @return true if this date is equal to the specified date
     */
    @Override  // override for Javadoc and performance
    public boolean isEqual(ChronoLocalDate other) {
        if (other instanceof LocalDateJalali) {
            return compareTo0((LocalDateJalali) other) == 0;
        }
        return ChronoLocalDate.super.isEqual(other);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this date is equal to another date.
     * <p>
     * Compares this {@code LocalDateJalali} with another ensuring that the date is the same.
     * <p>
     * Only objects of type {@code LocalDateJalali} are compared, other types return false.
     * To compare the dates of two {@code TemporalAccessor} instances, including dates
     * in two different chronologies, use {@link ChronoField#EPOCH_DAY} as a comparator.
     *
     * @param obj the object to check, null returns false
     * @return true if this is equal to the other date
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof LocalDateJalali) {
            return compareTo0((LocalDateJalali) obj) == 0;
        }
        return false;
    }

    /**
     * A hash code for this date.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        int yearValue = year;
        return (yearValue & 0xFFFFF800) ^ ((yearValue << 11) + ((int) month << 6) + ((int) day));
    }

    //-----------------------------------------------------------------------

    /**
     * Outputs this date as a {@code String}, such as {@code 1367-08-12}.
     * <p>
     * The output will be in the ISO-8601 format {@code uuuu-MM-dd}.
     *
     * @return a string representation of this date, not null
     */
    @Override
    public String toString() {
        int yearValue = year;
        int monthValue = month;
        int dayValue = day;
        int absYear = Math.abs(yearValue);
        StringBuilder buf = new StringBuilder(10);
        if (absYear < 1000) {
            if (yearValue < 0) {
                buf.append(yearValue - 10000).deleteCharAt(1);
            } else {
                buf.append(yearValue + 10000).deleteCharAt(0);
            }
        } else {
            if (yearValue > 9999) {
                buf.append('+');
            }
            buf.append(yearValue);
        }
        return buf.append(monthValue < 10 ? "-0" : "-")
                .append(monthValue)
                .append(dayValue < 10 ? "-0" : "-")
                .append(dayValue)
                .toString();
    }

}
