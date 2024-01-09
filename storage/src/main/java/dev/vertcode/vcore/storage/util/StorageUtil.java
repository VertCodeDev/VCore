package dev.vertcode.vcore.storage.util;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

/**
 * Utility class for storage-related things.
 */
@UtilityClass
public class StorageUtil {

    private static GsonBuilder GSON_BUILDER = Converters.registerAll(new GsonBuilder()
            .excludeFieldsWithModifiers(128)
            .serializeNulls()
            .enableComplexMapKeySerialization()
    );
    private static Gson GSON = GSON_BUILDER.create();

    /**
     * Get the Gson instance.
     *
     * @return the Gson instance
     */
    public static Gson getGson() {
        return GSON;
    }

    /**
     * Get the GsonBuilder instance.
     *
     * @return the GsonBuilder instance
     */
    public static GsonBuilder getGsonBuilder() {
        return GSON_BUILDER;
    }

    /**
     * Update the Gson instance.
     *
     * @param gsonBuilder The new GsonBuilder
     */
    public static void updateGsonBuilder(GsonBuilder gsonBuilder) {
        GSON_BUILDER = gsonBuilder;
        GSON = GSON_BUILDER.create();
    }

}
