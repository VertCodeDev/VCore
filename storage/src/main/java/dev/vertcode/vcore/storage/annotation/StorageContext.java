package dev.vertcode.vcore.storage.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageContext {

    /**
     * The name of the collection.
     *
     * @return the name of the collection
     */
    String collectionName();

    /**
     * The identifier field of the collection.
     *
     * @return the identifier field of the collection
     */
    String identifierField();

}
