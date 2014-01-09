package net.cubespace.CloudChat.Module.ChannelManager.Command;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
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
        Messages messages = plugin.getConfigManager().getConfig("messages");

        String name = args[0];
        String password = args[1];

        //Check if name collides
        for(ChannelDatabase channel : channelManager.getChannels()) {
            if(channel.Name.equalsIgnoreCase(name)) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Create_AlreadyExists)).send(sender);

                return;
            }
        }

        //Get the Alias
        String alias = name.substring(0 , 1).toUpperCase();

        //Save the new Channel
        ChannelDatabase channelDatabase = new ChannelDatabase(plugin, name.toLowerCase());
        channelDatabase.Name = name;
        channelDatabase.Password = password;
        channelDatabase.Forced = false;
        channelDatabase.Format = "&8[&2%channel_short&8] %prefix%nick{click:playerMenu}%suffix&r: %message";
        channelDatabase.Short = alias;
        channelDatabase.CanInvite.add(sender.getName());

        try {
            channelDatabase.save();
        } catch (InvalidConfigurationException e) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Create_ErrorInSave)).send(sender);
            plugin.getPluginLogger().warn("Error creating new Channel", e);
            return;
        }

        //Join the Channel
        channelManager.reload();

        sender.setPermission("cloudchat.channel." + channelDatabase.Name, true);
        if(sender instanceof ProxiedPlayer)
            channelManager.join((ProxiedPlayer) sender, channelDatabase);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Channel_Create_CreatedChannel.replace("%channel", channelDatabase.Name).replace("%password", channelDatabase.Password))).send(sender);
    }
}
