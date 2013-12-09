package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Util.MessageFormat;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.11.13 11:24
 */
public class PlayerQuitListener implements Listener {
    private final CloudChatPlugin plugin;

    public PlayerQuitListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDisconnect(final PlayerDisconnectEvent event) {
        //Unload the Player
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                final PlayerDatabase playerDatabase = plugin.getPlayerManager().get(event.getPlayer());
                plugin.getChannelManager().remove(event.getPlayer());
                plugin.getPlayerManager().remove(event.getPlayer());

                if(plugin.getConfig().Announce_PlayerQuit) {
                    //Get the global Channel to print the join message
                    final ChannelDatabase channelDatabase = plugin.getChannelManager().get("global");
                    final String formattedString = MessageFormat.format(plugin.getConfig().Announce_PlayerQuitMessage, channelDatabase, playerDatabase, true);
                    plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                        @Override
                        public void run() {
                            CloudChatFormattedChatEvent cloudChatFormattedChatEvent = new CloudChatFormattedChatEvent(formattedString, channelDatabase, event.getPlayer());
                            plugin.getProxy().getPluginManager().callEvent(cloudChatFormattedChatEvent);
                        }
                    }, 1, TimeUnit.SECONDS);
                }
            }
        });
    }
}
