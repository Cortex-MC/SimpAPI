package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;

/**
 * Recipe for a Smoker
 */
public class SmokerRecipe extends Recipe {
    @Override
    public String getType() { return "Smoker"; }

    private Material input;
    private float experience;
    private int cookingTime;

    public SmokerRecipe(ItemStack result, Material input, float experience, int cookingTime) {
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
    public SmokingRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        return new SmokingRecipe(key, result, input, experience, cookingTime);
    }
}
