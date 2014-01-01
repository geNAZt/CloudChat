package net.cubespace.CloudChat.Module.ChannelManager;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.lib.Manager.IManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.11.13 18:48
 */
public class ChannelManager implements IManager {
    private CloudChatPlugin plugin;

    private final HashMap<ProxiedPlayer, ArrayList<ChannelDatabase>> playerJoinedChannels = new HashMap<>();
    private HashMap<ChannelDatabase, ArrayList<ProxiedPlayer>> playerInChannel = new HashMap<>();

    private HashMap<String, ChannelDatabase> loadedChannels = new HashMap<>();

    public ChannelManager(CloudChatPlugin plugin) {
        plugin.getPluginLogger().debug("Creating new ChannelManager");

        //Store the Plugin ref
        this.plugin = plugin;

        //Load the Channels
        load();
    }

    /**
     * Load Channels from Disk and store them in the Cache
     */
    private void load() {
        plugin.getPluginLogger().info("Loading Channels...");

        //Check the AsyncDatabaseLogger Directory
        File databaseDirectory = new File(plugin.getDataFolder(), "database" + File.separator + "channels" + File.separator);
        if(!databaseDirectory.exists()) {
            if(!databaseDirectory.mkdirs()) {
                plugin.getPluginLogger().error("Could not create the AsyncDatabaseLogger Directory");
                throw new RuntimeException();
            }
        }

        //Get all Files contained
        File[] databaseFiles = databaseDirectory.listFiles();

        //There are no Channels
        if(databaseFiles == null || databaseFiles.length == 0) {
            generateBasicChannels();
            return;
        }

        //Try to load all Channels
        for(File file : databaseFiles) {
            ChannelDatabase channelDatabase = new ChannelDatabase(file);

            try {
                channelDatabase.init();
                loadedChannels.put(channelDatabase.Name, channelDatabase);
                plugin.getPluginLogger().info("Loaded Channel " + channelDatabase.Name);
            } catch (Exception e) {
                plugin.getPluginLogger().error("Could not load Channel", e);
                throw new RuntimeException();
            }
        }
    }

    /**
     * Reload all Channels
     */
    public void reload() {
        loadedChannels.clear();
        load();

        HashMap<ChannelDatabase, ArrayList<ProxiedPlayer>> playerInChannel = new HashMap<>();
        for(Map.Entry<ChannelDatabase, ArrayList<ProxiedPlayer>> playersInChannel : this.playerInChannel.entrySet()) {
            for(String channelName : loadedChannels.keySet()) {
                if(playersInChannel.getKey().Name.equals(channelName)) {
                    playerInChannel.put(loadedChannels.get(channelName), playersInChannel.getValue());
                }
            }
        }

        this.playerInChannel = playerInChannel;
    }

    private void generateBasicChannels() {
        plugin.getPluginLogger().info("Creating Global channel.");

        //Only generate a Global Channel
        ChannelDatabase global = new ChannelDatabase(plugin, "global");
        global.Short = "G";
        global.Name = "global";
        global.Format = "&8[&2%channel_short&8] %prefix%nick%suffix&r: %message";
        global.Forced = true;

        try {
            global.save();
            loadedChannels.put("global", global);
        } catch (Exception e) {
            plugin.getPluginLogger().error("Could not create Basic Channel Global", e);
            throw new RuntimeException();
        }
    }

    /**
     * Get the ChannelDatabase for the Channelname
     *
     * @param channel
     * @return
     */
    public ChannelDatabase get(String channel) {
        return loadedChannels.get(channel);
    }

