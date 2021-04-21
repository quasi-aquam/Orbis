package org.mcvictoriaTeam.mcvictoria;

import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Utility {
    public static final String DATA_FOLDER = "mcvictoria";
    public static final String FACTIONS_FOLDER = "factions";
    public static final String FACTIONS_FILE = "factions.json";

    public static final String[] COLORS = {
        "black",
        "dark_blue",
        "dark_green",
        "dark_aqua",
        "dark_red",
        "dark_purple",
        "gold",
        "gray",
        "dark_gray",
        "blue",
        "green",
        "aqua",
        "red",
        "light_purple",
        "yellow",
        "white",
        "minecoin_gold"
    };

    public static String getCurrentWorkingDirectory() {
        return Paths.get(System.getProperty("user.dir")).toString();
    }

    public static Player getPlayerByName(String pName) {
        Player rv = null;

        // Try to get the online player
        rv = Bukkit.getPlayer(pName);
        if (rv != null) {
            return rv;
        }

        // If the player is offline
        OfflinePlayer[] offlinePlayers = Bukkit.getOfflinePlayers();
        for (OfflinePlayer currentPlayer : offlinePlayers) {
            if (currentPlayer.getName().equals(pName)) {
                rv = (Player) currentPlayer;
                break;
            }
        }
        
        return rv;
    }
}
