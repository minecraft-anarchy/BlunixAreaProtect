package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.models.ProtectedArea;
import com.blunix.areaprotect.util.Messager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMembers extends AreaCommand {

    public CommandMembers() {
        setName("addmember");
        setHelpMessage("Adds the specified player to the selected protection.");
        setPermission("blunixareaprotect.addmember");
        setUsageMessage("/areaprotect members <AreaName> <Add/Remove> <Player>");
        setArgumentLength(4);
        setUniversalCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[3]);
        String areaName = args[1];
        ProtectedArea area = ProtectedArea.getByName(areaName);
        if (area == null) {
            Messager.sendUnknownAreaMessage(sender);
            return;
        }
        if (sender instanceof Player && !area.isOwner(((Player) sender))
                && !sender.hasPermission("blunixareaprotect.admin")) {
            Messager.sendErrorMessage(sender, "&cYou are not the owner of &l" + areaName);
            return;
        }
        if (args[2].equalsIgnoreCase("add")) { // Adding member
            addMember(sender, area, target);
        } else if (args[2].equalsIgnoreCase("remove")) {
            removeMember(sender, area, target);
        }
    }

    private void addMember(CommandSender sender, ProtectedArea area, OfflinePlayer member) {
        if (area.isMember(member)) {
            Messager.sendErrorMessage(sender, "&c&l" + member.getName() + " &calready is a member of &l"
                    + area.getName() + ".");
            return;
        }
        area.addMember(member);
        if (sender instanceof Player && area.isMember((Player) sender)) return;

        Messager.sendSuccessMessage(sender, "&a&l" + member.getName() + " &ahas been successfully added " +
                "to &l" + area.getName() + "&a.");
    }

    private void removeMember(CommandSender sender, ProtectedArea area, OfflinePlayer member) {
        if (!area.isMember(member)) {
            Messager.sendErrorMessage(sender, "&c&l" + member.getName() + " &cisn't part of &l"
                    + area.getName());
            return;
        }
        area.removeMember(member);
        if (sender instanceof Player && area.isMember((Player) sender)) return;

        Messager.sendSuccessMessage(sender, "&a&l" + member.getName() + " &ahas been successfully removed " +
                "from &l" + area.getName());
    }
}
