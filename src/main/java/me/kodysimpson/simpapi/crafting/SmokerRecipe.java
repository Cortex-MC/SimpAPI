package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmokingRecipe;

import java.util.Objects;

/**
 * Recipe for a Smoker
 */
public class SmokerRecipe extends Recipe {
    @Override
    public String getType() { return "Smoker"; }

    private final Material input;
    private final float experience;
    private final int cookingTime;

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

    public Material getInput() {
        return input;
    }

    public float getExperience() {
        return experience;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SmokerRecipe that = (SmokerRecipe) o;
        return Float.compare(that.experience, experience) == 0 && cookingTime == that.cookingTime && input == that.input;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), input, experience, cookingTime);
    }

    @Override
    public String toString() {
        return "SmokerRecipe{" +
                "input=" + input +
                ", experience=" + experience +
                ", cookingTime=" + cookingTime +
                "} " + super.toString();
    }
}
