package me.kodysimpson.simpapi.menu;

import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Used to interface with the Menu Manager API
 */
public class MenuManager {

    //each player will be assigned their own PlayerMenuUtility object
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static boolean isSetup = false;

    private static void registerMenuListener(Server server, Plugin plugin) {
        boolean isAlreadyRegistered = false;
        for (RegisteredListener rl : InventoryClickEvent.getHandlerList().getRegisteredListeners()) {
            System.out.println(rl.getListener().getClass().getSimpleName());
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }

        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }
    }

    /**
     * @param server The instance of your server. Provide by calling getServer()
     * @param plugin The instance of the plugin using this API. Can provide in plugin class by passing this keyword
     */
    public static void setup(Server server, Plugin plugin) {
        System.out.println("MENU MANAGER HAS BEEN SETUP");

        registerMenuListener(server, plugin);
        isSetup = true;
    }

    /**
     * @param menuClass The class reference of the Menu you want to open for a player
     * @param player    The player to open the menu for
     * @throws MenuManagerNotSetupException Thrown if the setup() method has not been called and used properly
     */
    public static void openMenu(Class<? extends Menu> menuClass, Player player) throws MenuManagerException, MenuManagerNotSetupException {
        try {
            menuClass.getConstructor(PlayerMenuUtility.class).newInstance(getPlayerMenuUtility(player)).open();
        } catch (InstantiationException e) {
            throw new MenuManagerException("Failed to instantiate menu class", e);
        } catch (IllegalAccessException e) {
            throw new MenuManagerException("Illegal access while trying to instantiate menu class", e);
        } catch (InvocationTargetException e) {
            throw new MenuManagerException("An error occurred while trying to invoke the menu class constructor", e);
        } catch (NoSuchMethodException e) {
            throw new MenuManagerException("The menu class constructor could not be found", e);
        }
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) throws MenuManagerNotSetupException {
        if (!isSetup) {
            throw new MenuManagerNotSetupException();
        }

        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a pmu "saved" for them

            //Construct PMU
            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }

}
