package de.ztiger.IF;

import de.ztiger.IF.inventory.InventoryListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for the IF plugin.
 */
public class IFPlugin extends JavaPlugin {
    /**
     * Singleton instance of the IFPlugin.
     */
    @Getter
    protected static IFPlugin instance;

    /**
     * Called when the plugin is enabled.
     */
    @Override
    public void onEnable() {
        instance = this;
        InventoryListener.init(this);
    }
}
