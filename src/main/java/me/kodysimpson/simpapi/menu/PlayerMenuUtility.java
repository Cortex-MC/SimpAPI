package me.kodysimpson.simpapi.menu;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerMenuUtility {

    private final Player owner;
    private final HashMap<String, Object> dataMap = new HashMap<>();

    public PlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }

    /**
     * @param identifier A key to store the data by
     * @param data The data itself to be stored
     */
    public void setData(String identifier, Object data){
        this.dataMap.put(identifier, data);
    }

    /**
     * @param identifier The key for the data stored in the PMC
     * @return The retrieved value or null if not found
     */
    public Object getData(String identifier){
        return this.dataMap.get(identifier);
    }

}

