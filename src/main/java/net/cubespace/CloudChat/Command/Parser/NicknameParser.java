package net.cubespace.CloudChat.Command.Parser;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.CloudChat.Util.AutoComplete;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class NicknameParser {
    private static ProxiedPlayer parseNickname(CubespacePlugin plugin, String tabCompleteString) {
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

    public static ProxiedPlayer getPlayer(CubespacePlugin plugin, String player) {
        ProxiedPlayer rec = plugin.getProxy().getPlayer(player);
        if(rec == null) {
            plugin.getPluginLogger().debug("Direct lookup returned null");

            //Check for autocomplete
            player = AutoComplete.completeUsername(player);
            rec = plugin.getProxy().getPlayer(player);

            if(rec == null) {
                plugin.getPluginLogger().debug("Autocomplete lookup returned null");
                rec = parseNickname(plugin, player);

                if(rec == null) {
                    plugin.getPluginLogger().debug("Nickname parsing returned null");
                    return null;
                }
            }
        }

        return rec;
    }
}
