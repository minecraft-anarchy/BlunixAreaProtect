package com.blunix.areaprotect.models;

import com.blunix.areaprotect.AreaProtect;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProtectedArea {
    private String name;
    private UUID ownerUUID;
    private List<UUID> members;
    private List<Chunk> protectedChunks;

    public ProtectedArea(String name, UUID ownerUUID, Chunk initialChunk) {
        this.name = name;
        this.ownerUUID = ownerUUID;
        this.members = new ArrayList<>();
        this.protectedChunks = new ArrayList<>();
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

    private void saveAreaData() {
        AreaProtect.getInstance().getAreaDataManager().saveArea(this);
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
