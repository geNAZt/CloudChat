package net.cubespace.CloudChat.Module.IRC.Commands;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.IRC.Format.MCToIrcFormat;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;

public class Players implements Command {
    private CloudChatPlugin plugin;
    private IRCModule ircModule;
    private PlayerManager playerManager;

    public Players(IRCModule ircModule, CloudChatPlugin pl) {
        plugin = pl;
        this.ircModule = ircModule;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Override
    public boolean execute(IRCSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        //Check for Permissions
        if(!ircModule.getPermissions().has(sender.getRawNick(), "command.players")) {
            ircModule.getIrcBot().sendToChannel(MCToIrcFormat.translateString(messages.IRC_Command_Players_NotEnoughPermission.replace("%nick", sender.getRawNick())), sender.getChannel());
            return true;
        }

        ircModule.getIrcBot().sendToChannel(MCToIrcFormat.translateString(messages.IRC_Command_Players_Header.replace("%count", "" + plugin.getProxy().getOnlineCount())), sender.getChannel());

        StringBuilder sb = new StringBuilder();
        Collection<ProxiedPlayer> connectedPlayers = plugin.getProxy().getPlayers();
        Integer count = 0;
        if(connectedPlayers.size() > 0) {
            for(ProxiedPlayer player : connectedPlayers) {
                PlayerDatabase playerDatabase = playerManager.get(player.getName());
                sb.append(MCToIrcFormat.translateString(MessageFormat.format(messages.IRC_Command_Players_Player, null, playerDatabase)));

                if(count == connectedPlayers.size() - 1) {
                    break;
                }

                sb.append(", ");
                count++;
            }
        }

        ircModule.getIrcBot().sendToChannel(sb.toString(), sender.getChannel());

        return true;
    }
}
