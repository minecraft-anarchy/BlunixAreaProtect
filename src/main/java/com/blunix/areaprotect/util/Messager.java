package com.blunix.areaprotect.util;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.commands.AreaCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Iterator;

public class Messager {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendSuccessMessage(CommandSender sender, String message) {
        sendMessage(sender, message);
        SFXManager.playSuccessSound(sender);
    }

    public static void sendErrorMessage(CommandSender sender, String message) {
        sendMessage(sender, message);
        SFXManager.playErrorSound(sender);
    }

    public static void sendHelpMessage(CommandSender sender) {
        AreaProtect plugin = AreaProtect.getPlugin(AreaProtect.class);
        String finalMessage = "&6&n&lCommands\n";

        Iterator<AreaCommand> iterator = plugin.getSubcommands().values().iterator();

        while (iterator.hasNext()) {
            AreaCommand subcommand = iterator.next();
            if (!sender.hasPermission(subcommand.getPermission()))
                continue;

            finalMessage += "&a" + subcommand.getUsageMessage() + " &6- &9" + subcommand.getHelpMessage();
            if (iterator.hasNext())
                finalMessage += "\n";

        }

        Messager.sendSuccessMessage(sender, finalMessage);
    }

    public static void sendNoPermissionMessage(CommandSender sender) {
        sendErrorMessage(sender, "&cYou do not have permissions to use this command!");
    }

    public static void sendUnknownAreaMessage(CommandSender sender) {
        sendErrorMessage(sender, "&cUnknown area.");
    }
}
