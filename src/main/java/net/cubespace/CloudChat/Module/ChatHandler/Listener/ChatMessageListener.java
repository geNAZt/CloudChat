package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.PluginMessages.LocalPlayersRequest;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ChatMessageListener implements Listener
{

    private CubespacePlugin plugin;
    private ChannelManager channelManager;
    //JR start
    private Main mainConfig;
    //JR end
    
    public ChatMessageListener(CubespacePlugin plugin)
    {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
        //JR start
        this.mainConfig = plugin.getConfigManager().getConfig("main");
        //JR end
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatMessage(ChatMessageEvent event)
    {
        //JR start
        // Check if local is enabled && the channel 
        // is a local channel (ranged)
        // else send regular global chat 
        if (mainConfig.LocalChat && event.getSender().getChannel().IsLocal) {
            onLocalChatMessage(event);
        }
        else {
            onGlobalChatMessage(event);
        }
        //JR end
        plugin.getPluginLogger().info(event.getMessage());
    }

    public void onLocalChatMessage(ChatMessageEvent event)
    {
        //JR start
        // Simplified the handling of the range (Okay, would have made packets slightly bigger and I didnt feel right about it.
        // You might have a magical idea to make it work nicely with multiple ranges.
        // That being said, local is usually local. One range for all.
        plugin.getPluginMessageManager("CloudChat").sendPluginMessage(plugin.getProxy().getPlayer(event.getSender().getNick()), new LocalPlayersRequest(event.getMessage(), mainConfig.LocalRange));
        //JR end
    }

    public void onGlobalChatMessage(ChatMessageEvent event)
    {
        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setAction(ClickAction.RUN_COMMAND);
        clickEvent.setValue("/cc:playermenu " + event.getSender().getNick());

        ClickEvent clickEvent1 = new ClickEvent();
        clickEvent1.setAction(ClickAction.RUN_COMMAND);
        clickEvent1.setValue("/focus " + event.getSender().getChannel().Name);
        for (ProxiedPlayer player : channelManager.getAllInChannel(event.getSender().getChannel())) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.addEvent("playerMenu", clickEvent).addEvent("focusChannel", clickEvent1);
            messageBuilder.setText(event.getMessage());
            plugin.getAsyncEventBus().callEvent(new PlayerSendMessageEvent(player, messageBuilder, event.getSender()));
        }
    }
}
