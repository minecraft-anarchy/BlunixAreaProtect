package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.models.ConfigManager;
import com.blunix.areaprotect.models.ProtectedArea;
import com.blunix.areaprotect.util.Messager;
import com.blunix.areaprotect.util.SFXManager;
import com.blunix.areaprotect.util.StringUtil;
import com.blunix.areaprotect.util.UUIDUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommandCreate extends AreaCommand {
    private final AreaProtect plugin;

    public CommandCreate(AreaProtect plugin) {
        this.plugin = plugin;

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
        UUID ownerUUID = UUIDUtil.getOfflineUUID(owner.getName());
        String areaName = args[1];
        Chunk currentChunk = player.getLocation().getChunk();
        if (areaName.equals("none")) {
            Messager.sendErrorMessage(player, "&cThis area name is reserved and can't be used.");
            return;
        }
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
        if (ownsAdjacentAreas(player, currentChunk) && ConfigManager.isMergeEnabled()) {
            // Ask if the player wants to merge the adjacent areas
            plugin.getAreaMergers().put(player, new ProtectedArea(areaName, ownerUUID, currentChunk));
            player.spigot().sendMessage(getMergeMessage(currentChunk));
            SFXManager.playSuccessSound(player);
            return;
        }
        new ProtectedArea(areaName, ownerUUID, currentChunk).registerArea();
        Messager.sendSuccessMessage(player, "&aArea &l" + areaName + " &asuccessfully created under &l"
                + owner.getName() + "'s &aownage.");
        if (!owner.isOnline() || owner.getPlayer().equals(player)) return;

        Messager.sendSuccessMessage(owner.getPlayer(), "&6An area has been protected under your ownage!");
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
                if (adjacentArea == null || adjacentAreas.contains(adjacentArea)) continue;

                adjacentAreas.add(adjacentArea);
            }
        }
        return adjacentAreas;
    }

    private BaseComponent[] getMergeMessage(Chunk center) {
        ComponentBuilder builder = new ComponentBuilder(StringUtil.formatColor("&6This area is close to another " +
                "area owned by the same player, which one of the following areas would you like to merge to the " +
                "current one you are creating?\n"));
        Iterator<ProtectedArea> areaIterator = getAdjacentAreas(center).iterator();
        while (areaIterator.hasNext()) {
            builder.append(getClickableArea(areaIterator.next()));
            builder.append("\n");
        }
        return builder.append(getClickableNone()).create();
    }

    private TextComponent getClickableArea(ProtectedArea area) {
        TextComponent textComponent = new TextComponent(area.getName());
        textComponent.setColor(ChatColor.GREEN);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click " +
                "here to merge the new area to " + area.getName()).color(ChatColor.GOLD).italic(true).create()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/areaprotect merge " +
                area.getName()));

        return textComponent;
    }

    private TextComponent getClickableNone() {
        TextComponent textComponent = new TextComponent("None");
        textComponent.setColor(ChatColor.RED);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click " +
                "here to create an individual area").color(ChatColor.GOLD).italic(true).create()));
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/areaprotect merge none"));

        return textComponent;
    }
}
