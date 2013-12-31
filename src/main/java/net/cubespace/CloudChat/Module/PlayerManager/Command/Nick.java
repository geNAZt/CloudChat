package net.cubespace.CloudChat.Module.PlayerManager.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 13:13
 */
public class Nick implements CLICommand {
    private CloudChatPlugin plugin;

    public Nick(CloudChatPlugin plugin) {
        //Check if this Command is enabled
        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("nick")) {
            this.plugin = plugin;

            //Register the correct Binder
            plugin.getProxy().getPluginManager().registerCommand(plugin, new Binder(plugin, "nick"));

            //Register this as a Command Handler
            plugin.getCommandExecutor().add(this);
        }
    }

    @Command(command="nick", arguments = 1)
    public void nickCommand(CommandSender sender, String[] args) {
        plugin.getPluginLogger().debug("Got a Nickchange");

        //Check if the Sender is a Player since we only can change Players Nicknames
        if(!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But not for a Player");
            sender.sendMessage("You must be a Player to change the Nickname");
            return;
        }

        //Fire the correct Event to check if this is ok
        PlayerDatabase playerDatabase = ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).get(sender.getName());
        plugin.getAsyncEventBus().callEvent(new PlayerNickchangeEvent(sender, playerDatabase.Nick, StringUtils.join(args, " ")));
    }
}
