package net.cubespace.CloudChat.Module.Spam;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Spam;
import net.cubespace.CloudChat.Config.Sub.SpamEntry;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 01:08
 */
public class SpamManager {
    private CloudChatPlugin plugin;
    private HashMap<String, HashMap<SpamEntry, SpamCounter>> playerSpamCounter = new HashMap<>();

    public SpamManager(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    public void prepareSpamEntry(ProxiedPlayer player) {
        ArrayList<SpamEntry> spamEntries = ((Spam) plugin.getConfigManager().getConfig("spam")).SpamRules;
        HashMap<SpamEntry, SpamCounter> spamCounterHashMap = new HashMap<>();

        for(SpamEntry spamEntry : spamEntries) {
            if(!player.hasPermission(spamEntry.ExcludePermission)) {
                spamCounterHashMap.put(spamEntry, new SpamCounter());
            }
        }

        playerSpamCounter.put(player.getName(), spamCounterHashMap);
    }

    public void removeSpamEntries(ProxiedPlayer player) {
        playerSpamCounter.remove(player.getName());
    }

    public synchronized HashMap<String, HashMap<SpamEntry, SpamCounter>> getPlayerSpamCounter() {
        return new HashMap<>(playerSpamCounter);
    }

    public synchronized void addOneMessage(final ProxiedPlayer player) {
        if(!playerSpamCounter.containsKey(player.getName())) return;

        for(final Map.Entry<SpamEntry, SpamCounter> spamCounterEntry : playerSpamCounter.get(player.getName()).entrySet()) {
            spamCounterEntry.getValue().addOne();

            final SpamManager spamManager = this;
            plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    spamManager.removeOneMessage(player, spamCounterEntry.getKey());
                }
            }, spamCounterEntry.getKey().InHowMuchSeconds, TimeUnit.SECONDS);
        }
    }

    public synchronized void removeOneMessage(ProxiedPlayer player, SpamEntry spamEntry) {
        if(!playerSpamCounter.containsKey(player.getName())) return;

        playerSpamCounter.get(player.getName()).get(spamEntry).removeOne();
    }
}
