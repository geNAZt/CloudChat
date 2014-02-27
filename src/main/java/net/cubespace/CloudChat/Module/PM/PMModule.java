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

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("msg"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("msg"), PlayerBinder.class, commandAliases.Msg.toArray(new String[0]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("reply"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("reply"), Binder.class, commandAliases.Reply.toArray(new String[0]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("socialspy"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("socialspy"), Binder.class, commandAliases.Socialspy.toArray(new String[0]));
            plugin.getCommandExecutor().add(this, new SocialSpy(plugin));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("togglepm"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("togglepm"), Binder.class, commandAliases.TogglePM.toArray(new String[0]));
        }

        plugin.getCommandExecutor().add(this, new PM(plugin));

        plugin.getAsyncEventBus().addListener(this, new PMListener(plugin));
    }

    @Override
    public void onDisable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("msg"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("msg"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("reply"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("reply"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("socialspy"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("socialspy"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("togglepm"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("togglepm"));
        }

        plugin.getCommandExecutor().remove(this);

        plugin.getAsyncEventBus().removeListener(this);
    }
}
