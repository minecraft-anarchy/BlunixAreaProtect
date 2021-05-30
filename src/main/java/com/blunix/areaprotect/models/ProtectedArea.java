package com.blunix.areaprotect.models;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.util.Messager;
import com.blunix.areaprotect.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtectedArea {
    private final String name;
    private final UUID ownerUUID;
    private final List<UUID> members;
    private final List<Chunk> protectedChunks;

    public ProtectedArea(String name, UUID ownerUUID, Chunk initialChunk) {
        this.name = name;
        this.ownerUUID = ownerUUID;
        this.members = new ArrayList<>();
        this.protectedChunks = new ArrayList<>();
        members.add(ownerUUID);
        protectedChunks.add(initialChunk);
    }

    public ProtectedArea(String name, UUID ownerUUID, List<UUID> members, List<Chunk> protectedChunks) {
        this.name = name;
        this.ownerUUID = ownerUUID;
        this.members = members;
        this.protectedChunks = protectedChunks;
    }

    public void registerArea() {
        AreaProtect.getInstance().getProtectedAreas().add(this);
    }

    public void unregisterArea() {
        AreaProtect plugin = AreaProtect.getInstance();
        plugin.getProtectedAreas().remove(this);
        plugin.getAreaDataManager().deleteArea(this);
    }

    public boolean isOwner(Player player) {
        return ownerUUID.equals(player.getUniqueId());
    }

    public boolean isMember(OfflinePlayer player) {
        return members.contains(UUIDUtil.getOfflineUUID(player.getName()));
    }

    public void addProtectedChunk(Chunk newChunk) {
        protectedChunks.add(newChunk);
        saveAreaData();
    }

    public void addMember(OfflinePlayer member) {
        String name = member.getName();
        members.add(UUIDUtil.getOfflineUUID(name));
        broadcastToMembers("&a&l" + name + " &ahas been added to &l" + this.name + "&a!");
        saveAreaData();
    }

    public void removeMember(OfflinePlayer member) {
        String name = member.getName();
        members.remove(UUIDUtil.getOfflineUUID(name));
        broadcastToMembers("&c&l" + name + " &chas been removed from &l" + this.name + "&c!");
        saveAreaData();
    }

    public static ProtectedArea getByName(String name) {
        for (ProtectedArea area : AreaProtect.getInstance().getProtectedAreas()) {
            if (!area.getName().equals(name)) continue;

            return area;
        }
        return null;
    }

    public static ProtectedArea getByChunk(Chunk chunk) {
        for (ProtectedArea area : AreaProtect.getInstance().getProtectedAreas()) {
            if (!area.getProtectedChunks().contains(chunk)) continue;

            return area;
        }
        return null;
    }

    private void saveAreaData() {
        AreaProtect.getInstance().getAreaDataManager().saveArea(this);
    }

    private void broadcastToMembers(String text) {
        Player member;
        for (UUID uuid : members) {
            member = Bukkit.getPlayer(uuid);
            if (member == null) continue;

            Messager.sendMessage(member, text);
        }
    }

    public String getName() {
        return name;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public List<Chunk> getProtectedChunks() {
        return protectedChunks;
    }
}
