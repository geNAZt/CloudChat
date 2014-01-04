package net.cubespace.CloudChat.Config;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Sub.IRCPermissionGroup;
import net.cubespace.CloudChat.Config.Sub.IRCPermissionUser;
import net.cubespace.lib.Configuration.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class IRCPermissions extends Config {
    public IRCPermissions(CloudChatPlugin plugin) {
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

    @Comment("IRC Permission Groups")
    public HashMap<String, IRCPermissionGroup> Groups = new HashMap<>();
    @Comment("IRC User Permissions/Groups")
    public HashMap<String, IRCPermissionUser> Users = new HashMap<>();
}
