package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.util.Messager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRunner implements CommandExecutor {
    private final AreaProtect plugin;

    public CommandRunner(AreaProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("areaprotect"))
            return true;
        if (args.length == 0) {
            Messager.sendHelpMessage(sender);
            return true;
        }
        if (!plugin.getSubcommands().containsKey(args[0].toLowerCase())) {
            Messager.sendErrorMessage(sender, "&cUnknown command. Type &l/ help &cto see the full command list.");
            return true;
        }

        AreaCommand subcommand = plugin.getSubcommands().get(args[0].toLowerCase());
        if (!sender.hasPermission(subcommand.getPermission())) {
            Messager.sendNoPermissionMessage(sender);
            return true;
        }
        if (subcommand.isPlayerCommand() && !(sender instanceof Player)) {
            Messager.sendErrorMessage(sender, "&cNot available for consoles.");
            return true;
        }
        if (subcommand.isConsoleCommand() && sender instanceof Player) {
            Messager.sendErrorMessage(sender, "&cNot available for players.");
            return true;
        }
        if (args.length < subcommand.getArgumentLength()) {
            Messager.sendErrorMessage(sender, "&cUsage: &l" + subcommand.getUsageMessage());
            return true;
        }
        subcommand.execute(sender, args);
        return true;
    }
}
