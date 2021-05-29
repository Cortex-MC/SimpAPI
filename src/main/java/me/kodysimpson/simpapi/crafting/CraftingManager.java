package me.kodysimpson.simpapi.crafting;

import me.kodysimpson.simpapi.exceptions.CraftingTypeNotFound;
import org.bukkit.Bukkit;

public class CraftingManager {
    // Prevent Instantiation
    private CraftingManager() {}

    public static void createRecipe(Recipe recipe, String id) throws CraftingTypeNotFound {
        if (recipe instanceof CraftingTableRecipe) Bukkit.addRecipe(((CraftingTableRecipe) recipe).getRecipe(id));
        else throw new CraftingTypeNotFound("Crafting type '" + recipe.getType() + "' not recognized");
    }
}
