package io.myzticbean.mcdevtools.log;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

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
}
