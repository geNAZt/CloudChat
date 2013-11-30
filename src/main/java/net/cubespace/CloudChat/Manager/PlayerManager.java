package net.cubespace.CloudChat.Manager;

import net.craftminecraft.bungee.bungeeyaml.bukkitapi.InvalidConfigurationException;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.11.13 19:43
 */
public class PlayerManager {
    private CloudChatPlugin plugin;
    private HashMap<ProxiedPlayer, PlayerDatabase> loadedPlayers = new HashMap<>();

    public PlayerManager(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    public PlayerDatabase get(ProxiedPlayer player) {
        if(!loadedPlayers.containsKey(player)) {
            PlayerDatabase playerDatabase = new PlayerDatabase(plugin, player.getName());

            try {
                playerDatabase.init();
                loadedPlayers.put(player, playerDatabase);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException("Could not init PlayerDatabase for " + player.getName(), e);
            }
        }

        return loadedPlayers.get(player);
    }

    public void remove(ProxiedPlayer player) {
        if(loadedPlayers.containsKey(player)) {
            try {
                loadedPlayers.get(player).Reply = "";
                loadedPlayers.get(player).save();
                loadedPlayers.remove(player);
            } catch (InvalidConfigurationException e) {
                throw new RuntimeException("Could not save PlayerDatabase for " + player.getName(), e);
            }
        }
    }
}
