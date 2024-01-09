package dev.vertcode.vcore.storage;

import org.jetbrains.annotations.NotNull;

public abstract class StorageObject<T> {

    /**
     * Get the identifier of the object.
     *
     * @return the identifier
     */
    public abstract @NotNull T getIdentifier();

}
