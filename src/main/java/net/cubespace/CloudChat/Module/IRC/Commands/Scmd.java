package net.cubespace.CloudChat.Module.IRC.Commands;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.PluginMessages.DispatchScmdMessage;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Arrays;

public class Scmd implements Command {
    private CloudChatPlugin plugin;
    private IRCModule ircModule;

    public Scmd(IRCModule ircModule, CloudChatPlugin pl) {
        plugin = pl;
        this.ircModule = ircModule;
    }

    @Override
    public boolean execute(IRCSender sender, String[] args) {
        //Check for Permissions
        if(!ircModule.getPermissions().has(sender.getRawNick(), "command.scmd")) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": You have not enough Permissions to execute this", sender.getChannel());
            return true;
        }

        if(args.length < 2) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": Not enough Arguments for this", sender.getChannel());
            return true;
        }

        //Check for Server and command permission
        String server = args[0];
        String command = args[1];

        if(!ircModule.getPermissions().has(sender.getRawNick(), "command.scmd.server." + server)) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": You dont't have Permission to execute this on that Server", sender.getChannel());
            return true;
        }

        if(!ircModule.getPermissions().has(sender.getRawNick(), "command.scmd.command." + command)) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": You dont't have Permission to execute that Scmd Command", sender.getChannel());
            return true;
        }

        //Check if there is a Player online on the wanted Server
        ServerInfo serverInfo = plugin.getProxy().getServerInfo(server);

        if(serverInfo == null) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": The Server you have given is not valid", sender.getChannel());
            return true;
        }


        if(serverInfo.getPlayers().size() == 0) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": Can't send the Command. It needs to be at least one Player online on the Server", sender.getChannel());
            return true;
        }

        //Create a new SCMD Session
        String scommand = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
        plugin.getPluginLogger().info("Issuing SCMD '" + scommand + "' on " + args[0] + " for " + sender.getNick());
        Integer sessionId = ircModule.getIrcBot().getIrcManager().newScmdSession(sender.getRawNick(), sender.getChannel(), scommand);

        DispatchScmdMessage dispatchScmdMessage = new DispatchScmdMessage(scommand, sessionId);
        plugin.getPluginMessageManager("CloudChat").sendPluginMessage(serverInfo.getPlayers().iterator().next(), dispatchScmdMessage);

        ircModule.getIrcBot().sendToChannel("Command has been issued", sender.getChannel());
        return true;
    }
}
