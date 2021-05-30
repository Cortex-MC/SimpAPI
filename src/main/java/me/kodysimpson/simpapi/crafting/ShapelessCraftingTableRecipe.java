package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.*;

/**
 * Shapeless Recipe for a Crafting Table
 */
public class ShapelessCraftingTableRecipe extends Recipe {
    @Override
    public String getType() { return "ShapelessCraftingTable"; }

    private final List<Material> items;

    public ShapelessCraftingTableRecipe(ItemStack result, Material... items) {
        this.result = result;
        this.items = Arrays.asList(items);
    }

    /**
     * Returns a Bukkit recipe
     * @param id Recipe ID
     * @return Bukkit recipe
     */
    public ShapelessRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        ShapelessRecipe recipe = new ShapelessRecipe(key, result);

        items.forEach(recipe::addIngredient);

        return recipe;
    }

    public List<Material> getItems() {
        return new ArrayList<>(items);
    }
}
