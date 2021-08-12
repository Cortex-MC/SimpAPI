package me.kodysimpson.simpapi.config;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ConfigManager {

    enum FileType{
        JSON, YAML
    }

    public static <T> T loadConfig(JavaPlugin plugin, Class<T> configClass, String fileName, FileType fileType) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        T config = null;
        File messagesConfigFile = null;
        ObjectMapper mapper = null;

        if (fileType == FileType.YAML){
            messagesConfigFile = new File(plugin.getDataFolder(), fileName + ".yml");
            mapper = new ObjectMapper(new YAMLFactory());
        }else if(fileType == FileType.JSON){
            messagesConfigFile = new File(plugin.getDataFolder(), fileName + ".json");
            mapper = new ObjectMapper(new JsonFactory());
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
                return mapper.readValue(messagesConfigFile, configClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return config;
    }

}
