package net.cubespace.CloudChat.Command.Parser;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.BungeeCord;
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
        Main config = plugin.getConfigManager().getConfig("main");

        HashMap<String, PlayerDatabase> players = playerManager.getLoadedPlayers();
        ProxiedPlayer player = null;
        for(Map.Entry<String, PlayerDatabase> playerDatabaseEntry : players.entrySet()) {
            if(MessageFormat.format(config.Complete_Player, null, playerDatabaseEntry.getValue()).equals(tabCompleteString)) {
                player = BungeeCord.getInstance().getPlayer(playerDatabaseEntry.getKey());
                break;
            }
        }

        return player;
    }
}
