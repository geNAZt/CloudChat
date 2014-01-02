package net.cubespace.CloudChat.Module.IRC;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Module.IRC.Bot.Bot;
import net.cubespace.CloudChat.Module.IRC.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.IRC.Listener.PlayerChangeAFKListener;
import net.cubespace.CloudChat.Module.IRC.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.IRC.Listener.PlayerQuitListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 20:18
 */
public class IRCModule {
    private Bot ircBot;

    public IRCModule(CloudChatPlugin plugin) {
        if(((IRC) plugin.getConfigManager().getConfig("irc")).Enabled) {
            plugin.getPluginLogger().info("Starting IRC Module...");

            ircBot = new Bot(this, plugin);

            plugin.getAsyncEventBus().addListener(new ChatMessageListener(this, plugin));
            plugin.getAsyncEventBus().addListener(new PlayerJoinListener(this, plugin));
            plugin.getAsyncEventBus().addListener(new PlayerQuitListener(this, plugin));
            plugin.getAsyncEventBus().addListener(new PlayerChangeAFKListener(this, plugin));

            plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "irc:reconnect"));
            plugin.getCommandExecutor().add(new net.cubespace.CloudChat.Module.IRC.Command.IRC(this, plugin));
        }
    }

    public Bot getIrcBot() {
        return ircBot;
    }

    public void setIrcBot(Bot ircBot) {
        this.ircBot = ircBot;
    }
}
