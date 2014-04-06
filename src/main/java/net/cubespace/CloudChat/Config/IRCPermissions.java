package net.cubespace.CloudChat.Config;

import net.cubespace.CloudChat.Config.Sub.IRCPermissionGroup;
import net.cubespace.CloudChat.Config.Sub.IRCPermissionUser;
import net.cubespace.Yamler.Config.Config;
import net.cubespace.lib.CubespacePlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class IRCPermissions extends Config {
    public IRCPermissions(CubespacePlugin plugin) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "ircPermissions.yml");

        ArrayList<String> permissionDefault = new ArrayList<>();
        permissionDefault.add("command.players");

        IRCPermissionGroup ircPermissionGroup = new IRCPermissionGroup();
        ircPermissionGroup.Rank = 1;
        ircPermissionGroup.Permissions = permissionDefault;

        Groups.put("default", ircPermissionGroup);

        ArrayList<String> groups = new ArrayList<>();
        groups.add("default");

        IRCPermissionUser ircPermissionUser = new IRCPermissionUser();
        ircPermissionUser.Groups = groups;

        Users.put("IAmOnlyForDemo", ircPermissionUser);
    }

    @SuppressWarnings("CanBeFinal")
    public HashMap<String, IRCPermissionGroup> Groups = new HashMap<>();
    @SuppressWarnings("CanBeFinal")
    public HashMap<String, IRCPermissionUser> Users = new HashMap<>();
}
