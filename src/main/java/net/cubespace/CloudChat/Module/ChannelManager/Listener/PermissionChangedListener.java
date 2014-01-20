package net.cubespace.CloudChat.Module.ChannelManager.Listener;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
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
    private CubespacePlugin plugin;
    private ChannelManager channelManager;
    private PlayerManager playerManager;

    public PermissionChangedListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPermissionChanged(PermissionChangedEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getPlayer());

        if(player == null) return;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        //Check if Player still has permission for the Channels it is in
        ArrayList<ChannelDatabase> joinedChannels = channelManager.getAllJoinedChannels(player);

        boolean newFocus = false;
        for(ChannelDatabase channelDatabase : new ArrayList<>(joinedChannels)) {
            if(!plugin.getPermissionManager().has(player, "cloudchat.channel." + channelDatabase.Name)) {
                channelManager.leave(player, channelDatabase);

                if(playerDatabase.Focus.toLowerCase().equals(channelDatabase.Name.toLowerCase())) {
                    newFocus = true;
                }
            }
        }

        channelManager.joinForcedChannels(player);

        boolean focusedNew = false;
        for(ChannelDatabase channelDatabase : new ArrayList<>(joinedChannels)) {
            if(channelDatabase.FocusOnJoin) {
                playerDatabase.Focus = channelDatabase.Name.toLowerCase();
                focusedNew = true;
                break;
            }

            if(newFocus && (channelDatabase.Forced || channelDatabase.ForceIntoWhenPermission)) {
                playerDatabase.Focus = channelDatabase.Name.toLowerCase();
                focusedNew = true;
            }
        }

        if(focusedNew) {
            Messages messages = plugin.getConfigManager().getConfig("messages");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(messages.Command_Channel_Focus_FocusChannel.replace("%channel", playerDatabase.Focus)).send(player);
        }
    }
}
