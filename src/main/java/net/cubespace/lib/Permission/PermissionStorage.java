package net.cubespace.lib.Permission;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 07.01.14 21:12
 */
public class PermissionStorage {
    private ArrayList<String> permissions = new ArrayList<>();

    public void add(String permission) {
        permissions.add(permission);
    }

    public void clear() {
        permissions.clear();
    }

    public boolean has(String permission) {
        return permissions.contains(permission);
    }
}
