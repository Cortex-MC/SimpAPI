package me.kodysimpson.simpapi.command;

import org.bukkit.entity.Player;

import java.util.List;

@FunctionalInterface
public interface CommandList {

    /**
     * @param player The player to list the commands to
     * @param subCommandList A list of all the subcommands you can display
     */
    void displayCommandList(Player player, List<SubCommand> subCommandList);

}
