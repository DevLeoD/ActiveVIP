package com.swert.active.listeners;

import com.swert.active.Terminal;
import com.swert.active.manager.ActiveItem;
import de.tr7zw.nbtapi.NBTItem;
import lombok.val;
import lombok.var;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class UserListeners implements Listener {

    public UserListeners(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onInteractItem(PlayerInteractEvent event) {
        val item = event.getItem();
        val player = event.getPlayer();
        var name = ChatColor.translateAlternateColorCodes('&', Terminal.getPlugin().getConfig().getString("Item.name"));

        if (!event.hasItem()) return;
        if (!item.hasItemMeta()) return;
        if (!item.getItemMeta().hasDisplayName()) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (item.getItemMeta().getDisplayName().equals(name)) {
                val itemNbt = new NBTItem(item);

                ActiveItem.active(player, itemNbt);
            }
        }
    }
}
