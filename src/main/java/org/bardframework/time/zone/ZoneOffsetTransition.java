package org.bardframework.time.zone;

import lombok.Getter;
import org.bardframework.time.LocalDateTimeJalali;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.zone.ZoneRules;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A transition between two offsets caused by a discontinuity in the local time-line.
 * <p>
 * A transition between two offsets is normally the result of a daylight savings cutover.
 * The discontinuity is normally a gap in spring and an overlap in autumn.
 * {@code ZoneOffsetTransition} models the transition between the two offsets.
 * <p>
 * Gaps occur where there are local date-times that simply do not exist.
 * An example would be when the offset changes from {@code +03:00} to {@code +04:00}.
 * This might be described as 'the clocks will move forward one hour tonight at 1am'.
 * <p>
 * Overlaps occur where there are local date-times that exist twice.
 * An example would be when the offset changes from {@code +04:00} to {@code +03:00}.
 * This might be described as 'the clocks will move back one hour tonight at 2am'.
 * <p>
 * this class is immutable and thread-safe.
 */
@Getter
public final class ZoneOffsetTransition implements Comparable<ZoneOffsetTransition>, Serializable {

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = -6946044323557704546L;
    /**
     * The transition epoch-second.
     */
    private final long epochSecond;
    /**
     * The local transition date-time at the transition.
     */
    private final LocalDateTimeJalali transition;
    /**
     * The offset before transition.

     */
    private final ZoneOffset offsetBefore;
    /**
     * The offset after transition.

     */
    private final ZoneOffset offsetAfter;

    //-----------------------------------------------------------------------

    /**
     * Creates an instance defining a transition between two offsets.
     *
     * @param transition   the transition date-time with the offset before the transition, not null
     * @param offsetBefore the offset before the transition, not null
     * @param offsetAfter  the offset at and after the transition, not null
     */
    ZoneOffsetTransition(LocalDateTimeJalali transition, ZoneOffset offsetBefore, ZoneOffset offsetAfter) {
        assert transition.getNano() == 0;
        this.epochSecond = transition.toEpochSecond(offsetBefore);
        this.transition = transition;
        this.offsetBefore = offsetBefore;
        this.offsetAfter = offsetAfter;
    }

    /**
     * Creates an instance from epoch-second and offsets.
     *
     * @param epochSecond  the transition epoch-second
     * @param offsetBefore the offset before the transition, not null
     * @param offsetAfter  the offset at and after the transition, not null
     */
    ZoneOffsetTransition(long epochSecond, ZoneOffset offsetBefore, ZoneOffset offsetAfter) {
        this.epochSecond = epochSecond;
        this.transition = LocalDateTimeJalali.ofEpochSecond(epochSecond, 0, offsetBefore);
        this.offsetBefore = offsetBefore;
        this.offsetAfter = offsetAfter;
    }

    /**
     * Obtains an instance defining a transition between two offsets.
     * <p>
     * Applications should normally obtain an instance from {@link java.time.zone.ZoneRules}.
     * This factory is only intended for use when creating {@link ZoneRules}.
     *
     * @param transition   the transition date-time at the transition, which never
     *                     actually occurs, expressed local to the before offset, not null
     * @param offsetBefore the offset before the transition, not null
     * @param offsetAfter  the offset at and after the transition, not null
     * @return the transition, not null
     * @throws IllegalArgumentException if {@code offsetBefore} and {@code offsetAfter}
     *                                  are equal, or {@code transition.getNano()} returns non-zero value
     */
    public static ZoneOffsetTransition of(LocalDateTimeJalali transition, ZoneOffset offsetBefore, ZoneOffset offsetAfter) {
        Objects.requireNonNull(transition, "transition");
        Objects.requireNonNull(offsetBefore, "offsetBefore");
        Objects.requireNonNull(offsetAfter, "offsetAfter");
        if (offsetBefore.equals(offsetAfter)) {
            throw new IllegalArgumentException("Offsets must not be equal");
        }
        if (transition.getNano() != 0) {
            throw new IllegalArgumentException("Nano-of-second must be zero");
        }
        return new ZoneOffsetTransition(transition, offsetBefore, offsetAfter);
    }

