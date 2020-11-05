package org.bardframework.time.temporal;

import org.bardframework.time.LocalDateJalali;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.Map;

import static java.time.temporal.IsoFields.WEEK_BASED_YEAR;
import static java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR;

public class IsoFieldsJalali {

    public static ChronoLocalDate resolve(Map<TemporalField, Long> map, TemporalAccessor accessor, ResolverStyle style) {
        Long weekOfYear = map.get(WEEK_BASED_YEAR);
        Long dayOfWeek = map.get(ChronoField.DAY_OF_WEEK);
        if (weekOfYear != null && dayOfWeek != null) {
            int var6 = WEEK_BASED_YEAR.range().checkValidIntValue(weekOfYear, WEEK_BASED_YEAR);
            long var7 = map.get(WEEK_OF_WEEK_BASED_YEAR);
            LocalDateJalali date = LocalDateJalali.of(var6, 1, 4);
            if (style == ResolverStyle.LENIENT) {
                long var10 = dayOfWeek;
                if (var10 > 7L) {
                    date = date.plusWeeks((var10 - 1L) / 7L);
                    var10 = (var10 - 1L) % 7L + 1L;
                } else if (var10 < 1L) {
                    date = date.plusWeeks(Math.subtractExact(var10, 7L) / 7L);
                    var10 = (var10 + 6L) % 7L + 1L;
                }

                date = date.plusWeeks(Math.subtractExact(var7, 1L)).with(ChronoField.DAY_OF_WEEK, var10);
            } else {
                int var12 = ChronoField.DAY_OF_WEEK.checkValidIntValue(dayOfWeek);
                date = date.plusWeeks(var7 - 1L).with(ChronoField.DAY_OF_WEEK, var12);
            }

            map.remove(WEEK_BASED_YEAR);
            map.remove(ChronoField.DAY_OF_WEEK);
            return date;
        } else {
            return null;
        }
    }

    public static int getWeekBasedYear(LocalDateJalali date) {
        int year = date.getYear();
        int doy = date.getDayOfYear();
        if (doy <= 3) {
            int dow = date.getDayOfWeek().ordinal();
            if (doy - dow < -2) {
                year--;
            }
        } else if (doy >= 363) {
            int dow = date.getDayOfWeek().ordinal();
            doy = doy - 363 - (date.isLeapYear() ? 1 : 0);
            if (doy - dow >= 0) {
                year++;
            }
        }
        return year;
    }

    public static int getWeek(LocalDateJalali date) {
        int dayOfWeek = date.getDayOfWeek().ordinal();
        int dayOfYear = date.getDayOfYear() - 1;
        int var3 = dayOfYear + (3 - dayOfWeek);
        int var4 = var3 / 7;
        int var5 = var3 - var4 * 7;
        int var6 = var5 - 3;
        if (var6 < -3) {
            var6 += 7;
        }

        if (dayOfYear < var6) {
            return (int) getWeekRange(date.withDayOfYear(180).minusYears(1L)).getMaximum();
        } else {
            int var7 = (dayOfYear - var6) / 7 + 1;
            if (var7 == 53 && var6 != -3 && (var6 != -2 || !date.isLeapYear())) {
                var7 = 1;
            }

            return var7;
        }
    }

    private static ValueRange getWeekRange(LocalDateJalali date) {
        int weekOfYear = getWeekBasedYear(date);
        return ValueRange.of(1L, getWeekRange(weekOfYear));
    }

    private static int getWeekRange(int weekOfYear) {
        LocalDate date = LocalDate.of(weekOfYear, 1, 1);
        return date.getDayOfWeek() != DayOfWeek.THURSDAY && (date.getDayOfWeek() != DayOfWeek.WEDNESDAY || !date.isLeapYear()) ? 52 : 53;
    }
}
