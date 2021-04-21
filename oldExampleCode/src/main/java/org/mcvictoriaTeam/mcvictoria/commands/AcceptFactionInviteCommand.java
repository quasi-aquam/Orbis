package org.mcvictoriaTeam.mcvictoria.commands;

import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AcceptFactionInviteCommand implements CommandExecutor {
    public AcceptFactionInviteCommand(Main plugin) {
        plugin.getCommand(Factions.FACTION_COMMAND_ACCEPT_INVITE).setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return true;
        }

        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase(Factions.FACTION_COMMAND_ACCEPT_INVITE)) {
            if (!player.hasPermission(Factions.FACTION_COMMAND_ACCEPT_INVITE)) {
                player.sendMessage("You do not have permission to execute this command!");
                return true;
            }

            // Check that the appropriate number of parameters are passed
            if (args.length != 1) {
                player.sendMessage("Error: Invalid number of parameters. Needed: Faction to join");
                return true;
            }

            String factionName = args[0];
            
            // Check whether this player is already in this faction
            if (!Factions.getPlayerFaction(player.getUniqueId().toString()).equals(factionName)) {
                player.sendMessage("Error: Invited player is already in that faction.");
                return true;
            }
            // Check whether this player has received an invite
            if (!Main.getOnlinePlayerValues().get(player.getUniqueId().toString()).getJoinableFactions().contains(factionName)) {
                player.sendMessage("Error: You can only join a faction you have been invited to.");
                return true;
            }

            Main.getOnlinePlayerValues().get(player.getUniqueId().toString()).getJoinableFactions().remove(factionName);
            Factions.setPlayerFaction(player.getUniqueId().toString(), factionName);
        }

        return false;
    }
}
