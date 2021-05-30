package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Recipe for a Furnace
 */
public class FurnaceRecipe extends Recipe {
    @Override
    public String getType() { return "Furnace"; }

    private final Material input;
    private final float experience;
    private final int cookingTime;

    public FurnaceRecipe(ItemStack result, Material input, float experience, int cookingTime) {
        this.result = result;
        this.input = input;
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    /**
     * Creates a Bukkit recipe
     * @param id Recipe ID
     * @return Bukkit recipe
     */
    public org.bukkit.inventory.FurnaceRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        return new org.bukkit.inventory.FurnaceRecipe(key, result, input, experience, cookingTime);
    }
}
