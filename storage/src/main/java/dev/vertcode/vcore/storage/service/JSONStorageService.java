package dev.vertcode.vcore.storage.service;

import dev.vertcode.vcore.storage.StorageObject;
import dev.vertcode.vcore.storage.StorageService;
import dev.vertcode.vcore.storage.annotation.StorageContext;
import dev.vertcode.vcore.storage.util.StorageUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JSONStorageService<I, V extends StorageObject<I>> extends StorageService<I, V> {

    private final File databaseFolder;

    public JSONStorageService(Class<V> storageObjectClass, File databaseFolder) {
        super(storageObjectClass);
        this.databaseFolder = databaseFolder;
    }

    public JSONStorageService(Class<V> storageObjectClass, Long cacheTime, TimeUnit cacheTimeUnit, File databaseFolder) {
        super(storageObjectClass, cacheTime, cacheTimeUnit);
        this.databaseFolder = databaseFolder;
    }

    @Override
    public void startup() {
        // If the database folder doesn't exist, create it.
        if (!this.databaseFolder.exists()) {
            this.databaseFolder.mkdirs();
        }

        // Get the data folder
        File dataFolder = getDataFolder();
        if (dataFolder.exists()) {
            return;
        }

        // Create the data folder
        dataFolder.mkdirs();
    }

    @Override
    public @Nullable V get(I identifier, boolean cache) {
        File dataFile = new File(getDataFolder(), identifier.toString() + ".json");
        if (!dataFile.exists()) {
            return null;
        }

        V value = readObject(dataFile);
        if (!cache || value == null) {
            return value;
        }

        // Add the value to the cache
        addToCache(value);
        return value;
    }

    @Override
    public Collection<V> getAll(boolean cache) {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists()) {
            return new ArrayList<>();
        }

        File[] dataFiles = dataFolder.listFiles();
        if (dataFiles == null) {
            return new ArrayList<>();
        }

        List<V> storageObjects = new ArrayList<>();
        for (File dataFile : dataFiles) {
            if (!dataFile.getName().endsWith(".json")) {
                continue;
            }

            V value = readObject(dataFile);
            if (value == null) {
                continue;
            }

            // Add the value to the list
            storageObjects.add(value);

            if (!cache) {
                continue;
            }

            // Add the value to the cache
            addToCache(value);
        }

        return storageObjects;
    }

    @Override
    public void save(V value) {
        File dataFile = new File(getDataFolder(), value.getIdentifier() + ".json");

        try (Writer writer = new FileWriter(dataFile)) {
            StorageUtil.getGson().toJson(value, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(V value) {
        I identifier = value.getIdentifier();

        // Remove the value from the cache
        removeFromCache(identifier);

        File dataFile = new File(getDataFolder(), identifier + ".json");
        if (!dataFile.exists()) {
            return;
        }

        // Delete the data file
        dataFile.delete();
    }

    /**
     * Reads an object from a file.
     *
     * @param file The file to read from
     * @return The object read from the file
     */
    private V readObject(File file) {
        try (FileReader reader = new FileReader(file)) {
            return StorageUtil.getGson().fromJson(reader, this.storageObjectClass);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Get the data folder of the {@link StorageService}.
     *
     * @return The data folder
     */
    private File getDataFolder() {
        StorageContext storageContext = getStorageContext();
        if (storageContext == null) {
            throw new NullPointerException("StorageContext is null");
        }

        return new File(this.databaseFolder, storageContext.collectionName());
    }
}
