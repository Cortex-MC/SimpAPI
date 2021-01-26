package me.kodysimpson.simpapi.menu;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MenuManager {

    //each player will be assigned their own PlayerMenuUtility object
    private static HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    private static Class<PlayerMenuUtility> pmuClass;

    private static void registerMenuListener(Server server, Plugin plugin){

        server.getPluginManager().registerEvents(new MenuListener(), plugin);

    }

    private static void registerPlayerMenuUtility(Class<PlayerMenuUtility> playerMenuUtilityClass){

        MenuManager.pmuClass = playerMenuUtilityClass;

    }

    public static void setup(Server server, Plugin plugin, Class<PlayerMenuUtility> playerMenuUtilityClass){

        registerMenuListener(server, plugin);
        registerPlayerMenuUtility(playerMenuUtilityClass);

    }

    public static PlayerMenuUtility grabPlayerMenuUtility(Player p) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) { //See if the player has a lockmenusystem "saved" for them

            //Construct PMU using reflection
            Constructor<PlayerMenuUtility> constructor = pmuClass.getConstructor(Player.class);

            playerMenuUtility = constructor.newInstance(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p); //Return the object by using the provided player
        }

    }

}
