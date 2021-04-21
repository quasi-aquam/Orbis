package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.Utility;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickFromFactionCommand implements CommandExecutor {
    public KickFromFactionCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_KICK).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_KICK)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_KICK)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that the appropriate number of parameters are passed
            if (args.length != 1) {
                player.sendMessage("Error: Invalid number of parameters. Needed: Player to kick");
                return true;
            }

            String factionName = Factions.getPlayerFaction(player.getUniqueId().toString());
            Player targetPlayer = Utility.getPlayerByName(args[0]);

            if (targetPlayer == null) {
                player.sendMessage("Error: Player '" + args[0] + "' was not found on this server.");
                return true;
            }

            if (!Factions.getPlayerFaction(targetPlayer.getUniqueId().toString()).equals(factionName)) {
                player.sendMessage("Error: Player '" + args[0] + "' is not in your faction.");
                return true;
            }

            String factionRank = Factions.getFactionRank(player.getUniqueId().toString());

            // Check whether this person has permissions to kick from their faction
            if (factionRank.equals(Factions.FACTION_MEMBER_RANK)) {
                // Player is a member
                player.sendMessage("Error: Rank (Member) cannot kick other players from a faction.");
                return true;
            } else if (factionRank.equals(Factions.FACTION_OFFICIAL_RANK) && !Factions.getFactionRank(targetPlayer.getUniqueId().toString()).equals(Factions.FACTION_MEMBER_RANK)) {
                // Player is official and target to kick is not a member
                player.sendMessage("Error: Rank (Official) can only kick players of lower rank.");
                return true;
            }

            Factions.removePlayerFromFaction(targetPlayer.getUniqueId().toString(), factionName);
            Factions.addPlayerToFaction(targetPlayer.getUniqueId().toString(), Factions.FACTION_NO_FACTION_NAME);
        }
        
        return false;
    }
}
