package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Recipe for a Campfire
 */
public class CampfireRecipe extends Recipe {
    @Override
    public String getType() { return "Campfire"; }

    private final Material input;
    private final float experience;
    private final int cookingTime;

    public CampfireRecipe(ItemStack result, Material input, float experience, int cookingTime) {
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
    public org.bukkit.inventory.CampfireRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        return new org.bukkit.inventory.CampfireRecipe(key, result, input, experience, cookingTime);
    }
}
