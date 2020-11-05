package org.bardframework.time.format;

/**
 * Enumeration of ways to handle the positive/negative sign.
 * <p>
 * The formatting engine allows the positive and negative signs of numbers
 * to be controlled using this enum.
 * See {@link DateTimeFormatterBuilderJalali} for usage.
 */
public enum SignStyle {

    /**
     * Style to output the sign only if the value is negative.
     * <p>
     * In strict parsing, the negative sign will be accepted and the positive sign rejected.
     * In lenient parsing, any sign will be accepted.
     */
    NORMAL,
    /**
     * Style to always output the sign, where zero will output '+'.
     * <p>
     * In strict parsing, the absence of a sign will be rejected.
     * In lenient parsing, any sign will be accepted, with the absence
     * of a sign treated as a positive number.
     */
    ALWAYS,
    /**
     * Style to never output sign, only outputting the absolute value.
     * <p>
     * In strict parsing, any sign will be rejected.
     * In lenient parsing, any sign will be accepted unless the width is fixed.
     */
    NEVER,
    /**
     * Style to block negative values, throwing an exception on printing.
     * <p>
     * In strict parsing, any sign will be rejected.
     * In lenient parsing, any sign will be accepted unless the width is fixed.
     */
    NOT_NEGATIVE,
    /**
     * Style to always output the sign if the value exceeds the pad width.
     * A negative value will always output the '-' sign.
     * <p>
     * In strict parsing, the sign will be rejected unless the pad width is exceeded.
     * In lenient parsing, any sign will be accepted, with the absence
     * of a sign treated as a positive number.
     */
    EXCEEDS_PAD;

    /**
     * Parse helper.
     *
     * @param positive   true if positive sign parsed, false for negative sign
     * @param strict     true if strict, false if lenient
     * @param fixedWidth true if fixed width, false if not
     * @return result
     */
    public boolean parse(boolean positive, boolean strict, boolean fixedWidth) {
        switch (ordinal()) {
            case 0: // NORMAL
                // valid if negative or (positive and lenient)
                return !positive || !strict;
            case 1: // ALWAYS
            case 4: // EXCEEDS_PAD
                return true;
            default:
                // valid if lenient and not fixed width
                return !strict && !fixedWidth;
        }
    }

}
