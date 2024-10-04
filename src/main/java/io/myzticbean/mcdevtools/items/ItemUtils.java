package io.myzticbean.mcdevtools.items;

import io.myzticbean.mcdevtools.colors.ColorTranslator;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class for Utilities related to Minecraft/Bukkit Items
 */
@UtilityClass
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
        // Automatically translate color codes provided
        itemMeta.setLore(Arrays.stream(lore).map(ColorTranslator::translateColorCodes).toList());
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * @param material The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @param lore The lore of the ItemStack, with the Strings being automatically color coded with ColorTranslator
     * @return The constructed ItemStack object
     */
    public static ItemStack makeItem(Material material, String displayName, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = Objects.requireNonNull(item.getItemMeta());
        itemMeta.setDisplayName(displayName);
        // Automatically translate color codes provided
        itemMeta.setLore(lore.stream().map(ColorTranslator::translateColorCodes).toList());
        item.setItemMeta(itemMeta);
        return item;
    }

    /**
     * @param material The material to base the ItemStack on
     * @param displayName The display name of the ItemStack
     * @param lore The lore of the ItemStack, with the Strings being automatically color coded with ColorTranslator
     * @param enchants Enchantments list (ignores level restriction)
     * @return The constructed ItemStack object
     */
    public static ItemStack makeItem(Material material, String displayName, List<String> lore, Map<Enchantment, Integer> enchants) {
        var itemStack = makeItem(material, displayName, lore);
        var itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        enchants.forEach((k, v) -> itemMeta.addEnchant(k, v, true));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
