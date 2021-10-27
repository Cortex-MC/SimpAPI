package me.kodysimpson.simpapi.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ConfigManager {

    private final PrettyPrinter prettyPrinter = null;

    /**
     * @param plugin      An instance of your plugin
     * @param configClass A class reference to a Java class annotated with @Config containing your config values
     * @param <T>         The generic type of the Config class
     * @return A new instance of the Config class to be used throughout your plugin
     */
    public static <T> T loadConfig(JavaPlugin plugin, Class<T> configClass) {

        T config = null;

        Config configAnnotation = configClass.getAnnotation(Config.class);

        if (configAnnotation == null) {
            plugin.getLogger().severe("The provided Configuration Java class was not annotated properly with @Config from SimpAPI. Therefore the config could not be loaded.");
            plugin.getPluginLoader().disablePlugin(plugin);
        } else {

            String fileName = configAnnotation.fileName();
            FileType fileType = configAnnotation.fileType();

            File messagesConfigFile = getConfigFile(plugin, fileName, fileType);
            ObjectMapper mapper = getObjectMapper(fileType);

            if (!messagesConfigFile.exists()) {
                try {
                    config = configClass.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }

                try {
                    mapper.writeValue(messagesConfigFile, config);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //since it exists already, load the values into the object
                try {
                    plugin.getLogger().info("Attempting to read " + fileName + " config file.");
                    T t = mapper.readValue(messagesConfigFile, configClass);
                    saveConfig(plugin, t);
                    return t;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return config;
    }

    /**
     * @param plugin       Your plugin class
     * @param configObject An instance of your Config class to use to save the contents of it to file
     */
    public static void saveConfig(JavaPlugin plugin, Object configObject) {

        Config configAnnotation = configObject.getClass().getAnnotation(Config.class);
        if (configAnnotation == null) {
            plugin.getLogger().severe("The provided Configuration Java class was not annotated properly with @Config from SimpAPI. Therefore the config could not be saved.");
            plugin.getPluginLoader().disablePlugin(plugin);
        } else {

            String fileName = configAnnotation.fileName();
            FileType fileType = configAnnotation.fileType();

            plugin.getLogger().info("Attempting to save " + fileName + " config file.");

            File messagesConfigFile = getConfigFile(plugin, fileName, fileType);
            ObjectMapper mapper = getObjectMapper(fileType);

            try {
                mapper.writeValue(messagesConfigFile, configObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static File getConfigFile(JavaPlugin plugin, String fileName, FileType fileType) {
        switch (fileType) {
            case YAML:
                return new File(plugin.getDataFolder(), fileName + ".yml");
            case JSON:
                return new File(plugin.getDataFolder(), fileName + ".json");
            default:
                return null;
        }
    }

    private static ObjectMapper getObjectMapper(FileType fileType) {
        if (fileType == FileType.YAML) {
            return new ObjectMapper(new YAMLFactory()).configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(JsonParser.Feature.IGNORE_UNDEFINED, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        } else {
            return new ObjectMapper(new JsonFactory()).configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(JsonParser.Feature.IGNORE_UNDEFINED, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        }
    }

    public enum FileType {
        JSON, YAML
    }

}
