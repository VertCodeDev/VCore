package dev.vertcode.vcore.config;

import java.io.File;

public interface VConfigLoader {

    /**
     * Load the {@link VConfig} enum class from the config file.
     */
    void load();

    /**
     * Save the {@link VConfig} enum class to the config file.
     */
    void save();

    /**
     * Get the {@link VConfig} enum class.
     *
     * @return the enum class
     */
    Class<? extends VConfig> getConfigClass();

    /**
     * Get the config file.
     *
     * @return the config file
     */
    File getConfigFile();

}
