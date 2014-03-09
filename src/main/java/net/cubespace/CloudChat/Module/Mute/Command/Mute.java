package net.cubespace.CloudChat.Module.Mute.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.Mute.MuteModule;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Mute implements CLICommand {
    private CubespacePlugin plugin;
    private MuteModule muteModule;

    public Mute(MuteModule muteModule, CubespacePlugin plugin) {
        this.plugin = plugin;
        this.muteModule = muteModule;
    }

    @Command(command = "mute", arguments = 1)
    public void muteCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a mute Request");

        if (!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But the sender is not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Mute_NotPlayer)).send(sender);
            return;
        }

        ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
        if (player == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Mute_OfflinePlayer)).send(sender);

            return;
        }

        if(plugin.getPermissionManager().has(player, "cloudchat.cannot.muted")) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Mute_CannotBeMuted)).send(sender);

            return;
        }

        muteModule.getMuteManager().addPlayerMute(sender.getName(), player.getName());

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Mute_Success.replace("%player", player.getName()).replace("%nick", player.getDisplayName()))).send(sender);

        plugin.getPluginLogger().info(sender.getName() + " muted " + player.getName());
    }

    @Command(command = "unmute", arguments = 1)
    public void unMuteCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a unmute Request");

        if (!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But the sender is not a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Unmute_NotPlayer)).send(sender);

            return;
        }

        ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
        if (player == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Unmute_OfflinePlayer.replace("%player", args[0]))).send(sender);

            return;
        }

        muteModule.getMuteManager().removePlayerMute(sender.getName(), player.getName());
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Unmute_Success.replace("%player", player.getName()))).send(sender);

        plugin.getPluginLogger().info(sender.getName() + " unmuted " + player.getName());
    }

    @Command(command = "cc:mute", arguments = 1)
    public void ccMuteCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a global mute Request");

        ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
        if (player == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_CC_Mute_OfflinePlayer.replace("%player", args[0]))).send(sender);
            return;
        }

        int timedMute = 0;
        if (args.length > 1) {
            timedMute = StringUtils.getTime(args[1]);
        }

        muteModule.getMuteManager().addGlobalMute(player.getName(), timedMute);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_CC_Mute_Success.replace("%player", player.getName()))).send(sender);
        plugin.getPluginLogger().info(sender.getName() + " globally muted " + player.getName());
    }

    @Command(command = "cc:unmute", arguments = 1)
    public void ccUnMuteCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a global unmute Request");

        ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
        if (player == null) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_CC_Unmute_OfflinePlayer.replace("%player", args[0]))).send(sender);
            return;
        }

        muteModule.getMuteManager().removeGlobalMute(player.getName());
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_CC_Unmute_Success.replace("%player", player.getName()))).send(sender);

        plugin.getPluginLogger().info(sender.getName() + " globally unmuted " + player.getName());
    }
}
