package dev.vertcode.vcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class VertConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final File configFile;
    private JsonObject jsonObject;

    public VertConfig(File configFile) {
        this.configFile = configFile;
    }

    /**
     * Loads the {@link VertConfig} from the config file.
     */
    public void load() {
        if (!this.configFile.exists()) {
            this.jsonObject = new JsonObject();
            return;
        }

        try (Reader reader = new FileReader(this.configFile)) {
            this.jsonObject = GSON.fromJson(reader, JsonObject.class);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to load config file " + this.configFile.getName(), ex);
        }
    }

    /**
     * Saves the {@link VertConfig} to the config file.
     */
    public void save() {
        try (Writer writer = new FileWriter(this.configFile)) {
            GSON.toJson(this.jsonObject, writer);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to save config file " + this.configFile.getName(), ex);
        }
    }

    /**
     * Set a value in the config by a path.
     *
     * @param key   The path to the value
     * @param value The value
     */
    public void set(String key, Object value) {
        List<String> path = Arrays.asList(key.split("\\."));
        // If "instant" path is given, return the value.
        if (path.size() == 1) {
            this.jsonObject.add(key, GSON.toJsonTree(value));
            return;
        }

        List<String> floorPath = path.subList(0, path.size() - 1);
        Object embeddedValue = getEmbeddedValue(floorPath, true);
        if (!(embeddedValue instanceof JsonObject)) {
            String newKey = String.join(".", path.subList(1, path.size()));

            set(newKey, value);
            return;
        }

        String where = path.get(path.size() - 1);
        ((JsonObject) embeddedValue).add(where, GSON.toJsonTree(value));
    }

    /**
     * Get a value from the config by a path.
     *
     * @param key   The path to the value
     * @param clazz The class of the value
     * @param <T>   The type of the value
     * @return The value
     */
    public <T> T get(String key, Class<T> clazz) {
        List<String> path = Arrays.asList(key.split("\\."));
        // If "instant" path is given, return the value.
        if (path.size() == 1) {
            return GSON.fromJson(this.jsonObject.get(key), clazz);
        }

        List<String> floorPath = path.subList(0, path.size() - 1);
        Object embeddedValue = getEmbeddedValue(floorPath, false);
        if (!(embeddedValue instanceof JsonObject)) {
            String newKey = String.join(".", path.subList(1, path.size()));

            return get(newKey, clazz);
        }

        String where = path.get(path.size() - 1);

        return GSON.fromJson(((JsonObject) embeddedValue).get(where), clazz);
    }

    /**
     * Get a string from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public String getString(String key) {
        return get(key, String.class);
    }

    /**
     * Get a string from the config by a path.
     *
     * @param key        The path to the value
     * @param defaultStr The default value
     * @return The value
     */
    public String getString(String key, String defaultStr) {
        String value = get(key, String.class);
        if (value == null) {
            return defaultStr;
        }

        return value;
    }

    /**
     * Get a boolean from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public Boolean getBoolean(String key) {
        return get(key, boolean.class);
    }

    /**
     * Get a boolean from the config by a path.
     *
     * @param key         The path to the value
     * @param defaultBool The default value
     * @return The value
     */
    public Boolean getBoolean(String key, boolean defaultBool) {
        Boolean value = get(key, boolean.class);
        if (value == null) {
            return defaultBool;
        }

        return value;
    }

    /**
     * Get an integer from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public Integer getInteger(String key) {
        return get(key, int.class);
    }

    /**
     * Get an integer from the config by a path.
     *
     * @param key        The path to the value
     * @param defaultInt The default value
     * @return The value
     */
    public Integer getInteger(String key, int defaultInt) {
        Integer value = get(key, int.class);
        if (value == null) {
            return defaultInt;
        }

        return value;
    }

    /**
     * Get a long from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public Long getLong(String key) {
        return get(key, long.class);
    }

    /**
     * Get a long from the config by a path.
     *
     * @param key         The path to the value
     * @param defaultLong The default value
     * @return The value
     */
    public Long getLong(String key, long defaultLong) {
        Long value = get(key, long.class);
        if (value == null) {
            return defaultLong;
        }

        return value;
    }

    /**
     * Get a double from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public Double getDouble(String key) {
        return get(key, double.class);
    }

    /**
     * Get a double from the config by a path.
     *
     * @param key           The path to the value
     * @param defaultDouble The default value
     * @return The value
     */
    public Double getDouble(String key, double defaultDouble) {
        Double value = get(key, double.class);
        if (value == null) {
            return defaultDouble;
        }

        return value;
    }

    /**
     * Get a list from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public List<Object> getList(String key) {
        return (List<Object>) get(key, Object.class);
    }

    /**
     * Get a list from the config by a path.
     *
     * @param key         The path to the value
     * @param defaultList The default value
     * @return The value
     */
    public List<Object> getList(String key, List<Object> defaultList) {
        List<Object> value = (List<Object>) get(key, Object.class);
        if (value == null) {
            return defaultList;
        }

        return value;
    }

    /**
     * Get a list of strings from the config by a path.
     *
     * @param key The path to the value
     * @return The value
     */
    public List<String> getStringList(String key) {
        return (List<String>) get(key, Object.class);
    }

    /**
     * Get a list of strings from the config by a path.
     *
     * @param key         The path to the value
     * @param defaultList The default value
     * @return The value
     */
    public List<String> getStringList(String key, List<String> defaultList) {
        List<String> value = (List<String>) get(key, Object.class);
        if (value == null) {
            return defaultList;
        }

        return value;
    }

    /**
     * Check if the config contains a value by a path.
     *
     * @param key The path to the value
     * @return If the config contains the value
     */
    public boolean has(String key) {
        return get(key, Object.class) != null;
    }

    /**
     * Get a value from the config by a path.
     *
     * @param keys              The path to the value
     * @param createIfNotExists If the value should be created if it does not exist
     * @param <T>               The type of the value
     * @return The value
     */
    private <T> T getEmbeddedValue(List<String> keys, boolean createIfNotExists) {
        JsonObject currentSection = this.jsonObject;
        for (String key : keys) {
            if (!currentSection.has(key)) {
                if (!createIfNotExists) {
                    return null;
                }

                currentSection.add(key, new JsonObject());
            }

            currentSection = currentSection.getAsJsonObject(key);
            if (currentSection == null) {
                return null;
            }
        }

        return ((Class<T>) Object.class).cast(currentSection);
    }

}
