package com.swert.active.manager;

import com.swert.active.Terminal;
import com.swert.active.utils.ItemBuilder;
import com.swert.active.utils.nms.ActionBar;
import de.tr7zw.nbtapi.NBTItem;
import lombok.val;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class ActiveItem {

    public static ItemStack giveActive(String vip, int duration) {
        var config = Terminal.getPlugin().getConfig();

        var name = ChatColor.translateAlternateColorCodes('&', config.getString("Item.name"));
        var material = Material.matchMaterial(config.getString("Item.material"));
        var lore = config.getStringList("Item.lore");
        var skullCustom = config.getBoolean("Item.skullCustom");
        var skullURL = config.getString("Item.skullURL");

        lore = lore.stream().map(lores -> ChatColor.translateAlternateColorCodes('&', lores.replace("{vip}", getVIPByColor(vip))
                .replace("{days}", String.valueOf(duration)))).collect(Collectors.toList());

        val item = new ItemBuilder(skullCustom ? Material.SKULL_ITEM : material, skullCustom ? (byte) 3 : (byte) 0)
                .setName(name)
                .addLore(lore)
                .setSkullURL(skullURL)
                .build();

        val nbtItem = new NBTItem(item);
        nbtItem.setString("vip_type", vip);
        nbtItem.setInteger("vip_days", duration);

        return nbtItem.getItem();
    }

    public static void active(Player player, NBTItem item) {
        val type = item.getString("vip_type");
        val days = item.getInteger("vip_days");

        val containsMessage = Terminal.getPlugin().getConfig().getString("Messages.containsVIP");
        val active = Terminal.getPlugin().getConfig().getString("Messages.activeVIP").replace("{vip}", getVIPByColor(type));

        val commandExecute = Terminal.getPlugin().getConfig().getString("Settings.command-execute");
        val soundError = Terminal.getPlugin().getConfig().getString("Settings.Sounds.error");
        val soundSuccess = Terminal.getPlugin().getConfig().getString("Settings.Sounds.success");

        if (player.hasPermission("group." + type)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', containsMessage));
            player.playSound(player.getLocation(), soundError, 1.0f, 1.0f);
            return;
        }

        val actionBar = new ActionBar(ChatColor.translateAlternateColorCodes('&', active));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandExecute.replace("{player}", player.getName())
                .replace("{vip}", type).replace("{days}", String.valueOf(days)));
        player.setItemInHand(null);
        actionBar.show(player);
        player.playSound(player.getLocation(), soundSuccess, 1.0f, 1.0f);
    }

    public static String getVIPByColor(String vip) {
        val config = Terminal.getPlugin().getConfig().getConfigurationSection("Vips").getKeys(false);

        var name = Terminal.getPlugin().getConfig().getString("Vips." + config + ".name");

        return config.contains(vip) ? ChatColor.translateAlternateColorCodes('&', name) : "§cDesconhecido";
    }

    public static boolean containsVIP(String vip) {
        val config = Terminal.getPlugin().getConfig().getConfigurationSection("Vips").getKeys(false);

        return config.contains(vip);
    }
}
