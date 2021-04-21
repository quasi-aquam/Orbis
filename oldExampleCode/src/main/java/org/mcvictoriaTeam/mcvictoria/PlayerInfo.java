package org.mcvictoriaTeam.mcvictoria;

import java.util.ArrayList;
import java.util.List;

public class PlayerInfo {
    List<String> joinableFactions = null;

    PlayerInfo() {
        joinableFactions = new ArrayList<String>();
    }

    public List<String> getJoinableFactions() {
        return joinableFactions;
    }

    public void setJoinableFactions(List<String> pJoinableFactions) {
        joinableFactions = pJoinableFactions;
    }
}