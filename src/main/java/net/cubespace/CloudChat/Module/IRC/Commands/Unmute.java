package net.cubespace.CloudChat.Module.IRC.Commands;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.IRCSender;
import net.cubespace.CloudChat.Module.Mute.MuteManager;
import net.cubespace.CloudChat.Util.AutoComplete;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Unmute implements Command {
    private CloudChatPlugin plugin;
    private IRCModule ircModule;
    private MuteManager muteManager;

    public Unmute(IRCModule ircModule, CloudChatPlugin pl) {
        plugin = pl;
        this.ircModule = ircModule;
        this.muteManager = plugin.getManagerRegistry().getManager("muteManager");
    }

    @Override
    public boolean execute(IRCSender sender, String[] args) {
        //Check for Permissions
        if(!ircModule.getPermissions().has(sender.getRawNick(), "command.unmute")) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": You have not enough Permissions to execute this", sender.getChannel());
            return true;
        }

        if(args.length < 1) {
            ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": Not enough Arguments for this", sender.getChannel());
            return true;
        }

        ProxiedPlayer player = plugin.getProxy().getPlayer(args[0]);
        if(player == null) {
            plugin.getPluginLogger().debug("Direct lookup returned null");
            player = plugin.getProxy().getPlayer(AutoComplete.completeUsername(args[0]));

            if(player == null) {
                plugin.getPluginLogger().debug("Autocomplete lookup returned null");
                player = NicknameParser.getPlayer(plugin, args[0]);

                if(player == null) {
                    plugin.getPluginLogger().debug("Nickname Parser returned null");
                    ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": You can not unmute offline Players", sender.getChannel());
                    return true;
                }
            }
        }

        muteManager.removeGlobalMute(player.getName());
        ircModule.getIrcBot().sendToChannel(sender.getRawNick() + ": " + args[0] + " is unmuted", sender.getChannel());
        return true;
    }
}
