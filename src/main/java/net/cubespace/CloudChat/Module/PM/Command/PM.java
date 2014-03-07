package net.cubespace.CloudChat.Module.PM.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;

public class PM implements CLICommand {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;

    public PM(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "msg", arguments = 2)
    public void msgCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a new PM");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But sender was not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Msg_NotPlayer)).send(sender);

            return;
        }

        ProxiedPlayer sen = (ProxiedPlayer) sender;

        String message = FontFormat.translateString(StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " "));
        PMEvent event = new PMEvent(sen.getName(), args[0], message);
        plugin.getAsyncEventBus().callEvent(event);
    }

    @Command(command = "reply", arguments = 1)
    public void replyCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a PM Reply");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But it was not send from a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Reply_NotPlayer)).send(sender);

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        if(playerDatabase.Reply.equals("")) {
            plugin.getPluginLogger().debug("Can't be replied because the Player has no conversation up");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Reply_NoConversation)).send(sender);

            return;
        }

        String message = FontFormat.translateString(StringUtils.join(args, " "));
        PMEvent event = new PMEvent(player.getName(), playerDatabase.Reply, message);
        plugin.getAsyncEventBus().callEvent(event);
    }

    @Command(command = "togglepm", arguments = 0)
    public void togglepmCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a PM toggle");

        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But it was not send from a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Reply_NotPlayer)).send(sender);

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;
        PlayerDatabase playerDatabase = playerManager.get(player.getName());

        playerDatabase.IgnorePM = !playerDatabase.IgnorePM;
        MessageBuilder messageBuilder = new MessageBuilder();

        if (playerDatabase.IgnorePM) {
            messageBuilder.setText(FontFormat.translateString(messages.Command_TogglePM_YouNowIgnore)).send(sender);
        } else {
            messageBuilder.setText(FontFormat.translateString(messages.Command_TogglePM_YouDontIgnore)).send(sender);
        }
    }
}
