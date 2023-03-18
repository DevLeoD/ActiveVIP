package com.swert.active;

import com.swert.active.command.GiveActiveCommand;
import com.swert.active.listeners.UserListeners;
import lombok.Getter;
import lombok.val;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Terminal extends JavaPlugin {

    @Getter
    private static Terminal plugin;

    @Override
    public void onEnable() {
        plugin = this;

        val file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) saveDefaultConfig();


        register();

        getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled");
    }

    private void register() {
        new UserListeners(this);

        getCommand("givepapel").setExecutor(new GiveActiveCommand());
    }
}
