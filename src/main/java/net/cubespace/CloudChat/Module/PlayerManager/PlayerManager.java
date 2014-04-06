package net.cubespace.CloudChat.Module.PlayerManager;

import net.cubespace.CloudChat.Config.UUIDMappings;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Util.FeatureDetector;
import net.cubespace.Yamler.Config.InvalidConfigurationException;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Manager.IManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerManager implements IManager {
    private final CubespacePlugin plugin;

    private final HashMap<String, PlayerDatabase> loadedPlayers = new HashMap<>();
    private boolean savingMappings = false;

    public PlayerManager(CubespacePlugin plugin) {
        this.plugin = plugin;
        plugin.getPluginLogger().debug("Created new PlayerManager");
    }

    public PlayerDatabase get(String player) {
        plugin.getPluginLogger().debug("Getting PlayerDatabase for " + player);

        return loadedPlayers.get(player);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isLoaded(String player) {
        return loadedPlayers.containsKey(player);
    }

    public HashMap<String, PlayerDatabase> getLoadedPlayers() {
        return new HashMap<>(loadedPlayers);
    }

    public void remove(ProxiedPlayer player) {
        plugin.getPluginLogger().debug("Removing PlayerDatabase for " + player);

        if (loadedPlayers.containsKey(player.getName())) {
            plugin.getPluginLogger().info("Saving PlayerDatabase for " + player);

            loadedPlayers.get(player.getName()).Reply = "";

            //Get the Channels the Player is in
            loadedPlayers.get(player.getName()).JoinedChannels = new ArrayList<>();
            ChannelManager channelManager = plugin.getManagerRegistry().getManager("channelManager");
            for (ChannelDatabase channelDatabase : channelManager.getAllJoinedChannels(player)) {
                loadedPlayers.get(player.getName()).JoinedChannels.add(channelDatabase.Name);
            }

            loadedPlayers.get(player.getName()).PatternCache = new ArrayList<>();

            save(player.getName());
        }
    }

    public void save(String player) {
        try {
            loadedPlayers.get(player).save();
            loadedPlayers.remove(player);
        } catch (Exception e) {
            plugin.getPluginLogger().error("Could not save PlayerDatabase for " + player, e);
            throw new RuntimeException();
        }
    }

    public void load(String player) {
        String storageKey = player;
        if (FeatureDetector.canUseUUID()) {
            UUIDMappings mappings = plugin.getConfigManager().getConfig("uuidMappings");
            storageKey = mappings.NameToUUID.get(player);

            if (storageKey == null) {
                return;
            }
        }

        load(storageKey, player);
    }

    public void load(ProxiedPlayer player) {
        load((FeatureDetector.canUseUUID()) ? FeatureDetector.getUUID(player) : player.getName(), player.getName());
    }

    @SuppressWarnings("WeakerAccess")
    public void load(String storageKey, String player) {
        plugin.getPluginLogger().debug("Check for load of PlayerDatabase for " + player);
        if (!loadedPlayers.containsKey(player)) {
            plugin.getPluginLogger().info("Loading PlayerDatabase for " + player);

            if (storageKey.length() > 4) {
                // Check if there is a pre v7 Database
                File check = new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + player + ".yml");
                if (check.exists()) {
                    String folder = storageKey.substring(0, 2);

                    File newFile = new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + folder + File.separator + storageKey + ".yml");

                    if (newFile.exists()) {
                        if (!newFile.delete()) {
                            plugin.getPluginLogger().warn("Could not delete new Database but a old one was found. There is something wrong here");
                        }
                    }

                    try {
                        if (!newFile.getParentFile().exists()) {
                            if (!newFile.getParentFile().mkdirs()) {
                                plugin.getPluginLogger().warn("Could not create StorageKey folder");
                            }
                        }

                        Files.copy(check.toPath(), newFile.toPath());

                        if (!check.delete()) {
                            check.deleteOnExit();
                        }
                    } catch (IOException e) {
                        plugin.getPluginLogger().error("Could not convert a PlayerDatabase", e);
                    }
                }
            }

            PlayerDatabase playerDatabase = new PlayerDatabase(plugin, storageKey, player);

            // Update the Mapping if needed
            if (FeatureDetector.canUseUUID()) {
                final UUIDMappings mappings = plugin.getConfigManager().getConfig("uuidMappings");

                if (mappings.UUIDToName.containsKey(storageKey)) {
                    // Update the name
                    if (!mappings.UUIDToName.get(storageKey).equals(player)) {
                        mappings.NameToUUID.remove(mappings.UUIDToName.get(storageKey));
                        mappings.NameToUUID.put(player, storageKey);
                        mappings.UUIDToName.put(storageKey, player);

                        if (!savingMappings) {
                            savingMappings = true;

                            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mappings.save();
                                    } catch (InvalidConfigurationException e) {
                                        plugin.getPluginLogger().error("Could not save mappings");
                                    }

                                    savingMappings = false;
                                }
                            });
                        }
                    }
                } else {
                    // New mapping
                    mappings.NameToUUID.put(player, storageKey);
                    mappings.UUIDToName.put(storageKey, player);

                    if (!savingMappings) {
                        savingMappings = true;

                        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mappings.save();
                                } catch (InvalidConfigurationException e) {
                                    plugin.getPluginLogger().error("Could not save mappings");
                                }

                                savingMappings = false;
                            }
                        });
                    }
                }
            }

            try {
                playerDatabase.init();

                for (String pattern : playerDatabase.HighlightPattern) {
                    playerDatabase.PatternCache.add(Pattern.compile(pattern));
                }

                loadedPlayers.put(player, playerDatabase);
            } catch (Exception e) {
                plugin.getPluginLogger().error("Could not init PlayerDatabase for " + player, e);
                throw new RuntimeException();
            }
        }
    }

    public boolean exists(String player) {
        String storageKey = player;
        if (FeatureDetector.canUseUUID()) {
            UUIDMappings mappings = plugin.getConfigManager().getConfig("uuidMappings");
            storageKey = mappings.NameToUUID.get(player);

            if (storageKey == null) {
                return false;
            }
        }

        String folder = storageKey.substring(0, 2);
        return (new File(plugin.getDataFolder(), "database" + File.separator + "users" + File.separator + folder + File.separator + storageKey + ".yml")).exists();
    }
}
