package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.Logging.ChatMessage;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Event.CloudChatIRCChatEvent;
import net.cubespace.CloudChat.Util.MessageFormat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 10:31
 */
public class CloudChatFormattedListener implements Listener {
    private CloudChatPlugin plugin;

    public CloudChatFormattedListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFormattedChat(final CloudChatFormattedChatEvent event) {
        //Get the Channel
        ArrayList<ProxiedPlayer> playersInChannel = plugin.getChannelManager().getAllInChannel(event.getChannel());

        for(ProxiedPlayer proxiedPlayer : playersInChannel) {
            proxiedPlayer.sendMessage(event.getMessage());
        }

        if(plugin.getDatabaseConfig().Enabled && plugin.getDatabase().getConnectionSource().isOpen()) {
            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.setSource(ChatMessage.SourceType.PLAYER);
                    chatMessage.setChannel(event.getChannel().Name);
                    chatMessage.setNick(event.getPlayer().getName());
                    chatMessage.setMessage(event.getMessage());
                    chatMessage.setDate(new Date());

                    try {
                        plugin.getDatabase().getDAO(ChatMessage.class).create(chatMessage);
                    } catch (SQLException e) {
                        plugin.getLogger().log(Level.WARNING, "Could not log Chat", e);
                    }
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFormattedIRCChat(final CloudChatIRCChatEvent event) {
        for(Map.Entry<String, String> channel : plugin.getIrcConfig().Channels.entries()) {
            if(channel.getValue().equals(event.getSender().getChannel())) {
                //Get the Channel
                final ChannelDatabase channelDatabase = plugin.getChannelManager().get(channel.getKey());
                ArrayList<ProxiedPlayer> playersInChannel = plugin.getChannelManager().getAllInChannel(channelDatabase);
                String message = MessageFormat.format(event.getMessage(), channelDatabase, event.getSender());

                for(ProxiedPlayer proxiedPlayer : playersInChannel) {
                    proxiedPlayer.sendMessage(message);
                }

                if(plugin.getDatabaseConfig().Enabled && plugin.getDatabase().getConnectionSource().isOpen()) {
                    plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                        @Override
                        public void run() {
                            ChatMessage chatMessage = new ChatMessage();
                            chatMessage.setSource(ChatMessage.SourceType.IRC);
                            chatMessage.setChannel(channelDatabase.Name);
                            chatMessage.setNick(event.getSender().getNick());
                            chatMessage.setMessage(event.getMessage());
                            chatMessage.setIrcChannel(event.getSender().getChannel());
                            chatMessage.setDate(new Date());

                            try {
                                plugin.getDatabase().getDAO(ChatMessage.class).create(chatMessage);
                            } catch (SQLException e) {
                                plugin.getLogger().log(Level.WARNING, "Could not log Chat", e);
                            }
                        }
                    });
                }
            }
        }
    }
}
