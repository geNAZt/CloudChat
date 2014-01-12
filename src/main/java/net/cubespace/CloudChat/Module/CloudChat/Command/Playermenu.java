package net.cubespace.CloudChat.Module.CloudChat.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.Mute.MuteManager;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 07.01.14 20:30
 */
public class Playermenu implements CLICommand {
    private CubespacePlugin plugin;
    private MuteManager muteManager;
    private PlayerManager playerManager;

    public Playermenu(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.muteManager = plugin.getManagerRegistry().getManager("muteManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @Command(command = "cc:playermenu", arguments = 1)
    public void reloadCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        boolean muted = muteManager.isPlayerMute(sender.getName(), args[0]);

        String message = messages.Playermenu_Prefix;

        if(plugin.getPermissionManager().has(sender, "cloudchat.command.mute") && !muted) {
            message += messages.Playermenu_Mute;
        } else if(plugin.getPermissionManager().has(sender, "cloudchat.command.unmute") && muted) {
            message += messages.Playermenu_Unmute;
        }

        message += messages.Playermenu_Message;

        ClickEvent clickEvent = new ClickEvent();
        clickEvent.setValue("/mute " + args[0]);
        clickEvent.setAction(ClickAction.RUN_COMMAND);

        ClickEvent clickEvent2 = new ClickEvent();
        clickEvent2.setValue("/unmute " + args[0]);
        clickEvent2.setAction(ClickAction.RUN_COMMAND);

        ClickEvent clickEvent3 = new ClickEvent();
        clickEvent3.setValue("/msg " + args[0] + " ");
        clickEvent3.setAction(ClickAction.SUGGEST_COMMAND);

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.addEvent("unMute", clickEvent2);
        messageBuilder.addEvent("mute", clickEvent);
        messageBuilder.addEvent("message", clickEvent3);
        messageBuilder.setText(MessageFormat.format(message, null, playerManager.get(args[0]))).send(sender);
    }
}