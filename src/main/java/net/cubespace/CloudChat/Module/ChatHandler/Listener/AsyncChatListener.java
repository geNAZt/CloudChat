package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.LocalPlayersRequest;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class AsyncChatListener {
    private final CubespacePlugin plugin;
    private final ChannelManager channelManager;
    private final PlayerManager playerManager;

    public AsyncChatListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsynChat(AsyncChatEvent event) {
        ChannelDatabase channelDatabase = channelManager.get(playerManager.get(event.getSender().getName()).Focus);

        if (channelDatabase.IsLocal) {
            plugin.getPluginMessageManager("CloudChat").sendPluginMessage(plugin.getProxy().getPlayer(event.getSender().getName()), new LocalPlayersRequest(event.getMessage(), channelDatabase.Name, channelDatabase.LocalRange));
            return;
        }

        Sender sender = new Sender(event.getSender().getName(), channelDatabase, playerManager.get(event.getSender().getName()));

        plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, event.getMessage(), new ArrayList<String>(){{
            add("§ALL");
        }}));
    }
}
