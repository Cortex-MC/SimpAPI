package me.kodysimpson.simpapi.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public abstract class PaginatedMenu extends Menu {

    //The items being paginated
    protected List<Object> data;

    //Keep track of what page the menu is on
    protected int page = 0;
    //28 is max items because with the border set below,
    //28 empty slots are remaining.
    protected int maxItemsPerPage = 28;

    // Add cache field
    private List<ItemStack> cachedItems;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    /**
     * @return A list of ItemStacks that you want to be placed in the menu. This is the data that will be paginated
     * You can also use this as a way to convert your data to items if you need to
     */
    public abstract List<ItemStack> dataToItems();

    /**
     * @return A hashmap of items you want to be placed in the paginated menu border. This will override any items already placed by default. Key = slot, Value = Item
     */
    @Nullable
    public abstract HashMap<Integer, ItemStack> getCustomMenuBorderItems();

    /**
     * Set the border and menu buttons for the menu. Override this method to provide a custom menu border or specify custom items in customMenuBorderItems()
     */
    protected void addMenuBorder() {
        // First page button
        inventory.setItem(47, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "First Page"));
        // Previous page button
        inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Previous"));
        // Close button
        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));
        // Next page button
        inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Next"));
        // Last page button
        inventory.setItem(51, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Last Page"));

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        //place the custom items if they exist
        if (getCustomMenuBorderItems() != null) {
            getCustomMenuBorderItems().forEach((integer, itemStack) -> inventory.setItem(integer, itemStack));
        }

    }

    /**
     * Gets the paginated items, using cache if available
     * @return List of ItemStacks to display
     */
    protected List<ItemStack> getItems() {
        if (cachedItems == null) {
            cachedItems = dataToItems();
        }
        return cachedItems;
    }

    /**
     * Clears the item cache, forcing items to be reloaded next time
     */
    protected void invalidateCache() {
        cachedItems = null;
    }

    /**
     * Place each item in the paginated menu, automatically coded by default but override if you want custom functionality. Calls the loopCode() method you define for each item returned in the getData() method
     */
    @Override
    public void setMenuItems() {
        addMenuBorder();
        
        List<ItemStack> items = getItems(); // Use cached items
        
        int slot = 10;
        for (int i = 0; i < maxItemsPerPage; i++) {
            int index = maxItemsPerPage * page + i;
            if (index >= items.size()) break;
            
            if (slot % 9 == 8) slot += 2;
            
            inventory.setItem(slot, items.get(index));
            slot++;
        }
    }

    /**
     * @return true if successful, false if already on the first page
     */
    public boolean prevPage() {
        if (page == 0) {
            return false;
        } else {
            page = page - 1;
            reloadItems();
            return true;
        }
    }

    /**
     * @return true if successful, false if already on the last page
     */
    public boolean nextPage() {
        int totalItems = getItems().size(); // Use cached items
        int lastPageNumber = (totalItems - 1) / maxItemsPerPage;
        
        if (page < lastPageNumber) {
            page++;
            reloadItems();
            return true;
        }
        return false;
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public int getCurrentPage() {
        return page + 1; // Convert to 1-based page numbers for display
    }

    public int getTotalPages() {
        return ((getItems().size() - 1) / maxItemsPerPage) + 1; // Use cached items
    }

    @Override
    public void open() {
        invalidateCache(); // Force items to be reloaded when menu opens
        super.open();
    }

    /**
     * Refreshes the menu data and reloads items while keeping the menu open
     */
    public void refreshData() {
        invalidateCache();
        reloadItems();
    }

    /**
     * Sets the current page to the first page (0)
     * @return true if the page was changed, false if already on first page
     */
    public boolean firstPage() {
        if (page == 0) {
            return false;
        }
        page = 0;
        reloadItems();
        return true;
    }

    /**
     * Sets the current page to the last page
     * @return true if the page was changed, false if already on last page
     */
    public boolean lastPage() {
        int lastPageNum = (getItems().size() - 1) / maxItemsPerPage;
        if (page == lastPageNum) {
            return false;
        }
        page = lastPageNum;
        reloadItems();
        return true;
    }
}

