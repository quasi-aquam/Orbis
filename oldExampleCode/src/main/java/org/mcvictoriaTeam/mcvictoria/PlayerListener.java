package org.mcvictoriaTeam.mcvictoria;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import org.mcvictoriaTeam.mcvictoria.faction.Factions;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent pEvent) {
        Player player =  pEvent.getPlayer();
        
        // Make sure this player is part of a faction
        if (Factions.getPlayerFaction(player.getUniqueId().toString()).equals("")) {
            Factions.setPlayerFaction(player.getUniqueId().toString(), Factions.FACTION_NO_FACTION_NAME);
        }

        // Setup the player info
        Main.createOnlinePlayerValue(player.getUniqueId().toString());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent pEvent) {
        //Player player = pEvent.getPlayer();
    }
}
