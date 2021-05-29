package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.BlastingRecipe;
import org.bukkit.inventory.ItemStack;

/**
 * Recipe for a Blast Furnace
 */
public class BlastFurnaceRecipe extends Recipe {
    @Override
    public String getType() { return "BlastFurnace"; }

    private Material input;
    private float experience;
    private int cookingTime;

    public BlastFurnaceRecipe(ItemStack result, Material input, float experience, int cookingTime) {
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
    public BlastingRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        return new BlastingRecipe(key, result, input, experience, cookingTime);
    }
}
