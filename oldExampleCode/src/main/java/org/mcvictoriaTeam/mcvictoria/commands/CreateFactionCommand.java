package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateFactionCommand implements CommandExecutor {
    public CreateFactionCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_CREATE).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_CREATE)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_CREATE)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that both parameters are passed
            if (args.length != 2) {
                player.sendMessage("Error: Invalid number of parameters. Needed: Faction Name, Faction Prefix");
                return true;
            }

            String factionName = args[0];
            String factionPrefix = args[1];
            String playerUUID = player.getUniqueId().toString();

            // Verify the faction name
            if (Factions.doesFactionExist(factionName)) {
                player.sendMessage("Error: Faction already exists. Choose a different name.");
                return true;
            }
            // Verify the faction prefix
            if (Factions.doesFactionAbbreviationExist(factionPrefix)) {
                player.sendMessage("Error: Faction prefix already exists. Choose a different prefix.");
                return true;
            }
            if (!Factions.isPrefixValid(factionPrefix)) {
                player.sendMessage("Error: Invalid Faction Prefix. Needed: 3 capital alphanumeric characters.");
                return true;
            }

            // Create the faction
            Factions.createFaction(factionName, factionPrefix);

            // Assign this player to the faction
            Factions.removePlayerFromFaction(playerUUID, Factions.getPlayerFaction(playerUUID));
            Factions.addPlayerToFaction(playerUUID, factionName);

            // Make this player the leader of the faction
            Factions.setFactionLeader(playerUUID, factionName);
        }
        return false;
    }
}
