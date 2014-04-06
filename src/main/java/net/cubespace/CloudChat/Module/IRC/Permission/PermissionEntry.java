package net.cubespace.CloudChat.Module.IRC.Permission;

import net.cubespace.CloudChat.Config.Sub.IRCPermissionGroup;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
@SuppressWarnings("WeakerAccess")
public class PermissionEntry {
    private IRCPermissionGroup primaryGroup;
    private ArrayList<String> permissions;

    public IRCPermissionGroup getPrimaryGroup() {
        return primaryGroup;
    }

    public void setPrimaryGroup(IRCPermissionGroup primaryGroup) {
        this.primaryGroup = primaryGroup;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
