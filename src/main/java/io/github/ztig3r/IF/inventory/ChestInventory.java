package io.github.ztig3r.IF.inventory;

import io.github.ztig3r.IF.IFPlugin;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a chest inventory with panes and items.
 */
public class ChestInventory implements InventoryHolder {
    /**
     * The underlying Bukkit inventory.
     */
    @Getter
    private final Inventory inventory;

    /**
     * The panes contained in the inventory.
     */
    private final List<Pane> panes = new ArrayList<>();

    /**
     * Global click handler for the inventory.
     */
    @Getter
    @Setter
    protected Consumer<InventoryClickEvent> onGlobalClick;

    /**
     * Global drag handler for the inventory.
     */
    @Getter
    @Setter
    protected Consumer<InventoryDragEvent> onGlobalDrag;

    @Getter
    @Setter
    protected Consumer<InventoryCloseEvent> onClose;

    /**
     * Creates a chest inventory with the given title and number of rows.
     *
     * @param title Title of the inventory
     * @param rows  Number of rows (1-6)
     */
    public ChestInventory(int rows, Component title) {
        this.inventory = IFPlugin.getInstance().getServer().createInventory(this, rows * 9, title);
    }

    /**
     * Opens the inventory for the given player.
     *
     * @param player Player to open the inventory for
     */
    public void open(Player player) {
        player.openInventory(inventory);
        InventoryListener.getInstance().register(this, player);
    }

    /**
     * Adds a pane to the inventory.
     *
     * @param pane {@link Pane} to add
     */
    public void addPane(Pane pane) {
        pane.setChestInventory(this);
        panes.add(pane);
        updateByPane(pane);
    }

    /**
     * Removes a pane from the inventory.
     *
     * @param pane {@link Pane} to remove
     */
    public void removePane(Pane pane) {
        panes.remove(pane);
    }

    /**
     * Updates the inventory items based on the given pane.
     *
     * @param pane The {@link Pane} to update from
     */
    public void updateByPane(Pane pane) {
        for (Integer position : pane.getInventoryItems().keySet()) {
            InventoryItem item = pane.getInventoryItems().get(position);
            inventory.setItem(position, item.getItem());
        }
    }

    /**
     * Disables the items in the inventory based on the given pane.
     *
     * @param pane The {@link Pane} to disable
     */
    public void disablePane(Pane pane) {
        for (Integer position : pane.getInventoryItems().keySet()) {
            inventory.setItem(position, null);
        }
    }

    /**
     * Updates an item in the inventory at the given position.
     *
     * @param position Position of the item to update
     * @param item     New {@link InventoryItem} to set
     */
    public void updateItem(int position, InventoryItem item) {
        inventory.setItem(position, item.getItem());
    }

    /**
     * Sets the global click handler for the inventory.
     *
     * @param event {@link InventoryClickEvent}
     */
    public void callOnGlobalClick(InventoryClickEvent event) {
        if (onGlobalClick != null) onGlobalClick.accept(event);
    }

    /**
     * Sets the global drag handler for the inventory.
     *
     * @param event InventoryDragEvent
     */
    public void callOnGlobalDrag(InventoryDragEvent event) {
        if (onGlobalDrag != null) onGlobalDrag.accept(event);
    }

    public void callOnClose(InventoryCloseEvent event) {
        if (onClose != null) onClose.accept(event);
    }

    /**
     * Calls the onClick handler for the item at the clicked position.
     *
     * @param event {@link InventoryClickEvent}
     */
    public void callOnItemClick(InventoryClickEvent event) {
        for (Pane pane : panes) {
            if(!pane.isVisible()) continue;

            InventoryItem item = pane.getInventoryItems().get(event.getSlot());

            if (item != null) {
                item.callOnClick(event);
            }
        }
    }
}
