package net.cubespace.CloudChat.Module.Mute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 22:37
 */
public class MuteManager {
    private HashMap<String, ArrayList<String>> playerMute = new HashMap<>();
    private ArrayList<String> globalMute = new ArrayList<>();

    public void addPlayerMute(String muter, String muted) {
        if(!playerMute.containsKey(muter)) {
            playerMute.put(muter, new ArrayList<String>());
        }

        playerMute.get(muter).add(muted);
    }

    public boolean isPlayerMute(String muter, String muted) {
        return playerMute.containsKey(muter) && playerMute.get(muter).contains(muted);
    }

    public void removePlayerMute(String muter, String muted) {
        playerMute.get(muter).remove(muted);
    }

    public void addGlobalMute(String muted) {
        globalMute.add(muted);
    }

    public boolean isGlobalMute(String muted) {
        return globalMute.contains(muted);
    }

    public void removeGlobalMute(String muted) {
        globalMute.remove(muted);
    }

    public void remove(String player) {
        if(playerMute.containsKey(player)) {
            playerMute.remove(player);
        }

        for(Map.Entry<String, ArrayList<String>> mutedEntry : playerMute.entrySet()) {
            if(mutedEntry.getValue().contains(player)) {
                mutedEntry.getValue().remove(player);
            }
        }

        if(globalMute.contains(player)) {
            globalMute.remove(player);
        }
    }
}
