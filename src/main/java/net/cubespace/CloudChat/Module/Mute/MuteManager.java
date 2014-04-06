package net.cubespace.CloudChat.Module.Mute;

import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Manager.IManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class MuteManager implements IManager {
    private final HashMap<String, ArrayList<String>> playerMute = new HashMap<>();
    private final HashMap<String, Integer> globalMute = new HashMap<>();

    public MuteManager(CubespacePlugin plugin) {
        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                LinkedHashSet<String> remove = new LinkedHashSet<>();
                for (Map.Entry<String, Integer> entry : globalMute.entrySet()) {
                    if (entry.getValue() == 1) {
                        remove.add(entry.getKey());
                    } else {
                        entry.setValue(entry.getValue() - 1);
                    }
                }

                for (String remov : remove) {
                    removeGlobalMute(remov);
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void addPlayerMute(String muter, String muted) {
        if (!playerMute.containsKey(muter)) {
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

    public void addGlobalMute(String muted, int time) {
        globalMute.put(muted, time);
    }

    public boolean isGlobalMute(String muted) {
        return globalMute.containsKey(muted);
    }

    public void removeGlobalMute(String muted) {
        globalMute.remove(muted);
    }

    public void remove(String player) {
        if (playerMute.containsKey(player)) {
            playerMute.remove(player);
        }

        for (Map.Entry<String, ArrayList<String>> mutedEntry : playerMute.entrySet()) {
            if (mutedEntry.getValue().contains(player)) {
                mutedEntry.getValue().remove(player);
            }
        }

        if (globalMute.containsKey(player)) {
            globalMute.remove(player);
        }
    }

    public Integer getRestTimeGlobalMute(String name) {
        if (!isGlobalMute(name)) {
            return 0;
        }

        return globalMute.get(name);
    }
}
