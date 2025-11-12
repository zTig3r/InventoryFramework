package io.github.ztig3r.IF.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Represents an item in an inventory with an optional click handler.
 */
@RequiredArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    /**
     * The item stack representing the inventory item.
     */
    @Getter
    @NotNull
    protected ItemStack item;

    /**
     * An optional consumer that handles click events on this item.
     */
    @Nullable
    @Getter
    protected Consumer<InventoryClickEvent> onClick;

    /**
     * Calls the onClick consumer if it is set.
     *
     * @param event The {@link InventoryClickEvent} that triggered the click
     */
    public void callOnClick(InventoryClickEvent event) {
        if (onClick != null) onClick.accept(event);
    }
}
