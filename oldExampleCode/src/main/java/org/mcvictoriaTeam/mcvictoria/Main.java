package org.mcvictoriaTeam.mcvictoria;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.mcvictoriaTeam.mcvictoria.faction.Factions;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import org.mcvictoriaTeam.mcvictoria.commands.ChangeFactionColorCommand;
import org.mcvictoriaTeam.mcvictoria.commands.ChangeFactionNameCommand;
import org.mcvictoriaTeam.mcvictoria.commands.ChangeFactionPrefixCommand;
import org.mcvictoriaTeam.mcvictoria.commands.CreateFactionCommand;
import org.mcvictoriaTeam.mcvictoria.commands.DemoteToFactionMemberCommand;
import org.mcvictoriaTeam.mcvictoria.commands.DisbandFactionCommand;
import org.mcvictoriaTeam.mcvictoria.commands.KickFromFactionCommand;
import org.mcvictoriaTeam.mcvictoria.commands.LeaveFactionCommand;
import org.mcvictoriaTeam.mcvictoria.commands.PromoteToFactionOfficialCommand;
import org.mcvictoriaTeam.mcvictoria.commands.TransferFactionLeadershipCommand;

public class Main extends JavaPlugin {

    private static BukkitAudiences adventure = null;
    private static Map<String, PlayerInfo> onlinePlayerValues = null;

    // Adventure
    public static @NonNull BukkitAudiences getAdventure() {
        if (Main.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return Main.adventure;
    }

    public static void setAdventure(BukkitAudiences pAdventure) {
        Main.adventure = pAdventure;
    }

    // Online Player Values
    public static @NonNull Map<String, PlayerInfo> getOnlinePlayerValues() {
        if (onlinePlayerValues == null) {
            throw new IllegalStateException("Tried to access OnlinePlayerValues when it was null!");
        }
        return Main.onlinePlayerValues;
    }

    public static void createOnlinePlayerValue(String pPlayerUUID) {
        onlinePlayerValues.put(pPlayerUUID, new PlayerInfo());
    }

    @Override
    public void onEnable() {
        // Create file structure if necessary
        String cwd = Utility.getCurrentWorkingDirectory();

        File f = new File(cwd + "/" + Utility.DATA_FOLDER);
        if (!f.exists())
        {
            try {
                Files.createDirectories(Paths.get(cwd + "/" + Utility.DATA_FOLDER));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        f = new File(cwd + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER);
        if (!f.exists())
        {
            try {
                Files.createDirectories(Paths.get(cwd + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        f = new File(cwd + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER + "/" + Utility.FACTIONS_FILE);
        if (!f.exists())
        {
            FileWriter fileWriter = null;
            try {
                Files.createFile(Paths.get(cwd + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER + "/" + Utility.FACTIONS_FILE));
                fileWriter = new FileWriter(cwd + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER + "/" + Utility.FACTIONS_FILE);

                JSONObject rootObject = new JSONObject();

                JSONArray factionsArray = new JSONArray();

                rootObject.put(Factions.FACTIONS_FIELD, factionsArray);

                fileWriter.write(rootObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.flush();
                        fileWriter.close();
                    }

                    Factions.setFactionsJSONObject(Factions.loadFactionFile());
                    Factions.createFaction(Factions.FACTION_NO_FACTION_NAME, Factions.FACTION_NO_FACTION_PREFIX);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Audience
        setAdventure(BukkitAudiences.create(this));

        // Player Info
        onlinePlayerValues = new HashMap<String, PlayerInfo>();

        // Events
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Factions.setFactionsJSONObject(Factions.loadFactionFile());

        // Commands
        new ChangeFactionColorCommand(this);
        new ChangeFactionNameCommand(this);
        new ChangeFactionPrefixCommand(this);
        new CreateFactionCommand(this);
        new DemoteToFactionMemberCommand(this);
        new DisbandFactionCommand(this);
        new KickFromFactionCommand(this);
        new LeaveFactionCommand(this);
        new PromoteToFactionOfficialCommand(this);
        new TransferFactionLeadershipCommand(this);
    }

    @Override
    public void onDisable() {
        if (Main.adventure != null) {
            Main.adventure.close();
            setAdventure(null);
        }
    }
}
