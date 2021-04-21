package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.Utility;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InviteToFactionCommand implements CommandExecutor {
    public InviteToFactionCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_INVITE).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_PROMOTE_MEMBER)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_PROMOTE_MEMBER)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that a single parameter is passed
            if (args.length != 1) {
                player.sendMessage("Error: Invalid number of parameters. Needed: Player Name");
                return true;
            }

            String factionName = Factions.getPlayerFaction(player.getUniqueId().toString());
            String factionRank = Factions.getFactionRank(player.getUniqueId().toString());
            Player targetPlayer = Utility.getPlayerByName(args[0]);

            if (factionRank.equals(Factions.FACTION_MEMBERS_FIELD)) {
                player.sendMessage("Error: Only the leader and officials may invite to the faction.");
                return true;
            }

            if (targetPlayer == null) {
                player.sendMessage("Error: Player '" + args[0] + "' was not found on this server.");
                return true;
            }

            if (!targetPlayer.isOnline()) {
                player.sendMessage("Error: You can only invite players who are online to your faction.");
                return true;
            }

            if (Factions.getPlayerFaction(targetPlayer.getUniqueId().toString()).equals(factionName)) {
                player.sendMessage("Error: This player is already in your faction.");
                return true;
            }

            Factions.invitePlayerToFaction(player.getUniqueId().toString(), targetPlayer.getUniqueId().toString(), factionName);
        }

        return false;
    }
}
