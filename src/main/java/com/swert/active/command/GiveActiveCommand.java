package com.swert.active.command;

import com.swert.active.Terminal;
import com.swert.active.manager.ActiveItem;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GiveActiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("active.admin")) return false;

        if (args.length == 0) {
            sender.sendMessage("§cUtilize: /givepapel vip <jogador> <vip> <dias>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "vip":
                if (args.length == 4) {
                    val player = Bukkit.getPlayerExact(args[1]);
                    val type = args[2];
                    val days = Integer.parseInt(args[3]);

                    if (player == null) {
                        sender.sendMessage("§cEste jogador não existe.");
                        return false;
                    }

                    if (!ActiveItem.containsVIP(type)) {
                        sender.sendMessage("§cEste VIP não foi registrado!");
                        return false;
                    }

                    sender.sendMessage("§eAtivador vip do tipo " + ActiveItem.getVIPByColor(type) + " §egivado com sucesso para §f" + player.getName() );
                    player.getInventory().addItem(ActiveItem.giveActive(type, days));
                }
                break;
            case "reload":
                sender.sendMessage("§b§lATIVADOR §fConfiguração recarregada com sucesso!");
                Terminal.getPlugin().reloadConfig();
                break;
        }

        return true;
    }
}
