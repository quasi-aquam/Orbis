package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.Utility;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeFactionColorCommand implements CommandExecutor {
    public ChangeFactionColorCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_CHANGE_NAME).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_CHANGE_COLOR)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_CHANGE_COLOR)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that a single parameter is passed
            if (args.length != 1) {
                player.sendMessage("Error: Invalid number of parameters. Needed: New Faction Name");
                return true;
            }

            String factionName = Factions.getPlayerFaction(player.getUniqueId().toString());
            String factionRank = Factions.getFactionRank(player.getUniqueId().toString());
            String newFactionColor = args[0];

            if (!factionRank.equals(Factions.FACTION_LEADER_RANK)) {
                player.sendMessage("Error: You may only change the faction name if you are the faction leader.");
                return true;
            }

            if (!Arrays.asList(Utility.COLORS).contains(newFactionColor)) {
                player.sendMessage("Error: Invalid faction color. Valid colors are: " + Arrays.asList(Utility.COLORS).toString());
                return true;
            }

            Factions.setFactionColor(factionName, newFactionColor);
        }

        return false;
    }
}