    /**
     * Try to join a Channel
     *
     * @param player
     * @param channel
     * @return
     */
    public boolean join(ProxiedPlayer player, ChannelDatabase channel) {
        plugin.getPluginLogger().info(player.getName() + " tried to join Channel " + channel.Name);

        //Check if Player is in the playerJoinedChannels list
        if(!playerJoinedChannels.containsKey(player)) {
            playerJoinedChannels.put(player, new ArrayList<ChannelDatabase>());
        }

        //Check if Player has enough Space in their List (Maximum Channels)
        if(playerJoinedChannels.get(player).size() == ((Main) plugin.getConfigManager().getConfig("main")).MaxChannelsPerChatter) {
            player.sendMessage("You cant join this Channel. You are already in the maximum Amount of Channels");
            plugin.getPluginLogger().info(player.getName() + " got rejected due to maximum Amount of Channels of joining Channel " + channel.Name);
            return false;
        }

        //Check if the Player has the Permission to join
        if(!player.hasPermission("cloudchat.channel." + channel.Name)) {
            player.sendMessage("You cant join this Channel. You don't have enough Permissions");
            plugin.getPluginLogger().info(player.getName() + " got rejected due to missing Permission of joining Channel " + channel.Name);
            return false;
        }

        //Check if the Channel has some Members
        if(!playerInChannel.containsKey(channel)) {
            playerInChannel.put(channel, new ArrayList<ProxiedPlayer>());
        }

        //Check if player is already in this Channel
        ArrayList<ProxiedPlayer> proxiedPlayers = playerInChannel.get(channel);
        if(!proxiedPlayers.contains(player)) {
            proxiedPlayers.add(player);

            playerJoinedChannels.get(player).add(channel);

            plugin.getPluginLogger().info("Player " + player.getName() + " entered Channel " + channel.Name);
            return true;
        }

        //Player already is in the Channel
        plugin.getPluginLogger().info("Player " + player.getName() + " is already in the Channel " + channel.Name);
        return true;
    }

    /**
     * Force the Player to join all Forced channels
     *
     * @param player
     */
    public void joinForcedChannels(ProxiedPlayer player) {
        for(Map.Entry<String, ChannelDatabase> channelDatabaseEntry : loadedChannels.entrySet()) {
            if(channelDatabaseEntry.getValue().Forced) {
                join(player, channelDatabaseEntry.getValue());
            } else if(channelDatabaseEntry.getValue().ForceIntoWhenPermission && player.hasPermission("cloudchat.channel." + channelDatabaseEntry.getValue().Name)) {
                join(player, channelDatabaseEntry.getValue());
            }
        }
    }

    /**
     * Lets the Player leave all Channels he is in and removes him from the Cache
     *
     * @param player
     */
    public void remove(ProxiedPlayer player) {
        //Check if player is in any channels
        if(!playerJoinedChannels.containsKey(player)) return;

        for(ChannelDatabase channel : new ArrayList<>(playerJoinedChannels.get(player))) {
            leave(player, channel);
        }

        playerJoinedChannels.remove(player);
    }

    /**
     * Lets the Player leave the Channel
     *
     * @param player
     * @param channelDatabase
     */
    public void leave(ProxiedPlayer player, ChannelDatabase channelDatabase) {
        playerInChannel.get(channelDatabase).remove(player);
        playerJoinedChannels.get(player).remove(channelDatabase);
        plugin.getPluginLogger().info("Player " + player.getName() + " left Channel " + channelDatabase.Name);
    }

    /**
     * Gets a Channel via its Alias
     *
     * @param chShort
     * @return
     */
    public ChannelDatabase getViaShort(String chShort) {
        for(Map.Entry<String, ChannelDatabase> channelDatabaseEntry : loadedChannels.entrySet()) {
            if(channelDatabaseEntry.getValue().Short.equalsIgnoreCase(chShort)) {
                return channelDatabaseEntry.getValue();
            }
        }

        return null;
    }

    /**
     * Get a Channel via its Name or via its Alias
     *
     * @param selectedChannel
     * @return
     */
    public ChannelDatabase getViaShortOrName(String selectedChannel) {
        if(loadedChannels.containsKey(selectedChannel.toLowerCase())) {
            return loadedChannels.get(selectedChannel.toLowerCase());
        } else {
            return getViaShort(selectedChannel);
        }
    }

    /**
     * Gets all Players currently logged into the Channel
     *
     * @param channelDatabase
     * @return
     */
    public ArrayList<ProxiedPlayer> getAllInChannel(ChannelDatabase channelDatabase) {
        ArrayList<ProxiedPlayer> returnVal = playerInChannel.get(channelDatabase);
        if(returnVal == null) return new ArrayList<>();

        return returnVal;
    }

    /**
     * Get all Channels a Player has logged into
     *
     * @param player
     * @return
     */
    public ArrayList<ChannelDatabase> getAllJoinedChannels(ProxiedPlayer player) {
        ArrayList<ChannelDatabase> returnVal = playerJoinedChannels.get(player);
        if(returnVal == null) return new ArrayList<>();

        return returnVal;
    }

    /**
     * Get all loaded Channels
     *
     * @return
     */
    public Collection<ChannelDatabase> getChannels() {
        return loadedChannels.values();
    }
}
