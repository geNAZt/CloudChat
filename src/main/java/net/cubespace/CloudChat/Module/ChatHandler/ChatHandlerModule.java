package net.cubespace.CloudChat.Module.ChatHandler;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChatHandler.Command.Broadcast;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerChangeAFKListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PlayerSendMessageListener;
import net.cubespace.CloudChat.Module.ChatHandler.Listener.PluginMessageListener;
import net.cubespace.PluginMessages.FactionChatMessage;
import net.cubespace.PluginMessages.SendChatMessage;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatHandlerModule extends Module {
    private ChatBuffer chatBuffer;

    public ChatHandlerModule(CubespacePlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {
        this.chatBuffer = new ChatBuffer();
    }

    @Override
    public void onEnable() {
        Main config = plugin.getConfigManager().getConfig("main");

        //Register Command
        if(!config.DoNotBind.contains("broadcast")) {
            plugin.getBindManager().bind("broadcast", Binder.class);
        }

        plugin.getCommandExecutor().add(this, new Broadcast(plugin));

        //Register the Listener
        if(config.Announce_PlayerJoin)
            plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(plugin));

        if(config.Announce_PlayerQuit)
            plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(plugin));

        plugin.getAsyncEventBus().addListener(this, new PlayerChangeAFKListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new AsyncChatListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new ChatMessageListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerSendMessageListener(this, plugin));

        //Register PluginMessages and a Listener for them
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, FactionChatMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, SendChatMessage.class);
        plugin.getPluginMessageManager("CloudChat").addListenerToRegister(this, new PluginMessageListener(plugin));
    }

    @Override
    public void onDisable() {
        if(plugin.getBindManager().isBound("broadcast")) {
            plugin.getBindManager().unbind("broadcast");
        }

        plugin.getCommandExecutor().remove(this);

        //Remove all Listeners
        plugin.getAsyncEventBus().removeListener(this);

        //Remove all PluginMessages
        plugin.getPluginMessageManager("CloudChat").removeListener(this);
        plugin.getPluginMessageManager("CloudChat").removePacket(this);
    }

    public ChatBuffer getChatBuffer() {
        return chatBuffer;
    }
}


