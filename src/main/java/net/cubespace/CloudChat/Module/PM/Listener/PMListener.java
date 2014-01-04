package net.cubespace.CloudChat.Module.PM.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PM.Event.PMEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.AutoComplete;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 03.01.14 21:46
 */
public class PMListener implements Listener {
    private CloudChatPlugin plugin;
    private PlayerManager playerManager;

    public PMListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPM(PMEvent event) {
        ProxiedPlayer sen = plugin.getProxy().getPlayer(event.getFrom());
        ProxiedPlayer rec = plugin.getProxy().getPlayer(event.getTo());

        rec.sendMessage(FontFormat.translateString("&6" + sen.getName() + " &8 -> &6You&8:&7 " + event.getMessage()));
        sen.sendMessage(FontFormat.translateString("&6You&8 -> &6"+ rec.getName() +"&8:&7 " + event.getMessage()));
        plugin.getPluginLogger().info(sen.getName() + " -> " + rec.getName() + ": " + event.getMessage());

        playerManager.get(sen.getName()).Reply = rec.getName();
        playerManager.get(rec.getName()).Reply = sen.getName();
    }

    @EventHandler(priority = EventPriority.HIGH, canVeto = true)
    public boolean onPM2(PMEvent event) {
        String player = event.getTo();

        ProxiedPlayer sender = plugin.getProxy().getPlayer(event.getFrom());
        if(sender == null) {
            return true;
        }

        ProxiedPlayer rec = plugin.getProxy().getPlayer(player);
        if(rec == null) {
            plugin.getPluginLogger().debug("Direct lookup returned null");

            //Check for autocomplete
            player = AutoComplete.completeUsername(player);
            rec = plugin.getProxy().getPlayer(player);

            if(rec == null) {
                plugin.getPluginLogger().debug("Autocomplete lookup returned null");
                rec = NicknameParser.getPlayer(plugin, player);

                if(rec == null) {
                    plugin.getPluginLogger().debug("Nickname parsing returned null");
                    sender.sendMessage(FontFormat.translateString("&7The player is not online"));
                    return true;
                }
            }
        }

        if (sender.equals(rec)) {
            plugin.getPluginLogger().debug("Sender and Receiver are equal.");
            sender.sendMessage(FontFormat.translateString("&7You cannot send a pm to yourself"));
            return true;
        }

        event.setTo(rec.getName());

        return false;
    }
}
