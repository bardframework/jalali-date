package org.bardframework.time.format;

import org.bardframework.time.ChronologyJalali;

import java.time.chrono.Chronology;
import java.time.chrono.JapaneseChronology;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.time.temporal.ChronoField.*;

/**
 * A provider to obtain the textual form of a date-time field.
 * <p>
 * Implementations must be thread-safe.
 * Implementations should cache the textual information.
 */
class DateTimeTextProvider {

    /**
     * Cache.
     */
    private static final ConcurrentMap<Entry<TemporalField, Locale>, Object> CACHE = new ConcurrentHashMap<>(16, 0.75f, 2);
    /**
     * Comparator.
     */
    private static final Comparator<Entry<String, Long>> COMPARATOR = (obj1, obj2) -> {
        return obj2.getKey().length() - obj1.getKey().length();  // longest to shortest
    };

    DateTimeTextProvider() {
    }

    /**
     * Gets the provider of text.
     *
     * @return the provider, not null
     */
    static DateTimeTextProvider getInstance() {
        return new DateTimeTextProvider();
    }

    private static int toWeekDay(int calWeekDay) {
        if (calWeekDay == Calendar.SUNDAY) {
            return 7;
        } else {
            return calWeekDay - 1;
        }
    }

    /**
     * Helper method to create an immutable entry.
     *
     * @param text  the text, not null
     * @param field the field, not null
     * @return the entry, not null
     */
    private static <A, B> Entry<A, B> createEntry(A text, B field) {
        return new SimpleImmutableEntry<>(text, field);
    }

    /**
     * Returns the localized resource of the given key and locale, or null
     * if no localized resource is available.
     *
     * @param key    the key of the localized resource, not null
     * @param locale the locale, not null
     * @return the localized resource, or null if not available
     * @throws NullPointerException if key or locale is null
     */
    static <T> T getLocalizedResource(String key, Locale locale) {
        return null;
    }

    /**
     * Gets the text for the specified field, locale and style
     * for the purpose of formatting.
     * <p>
     * The text associated with the value is returned.
     * The null return value should be used if there is no applicable text, or
     * if the text would be a numeric representation of the value.
     *
     * @param field  the field to get text for, not null
     * @param value  the field value to get text for, not null
     * @param style  the style to get text for, not null
     * @param locale the locale to get text for, not null
     * @return the text for the field value, null if no text found
     */
    String getText(TemporalField field, long value, TextStyle style, Locale locale) {
        Object store = findStore(field, locale);
        if (store instanceof LocaleStore) {
            return ((LocaleStore) store).getText(value, style);
        }
        return null;
    }

    /**
     * Gets the text for the specified chrono, field, locale and style
     * for the purpose of formatting.
     * <p>
     * The text associated with the value is returned.
     * The null return value should be used if there is no applicable text, or
     * if the text would be a numeric representation of the value.
     *
     * @param chrono the Chronology to get text for, not null
     * @param field  the field to get text for, not null
     * @param value  the field value to get text for, not null
     * @param style  the style to get text for, not null
     * @param locale the locale to get text for, not null
     * @return the text for the field value, null if no text found
     */
    String getText(Chronology chrono, TemporalField field, long value, TextStyle style, Locale locale) {
        if (chrono == ChronologyJalali.INSTANCE || !(field instanceof ChronoField)) {
            return getText(field, value, style, locale);
        }

        int fieldIndex;
        int fieldValue;
        if (field == ERA) {
            fieldIndex = Calendar.ERA;
            if (chrono == JapaneseChronology.INSTANCE) {
                if (value == -999) {
                    fieldValue = 0;
                } else {
                    fieldValue = (int) value + 2;
                }
            } else {
                fieldValue = (int) value;
            }
        } else if (field == MONTH_OF_YEAR) {
            fieldIndex = Calendar.MONTH;
            fieldValue = (int) value - 1;
        } else if (field == DAY_OF_WEEK) {
            fieldIndex = Calendar.DAY_OF_WEEK;
            fieldValue = (int) value + 1;
            if (fieldValue > 7) {
                fieldValue = Calendar.SUNDAY;
            }
        } else if (field == AMPM_OF_DAY) {
            fieldIndex = Calendar.AM_PM;
            fieldValue = (int) value;
        } else {
            return null;
        }
        return DateTimeFormatterJalali.retrieveJavaTimeFieldValueName(fieldIndex, fieldValue, style.toCalendarStyle(), locale);
    }