    //-----------------------------------------------------------------------

    /**
     * Reads the state from the stream.
     *
     * @param in the input stream, not null
     * @return the created object, not null
     * @throws IOException if an error occurs
     */
    static ZoneOffsetTransition readExternal(DataInput in) throws IOException {
        long epochSecond = Ser.readEpochSec(in);
        ZoneOffset before = Ser.readOffset(in);
        ZoneOffset after = Ser.readOffset(in);
        if (before.equals(after)) {
            throw new IllegalArgumentException("Offsets must not be equal");
        }
        return new ZoneOffsetTransition(epochSecond, before, after);
    }

    /**
     * Defend against malicious streams.
     *
     * @param s the stream to read
     * @throws InvalidObjectException always
     */
    private void readObject(ObjectInputStream s) throws InvalidObjectException {
        throw new InvalidObjectException("Deserialization via serialization delegate");
    }

    /**
     * Writes the object using a
     * <a href="{@docRoot}/serialized-form.html#java.time.zone.Ser">dedicated serialized form</a>.
     *
     * @return the replacing object, not null
     * @serialData Refer to the serialized form of
     * <a href="{@docRoot}/serialized-form.html#java.time.zone.ZoneRules">ZoneRules.writeReplace</a>
     * for the encoding of epoch seconds and offsets.
     * <pre style="font-size:1.0em">{@code
     *
     *   out.writeByte(2);                // identifies a ZoneOffsetTransition
     *   out.writeEpochSec(toEpochSecond);
     *   out.writeOffset(offsetBefore);
     *   out.writeOffset(offsetAfter);
     * }
     * </pre>
     */
    private Object writeReplace() {
        return new Ser(Ser.ZOT, this);
    }

