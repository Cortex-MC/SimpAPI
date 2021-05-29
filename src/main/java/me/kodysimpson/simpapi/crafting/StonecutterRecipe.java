package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.StonecuttingRecipe;

/**
 * Recipe for a Stonecutter
 */
public class StonecutterRecipe extends Recipe {
    @Override
    public String getType() { return "Stonecutter"; }

    private Material input;

    public StonecutterRecipe(ItemStack result, Material input, ItemStack base, ItemStack addition) {
        this.result = result;
        this.input = input;
    }

    /**
     * Creates a Bukkit recipe
     * @param id Recipe ID
     * @return Bukkit recipe
     */
    public StonecuttingRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        return new StonecuttingRecipe(key, result, input);
    }
}
