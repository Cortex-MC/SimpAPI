package me.kodysimpson.simpapi.crafting;

import org.bukkit.inventory.ItemStack;

public abstract class Recipe {
    protected ItemStack result;

    public ItemStack getResult() { return result.clone(); }
    abstract public String getType();
}