    /**
     * Writes the state to the stream.
     *
     * @param out the output stream, not null
     * @throws IOException if an error occurs
     */
    void writeExternal(DataOutput out) throws IOException {
        Ser.writeEpochSec(epochSecond, out);
        Ser.writeOffset(offsetBefore, out);
        Ser.writeOffset(offsetAfter, out);
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the transition instant.
     * <p>
     * This is the instant of the discontinuity, which is defined as the first
     * instant that the 'after' offset applies.
     * <p>
     * The methods {@link #getInstant()}, {@link #getDateTimeBefore()} and {@link #getDateTimeAfter()}
     * all represent the same instant.
     *
     * @return the transition instant, not null
     */
    public Instant getInstant() {
        return Instant.ofEpochSecond(epochSecond);
    }

    /**
     * Gets the transition instant as an epoch second.
     *
     * @return the transition epoch second
     */
    public long toEpochSecond() {
        return epochSecond;
    }

    //-------------------------------------------------------------------------

    /**
     * Gets the local transition date-time, as would be expressed with the 'before' offset.
     * <p>
     * This is the date-time where the discontinuity begins expressed with the 'before' offset.
     * At this instant, the 'after' offset is actually used, therefore the combination of this
     * date-time and the 'before' offset will never occur.
     * <p>
     * The combination of the 'before' date-time and offset represents the same instant
     * as the 'after' date-time and offset.
     *
     * @return the transition date-time expressed with the before offset, not null
     */
    public LocalDateTimeJalali getDateTimeBefore() {
        return transition;
    }

    /**
     * Gets the local transition date-time, as would be expressed with the 'after' offset.
     * <p>
     * This is the first date-time after the discontinuity, when the new offset applies.
     * <p>
     * The combination of the 'before' date-time and offset represents the same instant
     * as the 'after' date-time and offset.
     *
     * @return the transition date-time expressed with the after offset, not null
     */
    public LocalDateTimeJalali getDateTimeAfter() {
        return transition.plusSeconds(getDurationSeconds());
    }

    /**
     * Gets the duration of the transition.
     * <p>
     * In most cases, the transition duration is one hour, however this is not always the case.
     * The duration will be positive for a gap and negative for an overlap.
     * Time-zones are second-based, so the nanosecond part of the duration will be zero.
     *
     * @return the duration of the transition, positive for gaps, negative for overlaps
     */
    public Duration getDuration() {
        return Duration.ofSeconds(getDurationSeconds());
    }

    /**
     * Gets the duration of the transition in seconds.
     *
     * @return the duration in seconds
     */
    private int getDurationSeconds() {
        return getOffsetAfter().getTotalSeconds() - getOffsetBefore().getTotalSeconds();
    }

    /**
     * Does this transition represent a gap in the local time-line.
     * <p>
     * Gaps occur where there are local date-times that simply do not exist.
     * An example would be when the offset changes from {@code +01:00} to {@code +02:00}.
     * This might be described as 'the clocks will move forward one hour tonight at 1am'.
     *
     * @return true if this transition is a gap, false if it is an overlap
     */
    public boolean isGap() {
        return getOffsetAfter().getTotalSeconds() > getOffsetBefore().getTotalSeconds();
    }

    /**
     * Does this transition represent an overlap in the local time-line.
     * <p>
     * Overlaps occur where there are local date-times that exist twice.
     * An example would be when the offset changes from {@code +02:00} to {@code +01:00}.
     * This might be described as 'the clocks will move back one hour tonight at 2am'.
     *
     * @return true if this transition is an overlap, false if it is a gap
     */
    public boolean isOverlap() {
        return getOffsetAfter().getTotalSeconds() < getOffsetBefore().getTotalSeconds();
    }

    /**
     * Checks if the specified offset is valid during this transition.
     * <p>
     * This checks to see if the given offset will be valid at some point in the transition.
     * A gap will always return false.
     * An overlap will return true if the offset is either the before or after offset.
     *
     * @param offset the offset to check, null returns false
     * @return true if the offset is valid during the transition
     */
    public boolean isValidOffset(ZoneOffset offset) {
        return !isGap() && (getOffsetBefore().equals(offset) || getOffsetAfter().equals(offset));
    }

    /**
     * Gets the valid offsets during this transition.
     * <p>
     * A gap will return an empty list, while an overlap will return both offsets.
     *
     * @return the list of valid offsets
     */
    List<ZoneOffset> getValidOffsets() {
        if (isGap()) {
            return Collections.emptyList();
        }
        return List.of(getOffsetBefore(), getOffsetAfter());
    }

    //-----------------------------------------------------------------------

    /**
     * Compares this transition to another based on the transition instant.
     * <p>
     * This compares the instants of each transition.
     * The offsets are ignored, making this order inconsistent with equals.
     *
     * @param transition the transition to compare to, not null
     * @return the comparator value, negative if less, positive if greater
     */
    @Override
    public int compareTo(ZoneOffsetTransition transition) {
        return Long.compare(epochSecond, transition.epochSecond);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this object equals another.
     * <p>
     * The entire state of the object is compared.
     *
     * @param other the other object to compare to, null returns false
     * @return true if equal
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other instanceof ZoneOffsetTransition) {
            ZoneOffsetTransition d = (ZoneOffsetTransition) other;
            return epochSecond == d.epochSecond &&
                offsetBefore.equals(d.offsetBefore) && offsetAfter.equals(d.offsetAfter);
        }
        return false;
    }

    /**
     * Returns a suitable hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return transition.hashCode() ^ offsetBefore.hashCode() ^ Integer.rotateLeft(offsetAfter.hashCode(), 16);
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a string describing this object.
     *
     * @return a string for debugging, not null
     */
    @Override
    public String toString() {
        String buf = "Transition[" +
            (isGap() ? "Gap" : "Overlap") +
            " at " +
            transition +
            offsetBefore +
            " to " +
            offsetAfter +
            ']';
        return buf;
    }

}
