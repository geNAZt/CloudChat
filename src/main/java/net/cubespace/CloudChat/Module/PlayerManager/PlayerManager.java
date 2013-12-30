package net.cubespace.CloudChat.Module.PlayerManager;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.lib.Manager.IManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:50
 */
public class PlayerManager implements IManager {
    private CloudChatPlugin plugin;

    private HashMap<String, PlayerDatabase> loadedPlayers = new HashMap<>();

    public PlayerManager(CloudChatPlugin plugin) {
        this.plugin = plugin;
        plugin.getPluginLogger().debug("Created new PlayerManager");
    }

    /**
     * Gets a PlayerDatabase for the Name given
     *
     * @param player
     * @return
     */
    public PlayerDatabase get(String player) {
        plugin.getPluginLogger().debug("Getting PlayerDatabase for " + player);

        return loadedPlayers.get(player);
    }

    /**
     * Get all loaded Players
     * @return
     */
    public HashMap<String, PlayerDatabase> getLoadedPlayers() {
        return new HashMap<>(loadedPlayers);
    }

    /**
     * Removes the Player from the Cache and saves its AsyncDatabaseLogger
     *
     * @param player
     */
    public void remove(ProxiedPlayer player) {
        plugin.getPluginLogger().debug("Removing PlayerDatabase for " + player.getName());

        if(loadedPlayers.containsKey(player.getName())) {
            plugin.getPluginLogger().info("Saving PlayerDatabase for " + player.getName());

            try {
                loadedPlayers.get(player.getName()).Reply = "";

                //Get the Channels the Player is in
                loadedPlayers.get(player.getName()).JoinedChannels = new ArrayList<>();
                ChannelManager channelManager =  plugin.getManagerRegistry().getManager("channelManager");
                for(ChannelDatabase channelDatabase : channelManager.getAllJoinedChannels(player)) {
                    loadedPlayers.get(player.getName()).JoinedChannels.add(channelDatabase.Name);
                }

                loadedPlayers.get(player.getName()).save();
                loadedPlayers.remove(player.getName());
            } catch (Exception e) {
                plugin.getPluginLogger().error("Could not save PlayerDatabase for " + player.getName(), e);
                throw new RuntimeException();
            }
        }
    }

    /**
     * Load a Player from the Filesystem in the Cache
     *
     * @param player
     */
    public void load(String player) {
        plugin.getPluginLogger().debug("Check for load of PlayerDatabase for " + player);
        if(!loadedPlayers.containsKey(player)) {
            plugin.getPluginLogger().info("Loading PlayerDatabase for " + player);
            PlayerDatabase playerDatabase = new PlayerDatabase(plugin, player);

            try {
                playerDatabase.init();
                loadedPlayers.put(player, playerDatabase);
            } catch (Exception e) {
                plugin.getPluginLogger().error("Could not init PlayerDatabase for " + player, e);
                throw new RuntimeException();
            }
        }
    }
}
