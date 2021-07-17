# SimpAPI v4.1.6
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
    <version>4.1.6</version>
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
    implementation 'com.github.KodySimpson:SimpAPI:4.1.6'
}
```

Kotlin:
```kotlin
dependencies {
    implementation("com.github.KodySimpson:SimpAPI:4.1.6")
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

## Command Manager
*Video*: https://youtu.be/NFYg9Tmk-vo