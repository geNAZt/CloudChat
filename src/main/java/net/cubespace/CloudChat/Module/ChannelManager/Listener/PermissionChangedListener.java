package net.cubespace.CloudChat.Module.ChannelManager.Listener;

import net.cubespace.CloudChat.Config.JoinOrder;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.Permission.Event.PermissionChangedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PermissionChangedListener {
    private final CubespacePlugin plugin;
    private final ChannelManager channelManager;
    private final PlayerManager playerManager;

    public PermissionChangedListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPermissionChanged(PermissionChangedEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getPlayer());
        if (player == null) return;

        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        //Check if Player still has permission for the Channels it is in
        ArrayList<ChannelDatabase> joinedChannels = channelManager.getAllJoinedChannels(player);

        boolean newFocus = false;
        for (ChannelDatabase channelDatabase : new ArrayList<>(joinedChannels)) {
            if (!channelDatabase.Forced && !plugin.getPermissionManager().has(player, "cloudchat.channel." + channelDatabase.Name)) {
                channelManager.leave(player, channelDatabase);

                if (playerDatabase.Focus.toLowerCase().equals(channelDatabase.Name.toLowerCase())) {
                    newFocus = true;
                }
            }
        }

        channelManager.joinForcedChannels(player);

        boolean focusedNew = false;
        HashSet<String> foundChannels = new HashSet<>();
        for (ChannelDatabase channelDatabase : new ArrayList<>(joinedChannels)) {
            if (channelDatabase.FocusOnJoin && !plugin.getPermissionManager().has(player, "cloudchat.ignore.focusonjoin")) {
                foundChannels.add(channelDatabase.Name);
            }
        }

        JoinOrder joinOrder = plugin.getConfigManager().getConfig("joinOrder");
        if (joinOrder.JoinOrder.containsKey(player.getServer().getInfo().getName())) {
            for (String channel : joinOrder.JoinOrder.get(player.getServer().getInfo().getName())) {
                if (foundChannels.contains(channel))  {
                    playerDatabase.Focus = channel.toLowerCase();
                    focusedNew = true;
                    break;
                }
            }
        } else {
            if (foundChannels.size() > 0) {
                playerDatabase.Focus = foundChannels.iterator().next().toLowerCase();
                focusedNew = true;
            }
        }

        Messages messages = plugin.getConfigManager().getConfig("messages");

        if (focusedNew) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Focus_FocusChannel.replace("%channel", playerDatabase.Focus))).send(player);
        } else {
            if (newFocus) {
                // Focus the global Channel as fallback
                Main config = plugin.getConfigManager().getConfig("main");
                plugin.getPluginLogger().warn("Falling back to the global Channel since there seems no FocusOnJoin Channels");

                playerDatabase.Focus = config.Global;
            }
        }

    }
}
