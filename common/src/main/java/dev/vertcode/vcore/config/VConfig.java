package dev.vertcode.vcore.config;

import dev.vertcode.vcore.object.Placeholder;
import dev.vertcode.vcore.util.StringUtil;

import java.util.List;

public interface VConfig {

    /**
     * Returns the enum value as a {@link Boolean}.
     *
     * @return true | false
     */
    default boolean getBoolean() {
        return (boolean) getValue();
    }

    /**
     * Returns the enum value as a {@link String}.
     *
     * @return the string
     */
    default String getString() {
        return (String) getValue();
    }

    /**
     * Returns the enum value as a {@link Integer}.
     *
     * @return the integer
     */
    default int getInteger() {
        return (int) getValue();
    }

    /**
     * Returns the enum value as a {@link Double}.
     *
     * @return the double
     */
    default double getDouble() {
        return (double) getValue();
    }

    /**
     * Returns the enum value as a {@link Float}.
     *
     * @return the float
     */
    default float getFloat() {
        return (float) getValue();
    }

    /**
     * Returns the enum value as a {@link List <Object>}.
     *
     * @return the object list
     */
    default List<Object> getList() {
        return (List<Object>) getValue();
    }

    /**
     * Returns the enum value as a {@link List<String>}.
     *
     * @return the string list
     */
    default List<String> getStringList() {
        return (List<String>) getValue();
    }

    /**
     * Returns the enum value as the specified class.
     *
     * @param clazz the class
     * @param <T>   the type
     * @return the value
     */
    default <T> T getValue(Class<T> clazz) {
        return (T) getValue();
    }

    /**
     * Returns the enum value as an {@link Object}.
     *
     * @return the value
     */
    Object getValue();

    /**
     * Sets the enum value.
     *
     * @param value the value
     */
    void setValue(Object value);

    /**
     * Returns the enum name. (This is always automatically implemented, but we need it here for the interface)
     *
     * @return the name
     */
    String name();

    /**
     * Returns the path of the enum value in the config.
     *
     * @return the path
     */
    default String getConfigurationPath() {
        return StringUtil.replacePlaceholders(name().toLowerCase(),
                new Placeholder("$", "."),
                new Placeholder("_", "-")
        );
    }
}
