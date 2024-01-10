package dev.vertcode.vcore.command;

import dev.vertcode.vcore.object.IStringParsable;
import dev.vertcode.vcore.util.StringParser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class VCommand<S extends CommandSender> implements CommandExecutor, TabCompleter {

    protected List<VCommand<?>> subCommands = new ArrayList<>();
    protected final Class<S> senderClass;

    public VCommand() {
        // Get the type of the <T> type
        Type type = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        // Check if the type is a class, if not throw an exception
        if (!(type instanceof Class)) {
            throw new RuntimeException("The type of the <T> type is not a class.");
        }

        // Set the source class
        this.senderClass = (Class<S>) type;
    }

    /**
     * Execute the command.
     *
     * @param sender the sender of the command
     * @param args   the arguments of the command
     */
    public abstract void execute(S sender, String[] args);

    /**
     * Tab complete the command.
     *
     * @param sender the sender of the command
     * @param args   the arguments of the command
     * @return the tab complete suggestions
     */
    public abstract @Nullable List<String> tabComplete(S sender, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        CommandMetadata metadata = getMetadata();
        if (metadata == null) {
            throw new NullPointerException("Command metadata cannot be null");
        }

        String permission = metadata.permission();
        // Ensure the sender has the permission
        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            // TODO: Send no permission message
            return true;
        }

        // Ensure the sender is the correct type
        if (!this.senderClass.isAssignableFrom(sender.getClass())) {
            // TODO: Send invalid sender message
            return true;
        }

        S castedSender = (S) sender;
        // No arguments, execute the command.
        if (args.length == 0) {
            execute(castedSender, args);
            return true;
        }

        String firstArg = args[0];
        VCommand<?> subCommand = getSubCommand(firstArg);
        // No valid sub command found, execute the command.
        if (subCommand == null) {
            execute(castedSender, args);
            return true;
        }

        // Execute the sub command
        subCommand.onCommand(
                sender, cmd, label,
                Arrays.copyOfRange(args, 1, args.length)
        );
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        CommandMetadata metadata = getMetadata();
        if (metadata == null) {
            throw new NullPointerException("Command metadata cannot be null");
        }

        String permission = metadata.permission();
        // Ensure the sender has the permission
        if (!permission.isEmpty() && !sender.hasPermission(permission)) {
            return null;
        }

        // Ensure the sender is the correct type
        if (!this.senderClass.isAssignableFrom(sender.getClass())) {
            return null;
        }

        S castedSender = (S) sender;
        // No arguments, tab complete the command.
        if (args.length == 0) {
            List<String> tabComplete = tabComplete(castedSender, args);
            // Ensure the tab complete is not null
            if (tabComplete == null) {
                tabComplete = new ArrayList<>();
            }

            // Add all sub commands to the tab complete
            for (VCommand<?> subCommand : this.subCommands) {
                CommandMetadata subMetadata = subCommand.getMetadata();
                if (subMetadata == null) {
                    continue;
                }

                tabComplete.add(subMetadata.name());
            }
            return tabComplete;
        }

        String firstArg = args[0];
        VCommand<?> subCommand = getSubCommand(firstArg);
        // No valid sub command found, tab complete the command.
        if (subCommand == null) {
            List<String> tabComplete = tabComplete(castedSender, args);
            if (args.length == 1) {
                // Ensure the tab complete is not null
                if (tabComplete == null) {
                    tabComplete = new ArrayList<>();
                }

                // Add all sub commands to the tab complete
                for (VCommand<?> sub : this.subCommands) {
                    CommandMetadata subMetadata = sub.getMetadata();
                    if (subMetadata == null) {
                        continue;
                    }

                    tabComplete.add(subMetadata.name());
                }
            }

            return sortTabComplete(tabComplete, args[args.length - 1]);
        }

        // Tab complete the sub command
        return subCommand.onTabComplete(
                sender, cmd, label,
                Arrays.copyOfRange(args, 1, args.length)
        );
    }

    /**
     * Parse an argument to a value.
     *
     * @param arg   the argument to parse
     * @param clazz the class of the value
     * @param <T>   the value type
     * @return the parsed value or null if not found
     */
    public @Nullable <T> T parseArg(String arg, Class<T> clazz) {
        IStringParsable<T> stringParser = StringParser.getStringParser(clazz);
        if (stringParser == null) {
            return null;
        }

        return stringParser.getValue(arg);
    }

    /**
     * Parse an argument to an enum.
     *
     * @param enumClass the enum class
     * @param name      the name of the enum
     * @param <T>       the enum type
     * @return the parsed enum
     */
    public @Nullable <T extends Enum<T>> T parseEnum(Class<T> enumClass, String name) {
        return StringParser.parseEnum(enumClass, name);
    }

    /**
     * Get a sub command by its name or alias.
     *
     * @param name the name or alias of the sub command
     * @return the sub command or null if not found
     */
    protected @Nullable VCommand<?> getSubCommand(String name) {
        for (VCommand<?> subCommand : this.subCommands) {
            CommandMetadata metadata = subCommand.getMetadata();
            if (metadata == null) {
                continue;
            }

            if (metadata.name().equalsIgnoreCase(name)) {
                return subCommand;
            }

            for (String alias : metadata.aliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    return subCommand;
                }
            }
        }

        return null;
    }

    /**
     * Get the metadata of the command.
     *
     * @return the metadata of the command
     */
    public @Nullable CommandMetadata getMetadata() {
        try {
            return this.getClass().getAnnotation(CommandMetadata.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Sort the tab complete suggestions.
     *
     * @param tabComplete the tab complete suggestions
     * @param lastArg     the last argument
     * @return the sorted tab complete suggestions
     */
    private List<String> sortTabComplete(@Nullable List<String> tabComplete, String lastArg) {
        if (tabComplete == null) {
            return null;
        }

        List<String> sortedTabComplete = new ArrayList<>();
        for (String suggestion : tabComplete) {
            if (!suggestion.toLowerCase().startsWith(lastArg.toLowerCase())) {
                continue;
            }

            sortedTabComplete.add(suggestion);
        }

        return sortedTabComplete;
    }

}