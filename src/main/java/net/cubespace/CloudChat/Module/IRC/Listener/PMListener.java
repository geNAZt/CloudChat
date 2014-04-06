package net.cubespace.CloudChat.Module.IRC.Listener;

import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.IRC.PMSession;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMListener implements Listener {
    private final CubespacePlugin plugin;
    private final IRCModule ircModule;
    private final PlayerManager playerManager;

    public PMListener(IRCModule ircModule, CubespacePlugin plugin) {
        this.plugin = plugin;
        this.ircModule = ircModule;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onPM(PMEvent event) {
        IRC config = plugin.getConfigManager().getConfig("irc");

        if(FontFormat.stripColor(event.getFrom()).contains(FontFormat.stripColor(config.IngameName))) {
            Messages messages = plugin.getConfigManager().getConfig("messages");

            //Check if there is a " " in the name
            String ircNick;
            if(event.getFrom().contains(config.IngameName)) {
                ircNick = FontFormat.stripColor(event.getFrom().replace(config.IngameName, ""));
            } else {
                ircNick = FontFormat.stripColor(event.getMessage().replace(config.IngameName, "").split(" ")[0]);
            }

            ProxiedPlayer sen = plugin.getProxy().getPlayer(event.getTo());

            if(sen == null) {
                ircModule.getIrcBot().sendToChannel("Player " + event.getTo() + " is offline", ircNick);
                return true;
            }

            if(ircModule.getIrcBot().getIrcManager().hasPmSession(ircNick)) {
                PMSession pmSession = ircModule.getIrcBot().getIrcManager().getPmSession(ircNick);
                pmSession.setTo(sen.getName());
            } else {
                ircModule.getIrcBot().getIrcManager().newPMSession(ircNick);
                PMSession pmSession = ircModule.getIrcBot().getIrcManager().getPmSession(ircNick);
                pmSession.setTo(sen.getName());
            }

            playerManager.get(sen.getName()).Reply = FontFormat.stripColor(config.IngameName) + ircNick;
            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Message_Receiver.replace("%sender", config.IngameName + ircNick).replace("%message", event.getMessage().replace(ircNick + " ", ""))));
            messageBuilder.send(sen);

            plugin.getPluginLogger().info(event.getFrom() + " -> " + event.getTo() + ": " + event.getMessage().replace(ircNick + " ", ""));

            return true;
        }

        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, canVeto = true)
    public boolean onPM2(PMEvent event) {
        IRC config = plugin.getConfigManager().getConfig("irc");

        if(FontFormat.stripColor(event.getTo()).contains(FontFormat.stripColor(config.IngameName))) {
            Messages messages = plugin.getConfigManager().getConfig("messages");

            //Check if there is a " " in the name
            String ircNick;
            if(event.getTo().contains(config.IngameName)) {
                ircNick = FontFormat.stripColor(event.getTo().replace(config.IngameName, ""));
            } else {
                ircNick = FontFormat.stripColor(event.getMessage().replace(config.IngameName, "").split(" ")[0]);
            }

            event.setTo(config.IngameName + ircNick);
            ProxiedPlayer sen = plugin.getProxy().getPlayer(event.getFrom());

            //Check if sender can do this
            if(!plugin.getPermissionManager().has(sen, "cloudchat.pm.irc")) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Message_NoIrcNick));
                messageBuilder.send(sen);

                return true;
            }

            if(ircModule.getIrcBot().getIrcManager().isNickOnline(ircNick)) {
                if(ircModule.getIrcBot().getIrcManager().hasPmSession(ircNick)) {
                    PMSession pmSession = ircModule.getIrcBot().getIrcManager().getPmSession(ircNick);
                    pmSession.setTo(sen.getName());
                } else {
                    ircModule.getIrcBot().getIrcManager().newPMSession(ircNick);
                    PMSession pmSession = ircModule.getIrcBot().getIrcManager().getPmSession(ircNick);
                    pmSession.setTo(sen.getName());
                }

                playerManager.get(sen.getName()).Reply = FontFormat.stripColor(config.IngameName) + ircNick;

                ircModule.getIrcBot().sendToChannel(event.getFrom() + ": " + event.getMessage().replace(ircNick + " ", ""), ircNick);

                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(messages.Message_Sender.replace("%receiver", config.IngameName + " " + ircNick).replace("%message", event.getMessage().replace(ircNick + " ", "")));
                messageBuilder.send(sen);

                plugin.getPluginLogger().info(event.getFrom() + " -> " + event.getTo() + ": " + event.getMessage().replace(ircNick + " ", ""));
            } else {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Message_IrcNickNotOnline));
                messageBuilder.send(sen);
            }

            return true;
        }

        return false;
    }
}
