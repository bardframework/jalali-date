package org.bardframework.time;

import java.io.Serializable;
import java.time.*;
import java.time.chrono.AbstractChronology;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * The JALALI calendar system.
 * <p>
 * This chronology defines the rules of the JALALI calendar system.
 * This calendar system is based on the JALALI-8601 standard, which is the
 * <i>de facto</i> world calendar.
 * <p>
 * The fields are defined as follows:
 * <ul>
 * <li>era - There are two eras, 'Current Era' (CE) and 'Before Current Era' (BCE).
 * <li>year-of-era - The year-of-era is the same as the proleptic-year for the current CE era.
 * For the BCE era before the JALALI epoch the year increases from 1 upwards as time goes backwards.
 * <li>proleptic-year - The proleptic year is the same as the year-of-era for the
 * current era. For the previous era, years have zero, then negative values.
 * <li>month-of-year - There are 12 months in an JALALI year, numbered from 1 to 12.
 * <li>day-of-month - There are between 28 and 31 days in each of the JALALI month, numbered from 1 to 31.
 * Months 4, 6, 9 and 11 have 30 days, Months 1, 3, 5, 7, 8, 10 and 12 have 31 days.
 * JalaliMonth 2 has 28 days, or 29 in a leap year.
 * <li>day-of-year - There are 365 days in a standard JALALI year and 366 in a leap year.
 * The days are numbered from 1 to 365 or 1 to 366.
 * <li>leap-year - Leap years occur every 4 years, except where the year is divisble by 100 and not divisble by 400.
 * </ul>
 *
 * @author Vahid Zafari
 */
public final class ChronologyJalali extends AbstractChronology implements Serializable {

    /**
     * Singleton instance of the JALALI chronology.
     */
    public static final ChronologyJalali INSTANCE = new ChronologyJalali();

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -1440403870442975015L;

    /**
     * Restricted constructor.
     */
    private ChronologyJalali() {
    }

    /**
     * Gets the ID of the chronology - 'JALALI'.
     * <p>
     * The ID uniquely identifies the {@code Chronology}.
     * It can be used to lookup the {@code Chronology} using {@link Chronology#of(String)}.
     *
     * @return the chronology ID - 'JALALI'
     * @see #getCalendarType()
     */
    @Override
    public String getId() {
        return "JALALI";
    }

