package net.cubespace.CloudChat.Module.FormatHandler;

import net.cubespace.CloudChat.Module.FormatHandler.Listener.ChatMessageListener;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class FormatHandlerModule extends Module {
    public FormatHandlerModule(CubespacePlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        plugin.getAsyncEventBus().addListener(this, new ChatMessageListener(plugin));
    }

    @Override
    public void onDisable() {
        plugin.getAsyncEventBus().removeListener(this);
    }
}
