package de.ztiger.IF.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Creates a new ItemBuilder for the specified material.
     *
     * @param material The {@link Material} of the item to be built.
     */
    public ItemBuilder(Material material) {
        item = new ItemStack(material, 1);
        meta = item.getItemMeta();
    }

    /**
     * Sets the custom name of the item.
     *
     * @param name The {@link Component} representing the name to set.
     * @return The current {@link ItemBuilder} instance for method chaining.
     */
    public ItemBuilder setName(Component name) {
        meta.customName(name);
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore The lore component to set.
     * @return The current {@link ItemBuilder} instance for method chaining.
     */
    public ItemBuilder setLore(Component lore) {
        meta.lore(Arrays.asList(lore));
        return this;
    }

    /**
     * Sets the lore of the item.
     *
     * @param lore The list of lore components to set.
     * @return The current {@link ItemBuilder} instance for method chaining.
     */
    public ItemBuilder setLore(List<Component> lore) {
        meta.lore(lore);
        return this;
    }

    /**
     * Adds a glowing effect to the item without displaying enchantments.
     *
     * @return The current {@link ItemBuilder} instance for method chaining.
     */
    public ItemBuilder addGlow() {
        meta.setEnchantmentGlintOverride(true);
    	return this;
    }

    /**
     * Sets the owner of a player head item by UUID.
     *
     * @param uuid The UUID of the player whose head is to be set as the owner.
     * @return The current {@link ItemBuilder} instance for method chaining.
     */
    public ItemBuilder setSkullOwner(UUID uuid) {
        SkullMeta skullMeta = (SkullMeta) this.meta;
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
        return this;
    }

    /**
     * Sets a custom texture for a player head item.
     *
     * @param texture The texture string to apply to the player head.
     * @return The current {@link ItemBuilder} instance for method chaining.
     */
    public ItemBuilder setCustomTexture(String texture) {
        SkullMeta meta = (SkullMeta) this.meta;
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        String encodedTexture = Base64.getEncoder().encodeToString(("{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}").getBytes(StandardCharsets.UTF_8));
        profile.setProperty(new ProfileProperty("textures", encodedTexture));
        meta.setPlayerProfile(profile);

        return this;
    }

    /**
     * Builds and returns the final ItemStack.
     *
     * @return The constructed {@link ItemStack}.
     */
    public ItemStack build() {
        item.setItemMeta(meta);
        item.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.PROFILE));
        return item;
    }
}
