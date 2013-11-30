package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Event.CloudChatIRCChatEvent;
import net.cubespace.CloudChat.Util.MessageFormat;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.Map;

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
    public void onFormattedChat(CloudChatFormattedChatEvent event) {
        //Get the Channel
        ArrayList<ProxiedPlayer> playersInChannel = plugin.getChannelManager().getAllInChannel(event.getChannel());

        for(ProxiedPlayer proxiedPlayer : playersInChannel) {
            proxiedPlayer.sendMessage(event.getMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFormattedIRCChat(CloudChatIRCChatEvent event) {
        for(Map.Entry<String, String> channel : plugin.getIrcConfig().Channels.entries()) {
            if(channel.getValue().equals(event.getSender().getChannel())) {
                //Get the Channel
                ChannelDatabase channelDatabase = plugin.getChannelManager().get(channel.getKey());
                ArrayList<ProxiedPlayer> playersInChannel = plugin.getChannelManager().getAllInChannel(channelDatabase);
                String message = MessageFormat.format(event.getMessage(), channelDatabase, event.getSender());

                for(ProxiedPlayer proxiedPlayer : playersInChannel) {
                    proxiedPlayer.sendMessage(message);
                }
            }
        }
    }
}
