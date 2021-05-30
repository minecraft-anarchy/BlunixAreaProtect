package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.models.ProtectedArea;
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
        if (args.length < 2) {
            return getCompletion(arguments, args, 0);
        } else if (args.length < 3) {
            arguments.clear();
            switch (subcommand.getName()) {
                case "delete":
                case "info":
                case "members":
                    return getCompletion(getAreaNames(), args, 1);
                case "merge":
                    arguments.addAll(getAreaNames());
                    arguments.add("None");
                    return getCompletion(arguments, args, 1);
            }
        } else if (args.length < 4) {
            arguments.clear();
            switch (subcommand.getName()) {
                case "create":
                    return null;
                case "members":
                    arguments.add("add");
                    arguments.add("remove");
                    return getCompletion(arguments, args, 2);
            }
        } else if (args.length < 5 && subcommand.getName().equals("members")) {
            return null;
        }
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

    private ArrayList<String> getAreaNames() {
        ArrayList<String> names = new ArrayList<>();
        for (ProtectedArea area : plugin.getProtectedAreas()) {
            names.add(area.getName());
        }
        return names;
    }
}
