package org.mcvictoriaTeam.mcvictoria.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

public class LeaveFactionCommand implements CommandExecutor {
    public LeaveFactionCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_LEAVE).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_LEAVE)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_LEAVE)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that no parameters are passed
            if (args.length != 0) {
                player.sendMessage("Error: Invalid number of parameters. Needed: No parameters");;
                return true;
            }

            String factionName = Factions.getPlayerFaction(player.getUniqueId().toString());

            // Verify this player is not the leader of the faction
            if (Factions.getFactionLeader(factionName).equals(player.getUniqueId().toString())) {
                player.sendMessage("Error: The leader of a faction cannot leave. Assign someone else as leader or disband the faction.");
                return true;
            }

            // Demote if this player is an official
            Factions.removeOfficialFromFaction(player.getUniqueId().toString(), factionName);

            // Remove this player from the current faction
            Factions.removePlayerFromFaction(player.getUniqueId().toString(), factionName);
            Factions.addPlayerToFaction(player.getUniqueId().toString(), Factions.FACTION_NO_FACTION_NAME);
        }

        return false;
    }
}
