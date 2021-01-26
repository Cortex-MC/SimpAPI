package me.kodysimpson.simpapi.menu;

import me.kodysimpson.simpapi.exceptions.MenuManagerException;
import me.kodysimpson.simpapi.exceptions.MenuManagerNotSetupException;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuManager {

    //each player will be assigned their own PlayerMenuUtility object
    private static HashMap<Player, AbstractPlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static Class<? extends AbstractPlayerMenuUtility> pmuClass;
    private static boolean isSetup = false;
    //private static Class<? extends Menu>[] menus;

    private static void registerMenuListener(Server server, Plugin plugin) {

        server.getPluginManager().registerEvents(new MenuListener(), plugin);

    }

    private static void registerPlayerMenuUtility(Class<? extends AbstractPlayerMenuUtility> playerMenuUtilityClass) {

        MenuManager.pmuClass = playerMenuUtilityClass;

    }

    /**
     * @param server The instance of your server. Provide by calling getServer()
     * @param plugin The instance of the plugin using this API. Can provide in plugin class by passing this keyword
     * @param playerMenuUtilityClass The class reference of your concrete defined PlayerMenuUtility subclass of AbstractPlayerMenuUtility
     */
    public static void setup(Server server, Plugin plugin, Class<? extends AbstractPlayerMenuUtility> playerMenuUtilityClass) {

        registerMenuListener(server, plugin);
        registerPlayerMenuUtility(playerMenuUtilityClass);

        isSetup = true;

    }

    public static void openMenu(Class<? extends Menu> menuClass, Player player) throws MenuManagerException, MenuManagerNotSetupException {

        try {
            menuClass.getConstructor(AbstractPlayerMenuUtility.class).newInstance(getPlayerMenuUtility(player));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MenuManagerException();
        }

    }

    public static void openMenu(Class<? extends Menu> menuClass, AbstractPlayerMenuUtility abstractPlayerMenuUtility) throws MenuManagerException, MenuManagerNotSetupException {

        try {
            menuClass.getConstructor(AbstractPlayerMenuUtility.class).newInstance(abstractPlayerMenuUtility);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new MenuManagerException();
        }

    }

    public static AbstractPlayerMenuUtility getPlayerMenuUtility(Player p) throws MenuManagerException, MenuManagerNotSetupException {

        if (!isSetup){
            throw new MenuManagerNotSetupException();
        }

        AbstractPlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a pmu "saved" for them

            //Construct PMU using reflection
            Constructor<? extends AbstractPlayerMenuUtility> constructor = null;
            try {
                constructor = pmuClass.getConstructor(Player.class);

                playerMenuUtility = constructor.newInstance(p);
                playerMenuUtilityMap.put(p, playerMenuUtility);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new MenuManagerException();
            }

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }
    }


    public static <T> T getPlayerMenuUtility(Player p, Class<T> t) throws MenuManagerException {

        AbstractPlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a lockmenusystem "saved" for them

            try{
                //Construct PMU using reflection
                Constructor<? extends AbstractPlayerMenuUtility> constructor = pmuClass.getConstructor(Player.class);

                playerMenuUtility = constructor.newInstance(p);
                playerMenuUtilityMap.put(p, playerMenuUtility);
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                throw new MenuManagerException();
            }

            return t.cast(playerMenuUtility);
        } else {
            return t.cast(playerMenuUtilityMap.get(p)); //Return the object by using the provided player
        }

    }
}
