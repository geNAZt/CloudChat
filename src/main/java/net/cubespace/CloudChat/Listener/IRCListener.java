package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Util.MCToIrcFormat;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * Created by Fabian on 29.11.13.
 */
public class IRCListener implements Listener {
    private CloudChatPlugin plugin;

    public IRCListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatMessage(CloudChatFormattedChatEvent event) {
        if(!plugin.getIrcConfig().Enabled || !plugin.getIrcBot().isConnected()) return;
        if(!plugin.getIrcConfig().Channels.containsKey(event.getChannel().Name)) return;

        String ircMessage = MCToIrcFormat.translateString(event.getMessage());

        for(String channel : plugin.getIrcConfig().Channels.get(event.getChannel().Name)) {
            plugin.getIrcBot().sendToChannel(ircMessage, channel);
        }
    }
}