    /**
     * Gets the calendar type of the underlying calendar system - 'iso8601'.
     * <p>
     * The calendar type is an identifier defined by the
     * <em>Unicode Locale Data Markup Language (LDML)</em> specification.
     * It can be used to lookup the {@code Chronology} using {@link Chronology#of(String)}.
     * It can also be used as part of a locale, accessible via
     * {@link Locale#getUnicodeLocaleType(String)} with the key 'ca'.
     *
     * @return the calendar system type - 'jalali'
     * @see #getId()
     */
    @Override
    public String getCalendarType() {
        return "jalali";
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an JALALI local date from the era, year-of-era, month-of-year
     * and day-of-month fields.
     *
     * @param era        the JALALI era, not null
     * @param yearOfEra  the JALALI year-of-era
     * @param month      the JALALI month-of-year
     * @param dayOfMonth the JALALI day-of-month
     * @return the JALALI local date, not null
     * @throws DateTimeException  if unable to create the date
     * @throws ClassCastException if the type of {@code era} is not {@code IsoEra}
     */
    @Override  // override with covariant return type
    public LocalDateJalali date(Era era, int yearOfEra, int month, int dayOfMonth)
            throws DateTimeException {
        return date(prolepticYear(era, yearOfEra), month, dayOfMonth);
    }

    /**
     * Obtains an JALALI local date from the proleptic-year, month-of-year
     * and day-of-month fields.
     * <p>
     * This is equivalent to {@link LocalDateJalali#of(int, int, int)}.
     *
     * @param prolepticYear the JALALI proleptic-year
     * @param month         the JALALI month-of-year
     * @param dayOfMonth    the JALALI day-of-month
     * @return the JALALI local date, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali date(int prolepticYear, int month, int dayOfMonth) {
        return LocalDateJalali.of(prolepticYear, month, dayOfMonth);
    }

    /**
     * Obtains an JALALI local date from the era, year-of-era and day-of-year fields.
     *
     * @param era       the JALALI era, not null
     * @param yearOfEra the JALALI year-of-era
     * @param dayOfYear the JALALI day-of-year
     * @return the JALALI local date, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali dateYearDay(Era era, int yearOfEra, int dayOfYear) {
        return dateYearDay(prolepticYear(era, yearOfEra), dayOfYear);
    }

    /**
     * Obtains an JALALI local date from the proleptic-year and day-of-year fields.
     * <p>
     * This is equivalent to {@link LocalDateJalali#ofYearDay(int, int)}.
     *
     * @param prolepticYear the JALALI proleptic-year
     * @param dayOfYear     the JALALI day-of-year
     * @return the JALALI local date, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali dateYearDay(int prolepticYear, int dayOfYear) {
        return LocalDateJalali.ofYearDay(prolepticYear, dayOfYear);
    }

    /**
     * Obtains an JALALI local date from the epoch-day.
     * <p>
     * This is equivalent to {@link LocalDateJalali#ofEpochDay(long)}.
     *
     * @param epochDay the epoch day
     * @return the JALALI local date, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali dateEpochDay(long epochDay) {
        return LocalDateJalali.ofEpochDay(epochDay);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains an JALALI local date from another date-time object.
     * <p>
     * This is equivalent to {@link LocalDateJalali#from(TemporalAccessor)}.
     *
     * @param temporal the date-time object to toModel, not null
     * @return the JALALI local date, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali date(TemporalAccessor temporal) {
        return LocalDateJalali.from(temporal);
    }

    /**
     * Obtains an JALALI local date-time from another date-time object.
     * <p>
     * This is equivalent to {@link LocalDateTimeJalali#from(TemporalAccessor)}.
     *
     * @param temporal the date-time object to toModel, not null
     * @return the JALALI local date-time, not null
     * @throws DateTimeException if unable to create the date-time
     */
    @Override  // override with covariant return type
    public LocalDateTimeJalali localDateTime(TemporalAccessor temporal) {
        return LocalDateTimeJalali.from(temporal);
    }

    /**
     * Obtains an JALALI zoned date-time from another date-time object.
     * <p>
     * This is equivalent to {@link ZonedDateTimeJalali#from(TemporalAccessor)}.
     *
     * @param temporal the date-time object to toModel, not null
     * @return the JALALI zoned date-time, not null
     * @throws DateTimeException if unable to create the date-time
     */
    @Override  // override with covariant return type
    public ZonedDateTimeJalali zonedDateTime(TemporalAccessor temporal)
            throws DateTimeException {
        return ZonedDateTimeJalali.from(temporal);
    }

    /**
     * Obtains an JALALI zoned date-time in this chronology from an {@code Instant}.
     * <p>
     * This is equivalent to {@link ZonedDateTimeJalali#ofInstant(Instant, ZoneId)}.
     *
     * @param instant the instant to create the date-time from, not null
     * @param zone    the time-zone, not null
     * @return the zoned date-time, not null
     * @throws DateTimeException if the result exceeds the supported range
     */
    @Override
    public ZonedDateTimeJalali zonedDateTime(Instant instant, ZoneId zone) {
        return ZonedDateTimeJalali.ofInstant(instant, zone);
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains the current JALALI local date from the system clock in the default time-zone.
     * <p>
     * This will query the {@link Clock#systemDefaultZone() system clock} in the default
     * time-zone to obtain the current date.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current JALALI local date using the system clock and default time-zone, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali dateNow() {
        return dateNow(Clock.systemDefaultZone());
    }

    /**
     * Obtains the current JALALI local date from the system clock in the specified time-zone.
     * <p>
     * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
     * Specifying the time-zone avoids dependence on the default time-zone.
     * <p>
     * Using this method will prevent the ability to use an alternate clock for testing
     * because the clock is hard-coded.
     *
     * @return the current JALALI local date using the system clock, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali dateNow(ZoneId zone) {
        return dateNow(Clock.system(zone));
    }

    /**
     * Obtains the current JALALI local date from the specified clock.
     * <p>
     * This will query the specified clock to obtain the current date - today.
     * Using this method allows the use of an alternate clock for testing.
     * The alternate clock may be introduced using {@link Clock dependency injection}.
     *
     * @param clock the clock to use, not null
     * @return the current JALALI local date, not null
     * @throws DateTimeException if unable to create the date
     */
    @Override  // override with covariant return type
    public LocalDateJalali dateNow(Clock clock) {
        Objects.requireNonNull(clock, "clock");
        return date(LocalDateJalali.now(clock));
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if the year is a leap year, according to the JALALI proleptic
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
     * This is historically inaccurate, but is correct for the JALALI-8601 standard.
     *
     * @param prolepticYear the JALALI proleptic year to check
     * @return true if the year is leap, false otherwise
     */
    @Override
    public boolean isLeapYear(long prolepticYear) {
        return YearJalali.isLeap(prolepticYear);
    }

    @Override
    public int prolepticYear(Era era, int yearOfEra) {
        if (!(era instanceof IsoEra)) {
            throw new ClassCastException("Era must be IsoEra");
        }
        return (era == IsoEra.CE ? yearOfEra : 1 - yearOfEra);
    }

    @Override
    public IsoEra eraOf(int eraValue) {
        return IsoEra.of(eraValue);
    }

    @Override
    public List<Era> eras() {
        return List.of(IsoEra.values());
    }

    //-----------------------------------------------------------------------

    /**
     * Resolves parsed {@code ChronoField} values into a date during parsing.
     * <p>
     * Most {@code TemporalField} implementations are resolved using the
     * resolve method on the field. By contrast, the {@code ChronoField} class
     * defines fields that only have meaning relative to the chronology.
     * As such, {@code ChronoField} date fields are resolved here in the
     * context of a specific chronology.
     * <p>
     * {@code ChronoField} instances on the JALALI calendar system are resolved
     * as follows.
     * <ul>
     * <li>{@code EPOCH_DAY} - If present, this is converted to a {@code LocalDateJalali}
     * and all other date fields are then cross-checked against the date.
     * <li>{@code PROLEPTIC_MONTH} - If present, then it is split into the
     * {@code YEAR} and {@code MONTH_OF_YEAR}. If the mode is strict or smart
     * then the field is validated.
     * <li>{@code YEAR_OF_ERA} and {@code ERA} - If both are present, then they
     * are combined to form a {@code YEAR}. In lenient mode, the {@code YEAR_OF_ERA}
     * range is not validated, in smart and strict mode it is. The {@code ERA} is
     * validated for range in all three modes. If only the {@code YEAR_OF_ERA} is
     * present, and the mode is smart or lenient, then the current era (CE/AD)
     * is assumed. In strict mode, no era is assumed and the {@code YEAR_OF_ERA} is
     * left untouched. If only the {@code ERA} is present, then it is left untouched.
     * <li>{@code YEAR}, {@code MONTH_OF_YEAR} and {@code DAY_OF_MONTH} -
     * If all three are present, then they are combined to form a {@code LocalDateJalali}.
     * In all three modes, the {@code YEAR} is validated. If the mode is smart or strict,
     * then the month and day are validated, with the day validated from 1 to 31.
     * If the mode is lenient, then the date is combined in a manner equivalent to
     * creating a date on the first of January in the requested year, then adding
     * the difference in months, then the difference in days.
     * If the mode is smart, and the day-of-month is greater than the maximum for
     * the year-month, then the day-of-month is adjusted to the last day-of-month.
     * If the mode is strict, then the three fields must form a valid date.
     * <li>{@code YEAR} and {@code DAY_OF_YEAR} -
     * If both are present, then they are combined to form a {@code LocalDateJalali}.
     * In all three modes, the {@code YEAR} is validated.
     * If the mode is lenient, then the date is combined in a manner equivalent to
     * creating a date on the first of January in the requested year, then adding
     * the difference in days.
     * If the mode is smart or strict, then the two fields must form a valid date.
     * <li>{@code YEAR}, {@code MONTH_OF_YEAR}, {@code ALIGNED_WEEK_OF_MONTH} and
     * {@code ALIGNED_DAY_OF_WEEK_IN_MONTH} -
     * If all four are present, then they are combined to form a {@code LocalDateJalali}.
     * In all three modes, the {@code YEAR} is validated.
     * If the mode is lenient, then the date is combined in a manner equivalent to
     * creating a date on the first of January in the requested year, then adding
     * the difference in months, then the difference in weeks, then in days.
     * If the mode is smart or strict, then the all four fields are validated to
     * their outer ranges. The date is then combined in a manner equivalent to
     * creating a date on the first day of the requested year and month, then adding
     * the amount in weeks and days to reach their values. If the mode is strict,
     * the date is additionally validated to check that the day and week adjustment
     * did not change the month.
     * <li>{@code YEAR}, {@code MONTH_OF_YEAR}, {@code ALIGNED_WEEK_OF_MONTH} and
     * {@code DAY_OF_WEEK} - If all four are present, then they are combined to
     * form a {@code LocalDateJalali}. The approach is the same as described above for
     * years, months and weeks in {@code ALIGNED_DAY_OF_WEEK_IN_MONTH}.
     * The day-of-week is adjusted as the next or same matching day-of-week once
     * the years, months and weeks have been handled.
     * <li>{@code YEAR}, {@code ALIGNED_WEEK_OF_YEAR} and {@code ALIGNED_DAY_OF_WEEK_IN_YEAR} -
     * If all three are present, then they are combined to form a {@code LocalDateJalali}.
     * In all three modes, the {@code YEAR} is validated.
     * If the mode is lenient, then the date is combined in a manner equivalent to
     * creating a date on the first of January in the requested year, then adding
     * the difference in weeks, then in days.
     * If the mode is smart or strict, then the all three fields are validated to
     * their outer ranges. The date is then combined in a manner equivalent to
     * creating a date on the first day of the requested year, then adding
     * the amount in weeks and days to reach their values. If the mode is strict,
     * the date is additionally validated to check that the day and week adjustment
     * did not change the year.
     * <li>{@code YEAR}, {@code ALIGNED_WEEK_OF_YEAR} and {@code DAY_OF_WEEK} -
     * If all three are present, then they are combined to form a {@code LocalDateJalali}.
     * The approach is the same as described above for years and weeks in
     * {@code ALIGNED_DAY_OF_WEEK_IN_YEAR}. The day-of-week is adjusted as the
     * next or same matching day-of-week once the years and weeks have been handled.
     * </ul>
     *
     * @param fieldValues   the map of fields to values, which can be updated, not null
     * @param resolverStyle the requested type of resolve, not null
     * @return the resolved date, null if insufficient information to create a date
     * @throws DateTimeException if the date cannot be resolved, typically
     *                           because of a conflict in the input data
     */
    @Override  // override for performance
    public LocalDateJalali resolveDate(Map<TemporalField, Long> fieldValues, ResolverStyle resolverStyle) {
        return (LocalDateJalali) super.resolveDate(fieldValues, resolverStyle);
    }


    //-----------------------------------------------------------------------
    @Override
    public ValueRange range(ChronoField field) {
        return field.range();
    }

    //-----------------------------------------------------------------------

    /**
     * Obtains a period for this chronology based on years, months and days.
     * <p>
     * This returns a period tied to the JALALI chronology using the specified
     * years, months and days. See {@link Period} for further details.
     *
     * @param years  the number of years, may be negative
     * @param months the number of years, may be negative
     * @param days   the number of years, may be negative
     * @return the JALALI period, not null
     */
    @Override  // override with covariant return type
    public Period period(int years, int months, int days) {
        return Period.of(years, months, days);
    }
}
