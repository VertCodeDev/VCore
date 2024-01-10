package dev.vertcode.vcore.config.loader;

import dev.vertcode.vcore.config.VConfig;
import dev.vertcode.vcore.config.VConfigLoader;
import dev.vertcode.vcore.config.VertConfigSerializable;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Method;

public class YamlConfigLoader implements VConfigLoader {

    private final Class<? extends VConfig> configClass;
    private final File configFile;
    private Configuration configuration;

    public YamlConfigLoader(Class<? extends VConfig> configClass, File configFile) {
        this.configClass = configClass;
        this.configFile = configFile;
    }

    @Override
    public void load() {
        // If the config file does not exist, save the config and return.
        if (!this.configFile.exists()) {
            save();
            return;
        }

        // Load the config
        this.configuration = YamlConfiguration.loadConfiguration(this.configFile);

        boolean insertedDefaultValues = false;
        for (Object object : this.configClass.getEnumConstants()) {
            if (!(object instanceof VConfig vConfig)) {
                continue;
            }

            String path = vConfig.getConfigurationPath();
            // If the path does not exist, set the default value.
            if (!this.configuration.contains(path)) {
                insertedDefaultValues = true;
                continue;
            }

            if (vConfig.getValue() instanceof VertConfigSerializable configSerializable) {
                ConfigurationSection configSection = this.configuration.getConfigurationSection(path);
                if (configSection == null) {
                    continue;
                }

                // Load the object from the section
                Object loadSerializable = loadFromSerializable(configSerializable, configSection);

                vConfig.setValue(loadSerializable);
                continue;
            }

            // Get the value from the config and set it to the enum value.
            Object value = this.configuration.get(path);

            vConfig.setValue(value);
        }

        // If we inserted default values, save the config.
        if (!insertedDefaultValues) {
            return;
        }

        save();
    }

    @Override
    public void save() {
        // First loop through all the enum constants and set the values in the config.
        for (Object object : this.configClass.getEnumConstants()) {
            if (!(object instanceof VConfig vConfig)) {
                continue;
            }

            String path = vConfig.getConfigurationPath();
            Object value = vConfig.getValue();
            if (value instanceof VertConfigSerializable configSerializable) {
                // Reset the section
                this.configuration.set(path, null);

                // Create the section
                ConfigurationSection configSection = this.configuration.createSection(path);

                // Save the object to the section
                configSerializable.saveToSection(configSection);
                continue;
            }

            this.configuration.set(path, value);
        }
    }

    @Override
    public Class<? extends VConfig> getConfigClass() {
        return this.configClass;
    }

    @Override
    public File getConfigFile() {
        return this.configFile;
    }

    /**
     * Load the object from the serializable.
     *
     * @param configSerializable The serializable
     * @param configSection      The config section
     * @return The object
     */
    private Object loadFromSerializable(VertConfigSerializable configSerializable, ConfigurationSection configSection) {
        try {
            Method fromConfigMethod = configSerializable.getClass().getMethod("fromConfig", ConfigurationSection.class);

            return fromConfigMethod.invoke(configSerializable, configSection);
        }catch (Exception ex) {
            throw new RuntimeException("Could not find fromConfig method in " + configSerializable.getClass().getName());
        }

    }
}
