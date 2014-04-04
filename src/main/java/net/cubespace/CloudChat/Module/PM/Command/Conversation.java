package net.cubespace.CloudChat.Module.PM.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PM.CancelReason;
import net.cubespace.CloudChat.Module.PM.Event.ConversationAcceptEvent;
import net.cubespace.CloudChat.Module.PM.Event.ConversationCancelEvent;
import net.cubespace.CloudChat.Module.PM.Event.ConversationRequestEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Conversation implements CLICommand {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;

    public Conversation(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "conversation", arguments = 1)
    public void conversationCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug(sender.getName() + " wants to send a Conversation request for " + args[0]);

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("Conversation request could not be send. The sender is not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_NotPlayer)).send(sender);

            return;
        }

        ConversationRequestEvent conversationRequestEvent = new ConversationRequestEvent(sender.getName(), args[0]);
        plugin.getAsyncEventBus().callEvent(conversationRequestEvent);
    }

    @Command(command = "conversation accept", arguments = 0)
    public void conversationAccept(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug(sender.getName() + " wants to accept a Conversation request");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("Conversation request could not be accepted. The sender is not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_NotPlayer)).send(sender);

            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(sender.getName());
        if (playerDatabase.Conversation_Request.equals("")) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_NoRequest)).send(sender);

            return;
        }

        ConversationAcceptEvent conversationAcceptEvent = new ConversationAcceptEvent(playerDatabase.Conversation_Request, sender.getName());
        plugin.getAsyncEventBus().callEvent(conversationAcceptEvent);
    }

    @Command(command = "conversation cancel", arguments = 0)
    public void conversationCancel(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug(sender.getName() + " wants to cancel a Conversation request");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("Conversation cancel could not be accepted. The sender is not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_NotPlayer)).send(sender);

            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(sender.getName());
        if (playerDatabase.Conversation_Request.equals("") && playerDatabase.Conversation_Current.equals("")) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Conversation_NothingToCancel)).send(sender);

            return;
        }

        ConversationCancelEvent conversationCancelEvent = new ConversationCancelEvent((playerDatabase.Conversation_Request.equals("")) ? playerDatabase.Conversation_Current : playerDatabase.Conversation_Request, sender.getName(), CancelReason.PLAYER_CANCEL);
        plugin.getAsyncEventBus().callEvent(conversationCancelEvent);
    }
}
