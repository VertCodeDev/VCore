package dev.vertcode.vcore.config.loader;

import dev.vertcode.vcore.config.VConfig;
import dev.vertcode.vcore.config.VConfigLoader;
import dev.vertcode.vcore.config.VertConfig;

import java.io.File;

public class JSONConfigLoader implements VConfigLoader {

    private final Class<? extends VConfig> configClass;
    private final File configFile;
    private final VertConfig vertConfig;

    public JSONConfigLoader(Class<? extends VConfig> configClass, File configFile) {
        this.configClass = configClass;
        this.configFile = configFile;
        this.vertConfig = new VertConfig(configFile);
    }

    @Override
    public void load() {
        // If the config file does not exist, save the config and return.
        if (!this.configFile.exists()) {
            save();
            return;
        }

        // Load the vert config
        this.vertConfig.load();

        boolean insertedDefaultValues = false;
        for (Object object : this.configClass.getEnumConstants()) {
            if (!(object instanceof VConfig vConfig)) {
                continue;
            }

            String path = vConfig.getConfigurationPath();
            // If the path does not exist, set the default value.
            if (!this.vertConfig.has(path)) {
                insertedDefaultValues = true;
                continue;
            }

            // Get the value from the config and set it to the enum value.
            Object value = this.vertConfig.get(path, vConfig.getValue().getClass());

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

            this.vertConfig.set(path, value);
        }

        // Save the vert config.
        this.vertConfig.save();
    }

    @Override
    public Class<? extends VConfig> getConfigClass() {
        return this.configClass;
    }

    @Override
    public File getConfigFile() {
        return this.configFile;
    }
}
