package net.cubespace.CloudChat.Module.Logging;

import com.j256.ormlite.dao.DaoManager;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.Logging.Entity.ChatMessage;
import net.cubespace.lib.Database.Database;
import net.cubespace.lib.EventBus.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:09
 */
public class AsyncDatabaseLogger implements Listener {
    private Database database;
    private ScheduledTask asyncWriter;
    private LinkedBlockingQueue<ChatMessage> chatMessages = new LinkedBlockingQueue<>();

    public AsyncDatabaseLogger(final CloudChatPlugin plugin) {
        net.cubespace.CloudChat.Config.Database config = plugin.getConfigManager().getConfig("database");

        database = new Database(plugin, config.Url, config.Username, config.Password);
        try {
            database.registerDAO(DaoManager.createDao(database.getConnectionSource(), ChatMessage.class), ChatMessage.class);

            asyncWriter = plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            ChatMessage chatMessage = chatMessages.take();
                            database.getDAO(ChatMessage.class).create(chatMessage);
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
        chatMessages.add(chatMessage);
    }
}
