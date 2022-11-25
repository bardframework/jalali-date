package org.bardframework.time;

import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Period;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.time.temporal.*;
import java.util.Objects;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;

/**
 * A year in the ISO-8601 calendar system, such as {@code 1367}.
 * <p>
 * {@code JalaliYear} is an immutable date-time object that represents a year.
 * Any field that can be derived from a year can be obtained.
 * <p>
 * <b>Note that years in the ISO chronology only align with years in the
 * Gregorian-Julian system for modern years. Parts of Russia did not switch to the
 * modern Gregorian/ISO rules until 1920.
 * As such, historical years must be treated with caution.</b>
 * <p>
 * This class does not store or represent a month, day, time or time-zone.
 * For example, the value "1367" can be stored in a {@code JalaliYear}.
 * <p>
 * Years represented by this class follow the ISO-8601 standard and use
 * the proleptic numbering system. JalaliYear 1 is preceded by year 0, then by year -1.
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
 * {@code JalaliYear} may have unpredictable results and should be avoided.
 * The {@code equals} method should be used for comparisons.
 *
 * @author Vahid Zafari
 * This class is immutable and thread-safe.
 */
public final class YearJalali implements Temporal, TemporalAdjuster, Comparable<YearJalali>, Serializable {

    /**
     * The minimum supported year, '-999,999,999'.
     */
    public static final int MIN_VALUE = -999_999_999;
    /**
     * The maximum supported year, '+999,999,999'.
     */
    public static final int MAX_VALUE = 999_999_999;
    private static final int[] validRemaining_after_474 = {0, 4, 8, 12, 16, 20, 24, 29, 33, 37, 41, 45, 49, 53, 57, 62, 66, 70, 74, 78, 82, 86, 90, 95, 99, 103, 107, 111, 115, 119, 124};
    private static final int[] validRemaining_before_474 = {0, 4, 8, 12, 16, 20, 25, 29, 33, 37, 41, 45, 49, 53, 58, 62, 66, 70, 74, 78, 82, 86, 91, 95, 99, 103, 107, 111, 115, 120, 124};
    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -23038383694477807L;
    /**
     * Parser.
     */
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .toFormatter();

    /**
     * The year being represented.
     */
    private final int year;

    //-----------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param year the year to represent
     */
    private YearJalali(int year) {
        this.year = year;
    }

    /**
     * Obtains the current year from the system clock in the default time-zone.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current year.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current year using the system clock and default time-zone, not null
     */
    public static YearJalali now() {
        return now(Clock.systemDefaultZone());
    }

