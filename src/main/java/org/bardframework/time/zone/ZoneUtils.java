package org.bardframework.time.zone;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.zone.ZoneRulesException;
import java.time.zone.ZoneRulesProvider;

public class ZoneUtils {
    /**
     * Gets the time-zone rules for this ID allowing calculations to be performed.
     * <p>
     * The rules provide the functionality associated with a time-zone,
     * such as finding the offset for a given instant or local date-time.
     * <p>
     * A time-zone can be invalid if it is deserialized in a Java Runtime which
     * does not have the same rules loaded as the Java Runtime that stored it.
     * In this case, calling this method will throw a {@code ZoneRulesException}.
     * <p>
     * The rules are supplied by {@link ZoneRulesProvider}. An advanced provider may
     * support dynamic updates to the rules without restarting the Java Runtime.
     * If so, then the result of this method may change over time.
     * Each individual call will be still remain thread-safe.
     * <p>
     * {@link ZoneOffset} will always return a set of rules where the offset never changes.
     *
     * @param zone zone
     * @return the rules, not null
     * @throws ZoneRulesException if no rules are available for this ID
     */
    public static ZoneRules getRules(ZoneId zone) {
        if (zone instanceof ZoneOffset) {
            return ZoneRules.of((ZoneOffset) zone);
        }
        /*
            can't access inner property of ZoneRegion
         */
        return ZoneRules.of(zone.getRules().getOffset(Instant.EPOCH));
    }
}
