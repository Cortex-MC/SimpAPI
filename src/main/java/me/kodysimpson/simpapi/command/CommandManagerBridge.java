package me.kodysimpson.simpapi.command;

public class CommandManagerBridge {

    private static final CommandManager commandManager = new CommandManager();

    public static CommandManager getCommandManager(){
        return commandManager;
    }

    public static void addSubCommand(SubCommand subCommand){
        commandManager.getSubCommands().add(subCommand);
    }

}
