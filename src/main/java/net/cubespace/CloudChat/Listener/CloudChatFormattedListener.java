package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Event.CloudChatIRCChatEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;

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
        //Get the Channel
        ArrayList<ProxiedPlayer> playersInChannel = plugin.getChannelManager().getAllInChannel(plugin.getChannelManager().get(plugin.getIrcConfig().RelayChannels));

        for(ProxiedPlayer proxiedPlayer : playersInChannel) {
            proxiedPlayer.sendMessage(event.getMessage());
        }
    }
}
