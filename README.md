# SimpAPI v4.1.2
****
SimpAPI, finally a good API that can make coding MC Plugins much easier and less painful.
This API includes all of my primary utilities like *Menu Manager*, *Command Manager*, *ColorTranslator*, and more.

Video Showcase: https://youtu.be/kKaIf7EkCWg

JavaDocs: https://kodysimpson.github.io/SimpAPI/index.html

## Installation

### Maven

#### Repository
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
#### Dependency
```xml
<dependency>
    <groupId>com.github.KodySimpson</groupId>
    <artifactId>SimpAPI</artifactId>
    <version>4.1.2</version>
</dependency>
```

### Gradle

#### Repository

Groovy:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Kotlin:
```kotlin
repositories {
    maven { url = uri("https://jitpack.io") }
}
```
#### Dependency

Groovy:
```groovy
dependencies {
    implementation 'com.github.KodySimpson:SimpAPI:4.1.2'
}
```

Kotlin:
```kotlin
dependencies {
    implementation("com.github.KodySimpson:SimpAPI:4.1.2")
}
```

## Usage

### ColorTranslator - Hexadecimal Color Usage
****
To produce MC text with hexadecimal colors in it, use the translateColorCodes() method from the ColorTranslator class.

Example:
```java
ColorTranslator.translateColorCodes("&#baebabThis is &c&lcool")
```

As you can see, all you need to do is provide the color code after an & as you would with a normal color code.

There is also a method for TextComponents called translateColorCodesToTextComponent() which works the same.


### Menu Manager
****
The Menu Manager is something I came up with a while ago and showed on my Youtube channel, but in the SimpAPI it is much more advanced and has been made much easier for the developers who use it.

**Step One**: Create your PlayerMenuUtility.
The PlayerMenuUtility is a class system that allows you to pass data across your menu inventories, avoiding situations where you pass them by Items or other tricky methods.
All you need to do is extend the AbstractPlayerMenuUtility and create the variables for the data you will be passing.

Example:
```java
public class PlayerMenuUtility extends AbstractPlayerMenuUtility {

    //Define the data you want passed between menus
    private Player playerToFreeze;
    private Player playerToMelt;

    //Make sure to call the super constructor
    public PlayerMenuUtility(Player p) {
        super(p);
    }

    //define the methods to access the data
    public Player getPlayerToFreeze() {
        return playerToFreeze;
    }

    public void setPlayerToFreeze(Player playerToFreeze) {
        this.playerToFreeze = playerToFreeze;
    }

    public Player getPlayerToMelt() {
        return playerToMelt;
    }

    public void setPlayerToMelt(Player playerToMelt) {
        this.playerToMelt = playerToMelt;
    }
}
```

**Step Two**: Setup and Initializise the MenuManager by calling the Setup method. Do this in the plugin's onEnable method and all you need to do is provide the PlayerMenuUtility you just created.

```java
    @Override
    public void onEnable() {
        // Plugin startup logic

        //Setup and register the MenuManager. It will take care of the annoying parts.
        MenuManager.setup(getServer(), this, PlayerMenuUtility.class);

    }
```

**Step Three**: Add Menus. 

```java
public class FreezeMainMenu extends Menu {

    //Make sure to add this constructor that calls the Menu superconstructor
    public FreezeMainMenu(AbstractPlayerMenuUtility pmu) {
        super(pmu);
    }

    //Override these other methods to give the Menu it's properties and functionality
    @Override
    public String getMenuName() {
        return "Iceberg";
    }

    //Max is 54 slots
    @Override
    public int getSlots() {
        return 9;
    }

    //Return true for this if you want it so that the player cannot
    //move any items in the menu
    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    //This method is used to handle the clicks of this Menu,
    //you can see what items they clicked on and then handle it
    //accordingly.
    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        switch (e.getCurrentItem().getType()){
            case PACKED_ICE:

                MenuManager.openMenu(FreezeListMenu.class, pmu);

                break;
            case LAVA_BUCKET:

                MenuManager.openMenu(MeltListMenu.class, pmu);

                break;
        }

    }

    //This is how you decide what items go into the menu and where.
    //inventory is obtained from the Menu superclass.
    @Override
    public void setMenuItems() {

        ItemStack freezePlayer = makeItem(Material.PACKED_ICE, ColorTranslator.translateColorCodes("&b&lFreeze Player"));
        ItemStack meltPlayer = makeItem(Material.LAVA_BUCKET, ColorTranslator.translateColorCodes("&e&lMelt player"));

        inventory.setItem(3, freezePlayer);
        inventory.setItem(5, meltPlayer);

    }
}
```

