package dev.vertcode.vcore.util;

import dev.vertcode.vcore.object.IStringParsable;
import dev.vertcode.vcore.parsers.BooleanParsable;
import dev.vertcode.vcore.parsers.DoubleParsable;
import dev.vertcode.vcore.parsers.FloatParsable;
import dev.vertcode.vcore.parsers.IntegerParsable;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing strings.
 */
@UtilityClass
public class StringParser {

    private static final Map<Class<?>, IStringParsable<?>> STRING_PARSERS = new HashMap<>();

    static {
        registerStringParser(new BooleanParsable());
        registerStringParser(new DoubleParsable());
        registerStringParser(new FloatParsable());
        registerStringParser(new IntegerParsable());
    }

    /**
     * Parse a string to a value.
     *
     * @param str   the string to parse
     * @param clazz the class of the value
     * @param <T>   the value type
     * @return the value
     */
    public <T> @Nullable T parse(String str, Class<T> clazz) {
        IStringParsable<T> stringParsable = (IStringParsable<T>) STRING_PARSERS.get(clazz);
        if (stringParsable == null) {
            return null;
        }

        return stringParsable.getValue(str);
    }

    /**
     * Safely parses a string into an enum.
     *
     * @param enumClass    the enum class
     * @param name         the name of the enum
     * @param defaultValue the default value to return if the enum is not found
     * @param <T>          the enum type
     * @return the parsed enum
     */
    public static <T extends Enum<T>> T parseEnum(Class<T> enumClass, String name, T defaultValue) {
        // Make sure the name is not null
        if (name == null) {
            return defaultValue;
        }

        try {
            return Enum.valueOf(enumClass, name);
        } catch (Exception ignored) {
            return defaultValue;
        }
    }

    /**
     * Safely parses a string into an enum.
     *
     * @param enumClass the enum class to parse the string to
     * @param name      the name of the enum
     * @param <T>       the enum type
     * @return the parsed enum
     */
    public static @Nullable <T extends Enum<T>> T parseEnum(Class<T> enumClass, String name) {
        return parseEnum(enumClass, name, null);
    }

    /**
     * Register a string parser.
     *
     * @param stringParsable the string parser to register
     */
    public static void registerStringParser(IStringParsable<?> stringParsable) {
        STRING_PARSERS.put(stringParsable.getParsableClass(), stringParsable);
    }

    /**
     * Unregister a string parser.
     *
     * @param clazz the class of the string parser to unregister
     */
    public static void unregisterStringParser(Class<?> clazz) {
        STRING_PARSERS.remove(clazz);
    }

    /**
     * Check if a string parser is registered.
     *
     * @param clazz the class of the string parser to check
     * @return if the string parser is registered
     */
    public boolean isRegistered(Class<?> clazz) {
        return STRING_PARSERS.containsKey(clazz);
    }

    /**
     * Get a string parser by a class.
     *
     * @param clazz the class to get the string parser by
     * @return the string parser
     */
    public static <T> @Nullable IStringParsable<T> getStringParser(Class<T> clazz) {
        return (IStringParsable<T>) STRING_PARSERS.get(clazz);
    }

}
