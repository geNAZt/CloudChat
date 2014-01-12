package net.cubespace.CloudChat.Module.IRC.Listener;

import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.lib.Chat.MessageBuilder.LegacyMessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 20:25
 */
public class ChatMessageListener {
    private IRCModule ircModule;
    private CubespacePlugin plugin;
    private IRC config;

    public ChatMessageListener(IRCModule ircModule, CubespacePlugin plugin) {
        this.ircModule = ircModule;
        this.plugin = plugin;
        this.config = plugin.getConfigManager().getConfig("irc");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(ChatMessageEvent event) {
        if(event.getSender().getNick().equals("IRC")) return;

        LegacyMessageBuilder legacyMessageBuilder = new LegacyMessageBuilder();
        legacyMessageBuilder.setText(event.getMessage());
        String message = legacyMessageBuilder.getString();
        for(String channel : config.Channels.get(event.getSender().getChannel().Name)) {
            ircModule.getIrcBot().sendToChannel(message, channel);
        }
    }
}