    /**
     * Gets an iterator of text to field for the specified field, locale and style
     * for the purpose of parsing.
     * <p>
     * The iterator must be returned in order from the longest text to the shortest.
     * <p>
     * The null return value should be used if there is no applicable parsable text, or
     * if the text would be a numeric representation of the value.
     * Text can only be parsed if all the values for that field-style-locale combination are unique.
     *
     * @param field  the field to get text for, not null
     * @param style  the style to get text for, null for all parsable text
     * @param locale the locale to get text for, not null
     * @return the iterator of text to field pairs, in order from longest text to shortest text,
     * null if the field or style is not parsable
     */
    Iterator<Entry<String, Long>> getTextIterator(TemporalField field, TextStyle style, Locale locale) {
        Object store = findStore(field, locale);
        if (store instanceof LocaleStore) {
            return ((LocaleStore) store).getTextIterator(style);
        }
        return null;
    }

    /**
     * Gets an iterator of text to field for the specified chrono, field, locale and style
     * for the purpose of parsing.
     * <p>
     * The iterator must be returned in order from the longest text to the shortest.
     * <p>
     * The null return value should be used if there is no applicable parsable text, or
     * if the text would be a numeric representation of the value.
     * Text can only be parsed if all the values for that field-style-locale combination are unique.
     *
     * @param chrono the Chronology to get text for, not null
     * @param field  the field to get text for, not null
     * @param style  the style to get text for, null for all parsable text
     * @param locale the locale to get text for, not null
     * @return the iterator of text to field pairs, in order from longest text to shortest text,
     * null if the field or style is not parsable
     */
    Iterator<Entry<String, Long>> getTextIterator(Chronology chrono, TemporalField field,
                                                  TextStyle style, Locale locale) {
        if (chrono == ChronologyJalali.INSTANCE || !(field instanceof ChronoField)) {
            return getTextIterator(field, style, locale);
        }

        int fieldIndex;
        switch ((ChronoField) field) {
            case ERA:
                fieldIndex = Calendar.ERA;
                break;
            case MONTH_OF_YEAR:
                fieldIndex = Calendar.MONTH;
                break;
            case DAY_OF_WEEK:
                fieldIndex = Calendar.DAY_OF_WEEK;
                break;
            case AMPM_OF_DAY:
                fieldIndex = Calendar.AM_PM;
                break;
            default:
                return null;
        }

        int calendarStyle = (style == null) ? Calendar.ALL_STYLES : style.toCalendarStyle();
        Map<String, Integer> map = null;
        return null;
    }

    private Object findStore(TemporalField field, Locale locale) {
        Entry<TemporalField, Locale> key = createEntry(field, locale);
        Object store = CACHE.get(key);
        if (store == null) {
            store = createStore(field, locale);
            CACHE.putIfAbsent(key, store);
            store = CACHE.get(key);
        }
        return store;
    }

