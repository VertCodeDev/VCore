package dev.vertcode.vcore.object;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * An interface to parse a string to a value.
 *
 * @param <V> the value type
 */
public interface IStringParsable<V> {

    /**
     * Get the value by a string.
     *
     * @param string the string to get the value by
     * @return the value
     */
    @Nullable V getValue(String string);

    /**
     * Get the examples of the value as a string.
     *
     * @return the examples
     */
    Collection<String> getExamples();

    /**
     * The class of the value.
     *
     * @return the class
     */
    Class<V> getParsableClass();

}
