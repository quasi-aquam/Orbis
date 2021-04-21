package org.mcvictoriaTeam.mcvictoria.faction;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mcvictoriaTeam.mcvictoria.Main;
import org.mcvictoriaTeam.mcvictoria.Utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;

public class Factions{
    public static final String FACTIONS_FIELD = "factions";
    public static final String FACTION_COLOR_FIELD = "color";
    public static final String FACTION_NAME_FIELD = "name";
    public static final String FACTION_PREFIX_FIELD = "prefix";
    public static final String FACTION_LEADER_FIELD = "leader";
    public static final String FACTION_OFFICIALS_FIELD = "officials";
    public static final String FACTION_MEMBERS_FIELD = "members";
    public static final String FACTION_NO_FACTION_NAME = "No Faction";
    public static final String FACTION_NO_FACTION_PREFIX = "---";
    public static final int FACTION_PREFIX_SIZE = 3;
    public static final String FACTION_PREFIX_PATTERN = "[A-Z0-9][A-Z0-9][A-Z0-9]";
    public static final String FACTION_DEFAULT_COLOR = "white";

    public static final String FACTION_LEADER_RANK = "Leader";
    public static final String FACTION_OFFICIAL_RANK = "Official";
    public static final String FACTION_MEMBER_RANK = "Member";

    public static final String FACTION_COMMAND_CHANGE_COLOR = "f.changeColor";
    public static final String FACTION_COMMAND_CHANGE_NAME = "f.changeName";
    public static final String FACTION_COMMAND_CHANGE_PREFIX = "f.changePrefix";
    public static final String FACTION_COMMAND_CREATE = "f.create";
    public static final String FACTION_COMMAND_DEMOTE_OFFICIAL = "f.demote";
    public static final String FACTION_COMMAND_DISBAND = "f.disband";
    public static final String FACTION_COMMAND_INVITE = "f.invite";
    public static final String FACTION_COMMAND_KICK = "f.kick";
    public static final String FACTION_COMMAND_LEAVE = "f.leave";
    public static final String FACTION_COMMAND_PROMOTE_MEMBER = "f.promote";
    public static final String FACTION_COMMAND_TRANSFER_LEADERSHIP = "f.transfer";
    public static final String FACTION_COMMAND_ACCEPT_INVITE = "f.acceptInvite";

    private static JSONObject factionsJSONObject = null;

    // File I/O
    public static JSONObject loadFactionFile() {
        JSONParser parser = new JSONParser();
        JSONObject rv = null;
        try {
            rv = (JSONObject) parser.parse(new FileReader(Utility.getCurrentWorkingDirectory() + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER + "/" + Utility.FACTIONS_FILE));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return rv;
    }
    
    public static void saveFactionFile() {
        File factionFile = new File(Utility.getCurrentWorkingDirectory() + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER + "/" + Utility.FACTIONS_FILE);
        if (factionFile.exists()) {
            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter(Utility.getCurrentWorkingDirectory() + "/" + Utility.DATA_FOLDER + "/" + Utility.FACTIONS_FOLDER + "/" + Utility.FACTIONS_FILE);
                fileWriter.write(factionsJSONObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileWriter != null) {
                        fileWriter.flush();
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Getters/Setters
    public static JSONObject getFactionsJSONObject() {
        return factionsJSONObject;
    }

    public static void setFactionsJSONObject(JSONObject pValue) {
        factionsJSONObject = pValue;
    }

    public static void setFactionName (String pOldFactionName, String pNewFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pOldFactionName)) {
                currentFactionJSONObject.replace(FACTION_NAME_FIELD, pNewFactionName);
                saveFactionFile();
                return;
            }
        }
    }

    public static String getFactionPrefix(String pFactionName) {        
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                return currentFactionJSONObject.get(FACTION_PREFIX_FIELD).toString();
            }
        }

