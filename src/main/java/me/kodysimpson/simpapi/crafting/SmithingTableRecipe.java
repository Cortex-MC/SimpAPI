package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;

/**
 * Recipe for a Smithing Table
 */
public class SmithingTableRecipe extends Recipe {
    @Override
    public String getType() { return "SmithingTable"; }

    private ItemStack base;
    private ItemStack addition;

    public SmithingTableRecipe(ItemStack result, ItemStack base, ItemStack addition) {
        this.result = result;
        this.base = base;
        this.addition = addition;
    }

    /**
     * Creates a Bukkit recipe
     * @param id Recipe ID
     * @return Bukkit recipe
     */
    public SmithingRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        return new SmithingRecipe(key, result, new RecipeChoice.ExactChoice(base), new RecipeChoice.ExactChoice(addition));
    }
}