**How to Open Menus for a Player(Without Data Transfer)**: 

This can be called anywhere. Just provide the correct arguments!
```java
MenuManager.openMenu(FreezeListMenu.class, pmu);
```
Provide the .class of the Menu Class you want to open for the player,
and then provide an instance of PlayerMenuUtility. If you are already
within another Menu, pmu is provided by the superclass. If you are outside
of that, you will need to obtain a PlayerMenuUtility instance.

Outside of a Menu Example:

```java
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){

            Player p = (Player) sender;

            try {
                MenuManager.openMenu(FreezeMainMenu.class, MenuManager.getPlayerMenuUtility(p));
            } catch (MenuManagerException | MenuManagerNotSetupException e) {
                e.printStackTrace();
            }

        }

        return true;
    }
```
As you can see, the PlayerMenuUtility can be obtained from calling MenuManager.getPlayerMenuUtility()
and passing in the player you want to open it for.

**How to Open Menus for a Player(With Data Transfer)**: 

```java
    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        //Since you are doing a transfer of data, cast the AbstractPlayerMenuUtility into the PlayerMenuUtility you created.
        PlayerMenuUtility playerMenuUtility = (PlayerMenuUtility) pmu;

        switch (e.getCurrentItem().getType()){
            case PLAYER_HEAD:

                Player target = Bukkit.getPlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

                playerMenuUtility.setPlayerToFreeze(target);

                MenuManager.openMenu(ConfirmFreezeMenu.class, playerMenuUtility);

                break;
        }

    }
```

You only have to convert an AbstractPlayerMenuUtility into yours when you need to transfer data into the new menu being opened.

To transfer data, simply call and use the setter methods you defined like done above.

## Command Manager
*Video*: https://youtu.be/NFYg9Tmk-vo

## MySQL
**Connecting using MySQLConnector**
```JAVA
public class MySQLExample {
    
    private MySQLConnector connector;

    public void connectToDatabase() {
        String host = "bla";
        String port = "bla";
        String database_name = "bla";
        String username = "bla";
        String password = "bla";
        connector = new MySQLConnector(host, port, database_name, username, password);

        try {
            connector.connect();
            System.out.println("Connected!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Could not connect to MySQL Database");
        }
    }
    
    // This method represents your onDisable method
    // in your main class
    @Override
    public void onDisable() {
        connector.disconnect();
    }
}
```

**Creating a MySQL Table using the MySQLTable interface**
```JAVA
public class MySQLExample implements MySQLTable {

    private Connection connection;
    public MySQLExample() {
        connection = plugin.getMySQL().getConnection();
    }

    @Override
    public String getTable() {
        return "player_data"; // Name of the table
    }

    @Override
    public void createTable() {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + getTable() + " (UUID VARCHAR(100), NICKNAME VARCHAR(100), PRIMARY KEY (UUID) )");

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // etc...
}
```

## ItemBuilder
**Example of using ItemBuilder**
```JAVA
    public void item(Player player) {
        ItemStack item = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .setName("&c&lSuper &b&lPickaxe")
                .addLoreLine("&7This pickaxe is forged from")
                .addLoreLine("&7the deep depths of hell...")
                .build();
        
        player.getInventory().addItem(item);
    }
```  

## Text Components
**Example of using TextComponents***
```JAVA
    public void send(Player player) {
        
        new TextComponentBuilder("&6&lCLICK TO PAY RESPECTS]")
                .setClickEvent(ClickEvent.Action.RUN_COMMAND, "/f")
                .setHoverText("&aClick to pay respects")
                .send(player);
        
    }
```

## Config Utils
**Example of using ConfigUtils**
```JAVA
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        FileConfiguration configuration = new ConfigUtils(MainPlugin.getInstance(), "messages").getConfig();
        String welcome = configuration.getString("welcome-message");
        event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', welcome));

    }
```

## Util Player
**Example of using UtilPlayer**
```JAVA
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UtilPlayer.reset(event.getPlayer());
        // Once the player joins the server
        // clear their inventory
        // and teleport them to spawn
        event.getPlayer().teleport(Spawn.getLocation());
    }
```
