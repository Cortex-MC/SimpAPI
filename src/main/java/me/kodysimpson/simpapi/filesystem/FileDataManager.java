import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public class FileDataManager {

    private final JavaPlugin PLUGIN;

    private final String FILENAME;

    private final File FOLDER;

    private FileConfiguration config;

    private File configFile;

    public FileDataManager(JavaPlugin mainClass, String filename, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = mainClass;
        this.FOLDER = this.PLUGIN.getDataFolder();
        this.config = null;
        this.configFile = null;
        if (saveDefault)
            saveDefaultConfig();
        reload();
    }

    public FileDataManager(JavaPlugin mainClass, String folder, String filename, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = mainClass;
        this.FOLDER = new File(this.PLUGIN.getDataFolder() + File.separator + folder);
        this.config = null;
        this.configFile = null;
        if (saveDefault)
            saveDefaultConfig();
        reload();
    }

    public FileDataManager(JavaPlugin mainClass, File folder, String filename, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = mainClass;
        this.FOLDER = folder;
        this.config = null;
        this.configFile = null;
        if (saveDefault)
            saveDefaultConfig();
        reload();
    }

    public FileConfiguration getConfig() {
        if (this.config == null)
            reload();
        return this.config;
    }

    public void reload() {
        if (!this.FOLDER.exists())
            try {
                if (this.FOLDER.mkdir()) {
                    this.PLUGIN.getLogger().log(Level.INFO, "Folder " + this.FOLDER.getName() + " created.");
                } else {
                    this.PLUGIN.getLogger().log(Level.WARNING, "Unable to create folder " + this.FOLDER.getName() + ".");
                }
            } catch (Exception ignored) {}
        this.configFile = new File(this.FOLDER, this.FILENAME);
        if (!this.configFile.exists())
            try {
                this.configFile.createNewFile();
            } catch (IOException ignored) {}
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveDefaultConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.PLUGIN.getDataFolder(), this.FILENAME);
        if (!this.configFile.exists())
            this.PLUGIN.saveResource(this.FILENAME, false);
    }

    public void save() {
        if (this.config == null || this.configFile == null)
            return;
        try {
            getConfig().save(this.configFile);
        } catch (IOException ex) {
            this.PLUGIN.getLogger().log(Level.WARNING, "Could not save config to " + this.configFile.getName(), ex);
        }
    }

    public void set(String path, Object o) {
        getConfig().set(path, o);
    }

    public void setExists(String path, Object o) {
        if (!getConfig().contains(path)) {
            getConfig().set(path, o);
            save();
        }
    }

    public void setLocation(String path, Location l) {
        getConfig().set(path + ".w", Objects.requireNonNull(l.getWorld()).getName());
        getConfig().set(path + ".x", l.getX());
        getConfig().set(path + ".y", l.getY());
        getConfig().set(path + ".z", l.getZ());
        getConfig().set(path + ".yaw", l.getYaw());
        getConfig().set(path + ".pitch", l.getPitch());
        save();
    }

    public Location getLocation(String path) {
        return new Location(Bukkit.getWorld(Objects.requireNonNull(getConfig().getString(path + ".w"))), getConfig().getDouble(path + ".x"), getConfig().getDouble(path + ".y"), getConfig().getDouble(path + ".z"), Float.parseFloat(String.valueOf(getConfig().getDouble(path + ".yaw"))), Float.parseFloat(String.valueOf(getConfig().getDouble(path + ".pitch"))));
    }
}
