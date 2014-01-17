package net.cubespace.CloudChat.Module.IRC.Commands;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.IRC.Format.MCToIrcFormat;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Message implements Command {
    private CubespacePlugin plugin;
    private IRCModule ircModule;

    public Message(IRCModule ircModule, CubespacePlugin pl) {
        plugin = pl;
        this.ircModule = ircModule;
    }

    @Override
    public boolean execute(IRCSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        //Check for Permissions
        if (!ircModule.getPermissions().has(sender.getRawNick(), "command.message")) {
            ircModule.getIrcBot().sendToChannel(MCToIrcFormat.translateString(messages.IRC_Command_Message_NotEnoughPermission.replace("%nick", sender.getRawNick())), sender.getChannel());
            return true;
        }

        if (args.length < 1) {
            ircModule.getIrcBot().sendToChannel(MCToIrcFormat.translateString(messages.IRC_Command_Message_NotEnoughArguments.replace("%nick", sender.getRawNick())), sender.getChannel());
            return true;
        }

        ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
        if (player == null) {
            ircModule.getIrcBot().sendToChannel(MCToIrcFormat.translateString(messages.IRC_Command_Message_OfflinePlayer.replace("%nick", sender.getRawNick())), sender.getChannel());
            return true;
        }

        if (ircModule.getIrcBot().getIrcManager().hasPmSession(sender.getRawNick())) {
            ircModule.getIrcBot().getIrcManager().getPmSession(sender.getRawNick()).setTo(player.getName());
        } else {
            ircModule.getIrcBot().getIrcManager().newPMSession(sender.getRawNick());
            ircModule.getIrcBot().getIrcManager().getPmSession(sender.getRawNick()).setTo(player.getName());
        }

        return true;
    }
}
