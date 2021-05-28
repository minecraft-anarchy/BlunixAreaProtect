package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.util.Messager;
import org.bukkit.command.CommandSender;

public class CommandHelp extends AreaCommand {
    public CommandHelp() {
        setName("help");
        setHelpMessage("Displays this list.");
        setPermission("blunixareaprotect.help");
        setUsageMessage("/areaprotect help");
        setArgumentLength(1);
        setUniversalCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Messager.sendHelpMessage(sender);
    }
}
