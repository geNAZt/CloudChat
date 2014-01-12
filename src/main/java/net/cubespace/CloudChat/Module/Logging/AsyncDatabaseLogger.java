package net.cubespace.CloudChat.Module.Logging;

import com.j256.ormlite.dao.DaoManager;
import net.cubespace.CloudChat.Module.Logging.Entity.ChatMessage;
import net.cubespace.CloudChat.Module.Logging.Entity.PrivateMessage;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Database.Database;
import net.cubespace.lib.EventBus.Listener;

import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:09
 */
public class AsyncDatabaseLogger implements Listener {
    private Database database;
    private LinkedBlockingQueue<ChatMessage> chatMessages = new LinkedBlockingQueue<>(5000);
    private LinkedBlockingQueue<PrivateMessage> privateMessages = new LinkedBlockingQueue<>(5000);
    private CubespacePlugin plugin;
    private long lastWarning = 0;

    public AsyncDatabaseLogger(final CubespacePlugin plugin) {
        this.plugin = plugin;
        net.cubespace.CloudChat.Config.Database config = plugin.getConfigManager().getConfig("database");

        database = new Database(plugin, config.Url, config.Username, config.Password);
        try {
            database.registerDAO(DaoManager.createDao(database.getConnectionSource(), ChatMessage.class), ChatMessage.class);
            database.registerDAO(DaoManager.createDao(database.getConnectionSource(), PrivateMessage.class), PrivateMessage.class);

            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            ChatMessage chatMessage = chatMessages.take();
                            database.getDAO(ChatMessage.class).create(chatMessage);
                            plugin.getPluginLogger().debug("Persisted Chat Message");
                        }
                    } catch(Exception e) {
                        plugin.getPluginLogger().error("Error in AsyncDatabaseLogger", e);
                        throw new RuntimeException();
                    }
                }
            });

            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            PrivateMessage privateMessage = privateMessages.take();
                            database.getDAO(PrivateMessage.class).create(privateMessage);
                            plugin.getPluginLogger().debug("Persisted Private Message");
                        }
                    } catch(Exception e) {
                        plugin.getPluginLogger().error("Error in AsyncDatabaseLogger", e);
                        throw new RuntimeException();
                    }
                }
            });
        } catch (SQLException e) {
            plugin.getPluginLogger().error("Could not register Entity for Logger", e);
            throw new RuntimeException();
        }
    }

    public void addChatMessage(ChatMessage chatMessage) {
        if(chatMessages.size() > 3000) {
            if(System.currentTimeMillis() - lastWarning > 10000) {
                plugin.getPluginLogger().warn("The AsyncDatabaseLogger has more than 3000 queued Chat Messages. Please issue /cc:report and post the Reportfile to geNAZt");
                lastWarning = System.currentTimeMillis();
            }
        }

        chatMessages.add(chatMessage);
    }

    public void addPrivateMessage(PrivateMessage privateMessage) {
        if(privateMessages.size() > 3000) {
            if(System.currentTimeMillis() - lastWarning > 10000) {
                plugin.getPluginLogger().warn("The AsyncDatabaseLogger has more than 3000 queued Private Messages. Please issue /cc:report and post the Reportfile to geNAZt");
                lastWarning = System.currentTimeMillis();
            }
        }

        privateMessages.add(privateMessage);
    }
}
