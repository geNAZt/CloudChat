package net.cubespace.CloudChat.Module.Logging;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Database;
import net.cubespace.CloudChat.Module.Logging.Listener.ChatMessageListener;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:19
 */
public class LoggingModule {
    private AsyncDatabaseLogger asyncDatabaseLogger;

    public LoggingModule(CloudChatPlugin plugin) {
        if(((Database) plugin.getConfigManager().getConfig("database")).Enabled) {
            asyncDatabaseLogger = new AsyncDatabaseLogger(plugin);

            plugin.getAsyncEventBus().addListener(new ChatMessageListener(this));
        }
    }

    public AsyncDatabaseLogger getAsyncDatabaseLogger() {
        return asyncDatabaseLogger;
    }
}
