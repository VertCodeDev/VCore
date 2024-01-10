package dev.vertcode.vcore.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigMetadata {

    /**
     * The config name.
     *
     * @return The config name
     */
    String name();

    /**
     * The config folder.
     *
     * @return The config folder
     */
    String folder() default "";

}
