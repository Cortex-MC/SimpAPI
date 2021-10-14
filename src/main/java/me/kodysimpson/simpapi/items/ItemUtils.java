package me.kodysimpson.simpapi.items;

import me.kodysimpson.simpapi.colors.ColorTranslator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Class for Utilities related to Minecraft/Bukkit Items
 */
public class ItemUtils {

    /**
     * @param material The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @param lore The lore of the ItemStack, with the Strings being automatically color coded with ColorTranslator
     * @return The constructed ItemStack object
     */
    public static ItemStack makeItem(Material material, String displayName, String... lore) {

        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(displayName);

        //Automatically translate color codes provided
        itemMeta.setLore(Arrays.stream(lore).map(ColorTranslator::translateColorCodes).collect(Collectors.toList()));
        item.setItemMeta(itemMeta);

        return item;
    }

}
