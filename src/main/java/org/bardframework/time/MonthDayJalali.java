package org.bardframework.time;


import java.io.Serializable;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.*;
import java.util.Objects;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;

/**
 * A month-day in the ISO-8601 calendar system, such as {@code --12-03}.
 * <p>
 * {@code JalaliMonthDay} is an immutable date-time object that represents the combination
 * of a month and day-of-month. Any field that can be derived from a month and day,
 * such as quarter-of-year, can be obtained.
 * <p>
 * This class does not store or represent a year, time or time-zone.
 * For example, the value "December 3rd" can be stored in a {@code JalaliMonthDay}.
 * <p>
 * Since a {@code JalaliMonthDay} does not possess a year, the leap day of
 * February 29th is considered valid.
 * <p>
 * This class implements {@link TemporalAccessor} rather than {@link Temporal}.
 * This is because it is not possible to define whether February 29th is valid or not
 * without external information, preventing the implementation of plus/minus.
 * Related to this, {@code JalaliMonthDay} only provides access to query and set the fields
 * {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH}.
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
 * {@code JalaliMonthDay} may have unpredictable results and should be avoided.
 * The {@code equals} method should be used for comparisons.
 *
 * @author Vahid Zafari
 * This class is immutable and thread-safe.
 */
public final class MonthDayJalali implements TemporalAccessor, TemporalAdjuster, Comparable<MonthDayJalali>, Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -939150713474957432L;
    /**
     * Parser.
     */
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
        .appendLiteral("--")
        .appendValue(MONTH_OF_YEAR, 2)
        .appendLiteral('-')
        .appendValue(DAY_OF_MONTH, 2)
        .toFormatter();

    /**
     * The month-of-year, not null.
     */
    private final int month;
    /**
     * The day-of-month.
     */
    private final int day;

    //-----------------------------------------------------------------------

    /**
     * Constructor, previously validated.
     *
     * @param month      the month-of-year to represent, validated from 1 to 12
     * @param dayOfMonth the day-of-month to represent, validated from 1 to 29-31
     */
    private MonthDayJalali(int month, int dayOfMonth) {
        this.month = month;
        this.day = dayOfMonth;
    }

    /**
     * Obtains the current month-day from the system clock in the default time-zone.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current month-day.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current month-day using the system clock and default time-zone, not null
     */
    public static MonthDayJalali now() {
        return now(Clock.systemDefaultZone());
    }

    /**
     * Obtains the current month-day from the system clock in the specified time-zone.
     * <p>
     * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current month-day.
     * Specifying the time-zone avoids dependence on the default time-zone.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @param zone the zone ID to use, not null
     * @return the current month-day using the system clock, not null
     */
    public static MonthDayJalali now(ZoneId zone) {
        return now(Clock.system(zone));
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains the current month-day from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current month-day.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@link Clock dependency injection}.
     *
     * @param clock the clock to use, not null
     * @return the current month-day, not null
     */
    public static MonthDayJalali now(Clock clock) {
        final LocalDateJalali now = LocalDateJalali.now(clock);  // called once
        return of(now.getMonth(), now.getDayOfMonth());
    }

    /**
     * Obtains an instance of {@code JalaliMonthDay}.
     * <p>
     * The day-of-month must be valid for the month within a leap year.
     * Hence, for February, day 29 is valid.
     * <p>
     * For example, passing in April and day 31 will throw an exception, as
     * there can never be April 31st in any year. By contrast, passing in
     * February 29th is permitted, as that month-day can sometimes be valid.
     *
     * @param month      the month-of-year to represent, not null
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return the month-day, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month
     */
    public static MonthDayJalali of(MonthJalali month, int dayOfMonth) {
        Objects.requireNonNull(month, "month");
        DAY_OF_MONTH.checkValidValue(dayOfMonth);
        if (dayOfMonth > month.maxLength()) {
            throw new DateTimeException("Illegal value for DayOfMonth field, value " + dayOfMonth +
                " is not valid for month " + month.name());
        }
        return new MonthDayJalali(month.getValue(), dayOfMonth);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliMonthDay}.
     * <p>
     * The day-of-month must be valid for the month within a leap year.
     * Hence, for month 2 (February), day 29 is valid.
     * <p>
     * For example, passing in month 4 (April) and day 31 will throw an exception, as
     * there can never be April 31st in any year. By contrast, passing in
     * February 29th is permitted, as that month-day can sometimes be valid.
     *
     * @param month      the month-of-year to represent, from 1 (January) to 12 (December)
     * @param dayOfMonth the day-of-month to represent, from 1 to 31
     * @return the month-day, not null
     * @throws DateTimeException if the value of any field is out of range,
     *                           or if the day-of-month is invalid for the month
     */
    public static MonthDayJalali of(int month, int dayOfMonth) {
        return of(MonthJalali.of(month), dayOfMonth);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliMonthDay} from a temporal object.
     * <p>
     * This obtains a month-day based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code JalaliMonthDay}.
     * <p>
     * The conversion extracts the {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} and
     * {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} fields.
     * The extraction is only permitted if the temporal object has an ISO
     * chronology, or can be converted to a {@code LocalDateJalali}.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code JalaliMonthDay::from}.
     *
     * @param temporal the temporal object to toModel, not null
     * @return the month-day, not null
     * @throws DateTimeException if unable to toModel to a {@code JalaliMonthDay}
     */
    public static MonthDayJalali from(TemporalAccessor temporal) {
        if (temporal instanceof MonthDayJalali) {
            return (MonthDayJalali) temporal;
        }
        try {
            if (!ChronologyJalali.INSTANCE.equals(Chronology.from(temporal))) {
                temporal = LocalDateJalali.from(temporal);
            }
            return of(temporal.get(MONTH_OF_YEAR), temporal.get(DAY_OF_MONTH));
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain JalaliMonthDay from TemporalAccessor: " +
                temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    /**
     * Obtains an instance of {@code JalaliMonthDay} from a text string such as {@code --12-03}.
     * <p>
     * The string must represent a valid month-day.
     * The format is {@code --MM-dd}.
     *
     * @param text the text to parse such as "--12-03", not null
     * @return the parsed month-day, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static MonthDayJalali parse(CharSequence text) {
        return parse(text, PARSER);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliMonthDay} from a text string using a specific formatter.
     * <p>
     * The text is parsed using the formatter, returning a month-day.
     *
     * @param text      the text to parse, not null
     * @param formatter the formatter to use, not null
     * @return the parsed month-day, not null
     * @throws DateTimeParseException if the text cannot be parsed
     */
    public static MonthDayJalali parse(CharSequence text, DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.parse(text, MonthDayJalali::from);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this month-day can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range} and
     * {@link #get(TemporalField) get} methods will throw an exception.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The supported fields are:
     * <ul>
     * <li>{@code MONTH_OF_YEAR}
     * <li>{@code YEAR}
     * </ul>
     * All other {@code ChronoField} instances will return false.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the field is supported is determined by the field.
     *
     * @param field the field to check, null returns false
     * @return true if the field is supported on this month-day, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == MONTH_OF_YEAR || field == DAY_OF_MONTH;
        }
        return field != null && field.isSupportedBy(this);
    }

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This month-day is used to enhance the accuracy of the returned range.
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
        if (field == MONTH_OF_YEAR) {
            return field.range();
        } else if (field == DAY_OF_MONTH) {
            return ValueRange.of(1, getMonth().minLength(), getMonth().maxLength());
        }
        return TemporalAccessor.super.range(field);
    }

    /**
     * Gets the value of the specified field from this month-day as an {@code int}.
     * <p>
     * This queries this month-day for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this month-day.
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
     * Gets the value of the specified field from this month-day as a {@code long}.
     * <p>
     * This queries this month-day for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is a {@link ChronoField} then the query is implemented here.
     * The {@link #isSupported(TemporalField) supported fields} will return valid
     * values based on this month-day.
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
                // alignedDOW and alignedWOM not supported because they cannot be set in with()
                case DAY_OF_MONTH:
                    return day;
                case MONTH_OF_YEAR:
                    return month;
            }
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.getFrom(this);
    }

    //-----------------------------------------------------------------------

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

    //-----------------------------------------------------------------------

    /**
     * Checks if the year is valid for this month-day.
     * <p>
     * This method checks whether this month and day and the input year form
     * a valid date. This can only return false for February 29th.
     *
     * @param year the year to validate
     * @return true if the year is valid for this month-day
     * @see YearJalali#isValidMonthDay(MonthDayJalali)
     */
    public boolean isValidYear(int year) {
        return !(day == 29 && month == 2 && !YearJalali.isLeap(year));
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a copy of this {@code JalaliMonthDay} with the month-of-year altered.
     * <p>
     * This returns a month-day with the specified month.
     * If the day-of-month is invalid for the specified month, the day will
     * be adjusted to the last valid day-of-month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param month the month-of-year to set in the returned month-day, from 1 (January) to 12 (December)
     * @return a {@code JalaliMonthDay} based on this month-day with the requested month, not null
     * @throws DateTimeException if the month-of-year value is invalid
     */
    public MonthDayJalali withMonth(int month) {
        return with(MonthJalali.of(month));
    }

    /**
     * Returns a copy of this {@code JalaliMonthDay} with the month-of-year altered.
     * <p>
     * This returns a month-day with the specified month.
     * If the day-of-month is invalid for the specified month, the day will
     * be adjusted to the last valid day-of-month.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param month the month-of-year to set in the returned month-day, not null
     * @return a {@code JalaliMonthDay} based on this month-day with the requested month, not null
     */
    public MonthDayJalali with(MonthJalali month) {
        Objects.requireNonNull(month, "month");
        if (month.getValue() == this.month) {
            return this;
        }
        int day = Math.min(this.day, month.maxLength());
        return new MonthDayJalali(month.getValue(), day);
    }

    /**
     * Returns a copy of this {@code JalaliMonthDay} with the day-of-month altered.
     * <p>
     * This returns a month-day with the specified day-of-month.
     * If the day-of-month is invalid for the month, an exception is thrown.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param dayOfMonth the day-of-month to set in the return month-day, from 1 to 31
     * @return a {@code JalaliMonthDay} based on this month-day with the requested day, not null
     * @throws DateTimeException if the day-of-month value is invalid,
     *                           or if the day-of-month is invalid for the month
     */
    public MonthDayJalali withDayOfMonth(int dayOfMonth) {
        if (dayOfMonth == this.day) {
            return this;
        }
        return of(month, dayOfMonth);
    }

    //-----------------------------------------------------------------------

    /**
     * Queries this month-day using the specified query.
     * <p>
     * This queries this month-day using the specified query strategy object.
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
        }
        return TemporalAccessor.super.query(query);
    }

    /**
     * Adjusts the specified temporal object to have this month-day.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the month and day-of-month changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * twice, passing {@link ChronoField#MONTH_OF_YEAR} and
     * {@link ChronoField#DAY_OF_MONTH} as the fields.
     * If the specified temporal object does not use the ISO calendar system then
     * a {@code DateTimeException} is thrown.
     * <p>
     * In most cases, it is clearer to reverse the calling pattern by using
     * {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   // these two lines are equivalent, but the second approach is recommended
     *   temporal = thisMonthDay.adjustInto(temporal);
     *   temporal = temporal.with(thisMonthDay);
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
        temporal = temporal.with(MONTH_OF_YEAR, month);
        return temporal.with(DAY_OF_MONTH, Math.min(temporal.range(DAY_OF_MONTH).getMaximum(), day));
    }

    /**
     * Formats this month-day using the specified formatter.
     * <p>
     * This month-day will be passed to the formatter to produce a string.
     *
     * @param formatter the formatter to use, not null
     * @return the formatted month-day string, not null
     * @throws DateTimeException if an error occurs during printing
     */
    public String format(DateTimeFormatter formatter) {
        Objects.requireNonNull(formatter, "formatter");
        return formatter.format(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Combines this month-day with a year to create a {@code LocalDateJalali}.
     * <p>
     * This returns a {@code LocalDateJalali} formed from this month-day and the specified year.
     * <p>
     * A month-day of February 29th will be adjusted to February 28th in the resulting
     * date if the year is not a leap year.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param year the year to use, from MIN_YEAR to MAX_YEAR
     * @return the local date formed from this month-day and the specified year, not null
     * @throws DateTimeException if the year is outside the valid range of years
     */
    public LocalDateJalali atYear(int year) {
        return LocalDateJalali.of(year, month, isValidYear(year) ? day : 28);
    }

    //-----------------------------------------------------------------------

    /**
     * Compares this month-day to another month-day.
     * <p>
     * The comparison is based first on value of the month, then on the value of the day.
     * It is "consistent with equals", as defined by {@link Comparable}.
     *
     * @param other the other month-day to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override
    public int compareTo(MonthDayJalali other) {
        int cmp = (month - other.month);
        if (cmp == 0) {
            cmp = (day - other.day);
        }
        return cmp;
    }

    /**
     * Checks if this month-day is after the specified month-day.
     *
     * @param other the other month-day to compare to, not null
     * @return true if this is after the specified month-day
     */
    public boolean isAfter(MonthDayJalali other) {
        return compareTo(other) > 0;
    }

    /**
     * Checks if this month-day is before the specified month-day.
     *
     * @param other the other month-day to compare to, not null
     * @return true if this point is before the specified month-day
     */
    public boolean isBefore(MonthDayJalali other) {
        return compareTo(other) < 0;
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this month-day is equal to another month-day.
     * <p>
     * The comparison is based on the time-line position of the month-day within a year.
     *
     * @param obj the object to check, null returns false
     * @return true if this is equal to the other month-day
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MonthDayJalali) {
            MonthDayJalali other = (MonthDayJalali) obj;
            return month == other.month && day == other.day;
        }
        return false;
    }

    /**
     * A hash code for this month-day.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return (month << 6) + day;
    }

    //-----------------------------------------------------------------------

    /**
     * Outputs this month-day as a {@code String}, such as {@code --12-03}.
     * <p>
     * The output will be in the format {@code --MM-dd}:
     *
     * @return a string representation of this month-day, not null
     */
    @Override
    public String toString() {
        return "--" +
            (month < 10 ? "0" : "") + month +
            (day < 10 ? "-0" : "-") + day;
    }
}
