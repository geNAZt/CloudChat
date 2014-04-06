package net.cubespace.CloudChat.Module.Spam.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.Spam.SpamModule;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener implements Listener {
    private final CubespacePlugin plugin;
    private final SpamModule spamModule;

    public ChatMessageListener(SpamModule spamModule, CubespacePlugin plugin) {
        this.spamModule = spamModule;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChatMessage(ChatMessageEvent event) {
        plugin.getPluginLogger().debug("Got a ChatMessageEvent for Spam Detection");
        final ProxiedPlayer player = plugin.getProxy().getPlayer(event.getSender().getNick());
        if(player == null) return;

        spamModule.getSpamManager().addOneMessage(player);
    }
}
