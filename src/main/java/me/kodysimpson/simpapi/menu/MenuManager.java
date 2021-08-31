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
    //private static Class<? extends PlayerMenuUtility> pmuClass;
    private static boolean isSetup = false;
    //private static Class<? extends Menu>[] menus;

    private static void registerMenuListener(Server server, Plugin plugin) {

        boolean isAlreadyRegistered = false;
        for (RegisteredListener rl : InventoryClickEvent.getHandlerList().getRegisteredListeners()) {
            System.out.println(rl.getListener().getClass().getSimpleName());
            if (rl.getListener() instanceof MenuListener) {
                isAlreadyRegistered = true;
                break;
            }
        }

//Dont touch this code -- kody
//        HandlerList.getHandlerLists().stream()
//                .forEach(handler -> {
//
//
//
//                    System.out.println(handler.toString());
////                    System.out.println(Modifier.toString(field.getModifiers()) + " " + field.getName());
//                });

        //System.out.println("erwiwjriwer: " + isAlreadyRegistered);
        if (!isAlreadyRegistered) {
            server.getPluginManager().registerEvents(new MenuListener(), plugin);
        }

    }

//    private static void registerPlayerMenuUtility(Class<? extends PlayerMenuUtility> playerMenuUtilityClass) {
//
//        MenuManager.pmuClass = playerMenuUtilityClass;
//
//    }

    /**
     * @param server The instance of your server. Provide by calling getServer()
     * @param plugin The instance of the plugin using this API. Can provide in plugin class by passing this keyword
     */
    public static void setup(Server server, Plugin plugin) {

        System.out.println("MENU MANAGER HAS BEEN SETUP");

        registerMenuListener(server, plugin);
        //registerPlayerMenuUtility(playerMenuUtilityClass);
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
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            throw new MenuManagerException();
        }
    }

//    /**
//     * @param menuClass The class reference of the Menu you want to open for a player
//     * @param abstractPlayerMenuUtility Usually used to pass in a custom PlayerMenuUtility, for data transfer
//     */
//    public static void openMenu(Class<? extends Menu> menuClass, PlayerMenuUtility pmc) throws MenuManagerException {
//
//        try {
//            menuClass.getConstructor(PlayerMenuUtility.class).newInstance(pmc).open();
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            throw new MenuManagerException();
//        }
//
//    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) throws MenuManagerException, MenuManagerNotSetupException {

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


//    /**
//     * @param p The player to get the custom PlayerMenuUtility from
//     * @param t The class reference of your custom PlayerMenuUtility
//     * @param <T> The custom PlayerMenuUtility Type
//     * @return The PlayerMenuUtility for that player
//     */
//    public static <T> T getPlayerMenuUtility(Player p, Class<T> t) throws MenuManagerException {
//
//        PlayerMenuUtility playerMenuUtility;
//        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a PMU "saved" for them
//
//            try{
//                //Construct PMU using reflection
//                Constructor<? extends PlayerMenuUtility> constructor = pmuClass.getConstructor(Player.class);
//
//                playerMenuUtility = constructor.newInstance(p);
//                playerMenuUtilityMap.put(p, playerMenuUtility);
//            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
//                throw new MenuManagerException();
//            }
//
//            return t.cast(playerMenuUtility);
//        } else {
//            return t.cast(playerMenuUtilityMap.get(p)); //Return the object by using the provided player
//        }
//
//    }
}
