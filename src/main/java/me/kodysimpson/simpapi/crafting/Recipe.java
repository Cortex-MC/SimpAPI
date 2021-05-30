package me.kodysimpson.simpapi.crafting;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public abstract class Recipe {
    protected ItemStack result;

    public ItemStack getResult() { return result.clone(); }
    abstract public String getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return getResult().equals(recipe.getResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResult());
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "result=" + result +
                '}';
    }
}
