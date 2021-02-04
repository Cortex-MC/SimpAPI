package me.kodysimpson.simpapi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a main command that has subcommands within it. Like /stafftools freeze is /[corecommand] [subcommand]
 */
class CoreCommand extends Command {

    private ArrayList<SubCommand> subcommands;

    public CoreCommand(String name, String description, String usageMessage, List<String> aliases, ArrayList<SubCommand> subCommands){
        super(name, description, usageMessage, aliases);
        //Get the subcommands so we can access them in the command manager class(here)
        this.subcommands = subCommands;
    }

    public ArrayList<SubCommand> getSubCommands(){
        return subcommands;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {

        if (sender instanceof Player){
            Player p = (Player) sender;

            if (args.length > 0){
                for (int i = 0; i < getSubCommands().size(); i++){
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                        getSubCommands().get(i).perform(p, args);
                    }
                }
            }else {
                p.sendMessage("--------------------------------");
                for (int i = 0; i < subcommands.size(); i++){
                    p.sendMessage(subcommands.get(i).getSyntax() + " - " + subcommands.get(i).getDescription());
                }
                p.sendMessage("--------------------------------");
            }

        }

        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (args.length == 1){ //prank <subcommand> <args>
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubCommands().size(); i++){
                subcommandsArguments.add(getSubCommands().get(i).getName());
            }

            return subcommandsArguments;
        }else if(args.length >= 2){
            for (int i = 0; i < getSubCommands().size(); i++){
                if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())){
                    return getSubCommands().get(i).getSubcommandArguments((Player) sender, args);
                }
            }
        }
        return null;
    }


}
