package com.blunix.areaprotect.files;

import com.blunix.areaprotect.AreaProtect;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class AreasFile {

    private AreaProtect plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public AreasFile(AreaProtect plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "areas.yml");
        }
        dataConfig = YamlConfiguration.loadConfiguration(this.configFile);
        InputStream defaultStream = plugin.getResource("areas.yml");
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (dataConfig == null) {
            reloadConfig();
        }
        return dataConfig;
    }

    public void saveConfig() {
        if (dataConfig == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Couldn't save information into " + configFile, e);
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "areas.yml");
        }
        if (!configFile.exists()) {
            plugin.saveResource("areas.yml", false);
        }
    }
}
