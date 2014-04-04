package net.cubespace.CloudChat.Module.ChannelManager.Listener;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Event.AsyncChatEvent;
import net.cubespace.CloudChat.Event.CheckCommandEvent;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PM.CancelReason;
import net.cubespace.CloudChat.Module.PM.Event.ConversationCancelEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.PluginMessages.AFKMessage;
import net.cubespace.PluginMessages.LocalPlayersRequest;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
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
            if (cmd.length == 0) return false;

            String selectedChannel = cmd[0].substring(1, cmd[0].length());
            ChannelDatabase channelDatabase = channelManager.getViaShortOrName(selectedChannel);
            
            // Focus
            if(cmd.length == 1 && ((Main) plugin.getConfigManager().getConfig("main")).UseChannelShortFocus) {
                Messages messages = plugin.getConfigManager().getConfig("messages");
                if(channelDatabase == null) {
                    return false;
                }

                if(playerManager.get(event.getSender().getName()).AFK) {
                    plugin.getPluginMessageManager("CloudChat").sendPluginMessage(event.getSender(), new AFKMessage(false));
                }

                ArrayList<ProxiedPlayer> playersInChannel = channelManager.getAllInChannel(channelDatabase);
                if(!playersInChannel.contains(event.getSender())) {
                    return false;
                }

                PlayerDatabase playerDatabase = playerManager.get(event.getSender().getName());
                playerDatabase.Focus = channelDatabase.Name.toLowerCase();

                if (!playerDatabase.Conversation_Current.equals("")) {
                    PlayerDatabase other = playerManager.get(playerDatabase.Conversation_Current);
                    ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent(playerDatabase.Realname, playerDatabase.Conversation_Current, CancelReason.PLAYER_CANCEL);
                    plugin.getAsyncEventBus().callEvent(conversationCancelEvent);

                    other.Conversation_Current = "";
                    playerDatabase.Conversation_Current = "";
                }

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Focus_FocusChannel.replace("%channel", channelDatabase.Name))).send(event.getSender());

                event.setCancelParent(true);
                return true;
            }

            // Message
            if(cmd.length > 1 && channelDatabase != null) {
                if(channelManager.getAllInChannel(channelDatabase).contains(event.getSender())) {
                    if(playerManager.get(event.getSender().getName()).AFK) {
                        plugin.getPluginMessageManager("CloudChat").sendPluginMessage(event.getSender(), new AFKMessage(false));
                    }

                    //Format the Message
                    String message = StringUtils.join(Arrays.copyOfRange(cmd, 1, cmd.length), " ");

                    //JR start
                    if (channelDatabase.IsLocal) {
                        plugin.getPluginMessageManager("CloudChat").sendPluginMessage(plugin.getProxy().getPlayer(event.getSender().getName()), new LocalPlayersRequest(message, channelDatabase.Name, channelDatabase.LocalRange));
                    } else {
                        Sender sender = new Sender(event.getSender().getName(), channelDatabase, playerManager.get(event.getSender().getName()));
                        plugin.getAsyncEventBus().callEvent(new ChatMessageEvent(sender, message, new ArrayList<String>(){{
                            add("§ALL");
                        }}
                        ));
                    }
                    //JR end

                    event.setCancelParent(true);
                    return true;
                }
            }

            return false;
        }

        return false;
    }
}
