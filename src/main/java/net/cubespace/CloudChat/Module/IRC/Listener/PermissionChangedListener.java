package net.cubespace.CloudChat.Module.IRC.Listener;

import net.cubespace.CloudChat.Config.IRCPermissions;
import net.cubespace.CloudChat.Config.Sub.IRCPermissionGroup;
import net.cubespace.CloudChat.Config.Sub.IRCPermissionUser;
import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.Permission.Event.PermissionChangedEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
@SuppressWarnings("WeakerAccess")
public class PermissionChangedListener {
    private final CubespacePlugin plugin;
    private final IRCModule ircModule;

    public PermissionChangedListener(IRCModule ircModule, CubespacePlugin plugin) {
        this.plugin = plugin;
        this.ircModule = ircModule;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPermissionChanged(PermissionChangedEvent event) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(event.getPlayer());

        if(player == null) return;

        //Check for additional IRC Groups
        IRCPermissions ircPermissions = plugin.getConfigManager().getConfig("ircPermissions");

        for(Map.Entry<String, IRCPermissionUser> userEntry : ircPermissions.Users.entrySet()) {
            if(userEntry.getValue().IngameName.equals(event.getPlayer())) {
                //Player found check for IRC Groups
                userEntry.getValue().Groups = new ArrayList<>();

                for(Map.Entry<String, IRCPermissionGroup> groups : ircPermissions.Groups.entrySet()) {
                    if(plugin.getPermissionManager().has(player, "cloudchat.irc.group." + groups.getKey().toLowerCase())) {
                        userEntry.getValue().Groups.add(groups.getKey());
                    }
                }

                //Reload the Permissions
                ircModule.getIrcBot().getIrcManager().getPermissionManager().reload(userEntry.getKey());
            }
        }
    }
}
