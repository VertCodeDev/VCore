package dev.vertcode.vcore.config;

import org.bukkit.configuration.ConfigurationSection;

public interface VertConfigSerializable {

    /**
     * Save the object to the config section.
     *
     * @param configSection the config section
     */
    void saveToSection(ConfigurationSection configSection);

}
