package net.cubespace.CloudChat.Module.ColorHandler;

import net.cubespace.CloudChat.Module.ColorHandler.Listener.AsyncChatListener;
import net.cubespace.CloudChat.Module.ColorHandler.Listener.PlayerNickchangeListener;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ColorHandlerModule extends Module {
    public ColorHandlerModule(CubespacePlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        plugin.getAsyncEventBus().addListener(this, new AsyncChatListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerNickchangeListener(plugin));
    }

    @Override
    public void onDisable() {
        plugin.getAsyncEventBus().removeListener(this);
    }
}
