package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.exceptions.CraftingTypeNotFound;
import org.bukkit.Bukkit;

/**
 * Manages custom crafting recipes
 */
public class CraftingManager {
    // Prevent Instantiation
    private CraftingManager() {}

    /**
     * Creates a Bukkit recipe
     * @param recipe Recipe instance
     * @param id Recipe ID
     * @throws CraftingTypeNotFound Gets thrown when the crafting type provided is not recognized
     */
    public static void createRecipe(Recipe recipe, String id) throws CraftingTypeNotFound {
        if (recipe instanceof CraftingTableRecipe) Bukkit.addRecipe(((CraftingTableRecipe) recipe).getRecipe(id));
        else if (recipe instanceof FurnaceRecipe) Bukkit.addRecipe(((FurnaceRecipe) recipe).getRecipe(id));
        else throw new CraftingTypeNotFound("Crafting type '" + recipe.getType() + "' not recognized");
    }
}
