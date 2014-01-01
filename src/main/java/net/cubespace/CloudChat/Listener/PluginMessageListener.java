package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 11:57
 */
public class PluginMessageListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;
    private ChannelManager channelManager;

    public PluginMessageListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
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

        PlayerDatabase playerDatabase = playerManager.get(player.getName());

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
                ArrayList<ProxiedPlayer> sent = new ArrayList<>();

                for(ChannelDatabase channel1 : channelManager.getAllJoinedChannels(player)) {
                    ArrayList<ProxiedPlayer> inChannel = channelManager.getAllInChannel(channel1);
                    String message = MessageFormat.format(((Main) plugin.getConfigManager().getConfig("main")).Announce_PlayerGotAfk, channel1, playerDatabase);
                    Sender sender = new Sender(player.getName(), channel1, playerDatabase);

                    for(ProxiedPlayer player1 : inChannel) {
                        if(sent.contains(player1)) continue;

                        plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, message, sender));
                        sent.add(player1);
                    }
                }

                sent.clear();
            } else if(!afk && playerDatabase.AFK) {
                //The Player has got out of AFK
                ArrayList<ProxiedPlayer> sent = new ArrayList<>();

                for(ChannelDatabase channel1 : channelManager.getAllJoinedChannels(player)) {
                    ArrayList<ProxiedPlayer> inChannel = channelManager.getAllInChannel(channel1);
                    String message = MessageFormat.format(((Main) plugin.getConfigManager().getConfig("main")).Announce_PlayerGotOutOfAfk, channel1, playerDatabase);
                    Sender sender = new Sender(player.getName(), channel1, playerDatabase);

                    for(ProxiedPlayer player1 : inChannel) {
                        if(sent.contains(player1)) continue;

                        plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, message, sender));
                        sent.add(player1);
                    }
                }

                sent.clear();
            }

            playerDatabase.AFK = afk;
        }

        //Factions
        if (channel.equalsIgnoreCase("FactionChat")) {
            ChannelDatabase channelDatabase = channelManager.get(playerDatabase.Focus);
            String message = in.readUTF();
            Sender sender = new Sender(player.getName(), channelDatabase, playerDatabase);
            plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, message));
        }
    }
}
