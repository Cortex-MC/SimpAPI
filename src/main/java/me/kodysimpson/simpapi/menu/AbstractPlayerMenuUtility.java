package me.kodysimpson.simpapi.menu;

/*
Companion class to all menus. This is needed to pass information across the entire
 menu system no matter how many inventories are opened or closed.

 Each player has one of these objects, and only one.
 */

import org.bukkit.entity.Player;

public abstract class AbstractPlayerMenuUtility {

    private Player owner;

    public AbstractPlayerMenuUtility(Player p) {
        this.owner = p;
    }

    public Player getOwner() {
        return owner;
    }
}

