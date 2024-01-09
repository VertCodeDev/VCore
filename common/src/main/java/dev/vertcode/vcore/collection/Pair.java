package dev.vertcode.vcore.collection;

/**
 * A pair is a container to store two values.
 *
 * @param <F> The first value type
 * @param <S> The second value type
 */
public class Pair<F, S> {

    private final F first;
    private final S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first value of the pair.
     *
     * @return The first value of the pair
     */
    public F getFirst() {
        return this.first;
    }

    /**
     * Get the second value of the pair.
     *
     * @return The second value of the pair
     */
    public S getSecond() {
        return this.second;
    }

    /**
     * Create a new pair from the given values.
     *
     * @param first  the first value
     * @param second the second value
     * @param <F>    the first value type
     * @param <S>    the second value type
     * @return the new pair
     */
    public static <F, S> Pair<F, S> of(F first, S second) {
        return new Pair<>(first, second);
    }

}
