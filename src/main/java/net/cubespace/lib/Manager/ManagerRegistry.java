package net.cubespace.lib.Manager;

import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 28.12.13 12:15
 */
public class ManagerRegistry {
    private HashMap<String, IManager> managerHashMap = new HashMap<>();

    public void registerManager(String name, IManager manager) {
        managerHashMap.put(name, manager);
    }

    public <T> T getManager(String name) {
        return (T) managerHashMap.get(name);
    }
}