    /**
     * Obtains the current year from the system clock in the specified time-zone.
     * <p>
     * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current year.
     * Specifying the time-zone avoids dependence on the default time-zone.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @param zone the zone ID to use, not null
     * @return the current year using the system clock, not null
     */
    public static YearJalali now(ZoneId zone) {
        return now(Clock.system(zone));
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains the current year from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current year.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@link Clock dependency injection}.
     *
     * @param clock the clock to use, not null
     * @return the current year, not null
     */
    public static YearJalali now(Clock clock) {
        final LocalDateJalali now = LocalDateJalali.now(clock);  // called once
        return YearJalali.of(now.getYear());
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliYear}.
     * <p>
     * This method accepts a year value from the proleptic ISO calendar system.
     * <p>
     * The year 2AD/CE is represented by 2.<br>
     * The year 1AD/CE is represented by 1.<br>
     * The year 1BC/BCE is represented by 0.<br>
     * The year 2BC/BCE is represented by -1.<br>
     *
     * @param isoYear the ISO proleptic year to represent, from {@code MIN_VALUE} to {@code MAX_VALUE}
     * @return the year, not null
     * @throws DateTimeException if the field is invalid
     */
    public static YearJalali of(int isoYear)
            throws DateTimeException {
        YEAR.checkValidValue(isoYear);
        return new YearJalali(isoYear);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliYear} from a temporal object.
     * <p>
     * This obtains a year based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code JalaliYear}.
     * <p>
     * The conversion extracts the {@link ChronoField#YEAR year} field.
     * The extraction is only permitted if the temporal object has an ISO
     * chronology, or can be converted to a {@code LocalDateJalali}.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code JalaliYear::from}.
     *
     * @param temporal the temporal object to toModel, not null
     * @return the year, not null
     * @throws DateTimeException if unable to toModel to a {@code JalaliYear}
     */
    public static YearJalali from(TemporalAccessor temporal) {
        if (temporal instanceof YearJalali) {
            return (YearJalali) temporal;
        }
        Objects.requireNonNull(temporal, "temporal");
        try {
            if (!ChronologyJalali.INSTANCE.equals(Chronology.from(temporal))) {
                temporal = LocalDateJalali.from(temporal);
            }
            return of(temporal.get(YEAR));
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain JalaliYear from TemporalAccessor: " +
                    temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    /**
     * Obtains an instance of {@code JalaliYear} from a text string such as {@code 1367}.
     * <p>
     * The string must represent a valid year.
     * Years outside the range 0000 to 9999 must be prefixed by the plus or minus symbol.
     *
     * @param text the text to parse such as "1367", not null
     * @return the parsed year, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static YearJalali parse(CharSequence text) {
        return parse(text, PARSER);
    }

    //-------------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliYear} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a year.
     *
     * @param text      the text to parse, not null
     * @param formatter the formatter to use, not null
     * @return the parsed year, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static YearJalali parse(CharSequence text, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.parse(text, YearJalali::from);
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
     * @param year the year to check
     * @return true if the year is leap, false otherwise
     */
    public static boolean isLeap(long year) {
        //Algorithm from www.wikipedia.com
        return (year % 33 == 1 || year % 33 == 5 || year % 33 == 9 || year % 33 == 13 ||
                year % 33 == 17 || year % 33 == 22 || year % 33 == 26 || year % 33 == 30);

//        long remain = year % 128;
//        int[] remaining;
//        if (year >= 474) {
//            remaining = validRemaining_after_474;
//        } else {
//            remaining = validRemaining_before_474;
//        }
//        for (int i : remaining) {
//            if (i == remain) {
//                return true;
//            }
//        }
//        return false;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the year value.
     * <p>
     * The year returned by this method is proleptic as per {@code get(YEAR)}.
     *
     * @return the year, {@code MIN_VALUE} to {@code MAX_VALUE}
     */
    public int getValue() {
        return year;
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this year can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range},
     * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
     * methods will throw an exception.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The supported fields are:
     * <ul>
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
     * @return true if the field is supported on this year, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == YEAR || field == YEAR_OF_ERA || field == ERA;
        }
        return field != null && field.isSupportedBy(this);
    }

    /**
     * Checks if the specified unit is supported.
     * <p>
     * This checks if the specified unit can be added to, or subtracted from, this year.
     * If false, then calling the {@link #plus(long, TemporalUnit)} and
     * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
     * <p>
     * If the unit is a {@link ChronoUnit} then the query is implemented here.
     * The supported units are:
     * <ul>
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
    @Override
    public boolean isSupported(TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            return unit == YEARS || unit == DECADES || unit == CENTURIES || unit == MILLENNIA || unit == ERAS;
        }
        return unit != null && unit.isSupportedBy(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This year is used to enhance the accuracy of the returned range.
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
        if (field == YEAR_OF_ERA) {
            return (year <= 0 ? ValueRange.of(1, MAX_VALUE + 1) : ValueRange.of(1, MAX_VALUE));
        }
        return Temporal.super.range(field);
    }

    /**
     * Gets the value of the specified field from this year as an {@code int}.
     * <p>
     * This queries this year for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this year.
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
    @Override  // override for Javadoc
    public int get(TemporalField field) {
        return range(field).checkValidIntValue(getLong(field), field);
    }

    /**
     * Gets the value of the specified field from this year as a {@code long}.
     * <p>
     * This queries this year for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this year.
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
            switch ((ChronoField) field) {
                case YEAR_OF_ERA:
                    return (year < 1 ? 1 - year : year);
                case YEAR:
                    return year;
                case ERA:
                    return (year < 1 ? 0 : 1);
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.getFrom(this);
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
    public boolean isLeap() {
        return YearJalali.isLeap(year);
    }

    /**
     * Checks if the month-day is valid for this year.
     * <p>
     * This method checks whether this year and the input month and day form
     * a valid date.
     *
     * @param monthDay the month-day to validate, null returns false
     * @return true if the month and day are valid for this year
     */
    public boolean isValidMonthDay(MonthDayJalali monthDay) {
        return monthDay != null && monthDay.isValidYear(year);
    }

    /**
     * Gets the length of this year in days.
     *
     * @return the length of this year in days, 365 or 366
     */
    public int length() {
        return isLeap() ? 366 : 365;
    }

    //-----------------------------------------------------------------------

    /**
     * Returns an adjusted copy of this year.
     * <p>
     * This returns a {@code JalaliYear}, based on this one, with the year adjusted.
     * The adjustment takes place using the specified adjuster strategy object.
     * Read the documentation of the adjuster to understand what adjustment will be made.
     * <p>
     * The result of this method is obtained by invoking the
     * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
     * specified adjuster passing {@code this} as the argument.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param adjuster the adjuster to use, not null
     * @return a {@code JalaliYear} based on {@code this} with the adjustment made, not null
     * @throws DateTimeException   if the adjustment cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public YearJalali with(TemporalAdjuster adjuster) {
        return (YearJalali) adjuster.adjustInto(this);
    }

    /**
     * Returns a copy of this year with the specified field set to a new value.
     * <p>
     * This returns a {@code JalaliYear}, based on this one, with the value
     * for the specified field changed.
     * If it is not possible to set the value, because the field is not supported or for
     * some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the adjustment is implemented here.
     * The supported fields behave as follows:
     * <ul>
     * <li>{@code YEAR_OF_ERA} -
     * Returns a {@code JalaliYear} with the specified year-of-era
     * The era will be unchanged.
     * <li>{@code YEAR} -
     * Returns a {@code JalaliYear} with the specified year.
     * This completely replaces the date and is equivalent to {@link #of(int)}.
     * <li>{@code ERA} -
     * Returns a {@code JalaliYear} with the specified era.
     * The year-of-era will be unchanged.
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
     * @return a {@code JalaliYear} based on {@code this} with the specified field set, not null
     * @throws DateTimeException                if the field cannot be set
     * @throws UnsupportedTemporalTypeException if the field is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public YearJalali with(TemporalField field, long newValue) {
        if (field instanceof ChronoField f) {
            f.checkValidValue(newValue);
            switch (f) {
                case YEAR_OF_ERA:
                    return YearJalali.of((int) (year < 1 ? 1 - newValue : newValue));
                case YEAR:
                    return YearJalali.of((int) newValue);
                case ERA:
                    return (getLong(ERA) == newValue ? this : YearJalali.of(1 - year));
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.adjustInto(this, newValue);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this year with the specified amount added.
     * <p>
     * This returns a {@code JalaliYear}, based on this one, with the specified amount added.
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
     * @return a {@code JalaliYear} based on this year with the addition made, not null
     * @throws DateTimeException   if the addition cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public YearJalali plus(TemporalAmount amountToAdd) {
        return (YearJalali) amountToAdd.addTo(this);
    }

    /**
     * Returns a copy of this year with the specified amount added.
     * <p>
     * This returns a {@code JalaliYear}, based on this one, with the amount
     * in terms of the unit added. If it is not possible to add the amount, because the
     * unit is not supported or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoUnit} then the addition is implemented here.
     * The supported fields behave as follows:
     * <ul>
     * <li>{@code YEARS} -
     * Returns a {@code JalaliYear} with the specified number of years added.
     * This is equivalent to {@link #plusYears(long)}.
     * <li>{@code DECADES} -
     * Returns a {@code JalaliYear} with the specified number of decades added.
     * This is equivalent to calling {@link #plusYears(long)} with the amount
     * multiplied by 10.
     * <li>{@code CENTURIES} -
     * Returns a {@code JalaliYear} with the specified number of centuries added.
     * This is equivalent to calling {@link #plusYears(long)} with the amount
     * multiplied by 100.
     * <li>{@code MILLENNIA} -
     * Returns a {@code JalaliYear} with the specified number of millennia added.
     * This is equivalent to calling {@link #plusYears(long)} with the amount
     * multiplied by 1,000.
     * <li>{@code ERAS} -
     * Returns a {@code JalaliYear} with the specified number of eras added.
     * Only two eras are supported so the amount must be one, zero or minus one.
     * If the amount is non-zero then the year is changed such that the year-of-era
     * is unchanged.
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
     * @return a {@code JalaliYear} based on this year with the specified amount added, not null
     * @throws DateTimeException                if the addition cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public YearJalali plus(long amountToAdd, TemporalUnit unit) {
        if (unit instanceof ChronoUnit) {
            switch ((ChronoUnit) unit) {
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

    /**
     * Returns a copy of this {@code JalaliYear} with the specified number of years added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToAdd the years to add, may be negative
     * @return a {@code JalaliYear} based on this year with the years added, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    public YearJalali plusYears(long yearsToAdd) {
        if (yearsToAdd == 0) {
            return this;
        }
        return of(YEAR.checkValidIntValue(year + yearsToAdd));  // overflow safe
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this year with the specified amount subtracted.
     * <p>
     * This returns a {@code JalaliYear}, based on this one, with the specified amount subtracted.
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
     * @return a {@code JalaliYear} based on this year with the subtraction made, not null
     * @throws DateTimeException   if the subtraction cannot be made
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public YearJalali minus(TemporalAmount amountToSubtract) {
        return (YearJalali) amountToSubtract.subtractFrom(this);
    }

    /**
     * Returns a copy of this year with the specified amount subtracted.
     * <p>
     * This returns a {@code JalaliYear}, based on this one, with the amount
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
     * @return a {@code JalaliYear} based on this year with the specified amount subtracted, not null
     * @throws DateTimeException                if the subtraction cannot be made
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public YearJalali minus(long amountToSubtract, TemporalUnit unit) {
        return (amountToSubtract == Long.MIN_VALUE ? plus(Long.MAX_VALUE, unit).plus(1, unit) : plus(-amountToSubtract, unit));
    }

    /**
     * Returns a copy of this {@code JalaliYear} with the specified number of years subtracted.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param yearsToSubtract the years to subtract, may be negative
     * @return a {@code JalaliYear} based on this year with the year subtracted, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    public YearJalali minusYears(long yearsToSubtract) {
        return (yearsToSubtract == Long.MIN_VALUE ? plusYears(Long.MAX_VALUE).plusYears(1) : plusYears(-yearsToSubtract));
    }

    //-----------------------------------------------------------------------

    /**
     * Queries this year using the specified query.
     * <p>
     * This queries this year using the specified query strategy object.
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
        if (query == TemporalQueries.chronology()) {
            return (R) ChronologyJalali.INSTANCE;
        } else if (query == TemporalQueries.precision()) {
            return (R) YEARS;
        }
        return Temporal.super.query(query);
    }

    /**
     * Adjusts the specified temporal object to have this year.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the year changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * passing {@link ChronoField#YEAR} as the field.
     * If the specified temporal object does not use the ISO calendar system then
     * a {@code DateTimeException} is thrown.
     * <p>
     * In most cases, it is clearer to reverse the calling pattern by using
     * {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   // these two lines are equivalent, but the second approach is recommended
     *   temporal = thisYear.adjustInto(temporal);
     *   temporal = temporal.with(thisYear);
     * </pre>
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param temporal the target object to be adjusted, not null
     * @return the adjusted object, not null
     * @throws DateTimeException   if unable to make the adjustment
     * @throws ArithmeticException if numeric overflow occurs
     */
    @Override
    public Temporal adjustInto(Temporal temporal) {
        if (!Chronology.from(temporal).equals(ChronologyJalali.INSTANCE)) {
            throw new DateTimeException("Adjustment only supported on ISO date-time");
        }
        return temporal.with(YEAR, year);
    }

    /**
     * Calculates the amount of time until another year in terms of the specified unit.
     * <p>
     * This calculates the amount of time between two {@code JalaliYear}
     * objects in terms of a single {@code TemporalUnit}.
     * The start and end points are {@code this} and the specified year.
     * The result will be negative if the end is before the start.
     * The {@code Temporal} passed to this method is converted to a
     * {@code JalaliYear} using {@link #from(TemporalAccessor)}.
     * For example, the amount in decades between two year can be calculated
     * using {@code startYear.until(endYear, DECADES)}.
     * <p>
     * The calculation returns a whole number, representing the number of
     * complete units between the two years.
     * For example, the amount in decades between 2012 and 2031
     * will only be one decade as it is one year short of two decades.
     * <p>
     * There are two equivalent ways of using this method.
     * The first is to invoke this method.
     * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
     * <pre>
     *   // these two lines are equivalent
     *   amount = start.until(end, YEARS);
     *   amount = YEARS.between(start, end);
     * </pre>
     * The choice should be made based on which makes the code more readable.
     * <p>
     * The calculation is implemented in this method for {@link ChronoUnit}.
     * The units {@code YEARS}, {@code DECADES}, {@code CENTURIES},
     * {@code MILLENNIA} and {@code ERAS} are supported.
     * Other {@code ChronoUnit} values will throw an exception.
     * <p>
     * If the unit is not a {@code ChronoUnit}, then the result of this method
     * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
     * passing {@code this} as the first argument and the converted input temporal
     * as the second argument.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param endExclusive the end date, exclusive, which is converted to a {@code JalaliYear}, not null
     * @param unit         the unit to measure the amount in, not null
     * @return the amount of time between this year and the end year
     * @throws DateTimeException                if the amount cannot be calculated, or the end
     *                                          temporal cannot be converted to a {@code JalaliYear}
     * @throws UnsupportedTemporalTypeException if the unit is not supported
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public long until(Temporal endExclusive, TemporalUnit unit) {
        YearJalali end = YearJalali.from(endExclusive);
        if (unit instanceof ChronoUnit) {
            long yearsUntil = ((long) end.year) - year;  // no overflow
            switch ((ChronoUnit) unit) {
                case YEARS:
                    return yearsUntil;
                case DECADES:
                    return yearsUntil / 10;
                case CENTURIES:
                    return yearsUntil / 100;
                case MILLENNIA:
                    return yearsUntil / 1000;
                case ERAS:
                    return end.getLong(ERA) - getLong(ERA);
            }
            throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit);
        }
        return unit.between(this, end);
    }

    /**
     * Formats this year using the specified formatter.
     * <p>
     * This year will be passed to the formatter to produce a string.
     *
     * @param formatter the formatter to use, not null
     * @return the formatted year string, not null
     * @throws DateTimeException if an error occurs during printing
     */
    public String format(DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.format(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Combines this year with a day-of-year to create a {@code LocalDateJalali}.
     * <p>
     * This returns a {@code LocalDateJalali} formed from this year and the specified day-of-year.
     * <p>
     * The day-of-year value 366 is only valid in a leap year.
     *
     * @param dayOfYear the day-of-year to use, from 1 to 365-366
     * @return the local date formed from this year and the specified date of year, not null
     * @throws DateTimeException if the day of year is zero or less, 366 or greater or equal
     *                           to 366 and this is not a leap year
     */
    public LocalDateJalali atDay(int dayOfYear) {
        return LocalDateJalali.ofYearDay(year, dayOfYear);
    }

    /**
     * Combines this year with a month to create a {@code JalaliYearMonth}.
     * <p>
     * This returns a {@code JalaliYearMonth} formed from this year and the specified month.
     * All possible combinations of year and month are valid.
     * <p>
     * This method can be used as part of a chain to produce a date:
     * <pre>
     *  LocalDateJalali date = year.atMonth(month).atDay(day);
     * </pre>
     *
     * @param month the month-of-year to use, not null
     * @return the year-month formed from this year and the specified month, not null
     */
    public YearMonthJalali atMonth(MonthJalali month) {
        return YearMonthJalali.of(year, month);
    }

    /**
     * Combines this year with a month to create a {@code JalaliYearMonth}.
     * <p>
     * This returns a {@code JalaliYearMonth} formed from this year and the specified month.
     * All possible combinations of year and month are valid.
     * <p>
     * This method can be used as part of a chain to produce a date:
     * <pre>
     *  LocalDateJalali date = year.atMonth(month).atDay(day);
     * </pre>
     *
     * @param month the month-of-year to use, from 1 (January) to 12 (December)
     * @return the year-month formed from this year and the specified month, not null
     * @throws DateTimeException if the month is invalid
     */
    public YearMonthJalali atMonth(int month) {
        return YearMonthJalali.of(year, month);
    }

    /**
     * Combines this year with a month-day to create a {@code LocalDateJalali}.
     * <p>
     * This returns a {@code LocalDateJalali} formed from this year and the specified month-day.
     * <p>
     * A month-day of February 29th will be adjusted to February 28th in the resulting
     * date if the year is not a leap year.
     *
     * @param monthDay the month-day to use, not null
     * @return the local date formed from this year and the specified month-day, not null
     */
    public LocalDateJalali atMonthDay(MonthDayJalali monthDay) {
        return monthDay.atYear(year);
    }

    //-----------------------------------------------------------------------

    /**
     * Compares this year to another year.
     * <p>
     * The comparison is based on the value of the year.
     * It is "consistent with equals", as defined by {@link Comparable}.
     *
     * @param other the other year to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override
    public int compareTo(YearJalali other) {
        return year - other.year;
    }

    /**
     * Checks if this year is after the specified year.
     *
     * @param other the other year to compare to, not null
     * @return true if this is after the specified year
     */
    public boolean isAfter(YearJalali other) {
        return year > other.year;
    }

    /**
     * Checks if this year is before the specified year.
     *
     * @param other the other year to compare to, not null
     * @return true if this point is before the specified year
     */
    public boolean isBefore(YearJalali other) {
        return year < other.year;
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this year is equal to another year.
     * <p>
     * The comparison is based on the time-line position of the years.
     *
     * @param obj the object to check, null returns false
     * @return true if this is equal to the other year
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof YearJalali) {
            return year == ((YearJalali) obj).year;
        }
        return false;
    }

    /**
     * A hash code for this year.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return year;
    }

    //-----------------------------------------------------------------------

    /**
     * Outputs this year as a {@code String}.
     *
     * @return a string representation of this year, not null
     */
    @Override
    public String toString() {
        return Integer.toString(year);
    }
}
