package net.cubespace.lib.Module;

import com.google.common.base.Preconditions;
import net.cubespace.lib.CubespacePlugin;

import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class ModuleManager {
    private CubespacePlugin plugin;
    private ArrayList<Module> registeredModules = new ArrayList<>();

    public ModuleManager(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    public void registerModule(Module module) {
        Preconditions.checkNotNull(module, "Module to register can not be null");

        registeredModules.add(module);
    }

    public void enable() {
        for(Module module : registeredModules) {
            plugin.getPluginLogger().info("Loading " + module.getClass().getName());
            module.onLoad();
        }

        for(Module module : registeredModules) {
            plugin.getPluginLogger().info("Enabling Module " + module.getClass().getName());
            module.onEnable();
        }
    }

    public <T extends Module> T getModule(Class<T> clazz) {
        Preconditions.checkNotNull(clazz, "Class to lookup can not be null");

        for(Module module : registeredModules) {
            if(module.getClass().equals(clazz)) {
                return clazz.cast(module);
            }
        }

        return null;
    }
}
