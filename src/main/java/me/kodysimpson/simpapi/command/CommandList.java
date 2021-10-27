package me.kodysimpson.simpapi.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A functional interface used to allow the dev to specify how the listing of the subcommands on a core command works.
 */
@FunctionalInterface
public interface CommandList {

    /**
     * @param sender         The thing that ran the command
     * @param subCommandList A list of all the subcommands you can display
     */
    void displayCommandList(CommandSender sender, List<SubCommand> subCommandList);

}
