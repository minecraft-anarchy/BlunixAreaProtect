package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.models.ProtectedArea;
import com.blunix.areaprotect.util.Messager;
import com.blunix.areaprotect.util.SFXManager;
import com.blunix.areaprotect.util.StringUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.Iterator;
import java.util.UUID;

public class CommandInfo extends AreaCommand {

    public CommandInfo() {
        setName("info");
        setHelpMessage("Displays information about the specified protected area.");
        setPermission("blunixareaprotect.info");
        setUsageMessage("/areaprotect info <AreaName>");
        setArgumentLength(2);
        setUniversalCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String areaName = args[1];
        ProtectedArea area = ProtectedArea.getByName(areaName);
        if (area == null) {
            Messager.sendUnknownAreaMessage(sender);
            return;
        }
        sender.spigot().sendMessage(getInfoMessage(area, sender.hasPermission("blunixareaprotect.admin")));
        SFXManager.playSuccessSound(sender);
    }

    private BaseComponent[] getInfoMessage(ProtectedArea area, boolean isAdmin) {
        ComponentBuilder builder = new ComponentBuilder(StringUtil.formatColor("&9&l" + area.getName() +
                "'s &9info\n"));
        Chunk initialChunk = area.getProtectedChunks().get(0);
        builder.append(StringUtil.formatColor("&aOwner: &l" + Bukkit.getOfflinePlayer(area.getOwnerUUID())
                .getName()) + "\n");
        builder.append(getMembersText(area));
        builder.append(StringUtil.formatColor("\n&aInitial Chunk: &lX: " + initialChunk.getX() * 16 +
                " Z: " + initialChunk.getZ() * 16) + "\n");
        builder.append(StringUtil.formatColor("&aProtected Chunks: &l" + area.getProtectedChunks().size()));

        return builder.create();
    }

    private TextComponent getMembersText(ProtectedArea area) {
        TextComponent textComponent = new TextComponent(StringUtil.formatColor("&aMembers: &l" +
                area.getMembers().size()));
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                getHoverableMembers(area.getMembers().iterator())));

        return textComponent;
    }

    private BaseComponent[] getHoverableMembers(Iterator<UUID> membersIterator) {
        ComponentBuilder builder = new ComponentBuilder();
        while (membersIterator.hasNext()) {
            OfflinePlayer member = Bukkit.getOfflinePlayer(membersIterator.next());
            builder.append(member.getName());
            if (membersIterator.hasNext()) {
                builder.append("\n");
            }
        }
        return builder.color(ChatColor.GOLD).create();
    }
}
