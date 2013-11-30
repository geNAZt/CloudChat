package net.cubespace.CloudChat.Listener;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 11:57
 */
public class PluginMessageListener implements Listener {
    private CloudChatPlugin plugin;

    public PluginMessageListener(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void receivePluginMessage(PluginMessageEvent event) throws IOException {
        if (!event.getTag().equalsIgnoreCase("CloudChat")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String channel = in.readUTF();

        //Load the Player
        ProxiedPlayer player = plugin.getProxy().getPlayer(in.readUTF());
        if (player == null) {
            return;
        }

        //New pair of Pre/Suffix
        if (channel.equalsIgnoreCase("Affix")) {
            PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);

            playerDatabase.Prefix = in.readUTF();
            playerDatabase.Suffix = in.readUTF();
        }
    }
}
