package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Util.MessageFormat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 11:57
 */
public class PluginMessageListener implements Listener {
    private CloudChatPlugin plugin;

    public PluginMessageListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void receivePluginMessage(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equalsIgnoreCase("CloudChat")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();

        //Load the Player
        ProxiedPlayer player = plugin.getProxy().getPlayer(in.readUTF());
        if (player == null) {
            return;
        }

        PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);

        //New pair of Pre/Suffix
        if (channel.equalsIgnoreCase("Affix")) {
            playerDatabase.Prefix = in.readUTF();
            playerDatabase.Suffix = in.readUTF();
        }

        //New World
        if (channel.equalsIgnoreCase("World")) {
            playerDatabase.World = in.readUTF();
            playerDatabase.WorldAlias = in.readUTF();
        }

        //AFK
        if (channel.equalsIgnoreCase("AFK")) {
            boolean afk = in.readBoolean();

            if(afk && !playerDatabase.AFK) {
                //The Player has gone AFK
                for(String inGameChannel : playerDatabase.JoinedChannels) {
                    ChannelDatabase channelDatabase = plugin.getChannelManager().get(inGameChannel);
                    String message = MessageFormat.format(plugin.getConfig().Announce_PlayerGotAfk, channelDatabase, playerDatabase, true);
                    CloudChatFormattedChatEvent cloudChatFormattedChatEvent = new CloudChatFormattedChatEvent(message, channelDatabase, player);
                    plugin.getProxy().getPluginManager().callEvent(cloudChatFormattedChatEvent);
                }
            } else if(!afk && playerDatabase.AFK) {
                //The Player has got out of AFK
                for(String inGameChannel : playerDatabase.JoinedChannels) {
                    ChannelDatabase channelDatabase = plugin.getChannelManager().get(inGameChannel);
                    String message = MessageFormat.format(plugin.getConfig().Announce_PlayerGotOutOfAfk, channelDatabase, playerDatabase, true);
                    CloudChatFormattedChatEvent cloudChatFormattedChatEvent = new CloudChatFormattedChatEvent(message, channelDatabase, player);
                    plugin.getProxy().getPluginManager().callEvent(cloudChatFormattedChatEvent);
                }
            }

            playerDatabase.AFK = afk;
        }

        //Factions
        if (channel.equalsIgnoreCase("FactionChat")) {
            ChannelDatabase channelDatabase = plugin.getChannelManager().get(playerDatabase.Focus);
            String message = MessageFormat.format(in.readUTF(), channelDatabase, playerDatabase, true);
            CloudChatFormattedChatEvent cloudChatFormattedChatEvent = new CloudChatFormattedChatEvent(message, channelDatabase, player);
            plugin.getProxy().getPluginManager().callEvent(cloudChatFormattedChatEvent);
        }
    }
}
