package com.blunix.areaprotect.commands;

import com.blunix.areaprotect.AreaProtect;
import com.blunix.areaprotect.models.ProtectedArea;
import com.blunix.areaprotect.util.Messager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMerge extends AreaCommand {
    private final AreaProtect plugin;

    public CommandMerge(AreaProtect plugin) {
        this.plugin = plugin;

        setName("merge");
        setHelpMessage("Merges the area you are currently creating with another adjacent area with the same owner.");
        setPermission("areaprotect.merge");
        setUsageMessage("/areaprotect merge <AreaName/None>");
        setArgumentLength(2);
        setPlayerCommand(true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (!plugin.getAreaMergers().containsKey(player)) {
            Messager.sendErrorMessage(player, "&cYou need to create an area next to another one to start " +
                    "merging areas.");
            return;
        }
        ProtectedArea currentArea = plugin.getAreaMergers().get(player);
        if (args[1].equals("none")) {
            currentArea.registerArea();
            Messager.sendSuccessMessage(player, "&cYou have successfully created an individual area under the " +
                    "name &l" + currentArea.getName());
        } else {
            ProtectedArea mergedArea = ProtectedArea.getByName(args[1]);
            if (mergedArea == null) {
                Messager.sendUnknownAreaMessage(player);
                return;
            }
            mergedArea.getProtectedChunks().add(currentArea.getProtectedChunks().remove(0));
            Messager.sendSuccessMessage(player, "&aYou have successfully merged &l" + currentArea.getName()
                    + " &ato &l" + mergedArea.getName());
        }
        plugin.getAreaMergers().remove(player);
    }
}
