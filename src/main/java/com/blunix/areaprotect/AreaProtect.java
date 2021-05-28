package com.blunix.areaprotect;

import com.blunix.areaprotect.commands.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Map;

public class AreaProtect extends JavaPlugin {
    private final Map<String, AreaCommand> subcommands = new LinkedHashMap<>();

    private static AreaProtect getInstance() {
        return AreaProtect.getPlugin(AreaProtect.class);
    }

    @Override
    public void onEnable() {
        loadFiles();
        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {

    }

    private void loadFiles() {
        saveDefaultConfig();
    }

    private void registerCommands() {
        getCommand("areaprotect").setExecutor(new CommandRunner(this));
        getCommand("areaprotect").setTabCompleter(new CommandCompleter(this));
        subcommands.put("help", new CommandHelp());
        subcommands.put("reload", new CommandReload(this));
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
    }

    public Map<String, AreaCommand> getSubcommands() {
        return subcommands;
    }
}
