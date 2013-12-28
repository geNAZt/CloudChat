package net.cubespace.CloudChat.Module.ChannelManager.Command;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 22.12.13 12:12
 */
public class CreateChannel implements CLICommand {
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;

    public CreateChannel(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @Command(command = "createchannel", arguments = 2)
    public void createChannelCommand(CommandSender sender, String[] args) {
        String name = args[0];
        String password = args[1];

        //Check if name collides
        for(ChannelDatabase channel : channelManager.getChannels()) {
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
            plugin.getPluginLogger().warn("Error creating new Channel", e);
            return;
        }

        //Join the Channel
        channelManager.reload();

        if(sender instanceof ProxiedPlayer)
            channelManager.join((ProxiedPlayer) sender, channelDatabase);

        sender.sendMessage("Channel " + name + " was created. Password is " + password);
    }
}
