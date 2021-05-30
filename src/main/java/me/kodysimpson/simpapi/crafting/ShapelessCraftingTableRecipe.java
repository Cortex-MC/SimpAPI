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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShapelessCraftingTableRecipe that = (ShapelessCraftingTableRecipe) o;
        return items.equals(that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), items);
    }

    @Override
    public String toString() {
        return "ShapelessCraftingTableRecipe{" +
                "items=" + items +
                "} " + super.toString();
    }
}
