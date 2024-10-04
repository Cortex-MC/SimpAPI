package io.myzticbean.mcdevtools.events.processor;

import io.myzticbean.mcdevtools.events.RegisterEventHandler;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@UtilityClass
public class EventRegistrar {

    /**
     * Gets all classes in the plugin's package and its subpackages, then checks each class
     * for the {@link RegisterEventHandler} annotation and if it implements the {@link Listener} interface.
     * If both conditions are met, it instantiates the class and registers it as an event listener.
     * <p><b>Note: </b>This can be performance intensive in a very large project.
     * Use <code>registerEvents(JavaPlugin plugin, String packageName)</code> instead.</p>
     * @param plugin {@link JavaPlugin}
     */
    public static void registerEvents(JavaPlugin plugin) {
        String packageName = plugin.getClass().getPackage().getName();
        registerEvents(plugin, packageName);
    }

    /**
     *  Gets all classes in the specified package and its subpackages, then checks each class
     *  for the {@link RegisterEventHandler} annotation and if it implements the {@link Listener} interface.
     *  If both conditions are met, it instantiates the class and registers it as an event listener.
     * @param plugin {@link JavaPlugin}
     * @param packageName Package name to check
     */
    public static void registerEvents(JavaPlugin plugin, String packageName) {
        List<Class<?>> classes = getClasses(plugin, packageName);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(RegisterEventHandler.class) && Listener.class.isAssignableFrom(clazz)) {
                try {
                    Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                    Bukkit.getPluginManager().registerEvents(listener, plugin);
                    plugin.getLogger().info("Registered events for " + clazz.getName());
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to register events for " + clazz.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private static List<Class<?>> getClasses(JavaPlugin plugin, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        URL resource = plugin.getClass().getClassLoader().getResource(path);
        if (resource == null) {
            plugin.getLogger().warning("Could not find resource for package: " + packageName);
            return classes;
        }
        // plugin.getLogger().info("Resource URL: " + resource.toString());
        URI uri;
        try {
            uri = resource.toURI();
        } catch (URISyntaxException e) {
            plugin.getLogger().warning("Invalid URI for resource: " + e.getMessage());
            return classes;
        }
        try (FileSystem fileSystem = (uri.getScheme().equals("jar") ? FileSystems.newFileSystem(uri, Collections.emptyMap()) : null)) {
            Path myPath = Paths.get(uri);
            classes = findClasses(plugin, myPath, packageName);
        } catch (IOException e) {
            plugin.getLogger().warning("Error reading classes: " + e.getMessage());
        }
        return classes;
    }

    private static List<Class<?>> findClasses(JavaPlugin plugin, Path directory, String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(directory, 1)) {
            walk.forEach(path -> {
                String fileName = path.getFileName().toString();
                if (fileName.endsWith(".class")) {
                    String className = packageName + '.' + fileName.substring(0, fileName.length() - 6);
                    try {
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                        // plugin.getLogger().info("Found class: " + className);
                    } catch (ClassNotFoundException e) {
                        plugin.getLogger().warning("Failed to load class: " + className);
                    }
                } else if (Files.isDirectory(path) && !path.equals(directory)) {
                    classes.addAll(findClasses(plugin, path, packageName + "." + fileName));
                }
            });
        } catch (IOException e) {
            plugin.getLogger().warning("Error walking directory: " + e.getMessage());
        }
        return classes;
    }
}
