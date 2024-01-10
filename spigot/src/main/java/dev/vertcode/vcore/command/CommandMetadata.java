package dev.vertcode.vcore.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandMetadata {

    /**
     * The name of the command
     *
     * @return the name of the command
     */
    String name();

    /**
     * The aliases of the command
     *
     * @return the aliases of the command
     */
    String[] aliases() default {};

    /**
     * The permission of the command
     *
     * @return the permission of the command
     */
    String permission();

    /**
     * The description of the command
     *
     * @return the description of the command
     */
    String description();

    /**
     * The usage of the command
     *
     * @return the usage of the command
     */
    String usage();

}
