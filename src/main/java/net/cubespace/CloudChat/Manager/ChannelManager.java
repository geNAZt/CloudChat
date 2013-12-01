package net.cubespace.CloudChat.Manager;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.util.*;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.11.13 18:48
 */
public class ChannelManager {
    private HashMap<String, ChannelDatabase> loadedChannels = new HashMap<>();
    private HashMap<ChannelDatabase, ArrayList<ProxiedPlayer>> playerInChannel = new HashMap<>();
    private CloudChatPlugin plugin;
    private Collection<ChannelDatabase> channels;

    public ChannelManager(CloudChatPlugin plugin) {
        //Store the Plugin ref
        this.plugin = plugin;

        load();
    }

    private void load() {
        //Check the Database Directory
        File databaseDirectory = new File(plugin.getDataFolder(), "database" + File.separator + "channels" + File.separator);
        if(!databaseDirectory.exists()) {
            if(!databaseDirectory.mkdirs()) {
                throw new RuntimeException("Could not create the Database Directory");
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
            } catch (Exception e) {
                throw new RuntimeException("Could not load Channel", e);
            }
        }
    }

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
            throw new RuntimeException("Could not create Basic Channel Global", e);
        }
    }

    public ChannelDatabase get(String channel) {
        return loadedChannels.get(channel);
    }

    public boolean join(ProxiedPlayer player, ChannelDatabase channel) {
        PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);

        if(playerDatabase.JoinedChannels.size() == plugin.getConfig().MaxChannelsPerChatter) {
            player.sendMessage("You cant join this Channel. You are already in the maximum Amount of Channels");
            return false;
        }

        if(!player.hasPermission("cloudchat.channel." + channel.Name)) {
            player.sendMessage("You cant join this Channel. You dont have enough Permissions");
            return false;
        }

        if(!playerInChannel.containsKey(channel)) {
            playerInChannel.put(channel, new ArrayList<ProxiedPlayer>());
        }

        ArrayList<ProxiedPlayer> proxiedPlayers = playerInChannel.get(channel);

        if(!proxiedPlayers.contains(player)) {
            proxiedPlayers.add(player);

            if(!playerDatabase.JoinedChannels.contains(channel.Name)) {
                playerDatabase.JoinedChannels.add(channel.Name);
            }

            plugin.getLogger().info("Player " + player.getName() + " entered Channel " + channel.Name);
            return true;
        }

        return true;
    }

    public void joinForcedChannels(ProxiedPlayer player) {
        for(Map.Entry<String, ChannelDatabase> channelDatabaseEntry : loadedChannels.entrySet()) {
            if(channelDatabaseEntry.getValue().Forced) {
                join(player, channelDatabaseEntry.getValue());
            }
        }
    }

    public void remove(ProxiedPlayer player) {
        PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);

        for(String channel : playerDatabase.JoinedChannels) {
            leave(player, get(channel));
        }
    }

    public void leave(ProxiedPlayer player, ChannelDatabase channelDatabase) {
        playerInChannel.get(channelDatabase).remove(player);
        plugin.getLogger().info("Player " + player.getName() + " left Channel " + channelDatabase.Name);
    }

    public ChannelDatabase getViaShort(String chShort) {
        for(Map.Entry<String, ChannelDatabase> channelDatabaseEntry : loadedChannels.entrySet()) {
            if(channelDatabaseEntry.getValue().Short.equalsIgnoreCase(chShort)) {
                return channelDatabaseEntry.getValue();
            }
        }

        return null;
    }

    public ChannelDatabase getViaShortOrName(String selectedChannel) {
        if(loadedChannels.containsKey(selectedChannel.toLowerCase())) {
            return loadedChannels.get(selectedChannel.toLowerCase());
        } else {
            return getViaShort(selectedChannel);
        }
    }

    public ArrayList<ProxiedPlayer> getAllInChannel(ChannelDatabase channelDatabase) {
        ArrayList<ProxiedPlayer> returnVal = playerInChannel.get(channelDatabase);
        if(returnVal == null) return new ArrayList<>();

        return returnVal;
    }

    public Collection<ChannelDatabase> getChannels() {
        return loadedChannels.values();
    }
}
