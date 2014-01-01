package net.cubespace.CloudChat.Module.IRC.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Event.PlayerJoinEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 15:50
 */
public class PlayerJoinListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;
    private ChannelManager channelManager;
    private IRCModule ircModule;

    public PlayerJoinListener(IRCModule ircModule, CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.ircModule = ircModule;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        IRC config = plugin.getConfigManager().getConfig("irc");

        for(ChannelDatabase channel : channelManager.getAllJoinedChannels(event.getPlayer())) {
            for(Map.Entry<String, String> ircChannel : config.Channels.entries()) {
                String message = MessageFormat.format(((Main) plugin.getConfigManager().getConfig("main")).Announce_PlayerJoinMessage, channel, playerDatabase);

                if(ircChannel.getKey().equals(channel.Name)) {
                    ircModule.getIrcBot().sendToChannel(message, ircChannel.getValue());
                    break;
                }
            }
        }
    }
}
