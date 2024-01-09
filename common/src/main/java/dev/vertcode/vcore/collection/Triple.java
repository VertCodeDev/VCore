package dev.vertcode.vcore.collection;

/**
 * A triple is a container to store three values.
 *
 * @param <F> The first value type
 * @param <S> The second value type
 * @param <T> The third value type
 */
public class Triple<F, S, T> {

    private final F first;
    private final S second;
    private final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Get the first value of the triple.
     *
     * @return The first value of the triple
     */
    public F getFirst() {
        return this.first;
    }

    /**
     * Get the second value of the triple.
     *
     * @return The second value of the triple
     */
    public S getSecond() {
        return this.second;
    }

    /**
     * Get the third value of the triple.
     *
     * @return The third value of the triple
     */
    public T getThird() {
        return this.third;
    }

    /**
     * Create a new triple from the given values.
     *
     * @param first  the first value
     * @param second the second value
     * @param third  the third value
     * @param <F>    the first value type
     * @param <S>    the second value type
     * @param <T>    the third value type
     * @return the new triple
     */
    public static <F, S, T> Triple<F, S, T> of(F first, S second, T third) {
        return new Triple<>(first, second, third);
    }

}
