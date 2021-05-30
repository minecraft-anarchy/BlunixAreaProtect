package com.blunix.areaprotect.files;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.models.ProtectedArea;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AreasDataManager {
    private final AreaProtect plugin;

    public AreasDataManager(AreaProtect plugin) {
        this.plugin = plugin;
    }

    public void saveAllAreas() {
        for (ProtectedArea area : plugin.getProtectedAreas()) {
            saveArea(area);
        }
    }

    public void loadAllAreas() {
        ConfigurationSection areaSection = plugin.getAreaData().getConfigurationSection("protected-areas");
        if (areaSection == null) {
            sendWarningLog("There was an error reading area data in areas.yml");
            return;
        }
        areaSection.getKeys(false).forEach(areaName -> {
            UUID ownerUUID = UUID.fromString(areaSection.getString(areaName + ".owner"));
            List<UUID> members = getMembersStringUUIDsAsUUID(areaSection.getStringList(areaName + ".members"));
            List<Chunk> protectedChunks = loadProtectedChunks(areaSection, areaName);

            plugin.getProtectedAreas().add(new ProtectedArea(areaName, ownerUUID, members, protectedChunks));
        });
    }

    public void saveArea(ProtectedArea area) {
        FileConfiguration data = plugin.getAreaData();
        String path = "protected-areas." + area.getName();

        data.set(path + ".owner", area.getOwnerUUID().toString());
        data.set(path + ".members", getMembersUUIDsAsStrings(area.getMembers()));
        data.set(path + ".world", area.getProtectedChunks().get(0).getWorld().getName());
        saveProtectedChunks(path, area.getProtectedChunks());

        plugin.saveAreaData();
    }

    public void deleteArea(ProtectedArea area) {
        plugin.getAreaData().set("protected-areas." + area.getName(), null);
        plugin.saveAreaData();
    }

    private List<String> getMembersUUIDsAsStrings(List<UUID> uuids) {
        List<String> stringUUIDs = new ArrayList<>();
        for (UUID uuid : uuids) {
            stringUUIDs.add(uuid.toString());
        }
        return stringUUIDs;
    }

    private List<UUID> getMembersStringUUIDsAsUUID(List<String> stringUUIDs) {
        List<UUID> uuids = new ArrayList<>();
        for (String stringUUID : stringUUIDs) {
            uuids.add(UUID.fromString(stringUUID));
        }
        return uuids;
    }

    private void saveProtectedChunks(String path, List<Chunk> protectedChunks) {
        FileConfiguration data = plugin.getAreaData();
        int var = 0;
        int chunkX;
        int chunkZ;
        path += ".protected-chunks";
        for (Chunk chunk : protectedChunks) {
            chunkX = chunk.getX();
            chunkZ = chunk.getZ();
            data.set(path + "." + var + ".x", chunkX);
            data.set(path + "." + var + ".z", chunkZ);
            var++;
        }
    }

    private List<Chunk> loadProtectedChunks(ConfigurationSection areaSection, String areaName) {
        List<Chunk> protectedChunks = new ArrayList<>();
        ConfigurationSection chunkSection = areaSection.getConfigurationSection(areaName + ".protected-chunks");
        if (chunkSection == null) {
            sendWarningLog("No protected chunks were loaded from " + areaName + ".");
            return protectedChunks;
        }
        chunkSection.getKeys(false).forEach(var -> {
            int chunkX = chunkSection.getInt(var + ".x");
            int chunkZ = chunkSection.getInt(var + ".z");
            World world = Bukkit.getWorld(areaSection.getString(areaName + ".world"));
            protectedChunks.add(world.getChunkAt(chunkX, chunkZ));
        });
        return protectedChunks;
    }

    private void sendWarningLog(String text) {
        Bukkit.getLogger().warning("[BlunixAreaProtect] " + text);
    }
}
