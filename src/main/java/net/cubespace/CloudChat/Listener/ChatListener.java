package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.Event.CloudChatFormattedChatEvent;
import net.cubespace.CloudChat.Util.MessageFormat;
import net.cubespace.CloudChat.Util.StringUtils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Arrays;

/**
 * Created by Fabian on 28.11.13.
 */
public class ChatListener implements Listener {
    private CloudChatPlugin plugin;

    public ChatListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(ChatEvent event) {
        //Check if sender is a Player
        if(!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        //Load the Player
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        //Check if his Server gets handled via CloudChat
        if(plugin.getConfig().DontHandleForServers.contains(player.getServer().getInfo().getName()) ||
           plugin.getConfig().FactionServers.contains(player.getServer().getInfo().getName())) {
            return;
        }

        PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);

        //Check if Chat is a Command
        if(event.isCommand()) {
            String[] cmd = event.getMessage().split(" ");

            if(cmd.length == 1) {
                return;
            }

            String selectedChannel = cmd[0].substring(1, cmd[0].length());
            ChannelDatabase channelDatabase = plugin.getChannelManager().getViaShortOrName(selectedChannel);
            if(channelDatabase != null) {
                if(plugin.getChannelManager().getAllInChannel(channelDatabase).contains(player)) {
                    //Format the Message
                    String[] msgParts = Arrays.copyOfRange(cmd, 1, cmd.length);
                    String message = MessageFormat.format(StringUtils.join(msgParts, " "), channelDatabase, playerDatabase);
                    plugin.getProxy().getPluginManager().callEvent(new CloudChatFormattedChatEvent(message, channelDatabase, player));

                    event.setCancelled(true);
                }
            }

            return;
        }

        //For da focus chat
        ChannelDatabase channelDatabase = plugin.getChannelManager().get(playerDatabase.Focus);
        String message = MessageFormat.format(event.getMessage(), channelDatabase, playerDatabase);
        plugin.getProxy().getPluginManager().callEvent(new CloudChatFormattedChatEvent(message, channelDatabase, player));
    }
}
