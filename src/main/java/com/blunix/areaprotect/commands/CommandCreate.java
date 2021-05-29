package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.models.ConfigManager;
import com.blunix.areaprotect.models.ProtectedArea;
import com.blunix.areaprotect.util.Messager;
import com.blunix.areaprotect.util.UUIDUtil;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommandCreate extends AreaCommand {

    public CommandCreate() {
        setName("create");
        setHelpMessage("Creates a protected area on the chunk you currently are with the specified owner.");
        setPermission("blunixareaprotect.create");
        setUsageMessage("/areaprotect create <AreaName> <Owner>");
        setArgumentLength(3);
        setPlayerCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        OfflinePlayer owner = Bukkit.getOfflinePlayer(args[2]);
        String areaName = args[1];
        Chunk currentChunk = player.getLocation().getChunk();
        if (ProtectedArea.getByName(areaName) != null) {
            Messager.sendErrorMessage(player, "&cName &l" + areaName + " &cis already taken by another area.");
            return;
        }
        if (ProtectedArea.getByChunk(currentChunk) != null) {
            Messager.sendErrorMessage(player, "&cThis chunk is already protected by another area.");
            return;
        }
        if (!hasPlayedEnoughTime(owner)) {
            Messager.sendErrorMessage(player, "&c&l" + owner.getName() + " &cneeds to play at least &l" +
                    TimeUnit.MINUTES.toHours((long) ConfigManager.getRequiredPlayTime()) + " hours &cto protect " +
                    "an area.");
            return;
        }
        if (ownsAdjacentAreas(player, currentChunk)) {
            // Ask if the player wants to merge the adjacent areas
        }
        new ProtectedArea(areaName, UUIDUtil.getOfflineUUID(owner.getName()), currentChunk).registerArea();
        Messager.sendSuccessMessage(player, "&aArea &l" + areaName + " &asuccessfully created under &l"
                + owner.getName() + "'s &aownage.");
        if (!owner.isOnline() || owner.getPlayer().equals(player)) return;

        Messager.sendMessage(owner.getPlayer(), "&6An area has been protected under your ownage!");
    }

    private boolean hasPlayedEnoughTime(OfflinePlayer player) {
        return getPlayTimeInMinutes(player) > ConfigManager.getRequiredPlayTime();
    }

    private int getPlayTimeInMinutes(OfflinePlayer player) {
        return (player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) * 60;
    }

    private boolean ownsAdjacentAreas(OfflinePlayer player, Chunk center) {
        UUID ownerUUID = UUIDUtil.getOfflineUUID(player.getName());
        for (ProtectedArea area : getAdjacentAreas(center)) {
            if (!area.getOwnerUUID().equals(ownerUUID)) continue;

            return true;
        }
        return false;
    }

    private List<ProtectedArea> getAdjacentAreas(Chunk center) {
        List<ProtectedArea> adjacentAreas = new ArrayList<>();
        World world = center.getWorld();
        int centerX = center.getX();
        int centerZ = center.getZ();
        ProtectedArea adjacentArea;

        // Getting all the chunks in a radius of 1 block around the center
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                adjacentArea = ProtectedArea.getByChunk(world.getChunkAt(centerX + x, centerZ + z));
                if (adjacentArea == null) continue;

                adjacentAreas.add(adjacentArea);
            }
        }
        return adjacentAreas;
    }
}
