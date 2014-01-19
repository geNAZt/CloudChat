package net.cubespace.CloudChat.Module.Logging;

import net.cubespace.CloudChat.Config.Database;
import net.cubespace.CloudChat.Module.Logging.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Logging.Listener.PMListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:19
 */
public class LoggingModule extends Module {
    private AsyncDatabaseLogger asyncDatabaseLogger;

    public AsyncDatabaseLogger getAsyncDatabaseLogger() {
        return asyncDatabaseLogger;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        if(((Database) plugin.getConfigManager().getConfig("database")).Enabled) {
            asyncDatabaseLogger = new AsyncDatabaseLogger(plugin);

            plugin.getAsyncEventBus().addListener(this, new ChatMessageListener(this));
            plugin.getAsyncEventBus().addListener(this, new PMListener(this));
        }
    }

    @Override
    public void onDisable() {
        if(asyncDatabaseLogger != null) {
            asyncDatabaseLogger = null;

            plugin.getAsyncEventBus().removeListener(this);
        }
    }
}
