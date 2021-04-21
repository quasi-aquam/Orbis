package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeFactionNameCommand implements CommandExecutor {
    public ChangeFactionNameCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_CHANGE_NAME).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_CHANGE_NAME)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_CHANGE_NAME)) {
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
            String newFactionName = args[0];

            if (!factionRank.equals(Factions.FACTION_LEADER_RANK)) {
                player.sendMessage("Error: You may only change the faction name if you are the faction leader.");
                return true;
            }

            if (Factions.doesFactionExist(newFactionName)) {
                player.sendMessage("Error: Faction name '" + newFactionName + "' is already taken.");
                return true;
            }

            Factions.setFactionName(factionName, newFactionName);
        }

        return false;
    }
}
