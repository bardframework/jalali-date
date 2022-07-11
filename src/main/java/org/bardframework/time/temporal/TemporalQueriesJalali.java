package org.bardframework.time.temporal;

import org.bardframework.time.LocalDateJalali;

import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalQuery;

import static java.time.temporal.ChronoField.EPOCH_DAY;

/**
 * Common implementations of {@code TemporalQuery}.
 * <p>
 * This class provides common implementations of {@link TemporalQuery}.
 * These are defined here as they must be constants, and the definition
 * of lambdas does not guarantee that. By assigning them once here,
 * they become 'normal' Java constants.
 * <p>
 * Queries are a key tool for extracting information from temporal objects.
 * They exist to externalize the process of querying, permitting different
 * approaches, as per the strategy design pattern.
 * Examples might be a query that checks if the date is the day before February 29th
 * in a leap year, or calculates the number of days to your next birthday.
 * <p>
 * The {@link TemporalField} interface provides another mechanism for querying
 * temporal objects. That interface is limited to returning a {@code long}.
 * By contrast, queries can return any type.
 * <p>
 * There are two equivalent ways of using a {@code TemporalQuery}.
 * The first is to invoke the method on this interface directly.
 * The second is to use {@link TemporalAccessor#query(TemporalQuery)}:
 * <pre>
 *   // these two lines are equivalent, but the second approach is recommended
 *   temporal = thisQuery.queryFrom(temporal);
 *   temporal = temporal.query(thisQuery);
 * </pre>
 * It is recommended to use the second approach, {@code query(TemporalQuery)},
 * as it is a lot clearer to read in code.
 * <p>
 * The most common implementations are method references, such as
 * {@code LocalDateJalali::from} and {@code ZoneId::from}.
 * Additional common queries are provided to return:
 * <ul>
 * <li> a LocalDateJalali,
 * </ul>
 */
public final class TemporalQueriesJalali {
    /**
     * A query for {@code LocalDateJalali} returning null if not found.
     */
    static final TemporalQuery<LocalDateJalali> LOCAL_DATE = new TemporalQuery<LocalDateJalali>() {
        @Override
        public LocalDateJalali queryFrom(TemporalAccessor temporal) {
            if (temporal.isSupported(EPOCH_DAY)) {
                return LocalDateJalali.ofEpochDay(temporal.getLong(EPOCH_DAY));
            }
            return null;
        }

        @Override
        public String toString() {
            return "LocalDateJalali";
        }
    };

    /**
     * Private constructor since this is a utility class.
     */
    private TemporalQueriesJalali() {
    }

    /**
     * A query for {@code LocalDateJalali} returning null if not found.
     * <p>
     * This returns a {@code TemporalQuery} that can be used to query a temporal
     * object for the local date. The query will return null if the temporal
     * object cannot supply a local date.
     * <p>
     * The query implementation examines the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
     * field and uses it to create a {@code LocalDateJalali}.
     * <p>
     * The method {@link ZoneOffset#from(TemporalAccessor)} can be used as a
     * {@code TemporalQuery} via a method reference, {@code LocalDateJalali::from}.
     * This query and {@code LocalDateJalali::from} will return the same result if the
     * temporal object contains a date. If the temporal object does not contain
     * a date, then the method reference will throw an exception, whereas this
     * query will return null.
     *
     * @return a query that can obtain the date of a temporal, not null
     */
    public static TemporalQuery<LocalDateJalali> localDate() {
        return TemporalQueriesJalali.LOCAL_DATE;
    }
}
