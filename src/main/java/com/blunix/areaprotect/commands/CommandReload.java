package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.util.Messager;
import org.bukkit.command.CommandSender;

public class CommandReload extends AreaCommand {
    private AreaProtect plugin;

    public CommandReload(AreaProtect plugin) {
        this.plugin = plugin;

        setName("reload");
        setHelpMessage("Reloads the plugin's config.");
        setPermission("blunixareaprotect.reload");
        setUsageMessage("/areaprotect reload");
        setArgumentLength(1);
        setUniversalCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        Messager.sendSuccessMessage(sender, "&aConfig reloaded.");
    }
}
