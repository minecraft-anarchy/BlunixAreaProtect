package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.AreaProtect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CommandCompleter implements TabCompleter {
    private final AreaProtect plugin;

    public CommandCompleter(AreaProtect plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        ArrayList<String> arguments = new ArrayList<>();
        for (AreaCommand subcommand : plugin.getSubcommands().values()) {
            if (!sender.hasPermission(subcommand.getPermission())) continue;

            arguments.add(subcommand.getName());
        }
        AreaCommand subcommand = plugin.getSubcommands().get(args[0]);
        if (args.length > 1 && (subcommand == null || !sender.hasPermission(subcommand.getPermission()))) {
            return arguments;
        }
        if (args.length < 2) return getCompletion(arguments, args, 0);

        arguments.clear();
        return arguments;
    }

    private ArrayList<String> getCompletion(ArrayList<String> arguments, String[] args, int index) {
        ArrayList<String> results = new ArrayList<>();
        for (String argument : arguments) {
            if (!argument.toLowerCase().startsWith(args[index].toLowerCase())) continue;

            results.add(argument);
        }
        return results;
    }
}
