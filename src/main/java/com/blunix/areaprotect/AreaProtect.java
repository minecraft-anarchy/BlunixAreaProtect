package com.blunix.areaprotect;

import com.blunix.areaprotect.commands.*;
import com.blunix.areaprotect.files.AreasDataManager;
import com.blunix.areaprotect.files.AreasFile;
import com.blunix.areaprotect.models.ProtectedArea;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class AreaProtect extends JavaPlugin {
    private AreasFile areaData;
    private AreasDataManager areaDataManager;
    private final Map<String, AreaCommand> subcommands = new LinkedHashMap<>();
    private final Map<Player, ProtectedArea> areaMergers = new HashMap<>();
    private final List<ProtectedArea> protectedAreas = new ArrayList<>();

    public static AreaProtect getInstance() {
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
        areaData = new AreasFile(this);
        areaDataManager = new AreasDataManager(this);
    }

    private void registerCommands() {
        getCommand("areaprotect").setExecutor(new CommandRunner(this));
        getCommand("areaprotect").setTabCompleter(new CommandCompleter(this));
        subcommands.put("create", new CommandCreate(this));
        subcommands.put("help", new CommandHelp());
        subcommands.put("reload", new CommandReload(this));
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();
    }

    public FileConfiguration getAreaData() {
        return areaData.getConfig();
    }

    public void saveAreaData() {
        areaData.saveConfig();
    }

    public Map<String, AreaCommand> getSubcommands() {
        return subcommands;
    }

    public List<ProtectedArea> getProtectedAreas() {
        return protectedAreas;
    }

    public AreasDataManager getAreaDataManager() {
        return areaDataManager;
    }

    public Map<Player, ProtectedArea> getAreaMergers() {
        return areaMergers;
    }
}
