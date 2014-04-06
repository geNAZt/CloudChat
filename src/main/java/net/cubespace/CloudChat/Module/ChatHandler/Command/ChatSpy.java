package net.cubespace.CloudChat.Module.ChatHandler.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
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
public class ChatSpy implements CLICommand {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;

    public ChatSpy(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @SuppressWarnings("UnusedParameters")
    @Command(command = "chatspy", arguments = 0)
    public void chatSpyCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But sender was not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_ChatSpy_NotPlayer)).send(sender);

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        if(playerDatabase.ChatSpy) {
            playerDatabase.ChatSpy = false;
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_ChatSpy_Disabled)).send(sender);
        } else {
            playerDatabase.ChatSpy = true;
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_ChatSpy_Enabled)).send(sender);
        }
    }
}
