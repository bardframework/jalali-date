package org.bardframework.time;

/*
 *
 * Copyright (c) 2016, Vahid Zafari
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.*;
import java.util.Locale;

import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoUnit.MONTHS;

/**
 * A month-of-year, such as 'Aban'.
 * <p>
 * {@code JalaliMonth} is an enum representing the 12 months of the year in Jalali Chronology -
 * Farvardin, Ordibehesht, Khordad, Tir, Mordad, Shahrivar, Mehr, Aban, Azar, Dey, Bahman and Esfand.
 * <p>
 * In addition to the textual enum name, each month-of-year has an {@code int} value.
 * The {@code int} value follows normal usage and the ISO-8601 standard,
 * from 1 (Farvardin) to 12 (Esfand). It is recommended that applications use the enum
 * rather than the {@code int} value to ensure code clarity.
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code JalaliMonth}.
 * Use {@code getValue()} instead.</b>
 * <p>
 * This enum represents a common concept that is found in many calendar systems.
 * As such, this enum may be used by any calendar system that has the month-of-year
 * concept defined exactly equivalent to the ISO-8601 calendar system.
 *
 * @author Vahid Zafari
 */
public enum MonthJalali implements TemporalAccessor, TemporalAdjuster {

    /**
     * The singleton instance for the month of Farvardin with 31 days.
     * This has the numeric value of {@code 1}.
     */
    FARVARDIN,
    /**
     * The singleton instance for the month of Ordibehesht with 31 days.
     * This has the numeric value of {@code 2}.
     */
    ORDIBEHESHT,
    /**
     * The singleton instance for the month of Khordad with 31 days.
     * This has the numeric value of {@code 3}.
     */
    KHORDAD,
    /**
     * The singleton instance for the month of Tir with 31 days.
     * This has the numeric value of {@code 4}.
     */
    TIR,
    /**
     * The singleton instance for the month of Mordad with 31 days.
     * This has the numeric value of {@code 5}.
     */
    MORDAD,
    /**
     * The singleton instance for the month of Shahrivar with 31 days.
     * This has the numeric value of {@code 6}.
     */
    SHAHRIVAR,
    /**
     * The singleton instance for the month of Mehr with 30 days.
     * This has the numeric value of {@code 7}.
     */
    MEHR,
    /**
     * The singleton instance for the month of Aban with 30 days.
     * This has the numeric value of {@code 8}.
     */
    ABAN,
    /**
     * The singleton instance for the month of Azar with 30 days.
     * This has the numeric value of {@code 9}.
     */
    AZAR,
    /**
     * The singleton instance for the month of Dey with 30 days.
     * This has the numeric value of {@code 10}.
     */
    DEY,
    /**
     * The singleton instance for the month of Bahman with 30 days.
     * This has the numeric value of {@code 11}.
     */
    BAHMAN,
    /**
     * The singleton instance for the month of Esfand with 29 days, or 30 in a leap year.
     * This has the numeric value of {@code 12}.
     */
    ESFAND;
    /**
     * Private cache of all the constants.
     */
    private static final MonthJalali[] ENUMS = MonthJalali.values();

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliMonth} from an {@code int} value.
     * <p>
     * {@code JalaliMonth} is an enum representing the 12 months of the year.
     * This factory allows the enum to be obtained from the {@code int} value.
     * The {@code int} value follows the ISO-8601 standard, from 1 (Farvardin) to 12 (Esfand).
     *
     * @param month the month-of-year to represent, from 1 (Farvardin) to 12 (Esfand)
     * @return the month-of-year, not null
     * @throws DateTimeException if the month-of-year is invalid
     */
    public static MonthJalali of(int month) {
        if (month < 1 || month > 12) {
            throw new DateTimeException("Invalid value for MonthOfYear: " + month);
        }
        return ENUMS[month - 1];
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an instance of {@code JalaliMonth} from a temporal object.
     * <p>
     * This obtains a month based on the specified temporal.
     * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
     * which this factory converts to an instance of {@code JalaliMonth}.
     * <p>
     * The conversion extracts the {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} field.
     * The extraction is only permitted if the temporal object has an ISO
     * chronology, or can be converted to a {@code LocalDateJalali}.
     * <p>
     * This method matches the signature of the functional interface {@link TemporalQuery}
     * allowing it to be used as a query via method reference, {@code JalaliMonth::from}.
     *
     * @param temporal the temporal object to toModel, not null
     * @return the month-of-year, not null
     * @throws DateTimeException if unable to toModel to a {@code JalaliMonth}
     */
    public static MonthJalali from(TemporalAccessor temporal) {
        if (temporal instanceof MonthJalali) {
            return (MonthJalali) temporal;
        }
        try {
            if (!ChronologyJalali.INSTANCE.equals(Chronology.from(temporal))) {
                temporal = LocalDate.from(temporal);
            }
            return of(temporal.get(MONTH_OF_YEAR));
        } catch (DateTimeException ex) {
            throw new DateTimeException("Unable to obtain JalaliMonth from TemporalAccessor: " + temporal + " of type " + temporal.getClass().getName(), ex);
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the month-of-year {@code int} value.
     * <p>
     * The values are numbered following the ISO-8601 standard,
     * from 1 (Farvardin) to 12 (Esfand).
     *
     * @return the month-of-year, from 1 (Farvardin) to 12 (Esfand)
     */
    public int getValue() {
        return ordinal() + 1;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the textual representation, such as 'Jan' or 'Esfand'.
     * <p>
     * This returns the textual name used to identify the month-of-year,
     * suitable for web to the user.
     * The parameters control the style of the returned text and the locale.
     * <p>
     * If no textual mapping is found then the {@link #getValue() numeric value} is returned.
     *
     * @param style  the length of the text required, not null
     * @param locale the locale to use, not null
     * @return the text value of the month-of-year, not null
     */
    public String getDisplayName(TextStyle style, Locale locale) {
        return new DateTimeFormatterBuilder().appendText(MONTH_OF_YEAR, style).toFormatter(locale).format(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the specified field is supported.
     * <p>
     * This checks if this month-of-year can be queried for the specified field.
     * If false, then calling the {@link #range(TemporalField) range} and
     * {@link #get(TemporalField) get} methods will throw an exception.
     * <p>
     * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then
     * this method returns true.
     * All other {@code ChronoField} instances will return false.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
     * passing {@code this} as the argument.
     * Whether the field is supported is determined by the field.
     *
     * @param field the field to check, null returns false
     * @return true if the field is supported on this month-of-year, false if not
     */
    @Override
    public boolean isSupported(TemporalField field) {
        if (field instanceof ChronoField) {
            return field == MONTH_OF_YEAR;
        }
        return field != null && field.isSupportedBy(this);
    }

    /**
     * Gets the range of valid values for the specified field.
     * <p>
     * The range object expresses the minimum and maximum valid values for a field.
     * This month is used to enhance the accuracy of the returned range.
     * If it is not possible to return the range, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then the
     * range of the month-of-year, from 1 to 12, will be returned.
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
        }
        return TemporalAccessor.super.range(field);
    }

    /**
     * Gets the value of the specified field from this month-of-year as an {@code int}.
     * <p>
     * This queries this month for the value of the specified field.
     * The returned value will always be within the valid range of values for the field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then the
     * value of the month-of-year, from 1 to 12, will be returned.
     * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
     * <p>
     * If the field is not a {@code ChronoField}, then the result of this method
     * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
     * passing {@code this} as the argument. Whether the value can be obtained,
     * and what the value represents, is determined by the field.
     *
     * @param field the field to get, not null
     * @return the value for the field, within the valid range of values
     * @throws DateTimeException                if a value for the field cannot be obtained or
     *                                          the value is outside the range of valid values for the field
     * @throws UnsupportedTemporalTypeException if the field is not supported or
     *                                          the range of values exceeds an {@code int}
     * @throws ArithmeticException              if numeric overflow occurs
     */
    @Override
    public int get(TemporalField field) {
        if (field == MONTH_OF_YEAR) {
            return getValue();
        }
        return TemporalAccessor.super.get(field);
    }

    /**
     * Gets the value of the specified field from this month-of-year as a {@code long}.
     * <p>
     * This queries this month for the value of the specified field.
     * If it is not possible to return the value, because the field is not supported
     * or for some other reason, an exception is thrown.
     * <p>
     * If the field is {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} then the
     * value of the month-of-year, from 1 to 12, will be returned.
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
        if (field == MONTH_OF_YEAR) {
            return getValue();
        } else if (field instanceof ChronoField) {
            throw new UnsupportedTemporalTypeException("Unsupported field: " + field);
        }
        return field.getFrom(this);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns the month-of-year that is the specified number of quarters after this one.
     * <p>
     * The calculation rolls around the end of the year from Farvardin to Esfand.
     * The specified period may be negative.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months the months to add, positive or negative
     * @return the resulting month, not null
     */
    public MonthJalali plus(long months) {
        int amount = (int) (months % 12);
        return ENUMS[(ordinal() + (amount + 12)) % 12];
    }

    /**
     * Returns the month-of-year that is the specified number of months before this one.
     * <p>
     * The calculation rolls around the start of the year from Farvardin to Esfand.
     * The specified period may be negative.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param months the months to subtract, positive or negative
     * @return the resulting month, not null
     */
    public MonthJalali minus(long months) {
        return plus(-(months % 12));
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the length of this month in days.
     * <p>
     * This takes a flag to determine whether to return the length for a leap year or not.
     * <p>
     * Esfand has 29 days in a standard year and 30 days in a leap year.
     * Farvardin, Ordibehesht, Khordad, Tir, Mordad and Shahrivar have 31 days.
     * All other months have 30 days.
     *
     * @param leapYear true if the length is required for a leap year
     * @return the length of this month in days, from 29 to 31
     */
    public int length(boolean leapYear) {
        switch (this) {
            case FARVARDIN:
            case ORDIBEHESHT:
            case KHORDAD:
            case TIR:
            case MORDAD:
            case SHAHRIVAR:
                return 31;
            case MEHR:
            case ABAN:
            case AZAR:
            case DEY:
            case BAHMAN:
                return 30;
            case ESFAND:
            default:
                return leapYear ? 30 : 29;
        }
    }

    /**
     * Gets the minimum length of this month in days.
     * <p>
     * Esfand has a minimum length of 29 days.
     * Farvardin, Ordibehesht, Khordad, Tir, Mordad and Shahrivar have 31 days.
     * All other months have 30 days.
     *
     * @return the minimum length of this month in days, from 29 to 31
     */
    public int minLength() {
        switch (this) {
            case FARVARDIN:
            case ORDIBEHESHT:
            case KHORDAD:
            case TIR:
            case MORDAD:
            case SHAHRIVAR:
                return 31;
            case MEHR:
            case ABAN:
            case AZAR:
            case DEY:
            case BAHMAN:
                return 30;
            case ESFAND:
            default:
                return 29;
        }
    }

    /**
     * Gets the maximum length of this month in days.
     * <p>
     * Esfand has a maximum length of 30 days.
     * Farvardin, Ordibehesht, Khordad, Tir, Mordad and Shahrivar have 31 days.
     * All other months have 30 days.
     *
     * @return the maximum length of this month in days, from 29 to 31
     */
    public int maxLength() {
        switch (this) {
            case FARVARDIN:
            case ORDIBEHESHT:
            case KHORDAD:
            case TIR:
            case MORDAD:
            case SHAHRIVAR:
                return 31;
            case MEHR:
            case ABAN:
            case AZAR:
            case DEY:
            case BAHMAN:
            case ESFAND:
            default:
                return 30;
        }
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the day-of-year corresponding to the first day of this month.
     * <p>
     * This returns the day-of-year that this month begins on, using the leap
     * year flag to determine the length of Esfand.
     *
     * @param leapYear true if the length is required for a leap year
     * @return the day of year corresponding to the first day of this month, from 1 to 336
     */
    public int firstDayOfYear(boolean leapYear) {
        int leap = leapYear ? 1 : 0;
        switch (this) {
            case FARVARDIN:
                return 1;
            case ORDIBEHESHT:
                return 32;
            case KHORDAD:
                return 63;
            case TIR:
                return 94;
            case MORDAD:
                return 125;
            case SHAHRIVAR:
                return 156;
            case MEHR:
                return 187;
            case ABAN:
                return 217;
            case AZAR:
                return 247;
            case DEY:
                return 277;
            case BAHMAN:
                return 307;
            case ESFAND:
            default:
                return 337;
        }
    }

    /**
     * Gets the month corresponding to the first month of this quarter.
     * <p>
     * The year can be divided into four quarters.
     * This method returns the first month of the quarter for the base month.
     * Farvardin, Ordibehesht and Khordad return Farvardin.
     * Tir, Mordad and Shahrivar return Tir.
     * Mehr, Aban and Azar return Mehr.
     * Dey, Bahman and Esfand return Dey.
     *
     * @return the first month of the quarter corresponding to this month, not null
     */
    public MonthJalali firstMonthOfQuarter() {
        return ENUMS[(ordinal() / 3) * 3];
    }

    //-----------------------------------------------------------------------

    /**
     * Queries this month-of-year using the specified query.
     * <p>
     * This queries this month-of-year using the specified query strategy object.
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
            return (R) MONTHS;
        }
        return TemporalAccessor.super.query(query);
    }

    /**
     * Adjusts the specified temporal object to have this month-of-year.
     * <p>
     * This returns a temporal object of the same observable type as the input
     * with the month-of-year changed to be the same as this.
     * <p>
     * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
     * passing {@link ChronoField#MONTH_OF_YEAR} as the field.
     * If the specified temporal object does not use the ISO calendar system then
     * a {@code DateTimeException} is thrown.
     * <p>
     * In most cases, it is clearer to reverse the calling pattern by using
     * {@link Temporal#with(TemporalAdjuster)}:
     * <pre>
     *   these two lines are equivalent, but the second approach is recommended
     *   temporal = thisMonth.adjustInto(temporal);
     *   temporal = temporal.with(thisMonth);
     * </pre>
     * <p>
     * For example, given a date in Mordad, the following are output:
     * <pre>
     *   dateInMordad.with(FARVARDIN);     // four months earlier
     *   dateInMordad.with(TIR);           // one months earlier
     *   dateInMordad.with(MORDAD);        // same date
     *   dateInMordad.with(SHAHRIVAR);     // one month later
     *   dateInMordad.with(ESFAND);        // seven months later
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
        return temporal.with(MONTH_OF_YEAR, getValue());
    }

}
