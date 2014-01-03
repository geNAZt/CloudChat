package net.cubespace.CloudChat.Module.Logging;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Database;
import net.cubespace.CloudChat.Module.Logging.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Logging.Listener.PMListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:19
 */
public class LoggingModule {
    private AsyncDatabaseLogger asyncDatabaseLogger;

    public LoggingModule(CloudChatPlugin plugin) {
        if(((Database) plugin.getConfigManager().getConfig("database")).Enabled) {
            plugin.getPluginLogger().info("Starting Database Logger...");

            asyncDatabaseLogger = new AsyncDatabaseLogger(plugin);

            plugin.getAsyncEventBus().addListener(new ChatMessageListener(this));
            plugin.getAsyncEventBus().addListener(new PMListener(this));
        }
    }

    public AsyncDatabaseLogger getAsyncDatabaseLogger() {
        return asyncDatabaseLogger;
    }
}
