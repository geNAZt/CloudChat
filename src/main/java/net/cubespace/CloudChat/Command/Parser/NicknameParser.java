package net.cubespace.CloudChat.Command.Parser;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.12.13 22:38
 */
public class NicknameParser {
    public static ProxiedPlayer getPlayer(CloudChatPlugin plugin, String tabCompleteString) {
        PlayerManager playerManager = plugin.getManagerRegistry().getManager("playerManager");
        Messages messages = plugin.getConfigManager().getConfig("messages");

        HashMap<String, PlayerDatabase> players = playerManager.getLoadedPlayers();
        ProxiedPlayer player = null;
        for(Map.Entry<String, PlayerDatabase> playerDatabaseEntry : players.entrySet()) {
            if(MessageFormat.format(messages.Complete_Player, null, playerDatabaseEntry.getValue(), true).equals(tabCompleteString) || playerDatabaseEntry.getValue().Nick.equals(tabCompleteString)) {
                player = plugin.getProxy().getPlayer(playerDatabaseEntry.getKey());
                break;
            }
        }

        return player;
    }
}
