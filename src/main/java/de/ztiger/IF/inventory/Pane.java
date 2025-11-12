package de.ztiger.IF.inventory;

import de.ztiger.IF.data.Pattern;
import de.ztiger.IF.util.ItemBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Pane {

    // Texture URLs for pagination heads
    private static final String backTextureUrl = "bd69e06e5dadfd84e5f3d1c21063f2553b2fa945ee1d4d7152fdc5425bc12a9";
    private static final String nextTextureUrl = "19bf3292e126a105b54eba713aa1b152d541a1d8938829c56364d178ed22bf";

    /**
     * Visibility of the pane in the chest inventory.
     */
    @Getter
    protected boolean isVisible = true;

    /**
     * Map of positions to inventory items in the pane.
     */
    protected final HashMap<Integer, InventoryItem> inventoryItems = new HashMap<>();

    /**
     * Reference to the parent chest inventory, if any.
     */
    @Setter
    @Nullable
    protected ChestInventory chestInventory;


    /**
     * Gets the map of inventory items in the pane.
     *
     * @return Map of positions to {@link InventoryItem}s
     */
    public Map<Integer, InventoryItem> getInventoryItems() {
        return inventoryItems;
    }

    /**
     * Adds an item to the pane at the given slot and row.
     *
     * @param slot Slot number (0-indexed)
     * @param row  Row number (0-indexed)
     * @param item The {@link InventoryItem} to add
     */
    public void addItem(int slot, int row, InventoryItem item) {
        this.inventoryItems.put(slot + (row * 9), item);
    }

    /**
     * Creates a pattern in the pane using the given item map and pattern.
     *
     * @param itemMap A map of numbers in the pattern to InventoryItems
     * @param pattern The {@link Pattern} to create
     */
    public void createPattern(Map<Character, InventoryItem> itemMap, Pattern pattern) {
        int[][] pat = pattern.getPattern();
        for (int row = 0; row < pat.length; row++) {
            for (int slot = 0; slot < pat[row].length; slot++) {
                char key = (char) pat[row][slot];
                if (itemMap.containsKey(key)) addItem(slot, row, itemMap.get(key));
            }
        }
    }

    /**
     * Updates an existing item in the pane at the given slot and row.
     *
     * @param slot Slot number (0-indexed)
     * @param row  Row number (0-indexed)
     * @param item The new {@link InventoryItem} to set
     */
    public void updateItem(int slot, int row, InventoryItem item) {
        int pos = slot + (row * 9);
        if (this.inventoryItems.containsKey(pos)) {
            this.inventoryItems.put(pos, item);
        }
    }

    /**
     * Updates the ItemStack of an existing item in the pane at the given slot and row.
     *
     * @param slot      Slot number (0-indexed)
     * @param row       Row number (0-indexed)
     * @param itemStack The new {@link ItemStack} to set
     */
    public void updateItemStack(int slot, int row, ItemStack itemStack) {
        int position = slot + (row * 9);
        if (this.inventoryItems.containsKey(position)) {
            InventoryItem existingItem = this.inventoryItems.get(position);
            InventoryItem updatedItem = new InventoryItem(itemStack, existingItem.getOnClick());
            this.inventoryItems.put(position, updatedItem);

            if (chestInventory != null) {
                chestInventory.updateItem(position, updatedItem);
            }
        }
    }

    /**
     * Sets the visibility of the pane in the chest inventory.
     *
     * @param visible True to show the pane, false to hide it
     */
    public void setVisible(boolean visible) {
        if (chestInventory != null) {
            if (visible) chestInventory.updateByPane(this);
            else chestInventory.disablePane(this);

            this.isVisible = visible;
        }
    }

    /**
     * Adds pagination items (back and next) to the pane.
     *
     * @param backItem      The {@link PaginationItem} for going back a page
     * @param nextItem      The {@link PaginationItem} for going to the next page
     * @param paginatedPane The {@link PaginatedPane} to control
     */
    public void addPaginationItems(PaginationItem backItem, PaginationItem nextItem, PaginatedPane paginatedPane) {
        if (backItem != null) {
            this.addItem(backItem.slot, backItem.row, new InventoryItem(new ItemBuilder(Material.PLAYER_HEAD).setCustomTexture(backTextureUrl).setName(backItem.name).build(), event -> paginatedPane.previousPage()));
        }

        if (nextItem != null) {
            this.addItem(nextItem.slot, nextItem.row, new InventoryItem(new ItemBuilder(Material.PLAYER_HEAD).setCustomTexture(nextTextureUrl).setName(nextItem.name).build(), event -> paginatedPane.nextPage()));
        }
    }

    /**
     * Pagination item record.
     *
     * @param slot Slot number (0-indexed)
     * @param row  Row number (0-indexed)
     * @param name The display name of the pagination item
     */
    public record PaginationItem(int slot, int row, Component name) {
    }
}
