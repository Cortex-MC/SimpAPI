package me.kodysimpson.simpapi.crafting;

import com.google.common.base.MoreObjects;
import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingRecipe;
import org.bukkit.inventory.SmokingRecipe;

import java.util.Objects;

/**
 * Recipe for a Smithing Table
 */
public class SmithingTableRecipe extends Recipe {
    @Override
    public String getType() { return "SmithingTable"; }

    private final ItemStack base;
    private final ItemStack addition;

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

    public ItemStack getBase() {
        return base;
    }

    public ItemStack getAddition() {
        return addition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmithingTableRecipe that = (SmithingTableRecipe) o;
        return base.equals(that.base) && addition.equals(that.addition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), base, addition);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("base", base)
                .add("addition", addition)
                .toString();
    }
}
