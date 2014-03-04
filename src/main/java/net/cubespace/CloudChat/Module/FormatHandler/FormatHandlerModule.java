package net.cubespace.CloudChat.Module.FormatHandler;

import net.cubespace.CloudChat.Module.ColorHandler.Listener.PMListener;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.FormatHandler.Listener.ChatMessageListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class FormatHandlerModule extends Module {
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        plugin.getAsyncEventBus().addListener(this, new ChatMessageListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PMListener(plugin));
        MessageFormat.setPlugin(plugin);
    }

    @Override
    public void onDisable() {
        plugin.getAsyncEventBus().removeListener(this);
        MessageFormat.setPlugin(null);
    }
}
