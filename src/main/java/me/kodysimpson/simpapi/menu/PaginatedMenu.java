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
    //the index represents the index of the slot
    //that the loop is on
    protected int index = 0;

    public PaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    /**
     * @return A list of the data being paginated. usually this is a list of items but it can be anything
     */
    public abstract List<?> getData();

    /**
     * @param object A single element of the data list that you do something with. It is recommended that you turn this into an item if it is not already and then put it into the inventory as you would with a normal Menu. You can execute any other logic in here as well.
     */
    public abstract void loopCode(Object object);

    /**
     * @return A hashmap of items you want to be placed in the paginated menu border. This will override any items already placed by default. Key = slot, Value = Item
     */
    @Nullable
    public abstract HashMap<Integer, ItemStack> getCustomMenuBorderItems();

    /**
     * Set the border and menu buttons for the menu. Override this method to provide a custom menu border or specify custom items in customMenuBorderItems()
     */
    protected void addMenuBorder() {

        inventory.setItem(48, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Left"));
        inventory.setItem(49, makeItem(Material.BARRIER, ChatColor.DARK_RED + "Close"));
        inventory.setItem(50, makeItem(Material.DARK_OAK_BUTTON, ChatColor.GREEN + "Right"));

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
     * Place each item in the paginated menu, automatically coded by default but override if you want custom functionality. Calls the loopCode() method you define for each item returned in the getData() method
     */
    @Override
    public void setMenuItems() {

        addMenuBorder();

        List<Object> data = (List<Object>) getData();

        if (data != null && !data.isEmpty()) {
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                System.out.println(index);
                if (index >= data.size()) break;
                if (data.get(index) != null) {
                    loopCode(data.get(index)); //run the code defined by the user
                }
            }
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
        if (!((index + 1) >= getData().size())) {
            page = page + 1;
            reloadItems();
            return true;
        } else {
            return false;
        }
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}

