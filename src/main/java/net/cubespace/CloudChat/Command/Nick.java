package net.cubespace.CloudChat.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.cubespace.CloudChat.Util.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 13:13
 */
public class Nick implements CLICommand {
    private CloudChatPlugin plugin;

    public Nick(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command="nick", arguments = 1)
    public void nickCommand(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("You must be a Player to change the Nickname");
            return;
        }

        PlayerDatabase playerDatabase = plugin.getPlayerManager().get((ProxiedPlayer) sender);
        playerDatabase.Nick = StringUtils.join(args, " ");
        sender.sendMessage("You changed your Nickname to: " + playerDatabase.Nick);
    }
}
