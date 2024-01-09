package dev.vertcode.vcore.storage.service;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.vertcode.vcore.storage.StorageObject;
import dev.vertcode.vcore.storage.StorageService;
import dev.vertcode.vcore.storage.annotation.StorageContext;
import dev.vertcode.vcore.storage.util.StorageUtil;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MongoStorageService<I, V extends StorageObject<I>> extends StorageService<I, V> {

    private final ConnectionString connectionString;
    private MongoDatabase mongoDatabase;

    public MongoStorageService(Class<V> storageObjectClass, ConnectionString connectionString) {
        super(storageObjectClass);
        this.connectionString = connectionString;
    }

    public MongoStorageService(Class<V> storageObjectClass, Long cacheTime, TimeUnit cacheTimeUnit, ConnectionString connectionString) {
        super(storageObjectClass, cacheTime, cacheTimeUnit);
        this.connectionString = connectionString;
    }

    @Override
    public void startup() {
        if (connectionString.getDatabase() == null) {
            throw new IllegalArgumentException("No database specified in the connection string.");
        }

        // Connect to the database
        MongoClient mongoClient = MongoClients.create(this.connectionString);

        // Get the database
        this.mongoDatabase = mongoClient.getDatabase(connectionString.getDatabase());
    }

    @Override
    public @Nullable V get(I identifier, boolean cache) {
        // Get the collection
        MongoCollection<Document> collection = getCollection();
        Document document = collection.find(Filters.eq(getIdentifierField(), parseIdentifier(identifier))).first();
        if (document == null) {
            return null;
        }

        V value = StorageUtil.getGson().fromJson(document.toJson(), this.storageObjectClass);
        if (!cache || value == null) {
            return value;
        }

        // Add the value to the cache
        addToCache(value);
        return value;
    }

    @Override
    public Collection<V> getAll(boolean cache) {
        // Get the collection
        MongoCollection<Document> collection = getCollection();
        List<V> storageObjects = new ArrayList<>();

        try (MongoCursor<Document> mongoCursor = collection.find().iterator()) {
            while (mongoCursor.hasNext()) {
                Document document = mongoCursor.next();
                V value = StorageUtil.getGson().fromJson(document.toJson(), this.storageObjectClass);
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
        }

        return storageObjects;
    }

    @Override
    public void save(V value) {
        // Get the collection and the document
        MongoCollection<Document> collection = getCollection();
        Document document = Document.parse(StorageUtil.getGson().toJson(value));

        // Insert the document
        collection.replaceOne(
                Filters.eq(getIdentifierField(), parseIdentifier(value.getIdentifier())),
                document,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public void delete(V value) {
        // Get the collection
        MongoCollection<Document> collection = getCollection();

        // Delete the document
        collection.deleteOne(Filters.eq(getIdentifierField(), parseIdentifier(value.getIdentifier())));
    }

    /**
     * Parse the identifier to a suitable object for the database.
     *
     * @param identifier The identifier you want to parse
     * @return The parsed identifier
     */
    private Object parseIdentifier(I identifier) {
        if (identifier instanceof UUID uuid) {
            return uuid.toString();
        }

        return identifier;
    }

    /**
     * Get the collection from the database.
     *
     * @return The collection
     */
    private @NotNull MongoCollection<Document> getCollection() {
        StorageContext storageContext = getStorageContext();
        if (storageContext == null) {
            throw new NullPointerException("StorageContext is null");
        }

        return this.mongoDatabase.getCollection(storageContext.collectionName());
    }

    /**
     * Get the identifier field from the {@link StorageContext}.
     *
     * @return The identifier field
     */
    private String getIdentifierField() {
        StorageContext storageContext = getStorageContext();
        if (storageContext == null) {
            throw new NullPointerException("StorageContext is null");
        }

        return storageContext.identifierField();
    }

}
