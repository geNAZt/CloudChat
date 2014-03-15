package net.cubespace.CloudChat.Module.PlayerManager;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.PlayerManager.Command.Nick;
import net.cubespace.CloudChat.Module.PlayerManager.Command.Realname;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerChangeAFKListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerNickchangeListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.PluginMessageListener;
import net.cubespace.CloudChat.Module.PlayerManager.Listener.ServerConnectListener;
import net.cubespace.PluginMessages.AFKMessage;
import net.cubespace.PluginMessages.AffixMessage;
import net.cubespace.PluginMessages.IgnoreMessage;
import net.cubespace.PluginMessages.OutputMessage;
import net.cubespace.PluginMessages.SetNickMessage;
import net.cubespace.PluginMessages.WorldMessage;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:19
 */
public class PlayerManagerModule extends Module {
    @Override
    public void onLoad() {
        //Register the PlayerManager
        plugin.getManagerRegistry().registerManager("playerManager", new PlayerManager(plugin));
    }

    @Override
    public void onEnable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("nick"))) {
            //Register the correct Binder
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("nick"), Binder.class, commandAliases.Nick.toArray(new String[0]));

            //Register this as a Command Handler
            plugin.getCommandExecutor().add(this, new Nick(plugin));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("realname"))) {
            //Register the correct Binder
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("realname"), PlayerBinder.class, commandAliases.Realname.toArray(new String[0]));

            //Register this as a Command Handler
            plugin.getCommandExecutor().add(this, new Realname(plugin));
        }

        //Register the Listener
        plugin.getAsyncEventBus().addListener(this, new PlayerJoinListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerNickchangeListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new ServerConnectListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerChangeAFKListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new AsyncChatListener(plugin));

        //Register the Packets and the Listeners
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, AffixMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, AFKMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, WorldMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, IgnoreMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, OutputMessage.class);
        plugin.getPluginMessageManager("CloudChat").addPacketToRegister(this, SetNickMessage.class);

        plugin.getPluginMessageManager("CloudChat").addListenerToRegister(this, new PluginMessageListener(plugin));
    }

    @Override
    public void onDisable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("nick"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("nick"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("realname"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("realname"));
        }

        plugin.getCommandExecutor().remove(this);

        plugin.getAsyncEventBus().removeListener(this);

        plugin.getPluginMessageManager("CloudChat").removePacket(this);
        plugin.getPluginMessageManager("CloudChat").removeListener(this);
    }
}
