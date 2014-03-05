package net.cubespace.CloudChat.Module.ChannelManager.Listener;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Event.CheckCommandEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;

import java.util.Arrays;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class AsyncChatListener {
    private CubespacePlugin plugin;
    private ChannelManager channelManager;
    private PlayerManager playerManager;

    public AsyncChatListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onAsynChat(AsyncChatEvent event) {
        //Check if the Player has already connected to a Server (this only is null when the Connection bungee -> spigot is slow)
        if(event.getSender().getServer() == null || event.getSender().getServer().getInfo() == null) {
            return true;
        }

        //Check if player is loaded (can not be due to delay config)
        if(!playerManager.isLoaded(event.getSender().getName())) {
            return true;
        }

        //Check if this Chat gets handled by CloudChat
        return (((Main) plugin.getConfigManager().getConfig("main")).DontHandleForServers.contains(event.getSender().getServer().getInfo().getName()));
    }

    @EventHandler(priority = EventPriority.HIGH, canVeto = true)
    public boolean onCheckCommand(CheckCommandEvent event) {
        if(event.isCommand()) {
            String[] cmd = event.getMessage().split(" ");

            if(cmd.length == 1) {
                return true;
            }

            String selectedChannel = cmd[0].substring(1, cmd[0].length());
            ChannelDatabase channelDatabase = channelManager.getViaShortOrName(selectedChannel);
            if(channelDatabase != null) {
                if(channelManager.getAllInChannel(channelDatabase).contains(event.getSender())) {
                    //Format the Message
                    String message = StringUtils.join(Arrays.copyOfRange(cmd, 1, cmd.length), " ");
                    Sender sender = new Sender(event.getSender().getName(), channelDatabase, playerManager.get(event.getSender().getName()));
                    plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, message));
                    event.setCancelParent(true);

                    return true;
                }
            }

            return true;
        }

        return false;
    }
}