    private Object createStore(TemporalField field, Locale locale) {
        Map<TextStyle, Map<Long, String>> styleMap = new HashMap<>();
        if (field == ERA) {
            for (TextStyle textStyle : TextStyle.values()) {
                if (textStyle.isStandalone()) {
                    // Stand-alone isn't applicable to era names.
                    continue;
                }
                Map<String, Integer> displayNames = null;
            }
            return new LocaleStore(styleMap);
        }

        if (field == MONTH_OF_YEAR) {
            for (TextStyle textStyle : TextStyle.values()) {
                Map<Long, String> map = new HashMap<>();
                for (Entry<Long, String> entry : DateTimeFormatterJalali.getMonthOfYear(locale).entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                }
                styleMap.put(textStyle, map);
            }
            return new LocaleStore(styleMap);
        }

        if (field == DAY_OF_WEEK) {
            for (TextStyle textStyle : TextStyle.values()) {
                Map<Long, String> map = new HashMap<>();
                for (Entry<Long, String> entry : DateTimeFormatterJalali.getDayOfWeek(locale).entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                }
                styleMap.put(textStyle, map);
            }
            return new LocaleStore(styleMap);
        }

        if (field == AMPM_OF_DAY) {
            for (TextStyle textStyle : TextStyle.values()) {
                Map<Long, String> map = new HashMap<>();
                for (Entry<Long, String> entry : DateTimeFormatterJalali.getAmPm(locale).entrySet()) {
                    map.put(entry.getKey(), entry.getValue());
                }
                styleMap.put(textStyle, map);
            }
            return new LocaleStore(styleMap);
        }

        if (field == IsoFields.QUARTER_OF_YEAR) {
            // The order of keys must correspond to the TextStyle.values() order.
            final String[] keys = {
                    "QuarterNames",
                    "standalone.QuarterNames",
                    "QuarterAbbreviations",
                    "standalone.QuarterAbbreviations",
                    "QuarterNarrows",
                    "standalone.QuarterNarrows",
            };
            for (int i = 0; i < keys.length; i++) {
                String[] names = getLocalizedResource(keys[i], locale);
                if (names != null) {
                    Map<Long, String> map = new HashMap<>();
                    for (int q = 0; q < names.length; q++) {
                        map.put((long) (q + 1), names[q]);
                    }
                    styleMap.put(TextStyle.values()[i], map);
                }
            }
            return new LocaleStore(styleMap);
        }

        return "";  // null marker for map
    }

    /**
     * Stores the text for a single locale.
     * <p>
     * Some fields have a textual representation, such as day-of-week or month-of-year.
     * These textual representations can be captured in this class for printing
     * and parsing.
     * <p>
     * This class is immutable and thread-safe.
     */
    static final class LocaleStore {
        /**
         * Map of value to text.
         */
        private final Map<TextStyle, Map<Long, String>> valueTextMap;
        /**
         * Parsable data.
         */
        private final Map<TextStyle, List<Entry<String, Long>>> parsable;

        /**
         * Constructor.
         *
         * @param valueTextMap the map of values to text to store, assigned and not altered, not null
         */
        LocaleStore(Map<TextStyle, Map<Long, String>> valueTextMap) {
            this.valueTextMap = valueTextMap;
            Map<TextStyle, List<Entry<String, Long>>> map = new HashMap<>();
            List<Entry<String, Long>> allList = new ArrayList<>();
            for (Entry<TextStyle, Map<Long, String>> vtmEntry : valueTextMap.entrySet()) {
                Map<String, Entry<String, Long>> reverse = new HashMap<>();
                for (Entry<Long, String> entry : vtmEntry.getValue().entrySet()) {
                    if (reverse.put(entry.getValue(), createEntry(entry.getValue(), entry.getKey())) != null) {
                        // TODO: BUG: this has no effect
                        continue;  // not parsable, try next style
                    }
                }
                List<Entry<String, Long>> list = new ArrayList<>(reverse.values());
                list.sort(COMPARATOR);
                map.put(vtmEntry.getKey(), list);
                allList.addAll(list);
                map.put(null, allList);
            }
            allList.sort(COMPARATOR);
            this.parsable = map;
        }

        /**
         * Gets the text for the specified field value, locale and style
         * for the purpose of printing.
         *
         * @param value the value to get text for, not null
         * @param style the style to get text for, not null
         * @return the text for the field value, null if no text found
         */
        String getText(long value, TextStyle style) {
            Map<Long, String> map = valueTextMap.get(style);
            return map != null ? map.get(value) : null;
        }

        /**
         * Gets an iterator of text to field for the specified style for the purpose of parsing.
         * <p>
         * The iterator must be returned in order from the longest text to the shortest.
         *
         * @param style the style to get text for, null for all parsable text
         * @return the iterator of text to field pairs, in order from longest text to shortest text,
         * null if the style is not parsable
         */
        Iterator<Entry<String, Long>> getTextIterator(TextStyle style) {
            List<Entry<String, Long>> list = parsable.get(style);
            return list != null ? list.iterator() : null;
        }
    }
}
