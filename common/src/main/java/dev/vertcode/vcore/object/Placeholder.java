package dev.vertcode.vcore.object;

import dev.vertcode.vcore.util.StringUtil;

/**
 * A {@link Placeholder} is a container to store a placeholder and the value
 * that should replace it. This is used to replace placeholders in strings.
 */
public class Placeholder {

    private final String placeholder;
    private final Object value;

    public Placeholder(String placeholder, Object value) {
        this.placeholder = placeholder;
        this.value = value;
    }

    /**
     * Replace the placeholder in the given string with the value.
     *
     * @param string The string to replace the placeholder in
     * @return The string with the placeholder replaced
     */
    public String replace(String string) {
        String replacement = String.valueOf(this.value);
        if (this.value instanceof Number number) {
            replacement = StringUtil.formatNumber(number);
        } else if (this.value instanceof Enum<?> enumValue) {
            replacement = StringUtil.formatEnum(enumValue);
        }

        return string.replace(this.placeholder, replacement);
    }

    /**
     * Get the placeholder that should be replaced.
     *
     * @return The placeholder that should be replaced
     */
    public String getPlaceholder() {
        return this.placeholder;
    }

    /**
     * Get the value that should replace the placeholder.
     *
     * @return The value that should replace the placeholder
     */
    public Object getValue() {
        return this.value;
    }

    /**
     * Create a new placeholder from the given values.
     *
     * @param placeholder the placeholder
     * @param value       the value
     * @return the new placeholder
     */
    public static Placeholder of(String placeholder, Object value) {
        return new Placeholder(placeholder, value);
    }

}
