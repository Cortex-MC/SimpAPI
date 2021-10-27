package me.kodysimpson.simpapi.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * A subcommand like: /corecommand subcommand args
 * A further example: /chunkcollector buy - buy would be the subcommand that opens a buy menu in that plugin
 */
public abstract class SubCommand {

    /**
     * @return The name of the subcommand
     */
    public abstract String getName();

    /**
     * @return The aliases that can be used for this command. Can be null
     */
    public abstract List<String> getAliases();

    /**
     * @return A description of what the subcommand does to be displayed
     */
    public abstract String getDescription();

    /**
     * @return An example of how to use the subcommand
     */
    public abstract String getSyntax();

    /**
     * @param sender The thing that ran the command
     * @param args   The args passed into the command when run
     */
    public abstract void perform(CommandSender sender, String[] args);

    /**
     * @param player The player who ran the command
     * @param args   The args passed into the command when run
     * @return A list of arguments to be suggested for autocomplete
     */
    public abstract List<String> getSubcommandArguments(Player player, String[] args);

}

