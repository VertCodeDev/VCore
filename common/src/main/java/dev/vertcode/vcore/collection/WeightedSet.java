package dev.vertcode.vcore.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link WeightedSet} is used to get a random value from a set with weights,
 * typically used for weighted random rewards like crates.
 *
 * @param <V> The type of the values
 */
public class WeightedSet<V> {

    private final Map<V, Double> chanceMap = new HashMap<>();
    private double totalWeight = 0.0D;

    /**
     * Adds a value to the map.
     *
     * @param value  The value
     * @param weight The weight of the value
     */
    public void add(V value, double weight) {
        this.chanceMap.put(value, weight);
        this.totalWeight += weight;
    }

    /**
     * Removes a value from the map.
     *
     * @param value The value
     */
    public void remove(V value) {
        Double weight = this.chanceMap.remove(value);
        if (weight == null) {
            return;
        }

        this.totalWeight -= weight;
    }

    /**
     * Clears the map.
     */
    public void clear() {
        this.chanceMap.clear();
        this.totalWeight = 0.0D;
    }

    /**
     * Returns a random value from the map.
     *
     * @return A random value from the map
     */
    public V getRandom() {
        double random = Math.random() * this.totalWeight;
        for (Map.Entry<V, Double> entry : this.chanceMap.entrySet()) {
            random -= entry.getValue();
            if (random <= 0.0D) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Returns the weight of the value.
     *
     * @param value The value
     * @return The weight of the value
     */
    public double getWeight(V value) {
        return this.chanceMap.getOrDefault(value, 0.0D);
    }

    /**
     * Returns the total weight of the map.
     *
     * @return The total weight of the map
     */
    public double getTotalWeight() {
        return this.totalWeight;
    }

    /**
     * Returns all the values from the map.
     *
     * @return All the values from the map
     */
    public Collection<V> getValues() {
        return this.chanceMap.keySet();
    }
}
