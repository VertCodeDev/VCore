package dev.vertcode.vcore.util;

import dev.vertcode.vcore.object.Placeholder;
import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * A utility class for string related methods.
 */
@UtilityClass
public class StringUtil {

    private static final NumberFormat FORMATTER = new DecimalFormat("###,###,###,###,###");
    private static final NumberFormat DECIMAL_FORMATTER = new DecimalFormat("###,###,###,###,###.###");

    /**
     * Replaces all placeholders in a string.
     *
     * @param string       The string to replace the placeholders in
     * @param placeholders The placeholders to replace
     * @return The string with the placeholders replaced
     */
    public static String replacePlaceholders(String string, Placeholder... placeholders) {
        for (Placeholder placeholder : placeholders) {
            string = placeholder.replace(string);
        }

        return string;
    }

    /**
     * Replaces all placeholders in a string.
     *
     * @param string       The string to replace the placeholders in
     * @param placeholders The placeholders to replace
     * @return The string with the placeholders replaced
     */
    public static String replacePlaceholders(String string, List<Placeholder> placeholders) {
        for (Placeholder placeholder : placeholders) {
            string = placeholder.replace(string);
        }

        return string;
    }

    /**
     * Capitalizes a string.
     *
     * @param string     The string to capitalize
     * @param delimiters The delimiters to capitalize the string with
     * @return The capitalized string
     */
    public static String capitalize(String string, String... delimiters) {
        for (String delimiter : delimiters) {
            string = capitalize(string, delimiter);
        }

        return string;
    }

    /**
     * Capitalizes a string.
     *
     * @param string    The string to capitalize
     * @param delimiter The delimiter to capitalize the string with
     * @return The capitalized string
     */
    public static String capitalize(String string, String delimiter) {
        String[] split = string.split(delimiter);
        StringBuilder builder = new StringBuilder();

        for (String str : split) {
            builder.append(capitalize(str)).append(delimiter);
        }

        return builder.toString().trim();
    }

    /**
     * Capitalizes a string.
     *
     * @param string The string to capitalize
     * @return The capitalized string
     */
    public static String capitalize(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    /**
     * Formats an enum to its captialized name.
     *
     * @param enumValue The enum to format
     * @return The formatted enum
     */
    public String formatEnum(Enum<?> enumValue) {
        String enumName = enumValue.name().toLowerCase();

        return capitalize(enumName, "_", "$");
    }

    /**
     * Formats a number into a string.
     *
     * @param number The number to format
     * @return The formatted number
     */
    public static String formatNumber(Number number) {
        if (number instanceof Double || number instanceof Float) {
            return formatNumber(number, DECIMAL_FORMATTER);
        }

        return formatNumber(number, FORMATTER);
    }

    /**
     * Formats a number into a string.
     *
     * @param number    The number to format
     * @param formatter The formatter to use
     * @return The formatted number
     */
    public static String formatNumber(Number number, NumberFormat formatter) {
        return formatter.format(number);
    }

}
