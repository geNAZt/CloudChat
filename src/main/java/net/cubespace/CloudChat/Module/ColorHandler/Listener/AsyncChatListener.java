package net.cubespace.CloudChat.Module.ColorHandler.Listener;

import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:22
 */
public class AsyncChatListener {
    private CubespacePlugin plugin;

    public AsyncChatListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onAsynChat(AsyncChatEvent event) {
        //Format the Message
        if(!plugin.getPermissionManager().has(event.getSender(), "cloudchat.use.color") && !plugin.getPermissionManager().has(event.getSender(), "cloudchat.use.color.message")) {
            event.setMessage(FontFormat.stripColor(event.getMessage()));
            plugin.getPluginLogger().debug("Stripped Colors away");
        }
    }
}
