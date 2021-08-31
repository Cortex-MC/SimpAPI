package me.kodysimpson.simpapi.command;

import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An Advanced Command-Managing system allowing you to register subcommands under a core command.
 */
public class CommandManager {

    /**
     * @param plugin             An instance of your plugin that is using this API. If called within plugin main class, provide this keyword
     * @param commandName        The name of the command
     * @param commandDescription Description of command as would put it in plugin.yml
     * @param commandUsage       Usage of command as would put it in plugin.yml
     * @param aliases            A String list of aliases(or nothing for overloaded method)
     * @param subcommands        Class reference to each SubCommand you create for this core command
     */
    @SafeVarargs
    public static void createCoreCommand(JavaPlugin plugin, String commandName,
                                         String commandDescription,
                                         String commandUsage,
                                         @Nullable CommandList commandList,
                                         List<String> aliases,
                                         Class<? extends SubCommand>... subcommands) throws NoSuchFieldException, IllegalAccessException {

        ArrayList<SubCommand> commands = new ArrayList<>();

        Arrays.stream(subcommands).map(subcommand -> {
            try {
                Constructor<? extends SubCommand> constructor = subcommand.getConstructor();
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(commands::add);

        //THANK YOU OZZYMAR <3 YOU'RE THE HOMIE
        Field commandField = plugin.getServer().getClass().getDeclaredField("commandMap");
        commandField.setAccessible(true);
        CommandMap commandMap = (CommandMap) commandField.get(plugin.getServer());
        commandMap.register(commandName, new CoreCommand(commandName, commandDescription, commandUsage, commandList, aliases, commands));
    }


    /**
     * @param plugin             An instance of your plugin that is using this API. If called within plugin main class, provide this keyword
     * @param commandName        The name of the command
     * @param commandDescription Description of command as would put it in plugin.yml
     * @param commandUsage       Usage of command as would put it in plugin.yml
     * @param subcommands        Class reference to each SubCommand you create for this core command
     */
    @SafeVarargs
    public static void createCoreCommand(JavaPlugin plugin, String commandName,
                                         String commandDescription,
                                         String commandUsage,
                                         @Nullable CommandList commandList,
                                         Class<? extends SubCommand>... subcommands) throws NoSuchFieldException, IllegalAccessException {
        createCoreCommand(plugin, commandName, commandDescription, commandUsage, commandList, Collections.singletonList(""), subcommands);
    }

}