        return "";
    }

    public static void setFactionPrefix(String pFactionName, String pFactionPrefix) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                currentFactionJSONObject.replace(FACTION_PREFIX_FIELD, pFactionPrefix);
                saveFactionFile();
                return;
            }
        }
    }

    public static String getFactionColor(String pFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                return currentFactionJSONObject.get(FACTION_COLOR_FIELD).toString();
            }
        }

        return "";
    }

    public static void setFactionColor(String pFactionName, String pNewFactionColor) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                currentFactionJSONObject.replace(FACTION_COLOR_FIELD, pNewFactionColor);
                saveFactionFile();
                return;
            }
        }
    }

    public static String getPlayerFaction(String pPlayerUUID) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentObject : factionArray) {
            JSONObject currentFactionObject = (JSONObject) currentObject;
            JSONArray factionMembers = (JSONArray) currentFactionObject.get(FACTION_MEMBERS_FIELD);
            for (Object currentMember : factionMembers) {
                String currentUUID = currentMember.toString();
                if (currentUUID.equals(pPlayerUUID)) {
                    return currentFactionObject.get(FACTION_NAME_FIELD).toString();
                }
            }
        }
        
        return "";
    }

    public static void setPlayerFaction(String pPlayerUUID, String pFactionName) {
        if (!doesFactionExist(pFactionName)) {
            return;
        }

        // Check whether player is part of a faction
        String oldFaction = getPlayerFaction(pPlayerUUID);

        if (oldFaction.equals(pFactionName)) {
            return;
        }

        addPlayerToFaction(pPlayerUUID, pFactionName);

        if (!oldFaction.equals("")) {
            // Remove player from whatever faction they are in
            removePlayerFromFaction(pPlayerUUID, pFactionName);
        }

        saveFactionFile();
    }

    public static String getFactionLeader(String pFactionName) {
        JSONArray factionsArray = (JSONArray) factionsJSONObject.get("factions");
        for (Object currentObject : factionsArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentObject;
            if (currentFactionJSONObject.get("name").equals(pFactionName)) {
                return currentFactionJSONObject.get(FACTION_LEADER_FIELD).toString();
            }
        }

        return null;
    }

    public static void setFactionLeader (String pPlayerUUID, String pFactionName) {
        JSONArray factionsJSONArray = (JSONArray) factionsJSONObject.get("factions");
        for (Object currentObject : factionsJSONArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                currentFactionJSONObject.replace(FACTION_LEADER_FIELD, pPlayerUUID);
                saveFactionFile();
                break;
            }
        }
    }

    public static JSONArray getFactionOfficials (String pFactionName) {
        JSONArray factionsJSONArray = (JSONArray) factionsJSONObject.get("factions");
        for (Object currentObject : factionsJSONArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentObject;
            if (currentFactionJSONObject.get("name").toString().equals(pFactionName)) {
                return (JSONArray) currentFactionJSONObject.get(FACTION_OFFICIALS_FIELD);
            }
        }

        return null;
    }
    
    public static JSONArray getFactionMembers(String pFactionName) {
        JSONArray factionsJSONArray = (JSONArray) factionsJSONObject.get("factions");
        for (Object currentObject : factionsJSONArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentObject;
            if (currentFactionJSONObject.get("name").toString().equals(pFactionName)) {
                return (JSONArray) currentFactionJSONObject.get(FACTION_MEMBERS_FIELD);
            }
        }

        return null;
    }

    public static String getFactionRank(String pPlayerUUID) {
        String playerFaction = getPlayerFaction(pPlayerUUID);

        JSONArray factionsJSONArray = (JSONArray) factionsJSONObject.get("factions");
        for (Object currentObject : factionsJSONArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentObject;
            if (currentFactionJSONObject.get("name").toString().equals(playerFaction)) {
                if (currentFactionJSONObject.get("leader").toString().equals(pPlayerUUID)) {
                    return FACTION_LEADER_RANK;
                } else {
                    JSONArray currentFactionOfficialsJSONArray = (JSONArray) currentFactionJSONObject.get(FACTION_OFFICIALS_FIELD);
                    if (currentFactionOfficialsJSONArray.contains(pPlayerUUID)) {
                        return FACTION_OFFICIAL_RANK;
                    } else {
                        return FACTION_MEMBER_RANK;
                    }
                }
            }
        }
        
        return FACTION_MEMBER_RANK;
    }

    // Public
    public static boolean doesFactionExist(String pFactionName) {
        JSONArray factionsArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object factionObject : factionsArray) {
            JSONObject factionJSONObject = (JSONObject) factionObject;
            if (factionJSONObject.get(FACTION_NAME_FIELD).equals(pFactionName)) {
                return true;
            }
        }

        return false;
    }

    public static boolean doesFactionAbbreviationExist(String pFactionAbbreviation) {
        JSONArray factionsArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object factionObject : factionsArray) {
            JSONObject factionJSONObject = (JSONObject) factionObject;
            if (factionJSONObject.get(FACTION_PREFIX_FIELD).equals(pFactionAbbreviation)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPrefixValid(String pFactionPrefix) {
        if (pFactionPrefix.length() != FACTION_PREFIX_SIZE) {
            return false;
        }
        if (pFactionPrefix.equals(FACTION_NO_FACTION_PREFIX)) {
            return true;
        }

        return Pattern.compile(FACTION_PREFIX_PATTERN).matcher(pFactionPrefix).find();
    }

    public static void createFaction(String pFactionName, String pFactionPrefix) {
        // Check for existing faction and abbreviation
        if (doesFactionExist(pFactionName)) {
            return;
        }

        // Check for valid and existing prefixes
        if (!isPrefixValid(pFactionPrefix)) {
            return;
        }
        if (doesFactionAbbreviationExist(pFactionPrefix)) {
            return;
        }

        // Create faction
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        
        // Create new faction object
        JSONObject newFactionObject = new JSONObject();
        newFactionObject.put(FACTION_NAME_FIELD, pFactionName);
        newFactionObject.put(FACTION_PREFIX_FIELD, pFactionPrefix);
        newFactionObject.put(FACTION_LEADER_FIELD, "");
        newFactionObject.put(FACTION_COLOR_FIELD, FACTION_DEFAULT_COLOR);

        // Create member list
        newFactionObject.put(FACTION_MEMBERS_FIELD, new JSONArray());

        // Create officials list
        newFactionObject.put(FACTION_OFFICIALS_FIELD, new JSONArray());

        // Insert new faction at end of list
        factionArray.add(newFactionObject);

        // Save object as new faction file
        factionsJSONObject.replace(FACTIONS_FIELD, factionArray);

        saveFactionFile();
    }

    public static void deleteFaction(String pFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                factionArray.remove(currentFactionObject);
                saveFactionFile();
                return;
            }
        }
    }

    public static void invitePlayerToFaction(String pSendingPlayerUUID, String pReceivingPlayerUUID, String pFactionName) {
        addJoinableFactionToPlayer(pSendingPlayerUUID, pFactionName);

        final TextComponent message = Component.text().
            content(Bukkit.getPlayer(UUID.fromString(pSendingPlayerUUID)).getDisplayName() + 
                " has invited you to " + pFactionName + ". Click this message to accept the invite.").
                clickEvent(ClickEvent.runCommand("/" + FACTION_COMMAND_ACCEPT_INVITE + " " + pFactionName)).
            build();            
        
        Main.getAdventure().player(UUID.fromString(pReceivingPlayerUUID)).sendMessage(message);
    }

    public static void addJoinableFactionToPlayer(String pPlayerUUID, String pFactionName) {
        if (!doesFactionExist(pFactionName)) {
            return;
        }
        Main.getOnlinePlayerValues().get(pPlayerUUID).getJoinableFactions().add(pFactionName);
    }

    public static void removeJoinableFactionFromPlayer(String pPlayerUUID, String pFactionName) {
        if (!doesFactionExist(pFactionName)) {
            return;
        }
        Main.getOnlinePlayerValues().get(pPlayerUUID).getJoinableFactions().remove(pFactionName);
    }

    public static void addPlayerToFaction(String pPlayerUUID, String pFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                JSONArray factionMemberArray = (JSONArray) currentFactionJSONObject.get(FACTION_MEMBERS_FIELD);
                factionMemberArray.add(pPlayerUUID);
                saveFactionFile();
                return;
            }
        }
    }

    public static void removePlayerFromFaction(String pPlayerUUID, String pFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                JSONArray factionMemberArray = (JSONArray) currentFactionJSONObject.get(FACTION_MEMBERS_FIELD);
                factionMemberArray.remove(pPlayerUUID);
                saveFactionFile();
                return;
            }
        }
    }

    public static void addOfficialToFaction(String pPlayerUUID, String pFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                JSONArray factionOfficialsArray = (JSONArray) currentFactionJSONObject.get(FACTION_OFFICIALS_FIELD);
                factionOfficialsArray.add(pPlayerUUID);
                saveFactionFile();
                return;
            }
        }
    }

    public static void removeOfficialFromFaction(String pPlayerUUID, String pFactionName) {
        JSONArray factionArray = (JSONArray) factionsJSONObject.get(FACTIONS_FIELD);
        for (Object currentFactionObject : factionArray) {
            JSONObject currentFactionJSONObject = (JSONObject) currentFactionObject;
            if (currentFactionJSONObject.get(FACTION_NAME_FIELD).toString().equals(pFactionName)) {
                JSONArray factionOfficialsArray = (JSONArray) currentFactionJSONObject.get(FACTION_OFFICIALS_FIELD);
                factionOfficialsArray.remove(pPlayerUUID);
                saveFactionFile();
                return;
            }
        }
    }
}
