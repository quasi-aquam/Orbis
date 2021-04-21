package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.Utility;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PromoteToFactionOfficialCommand implements CommandExecutor{
    public PromoteToFactionOfficialCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_PROMOTE_MEMBER).setExecutor(this);
    }

    @Override
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
            Player targetPlayer = Utility.getPlayerByName(args[0]);
            String factionRank = Factions.getFactionRank(player.getUniqueId().toString());

            if (!factionRank.equals(Factions.FACTION_LEADER_RANK)) {
                player.sendMessage("Error: You may only promote members if you are the faction leader.");
                return true;
            }

            if (targetPlayer == null) {
                player.sendMessage("Error: Player '" + args[0] + "' was not found on this server.");
                return true;
            }

            if (!Factions.getPlayerFaction(targetPlayer.getUniqueId().toString()).equals(factionName)) {
                player.sendMessage("Error: Player '" + args[0] + "' is not in your faction.");
                return true;
            }

            String targetPlayerRank = Factions.getFactionRank(targetPlayer.getUniqueId().toString());
            if (!targetPlayerRank.equals(Factions.FACTION_MEMBER_RANK)) {
                player.sendMessage("Error: Only players of the 'member' rank can be promoted.");
                return true;
            }

            Factions.addOfficialToFaction(targetPlayer.getUniqueId().toString(), factionName);
        }

        return false;
    }
}
