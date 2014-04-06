package net.cubespace.CloudChat.Module.Spam;

import net.cubespace.CloudChat.Config.Spam;
import net.cubespace.CloudChat.Config.Sub.SpamEntry;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Manager.IManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class SpamManager implements IManager {
    private final CubespacePlugin plugin;
    private final HashMap<String, HashMap<SpamEntry, SpamCounter>> playerSpamCounter = new HashMap<>();

    public SpamManager(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    public void prepareSpamEntry(ProxiedPlayer player) {
        plugin.getPluginLogger().debug("Creating new SpamManager Entry for " + player.getName());

        ArrayList<SpamEntry> spamEntries = ((Spam) plugin.getConfigManager().getConfig("spam")).SpamRules;
        HashMap<SpamEntry, SpamCounter> spamCounterHashMap = new HashMap<>();

        for(SpamEntry spamEntry : spamEntries) {
            if(!plugin.getPermissionManager().has(player, spamEntry.ExcludePermission)) {
                plugin.getPluginLogger().debug("Player does not have " + spamEntry.ExcludePermission + " adding Spam Rule Entry");
                spamCounterHashMap.put(spamEntry, new SpamCounter());
            }
        }

        playerSpamCounter.put(player.getName(), spamCounterHashMap);
    }

    public void removeSpamEntries(ProxiedPlayer player) {
        plugin.getPluginLogger().debug("Removing Player from the Spam Manager " + player.getName());
        playerSpamCounter.remove(player.getName());
    }

    public synchronized HashMap<String, HashMap<SpamEntry, SpamCounter>> getPlayerSpamCounter() {
        return new HashMap<>(playerSpamCounter);
    }

    public synchronized void addOneMessage(final ProxiedPlayer player) {
        if(!playerSpamCounter.containsKey(player.getName())) return;

        plugin.getPluginLogger().debug("Adding a Message for " + player.getName());

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

    @SuppressWarnings("WeakerAccess")
    public synchronized void removeOneMessage(ProxiedPlayer player, SpamEntry spamEntry) {
        if(!playerSpamCounter.containsKey(player.getName())) return;

        plugin.getPluginLogger().debug("Removing a Message for " + player.getName());
        playerSpamCounter.get(player.getName()).get(spamEntry).removeOne();
    }
}
