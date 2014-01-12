package net.cubespace.CloudChat.Module.PM;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.PM.Command.PM;
import net.cubespace.CloudChat.Module.PM.Listener.PMListener;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMModule extends Module {
    private boolean boundMsg = false;
    private boolean boundReply = false;

    public PMModule(CubespacePlugin plugin) {
        super(plugin);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("msg")) {
            plugin.getBindManager().bind("msg", PlayerBinder.class, "m", "t", "tell");
            boundMsg = true;
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("reply")) {
            plugin.getBindManager().bind("reply", Binder.class, "r");
            boundReply = true;
        }

        plugin.getCommandExecutor().add(this, new PM(plugin));

        plugin.getAsyncEventBus().addListener(this, new PMListener(plugin));
    }

    @Override
    public void onDisable() {
        if(boundMsg) {
            plugin.getBindManager().unbind("msg");
            boundMsg = false;
        }

        if(boundReply) {
            plugin.getBindManager().unbind("reply");
        }

        plugin.getCommandExecutor().remove(this);

        plugin.getAsyncEventBus().removeListener(this);
    }
}
