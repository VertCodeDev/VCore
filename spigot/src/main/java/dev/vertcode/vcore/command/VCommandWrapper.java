package dev.vertcode.vcore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VCommandWrapper extends Command {

    private final VCommand<?> command;
    private final CommandMetadata commandMetadata;

    public VCommandWrapper(VCommand<?> command, CommandMetadata commandMetadata) {
        super(commandMetadata.name(), commandMetadata.description(), commandMetadata.usage(), List.of(commandMetadata.aliases()));

        this.command = command;
        this.commandMetadata = commandMetadata;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return this.command.onCommand(sender, this, commandLabel, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        List<String> tabComplete = this.command.onTabComplete(sender, this, alias, args);
        if (tabComplete == null) {
            return new ArrayList<>();
        }

        return tabComplete;
    }

    /**
     * Gets the command metadata
     *
     * @return The command metadata
     */
    public CommandMetadata getCommandMetadata() {
        return this.commandMetadata;
    }
}
