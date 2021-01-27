package me.kodysimpson.simpapi.command;

import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandManager {

    public static void createCoreCommand(JavaPlugin plugin, String command, Class<? extends SubCommand>... subcommands){

        ArrayList<SubCommand> commands = new ArrayList<>();

        Arrays.stream(subcommands).map(subcommand -> {
            try{
                Constructor constructor = subcommand.getConstructor();
                return constructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).forEach(o -> commands.add((SubCommand) o));

        plugin.getCommand(command).setExecutor(new CoreCommand(commands));

    }

}
