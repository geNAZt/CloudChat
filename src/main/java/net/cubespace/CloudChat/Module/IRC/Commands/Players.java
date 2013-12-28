package net.cubespace.CloudChat.Module.IRC.Commands;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;

/**
 * Created by Fabian on 29.11.13.
 */
public class Players implements Command {
    private CloudChatPlugin plugin;
    private IRCModule ircModule;

    public Players(IRCModule ircModule, CloudChatPlugin pl) {
        plugin = pl;
        this.ircModule = ircModule;
    }

    @Override
    public boolean execute(IRCSender sender, String[] args) {
        ircModule.getIrcBot().sendToChannel("Current Players online (" + plugin.getProxy().getOnlineCount() + "): ", sender.getChannel());

        StringBuilder sb = new StringBuilder();
        Collection<ProxiedPlayer> connectedPlayers = plugin.getProxy().getPlayers();
        Integer count = 0;
        if(connectedPlayers.size() > 0) {
            for(ProxiedPlayer player : connectedPlayers) {
                sb.append(player.getDisplayName());

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
