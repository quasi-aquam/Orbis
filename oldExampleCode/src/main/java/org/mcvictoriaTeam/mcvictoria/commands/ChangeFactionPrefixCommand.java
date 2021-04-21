package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeFactionPrefixCommand implements CommandExecutor {
    public ChangeFactionPrefixCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_CHANGE_PREFIX).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_CHANGE_PREFIX)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_CHANGE_PREFIX)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that a single parameter is passed
            if (args.length != 1) {
                player.sendMessage("Error: Invalid number of parameters. Needed: New Faction Prefix");
                return true;
            }

            String factionName = Factions.getPlayerFaction(player.getUniqueId().toString());
            String factionRank = Factions.getFactionRank(player.getUniqueId().toString());
            String newFactionPrefix = args[0];

            if (!factionRank.equals(Factions.FACTION_LEADER_RANK)) {
                player.sendMessage("Error: You may only change the faction prefix if you are the faction leader.");
                return true;
            }

            if (Factions.doesFactionAbbreviationExist(newFactionPrefix)) {
                player.sendMessage("Error: Faction prefix '" + newFactionPrefix + "' is already taken.");
                return true;
            }

            Factions.setFactionPrefix(factionName, newFactionPrefix);
        }

        return false;
    }
}
