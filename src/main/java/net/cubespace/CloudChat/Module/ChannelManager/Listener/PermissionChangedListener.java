package net.cubespace.CloudChat.Module.ChannelManager.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.Permission.Event.PermissionChangedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:22
 */
public class PermissionChangedListener {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;

    public PermissionChangedListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermissionChanged(PermissionChangedEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getPlayer());

        if(player == null) return;

        //Check if Player still has permission for the Channels it is in
        ArrayList<ChannelDatabase> joinedChannels = channelManager.getAllJoinedChannels(player);

        for(ChannelDatabase channelDatabase : new ArrayList<>(joinedChannels)) {
            if(!plugin.getPermissionManager().has(player, "cloudchat.channel." + channelDatabase.Name)) {
                channelManager.leave(player, channelDatabase);
            }
        }

        channelManager.joinForcedChannels(player);
    }
}
