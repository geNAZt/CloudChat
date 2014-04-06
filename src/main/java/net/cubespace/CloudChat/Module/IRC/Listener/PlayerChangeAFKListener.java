package net.cubespace.CloudChat.Module.IRC.Listener;

import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerChangeAFKEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerChangeAFKListener implements Listener {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;
    private final ChannelManager channelManager;
    private final IRCModule ircModule;

    public PlayerChangeAFKListener(IRCModule ircModule, CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.ircModule = ircModule;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerAFKChange(PlayerChangeAFKEvent event) {
        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        IRC config = plugin.getConfigManager().getConfig("irc");

        for (ChannelDatabase channel : channelManager.getAllJoinedChannels(event.getPlayer())) {
            String ircChannel = config.Channels.get(channel.Name);
            if (ircChannel == null) continue;

            LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
            if (event.isAfk()) {
                legacyMessageBuilder.setText(MessageFormat.format(((Messages) plugin.getConfigManager().getConfig("messages")).PlayerGotAfk, channel, playerDatabase));
            } else {
                legacyMessageBuilder.setText(MessageFormat.format(((Messages) plugin.getConfigManager().getConfig("messages")).PlayerGotOutOfAfk, channel, playerDatabase));
            }

            ircModule.getIrcBot().sendToChannel(legacyMessageBuilder.getString(), ircChannel);
        }
    }
}
