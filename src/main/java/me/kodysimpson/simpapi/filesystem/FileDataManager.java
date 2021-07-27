package me.kodysimpson.simpapi.filesystem;

public class FileDataManager {

  private final Plugin PLUGIN;

    private final String FILENAME;

    private final File FOLDER;

    private FileConfiguration config;

    private File configFile;
  
    //Diffrent ways to save files

    public FileDataManager(String filename, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = CoreMain.getInstance();
        this.FOLDER = this.PLUGIN.getDataFolder();
        this.config = null;
        this.configFile = null;
        if (saveDefault)
            saveDefaultConfig();
        reload();
    }

    public FileDataManager(String folder, String filename, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = CoreMain.getInstance();
        this.FOLDER = new File(this.PLUGIN.getDataFolder() + File.separator + folder);
        this.config = null;
        this.configFile = null;
        if (saveDefault)
            saveDefaultConfig();
        reload();
    }

    public FileDataManager(File folder, String filename, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = CoreMain.getInstance();
        this.FOLDER = folder;
        this.config = null;
        this.configFile = null;
        if (saveDefault)
            saveDefaultConfig();
        reload();
    }

    public FileDataManager(File folder, String filename, Plugin pl, Boolean saveDefault) {
        if (!filename.endsWith(".yml"))
            filename = filename + ".yml";
        this.FILENAME = filename;
        this.PLUGIN = pl;
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

    //Reloads files
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
  
  
    //Saves premade config files
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

}
