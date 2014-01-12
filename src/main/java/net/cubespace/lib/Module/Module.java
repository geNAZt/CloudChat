package net.cubespace.lib.Module;

import net.cubespace.lib.CubespacePlugin;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public abstract class Module {
    protected CubespacePlugin plugin;

    public Module(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * This function will be used to register new Managers, PluginMessages, Configs and stuff
     */
    public abstract void onLoad();

    /**
     * This function will be used to enable a Module (it should register Listeners)
     */
    public abstract void onEnable();

    /**
     * This function will be used when an Module should be disabled
     */
    public abstract void onDisable();
}
