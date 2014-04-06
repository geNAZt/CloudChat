package net.cubespace.CloudChat.Module.Logging;

import com.j256.ormlite.dao.Dao;
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
 */
public class AsyncDatabaseLogger implements Listener {
    private final Database database;
    private final LinkedBlockingQueue<ChatMessage> chatMessages = new LinkedBlockingQueue<>(5000);
    private final LinkedBlockingQueue<PrivateMessage> privateMessages = new LinkedBlockingQueue<>(5000);
    private final CubespacePlugin plugin;
    private long lastWarning = 0;

    public AsyncDatabaseLogger(final CubespacePlugin plugin) {
        this.plugin = plugin;
        net.cubespace.CloudChat.Config.Database config = plugin.getConfigManager().getConfig("database");

        database = new Database(plugin, config.Url, config.Username, config.Password);
        try {
            database.registerDAO(DaoManager.createDao(database.getConnectionSource(), ChatMessage.class), ChatMessage.class);
            database.registerDAO(DaoManager.createDao(database.getConnectionSource(), PrivateMessage.class), PrivateMessage.class);

            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @SuppressWarnings("unchecked")
                @Override
                public void run() {
                    try {
                        Dao dao = database.getDAO(ChatMessage.class);

                        while(plugin.getProxy().getPluginManager().getPlugins().contains(plugin)) {
                            ChatMessage chatMessage = chatMessages.take();

                            dao.create(chatMessage);
                            plugin.getPluginLogger().debug("Persisted Chat Message");
                        }
                    } catch(Exception e) {
                        plugin.getPluginLogger().error("Error in AsyncDatabaseLogger", e);
                        throw new RuntimeException();
                    }
                }
            });

            plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @SuppressWarnings("unchecked")
                @Override
                public void run() {
                    try {
                        Dao dao = database.getDAO(PrivateMessage.class);

                        while(plugin.getProxy().getPluginManager().getPlugins().contains(plugin)) {
                            PrivateMessage privateMessage = privateMessages.take();

                            dao.create(privateMessage);
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
