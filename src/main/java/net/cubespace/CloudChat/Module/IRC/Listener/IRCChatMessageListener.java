package net.cubespace.CloudChat.Module.IRC.Listener;

import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.IRC.Event.IRCChatMessageEvent;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class IRCChatMessageListener implements Listener {
    private CubespacePlugin plugin;
    private ChannelManager channelManager;

    public IRCChatMessageListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(IRCChatMessageEvent event) {
        ClickEvent clickEvent1 = new ClickEvent();
        clickEvent1.setAction(ClickAction.RUN_COMMAND);
        clickEvent1.setValue("/focus " + event.getSender().getChannel().Name);

        for(ProxiedPlayer player : channelManager.getAllInChannel(event.getSender().getChannel())) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.addEvent("focusChannel", clickEvent1);
            messageBuilder.setText(event.getMessage());
            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender()));
        }

        plugin.getPluginLogger().info(event.getMessage());
    }
}
