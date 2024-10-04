# Minecraft Developer Tools 
## v${project.version}

**Note:** This is a fork of the amazing [SimpAPI](https://github.com/Cortex-MC/SimpAPI?tab=readme-ov-file), with some essential feature enhancements and code optimizations. 

## Table of Contents:
****
- [Introduction](#introduction)
- [Installation](#installation)
  - [Maven](#maven)
  - [Gradle](#gradle)
- [Usage](#usage)
  - [ColorTranslator - Hexadecimal Color Usage](#colortranslator---hexadecimal-color-usage)
  - [Skull Creator](#skull-creator)
  - [Menu Manager](#menu-manager)
  - [Command Manager](#command-manager)
  - [Auto Register Bukkit Events](#auto-register-bukkit-events)

## Introduction
****
MC Dev Tools can make coding MC Plugins much easier and less painful.
It contains APIs for utilities like *Menu Manager*, *Command Manager*, *ColorTranslator*, and more.

Video Showcase: https://youtu.be/kKaIf7EkCWg

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
    <groupId>com.github.myzticbean</groupId>
    <artifactId>MCDevTools</artifactId>
    <version>${project.version}</version>
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

Groovy/Kotlin:
```groovy
dependencies {
    implementation 'com.github.myzticbean:MCDevTools:${project.version}'
}
```
([Back to top](#table-of-contents))
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

([Back to top](#table-of-contents))
### Skull Creator
****

What the skull creator does should be pretty self-explanatory, here are a few tutorials on how to use it.

#### Creating a skull

 _**Example**_: 1
```java
ItemStack playerSkull = SkullCreator.itemFromUuid(player.getUniqueId());
```

In the example above, we are using the itemFromUuid, this will take in the player's uuid, which will return an ItemStack

 _**Example**_: 2
```java
ItemStack playerSkull = SkullCreator.itemFromUuid(player.getName());
```

Although the example above works, it is not recommended because names are not as accurate as id's.

Keep in mind that when you create a skull,  the default name of said skull will be `{Player name}'s Head`. If you would like to change this to be just the players name, without `'s head`, all you have to do is get the item's meta, and change the display name to the player's display name, simple!:

```java
ItemStack playerSkull = SkullCreator.itemFromUuid(player.getUniqueId());
playerSkull.getItemMeta().setDisplayName(
   player.getDisplayName() 
);
```
**Note:** The skull creator was not created by Kody Simpson, the creator of the [library](https://github.com/deanveloper/SkullCreator/blob/master/src/main/java/dev/dbassett/skullcreator/SkullCreator.java) is [Dean B](https://github.com/deanveloper)

([Back to top](#table-of-contents))
### Menu Manager
****
The Menu Manager is something I came up with a while ago and showed on my Youtube channel, but in the SimpAPI it is much more advanced and has been made much easier for the developers who use it.

**Step One**: Setup and Initializise the MenuManager by calling the Setup method. Do this in the plugin's onEnable method and all you need to do is provide the PlayerMenuUtility you just created.

```java
@Override
public void onEnable() {
    // Plugin startup logic

    //Setup and register the MenuManager. It will take care of the annoying parts.
    MenuManager.setup(getServer(), this);
}
```

**Step Two**: Add Menus. 

```java
public class FreezeMainMenu extends Menu {

    public FreezeMainMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Iceberg";
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public boolean cancelAllClicks() {
        return true;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

        switch (e.getCurrentItem().getType()){
            case PACKED_ICE:

                MenuManager.openMenu(FreezeListMenu.class, playerMenuUtility.getOwner());

                break;
            case LAVA_BUCKET:

                MenuManager.openMenu(MeltListMenu.class, playerMenuUtility.getOwner());

                break;
        }

    }

    @Override
    public void setMenuItems() {

        ItemStack freezePlayer = makeItem(Material.PACKED_ICE, ColorTranslator.translateColorCodes("&b&lFreeze Player"));
        ItemStack meltPlayer = makeItem(Material.LAVA_BUCKET, ColorTranslator.translateColorCodes("&e&lMelt player"));

        inventory.setItem(3, freezePlayer);
        inventory.setItem(5, meltPlayer);

    }
}
```

**How to Open Menus for a Player**: 

This can be called anywhere. Just provide the correct arguments!
```java
MenuManager.openMenu(FreezeListMenu.class, player);
```
Provide the .class of the Menu Class you want to open for the player,
and then provide an instance of the player to open it for.

**Storing Data in the PlayerMenuUtility to be passed between Menus:**
```java
@Override
public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    switch (e.getCurrentItem().getType()){
        case PLAYER_HEAD:

            Player target = Bukkit.getPlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
    
            playerMenuUtility.setData("playerToFreeze", target);
    
            MenuManager.openMenu(ConfirmFreezeMenu.class, playerMenuUtility.getOwner());

            break;
    }
}
```

**Retrieving Data from the PlayerMenuUtility:**
```java
@Override
public void handleMenu(InventoryClickEvent e) throws MenuManagerNotSetupException, MenuManagerException {

    switch (e.getCurrentItem().getType()){
        case GREEN_BANNER:

            Player target = playerMenuUtility.getData("playerToFreeze", Player.class);

            playerMenuUtility.getOwner().closeInventory();
            playerMenuUtility.getOwner().sendMessage(target.getDisplayName() + " has been frozen.");

            IcebergMenuManagerModule.getFrozenPlayers().add(target);

            break;
        case RED_BANNER:
            MenuManager.openMenu(FreezeListMenu.class, playerMenuUtility.getOwner());
            break;
    }
}
```

I recommend also making an Enum to go along with your Menus that correspond to the data you will be storing in the PMC(PlayerMenuUtility), it will make it easier to remember and pass in the keys.
```java
public enum PMUData {
    PLAYER_TO_FREEZE,
    PLAYER_TO_MELT
}
```

These enumerators can be passed into the PMC instead of a String key, they will be converted to a String internally.

([Back to top](#table-of-contents))
### Command Manager
****
*Video*: https://youtu.be/NFYg9Tmk-vo

([Back to top](#table-of-contents))
### Auto Register Bukkit Events
****
Rather than registering each and every event handler in your plugin one by one, just annotate your Event Handler classes with `@RegisterEventHandler`:
```java
@RegisterEventHandler   // Add this annotation on top of your event handler class
public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        event.getPlayer().sendMessage("Hey!");
    }
}
```
In your main class, you can either specify which package to look into for registering all event handlers:
```java
public final class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        EventRegistrar.registerEvents(this, "io.myzticbean.mypluginname.listeners");
        Logger.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger.info("Plugin has been disabled!");
    }
}
```
Or, you can choose to let it search through all of your classes to find event handlers and register them with Bukkit:
```java
public final class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        EventRegistrar.registerEvents(this);
        Logger.info("Plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger.info("Plugin has been disabled!");
    }
}
```
**Note:** This can be a bit performance intensive if your project has large number of classes.

([Back to top](#table-of-contents))