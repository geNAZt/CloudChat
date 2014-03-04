package net.cubespace.CloudChat.Module.ColorHandler.Listener;

import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMListener {
    private CubespacePlugin plugin;

    public PMListener(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPM(PMEvent event) {
        //Format the Message
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getFrom());

        if(!plugin.getPermissionManager().has(player, "cloudchat.use.color") && !plugin.getPermissionManager().has(player, "cloudchat.use.color.message")) {
            event.setMessage(FontFormat.stripColor(event.getMessage()));
            plugin.getPluginLogger().debug("Stripped Colors away");
        }
    }
}
