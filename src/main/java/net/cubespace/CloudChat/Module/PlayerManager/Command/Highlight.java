package net.cubespace.CloudChat.Module.PlayerManager.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.StringUtils;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Highlight implements CLICommand {
    private CubespacePlugin plugin;
    private PlayerManager playerManager;

    public Highlight(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @Command(command = "highlight on", arguments = 0)
    public void highlightOn(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Highlight on");
        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        playerManager.get(sender.getName()).Highlight = true;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Enabled)).send(sender);
    }

    @Command(command = "highlight off", arguments = 0)
    public void highlightOf(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Highlight off");
        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        playerManager.get(sender.getName()).Highlight = false;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Disabled)).send(sender);
    }

    @Command(command = "highlight sound on", arguments = 0)
    public void highlightSoundOn(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Highlight Sound on");
        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        playerManager.get(sender.getName()).HighlightSoundEnabled = true;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Sound_Enabled)).send(sender);
    }

    @Command(command = "highlight sound off", arguments = 0)
    public void highlightSoundOff(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Highlight Sound off");
        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        playerManager.get(sender.getName()).HighlightSoundEnabled = false;

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Sound_Disabled)).send(sender);
    }

    @Command(command = "highlight sound", arguments = 1)
    public void highlightSoundSet(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Highlight Sound set " + args[0]);
        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        playerManager.get(sender.getName()).HighlightSound = args[0].toUpperCase();

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Sound_Changed.replace("%sound", args[0]))).send(sender);
    }

    @Command(command = "highlight list", arguments = 0)
    public void highlightList(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Highlight list");
        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Header)).send(sender);

        for (String pattern : playerManager.get(sender.getName()).HighlightPattern) {
            MessageBuilder messageBuilder2 = new MessageBuilder();
            messageBuilder2.setText(pattern).send(sender);
        }
    }

    @Command(command = "highlight remove", arguments = 1)
    public void highlightRemove(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");
        plugin.getPluginLogger().debug("Got a Highlight remove");

        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        String remove = StringUtils.join(args, " ");
        playerManager.get(sender.getName()).HighlightPattern.remove(remove);

        playerManager.get(sender.getName()).PatternCache = new ArrayList<>();
        for (String pattern : playerManager.get(sender.getName()).HighlightPattern) {
            playerManager.get(sender.getName()).PatternCache.add(Pattern.compile(pattern));
        }

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Removed)).send(sender);
    }

    @Command(command = "highlight add", arguments = 1)
    public void highlightAdd(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");
        plugin.getPluginLogger().debug("Got a Highlight add");

        if (!(sender instanceof ProxiedPlayer)) {
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_NotPlayer)).send(sender);
            return;
        }

        String remove = StringUtils.join(args, " ");
        playerManager.get(sender.getName()).HighlightPattern.add(remove);

        playerManager.get(sender.getName()).PatternCache = new ArrayList<>();
        for (String pattern : playerManager.get(sender.getName()).HighlightPattern) {
            playerManager.get(sender.getName()).PatternCache.add(Pattern.compile(pattern));
        }

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setText(FontFormat.translateString(messages.Command_Highlight_Added)).send(sender);
    }
}
