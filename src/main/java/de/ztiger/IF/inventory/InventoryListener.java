package de.ztiger.IF.inventory;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Listener for inventory events to manage custom chest inventories.
 */
public class InventoryListener implements Listener {

    /**
     * Singleton instance of the InventoryListener.
     */
    @Getter
    private static InventoryListener instance;

    /**
     * Map of open {@link ChestInventory} associated with player {@link UUID}.
     */
    @Getter
    public final Map<UUID, ChestInventory> openInventories = new HashMap<>();

    /**
     * Initializes the InventoryListener and registers it with the given plugin.
     *
     * @param plugin The {@link Plugin} to register the listener with
     */
    public static void init(Plugin plugin) {
        instance = new InventoryListener();
        plugin.getServer().getPluginManager().registerEvents(instance, plugin);
    }

    /**
     * Registers a chest inventory as open for a specific player.
     *
     * @param gui    The {@link ChestInventory} to register
     * @param player The {@link Player} for whom the inventory is open
     */
    public void register(ChestInventory gui, Player player) {
        openInventories.put(player.getUniqueId(), gui);
    }

    /**
     * Unregisters the open chest inventory for a specific player.
     *
     * @param player The {@link Player} whose inventory should be unregistered
     */
    public void unregister(Player player) {
        openInventories.remove(player.getUniqueId());
    }

    /**
     * Handles inventory click events.
     *
     * @param event The {@link InventoryClickEvent} triggered by the click
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ChestInventory chestInventory = InventoryListener.getInstance().getOpenInventories().get(event.getWhoClicked().getUniqueId());

        if(chestInventory == null) return;

        chestInventory.callOnGlobalClick(event);
        chestInventory.callOnItemClick(event);
    }

    /**
     * Handles inventory drag events.
     *
     * @param event The {@link InventoryDragEvent} triggered by the drag
     */
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ChestInventory chestInventory = InventoryListener.getInstance().getOpenInventories().get(event.getWhoClicked().getUniqueId());

        if(chestInventory == null) return;

        chestInventory.callOnGlobalDrag(event);
    }

    /**
     * Handles inventory close events.
     *
     * @param event The {@link InventoryCloseEvent} triggered by closing the inventory
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        ChestInventory chestInventory = openInventories.get(player.getUniqueId());
        if (chestInventory == null) return;

        unregister(player);
        chestInventory.callOnClose(event);
    }
}
