package me.kodysimpson.simpapi.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ConfigManager {

    public enum FileType{
        JSON, YAML
    }

    public static <T> T loadConfig(JavaPlugin plugin, Class<T> configClass, String fileName, ConfigManager.FileType fileType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        T config = null;
        File messagesConfigFile = null;
        ObjectMapper mapper = null;

        String fileNameA = configClass.getAnnotation(Config.class).fileName();
        System.out.println("Grabbed filename: " + fileNameA);

        if (fileType == ConfigManager.FileType.YAML){
            messagesConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
            mapper = new ObjectMapper(new YAMLFactory()).configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(JsonParser.Feature.IGNORE_UNDEFINED, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }else if(fileType == ConfigManager.FileType.JSON){
            messagesConfigFile = new File(plugin.getDataFolder(), fileName + ".json");
            mapper = new ObjectMapper(new JsonFactory()).configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(JsonParser.Feature.IGNORE_UNDEFINED, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        if (!messagesConfigFile.exists()){
            config = configClass.getConstructor().newInstance();

            try {
                mapper.writeValue(messagesConfigFile, config);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //since it exists already, load the values into the object
            try {
                T t = mapper.readValue(messagesConfigFile, configClass);
                saveConfig(plugin, t, fileName, ConfigManager.FileType.YAML);
                return t;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

    public static void saveConfig(JavaPlugin plugin, Object configObject, String fileName, ConfigManager.FileType fileType) {

        File messagesConfigFile = null;
        ObjectMapper mapper = null;
        if (fileType == ConfigManager.FileType.YAML){
            messagesConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
            mapper = new ObjectMapper(new YAMLFactory()).configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(JsonParser.Feature.IGNORE_UNDEFINED, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }else if(fileType == ConfigManager.FileType.JSON){
            messagesConfigFile = new File(plugin.getDataFolder(), fileName + ".json");
            mapper = new ObjectMapper(new JsonFactory()).configure(JsonGenerator.Feature.IGNORE_UNKNOWN, true).configure(JsonParser.Feature.IGNORE_UNDEFINED, true).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        try {
            mapper.writeValue(messagesConfigFile, configObject);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
