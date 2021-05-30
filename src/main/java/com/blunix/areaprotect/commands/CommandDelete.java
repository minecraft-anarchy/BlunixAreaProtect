package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.models.ProtectedArea;
import com.blunix.areaprotect.util.Messager;
import org.bukkit.command.CommandSender;

public class CommandDelete extends AreaCommand {

    public CommandDelete() {
        setName("delete");
        setHelpMessage("Deletes the specified area.");
        setPermission("areaprotect.delete");
        setUsageMessage("/areaprotect delete <AreaName>");
        setArgumentLength(2);
        setUniversalCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String areaName = args[1];
        ProtectedArea toDelete = ProtectedArea.getByName(areaName);
        if (toDelete == null) {
            Messager.sendUnknownAreaMessage(sender);
            return;
        }
        toDelete.unregisterArea();
        Messager.sendSuccessMessage(sender, "&a&l" + areaName + " &ahas been successfully deleted.");
    }
}
