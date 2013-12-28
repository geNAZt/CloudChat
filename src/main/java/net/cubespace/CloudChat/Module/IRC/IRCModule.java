package net.cubespace.CloudChat.Module.IRC;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.IRC.Bot.Bot;
import net.cubespace.CloudChat.Module.IRC.Listener.ChatMessageListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 20:18
 */
public class IRCModule {
    private Bot ircBot;

    public IRCModule(CloudChatPlugin plugin) {
        if(((IRC) plugin.getConfigManager().getConfig("irc")).Enabled) {
            plugin.getPluginLogger().info("IRC is enabled. Starting bot...");

            ircBot = new Bot(this, plugin);


            plugin.getAsyncEventBus().addListener(new ChatMessageListener(this, plugin));
        }
    }

    public Bot getIrcBot() {
        return ircBot;
    }

    public void setIrcBot(Bot ircBot) {
        this.ircBot = ircBot;
    }
}
