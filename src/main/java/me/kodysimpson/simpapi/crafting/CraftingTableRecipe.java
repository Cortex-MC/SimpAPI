package me.kodysimpson.simpapi.crafting;

import com.google.common.collect.Maps;
import me.kodysimpson.simpapi.SimpAPI;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;

public class CraftingTableRecipe extends Recipe {
    @Override
    public String getType() { return "CraftingTable"; }

    private List<Material> items;

    private Map<Material, Character> itemMappings = new HashMap<>();

    public CraftingTableRecipe(ItemStack result, Material... items) {
        this.result = result;
        this.items = Arrays.asList(items);
        int index = 0;
        for (Material item : this.items) {
            if (item == null) continue;
            if (itemMappings.containsKey(item)) continue;
            itemMappings.put(item, String.valueOf(index++).charAt(0));
        }
    }

    public ShapedRecipe getRecipe(String id) {
        NamespacedKey key = new NamespacedKey(SimpAPI.getInstance(), id);

        ShapedRecipe recipe = new ShapedRecipe(key, result);

        int startIndex = 0;
        String[] rows = new String[]{"", "", ""};

        List<Material> list1 = items.subList(0, 3);
        List<Material> list2 = items.subList(3, 6);
        List<Material> list3 = items.subList(6, 9);

        System.out.println(list1.size());
        System.out.println(list2.size());
        System.out.println(list3.size());

        for (Material it : list1) {
            System.out.println(it);
            if (it == null) rows[0] += " ";
            else rows[0] += itemMappings.get(it);
        }

        for (Material it : list2) {
            System.out.println(it);
            if (it == null) rows[1] += " ";
            else rows[1] += itemMappings.get(it);
        }

        for (Material it : list3) {
            System.out.println(it);
            if (it == null) rows[2] += " ";
            else rows[2] += itemMappings.get(it);
        }

        System.out.println(Arrays.deepToString(rows));
        System.out.println(rows[0]);
        System.out.println(rows[1]);
        System.out.println(rows[2]);

        recipe.shape(rows[0], rows[1], rows[2]);

        for (Material material : itemMappings.keySet()) {
            recipe.setIngredient(itemMappings.get(material), material);
        }

        return recipe;
    }

    public List<Material> getItems() {
        return new ArrayList<>(items);
    }
}
