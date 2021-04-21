package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;

public class DisbandFactionCommand implements CommandExecutor {
    public DisbandFactionCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_DISBAND).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_DISBAND)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_DISBAND)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that no parameters are passed
            if (args.length != 0) {
                player.sendMessage("Error: Invalid number of parameters. Needed: No paramters");
                return true;
            }

            String factionName = Factions.getPlayerFaction(player.getUniqueId().toString());

            // Verify this player is the faction leader
            if (!Factions.getFactionLeader(factionName).equals(player.getUniqueId().toString())) {
                player.sendMessage("Error: You must be the faction leader to disband the faction.");
                return true;
            }

            // Remove all players from the current faction
            JSONArray factionMembersJSONArray = (JSONArray) Factions.getFactionMembers(factionName).clone();
            for (Object currentObject : factionMembersJSONArray) {
                String currentMemberUUID = currentObject.toString();
                Factions.removePlayerFromFaction(currentMemberUUID, factionName);
                Factions.addPlayerToFaction(currentMemberUUID, Factions.FACTION_NO_FACTION_NAME);
            }

            // Delete the faction
            Factions.deleteFaction(factionName);
        }

        return false;
    }
}
