package io.myzticbean.mcdevtools.log;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@UtilityClass
public class Logger {

    private static final java.util.logging.Logger bukkitLogger = Bukkit.getLogger();

    public static void info(String message) {
        bukkitLogger.info(message);
    }

    public static void warn(String message) {
        bukkitLogger.warning(message);
    }

    public static void error(String message) {
        bukkitLogger.severe(message);
    }

    public static void error(String message, Exception e) {
        bukkitLogger.severe(message);
        e.printStackTrace();
    }

    public static void info(JavaPlugin plugin, String message) {
        plugin.getLogger().info(message);
    }

    public static void warn(JavaPlugin plugin, String message) {
        plugin.getLogger().warning(message);
    }

    public static void error(JavaPlugin plugin, String message) {
        plugin.getLogger().severe(message);
    }

    public static void error(JavaPlugin plugin, String message, Exception e) {
        plugin.getLogger().severe(message);
        e.printStackTrace();
    }
}
