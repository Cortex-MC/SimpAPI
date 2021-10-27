package me.kodysimpson.simpapi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a main command that has subcommands within it. Like /stafftools freeze is /[corecommand] [subcommand]
 */
class CoreCommand extends Command {

    private final ArrayList<SubCommand> subcommands;
    private final CommandList commandList;

    public CoreCommand(String name, String description, String usageMessage, CommandList commandList, List<String> aliases, ArrayList<SubCommand> subCommands) {
        super(name, description, usageMessage, aliases);
        //Get the subcommands so we can access them in the command manager class(here)
        this.subcommands = subCommands;
        this.commandList = commandList;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subcommands;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {

        if (args.length > 0) {
            for (int i = 0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName()) || (getSubCommands().get(i).getAliases() != null && getSubCommands().get(i).getAliases().contains(args[0]))) {
                    getSubCommands().get(i).perform(sender, args);
                }
            }
        } else {
            if (commandList == null) {
                sender.sendMessage("--------------------------------");
                for (SubCommand subcommand : subcommands) {
                    sender.sendMessage(subcommand.getSyntax() + " - " + subcommand.getDescription());
                }
                sender.sendMessage("--------------------------------");
            } else {
                commandList.displayCommandList(sender, subcommands);
            }
        }

        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1) { //prank <subcommand> <args>
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            //Does the subcommand autocomplete
            for (int i = 0; i < getSubCommands().size(); i++) {
                subcommandsArguments.add(getSubCommands().get(i).getName());
            }
            return subcommandsArguments;
        } else if (args.length >= 2) {
            for (int i = 0; i < getSubCommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                    List<String> subCommandArgs = getSubCommands().get(i).getSubcommandArguments(
                            (Player) sender, args
                    );

                    //getSubcommandArguments will have returned null if no implementation was provided.
                    if (subCommandArgs != null)
                        return subCommandArgs;

                    return Collections.emptyList();
                }
            }
        }

        return Collections.emptyList();
    }


}
