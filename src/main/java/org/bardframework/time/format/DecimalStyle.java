package org.bardframework.time.format;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Localized decimal style used in date and time formatting.
 * <p>
 * A significant part of dealing with dates and times is the localization.
 * This class acts as a central point for accessing the information.
 */
final class DecimalStyle {

    /**
     * The standard set of non-localized decimal style symbols.
     * <p>
     * This uses standard ASCII characters for zero, positive, negative and a dot for the decimal point.
     */
    public static final DecimalStyle STANDARD = new DecimalStyle('0', '+', '-', '.');
    /**
     * The cache of DecimalStyle instances.
     */
    private static final ConcurrentMap<Locale, DecimalStyle> CACHE = new ConcurrentHashMap<>(16, 0.75f, 2);

    /**
     * The zero digit.
     */
    private final char zeroDigit;
    /**
     * The positive sign.
     */
    private final char positiveSign;
    /**
     * The negative sign.
     */
    private final char negativeSign;
    /**
     * The decimal separator.
     */
    private final char decimalSeparator;

    /**
     * Restricted constructor.
     *
     * @param zeroChar         the character to use for the digit of zero
     * @param positiveSignChar the character to use for the positive sign
     * @param negativeSignChar the character to use for the negative sign
     * @param decimalPointChar the character to use for the decimal point
     */
    private DecimalStyle(char zeroChar, char positiveSignChar, char negativeSignChar, char decimalPointChar) {
        this.zeroDigit = zeroChar;
        this.positiveSign = positiveSignChar;
        this.negativeSign = negativeSignChar;
        this.decimalSeparator = decimalPointChar;
    }

    /**
     * Obtains the DecimalStyle for the specified locale.
     * <p>
     * This method provides access to locale sensitive decimal style symbols.
     *
     * @param locale the locale, not null
     * @return the decimal style, not null
     */
    public static DecimalStyle of(Locale locale) {
        Objects.requireNonNull(locale, "locale");
        DecimalStyle info = CACHE.get(locale);
        if (info == null) {
            info = create(locale);
            CACHE.putIfAbsent(locale, info);
            info = CACHE.get(locale);
        }
        return info;
    }

    //-----------------------------------------------------------------------

    private static DecimalStyle create(Locale locale) {
        DecimalFormatSymbols oldSymbols = DecimalFormatSymbols.getInstance(locale);
        char zeroDigit = oldSymbols.getZeroDigit();
        char positiveSign = '+';
        char negativeSign = oldSymbols.getMinusSign();
        char decimalSeparator = oldSymbols.getDecimalSeparator();
        if (zeroDigit == '0' && negativeSign == '-' && decimalSeparator == '.') {
            return STANDARD;
        }
        return new DecimalStyle(zeroDigit, positiveSign, negativeSign, decimalSeparator);
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the character that represents zero.
     * <p>
     * The character used to represent digits may vary by culture.
     * This method specifies the zero character to use, which implies the characters for one to nine.
     *
     * @return the character for zero
     */
    public char getZeroDigit() {
        return zeroDigit;
    }

    /**
     * Gets the character that represents the positive sign.
     * <p>
     * The character used to represent a positive number may vary by culture.
     * This method specifies the character to use.
     *
     * @return the character for the positive sign
     */
    public char getPositiveSign() {
        return positiveSign;
    }
    //-----------------------------------------------------------------------

    /**
     * Gets the character that represents the negative sign.
     * <p>
     * The character used to represent a negative number may vary by culture.
     * This method specifies the character to use.
     *
     * @return the character for the negative sign
     */
    public char getNegativeSign() {
        return negativeSign;
    }

    //-----------------------------------------------------------------------

    /**
     * Gets the character that represents the decimal point.
     * <p>
     * The character used to represent a decimal point may vary by culture.
     * This method specifies the character to use.
     *
     * @return the character for the decimal point
     */
    public char getDecimalSeparator() {
        return decimalSeparator;
    }

    //-----------------------------------------------------------------------

    /**
     * Checks whether the character is a digit, based on the currently set zero character.
     *
     * @param ch the character to check
     * @return the value, 0 to 9, of the character, or -1 if not a digit
     */
    int convertToDigit(char ch) {
        int val = ch - zeroDigit;
        return (val >= 0 && val <= 9) ? val : -1;
    }

    /**
     * Converts the input numeric text to the internationalized form using the zero character.
     *
     * @param numericText the text, consisting of digits 0 to 9, to convert, not null
     * @return the internationalized text, not null
     */
    String convertNumberToI18N(String numericText) {
        if (zeroDigit == '0') {
            return numericText;
        }
        int diff = zeroDigit - '0';
        char[] array = numericText.toCharArray();
        for (int i = 0; i < array.length; i++) {
            array[i] = (char) (array[i] + diff);
        }
        return new String(array);
    }

    //-----------------------------------------------------------------------

    /**
     * Checks if this DecimalStyle is equal to another DecimalStyle.
     *
     * @param obj the object to check, null returns false
     * @return true if this is equal to the other date
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DecimalStyle other) {
            return (zeroDigit == other.zeroDigit && positiveSign == other.positiveSign &&
                negativeSign == other.negativeSign && decimalSeparator == other.decimalSeparator);
        }
        return false;
    }

    /**
     * A hash code for this DecimalStyle.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return zeroDigit + positiveSign + negativeSign + decimalSeparator;
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a string describing this DecimalStyle.
     *
     * @return a string description, not null
     */
    @Override
    public String toString() {
        return "DecimalStyle[" + zeroDigit + positiveSign + negativeSign + decimalSeparator + "]";
    }

}
