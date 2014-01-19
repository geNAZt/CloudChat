package net.cubespace.CloudChat.Module.PM;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.PM.Command.PM;
import net.cubespace.CloudChat.Module.PM.Command.SocialSpy;
import net.cubespace.CloudChat.Module.PM.Listener.PMListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PMModule extends Module {
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("msg")) {
            plugin.getBindManager().bind("msg", PlayerBinder.class, commandAliases.Msg.toArray(new String[0]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("reply")) {
            plugin.getBindManager().bind("reply", Binder.class, commandAliases.Reply.toArray(new String[0]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains("socialspy")) {
            plugin.getBindManager().bind("socialspy", Binder.class, commandAliases.Socialspy.toArray(new String[0]));
            plugin.getCommandExecutor().add(this, new SocialSpy(plugin));
        }

        plugin.getCommandExecutor().add(this, new PM(plugin));

        plugin.getAsyncEventBus().addListener(this, new PMListener(plugin));
    }

    @Override
    public void onDisable() {
        if(plugin.getBindManager().isBound("msg")) {
            plugin.getBindManager().unbind("msg");
        }

        if(plugin.getBindManager().isBound("reply")) {
            plugin.getBindManager().unbind("reply");
        }

        if(plugin.getBindManager().isBound("socialspy")) {
            plugin.getBindManager().unbind("socialspy");
        }

        plugin.getCommandExecutor().remove(this);

        plugin.getAsyncEventBus().removeListener(this);
    }
}
