package net.cubespace.CloudChat.Command;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;


/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 22.12.13 12:12
 */
public class CreateChannel implements CLICommand {
    private CloudChatPlugin plugin;

    public CreateChannel(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "createchannel", arguments = 2)
    public void createChannelCommand(CommandSender sender, String[] args) {
        String name = args[0];
        String password = args[1];

        //Check if name collides
        for(ChannelDatabase channel : plugin.getChannelManager().getChannels()) {
            if(channel.Name.equalsIgnoreCase(name)) {
                sender.sendMessage("This Channel already exists");
                return;
            }
        }

        //Get the Alias
        String alias = name.substring(0 , 1).toUpperCase();

        //Save the new Channel
        ChannelDatabase channelDatabase = new ChannelDatabase(plugin, name);
        channelDatabase.Name = name;
        channelDatabase.Password = password;
        channelDatabase.Forced = false;
        channelDatabase.Format = "&8[&2%channel_short&8] %prefix%nick%suffix&r: %message";
        channelDatabase.Short = alias;
        channelDatabase.CanInvite.add(sender.getName());
        try {
            channelDatabase.save();
        } catch (InvalidConfigurationException e) {
            sender.sendMessage("There was an error in the creation of your Channel. Please report this to a Admin");
            e.printStackTrace();
            return;
        }

        //Join the Channel
        plugin.getChannelManager().reload();

        if(sender instanceof ProxiedPlayer)
            plugin.getChannelManager().join((ProxiedPlayer) sender, channelDatabase);

        sender.sendMessage("Channel " + name + " was created. Password is " + password);
    }
}
