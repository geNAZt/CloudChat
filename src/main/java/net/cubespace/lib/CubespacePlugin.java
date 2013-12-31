package net.cubespace.lib;

import net.cubespace.lib.Command.CommandExecutor;
import net.cubespace.lib.Configuration.ConfigManager;
import net.cubespace.lib.EventBus.AsyncEventBus;
import net.cubespace.lib.Logger.Logger;
import net.cubespace.lib.Manager.ManagerRegistry;
import net.cubespace.lib.PluginMessage.PluginMessageManager;
import net.cubespace.lib.Report.ReportManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 24.12.13 14:29
 */
public class CubespacePlugin extends Plugin {
    private ReportManager reportManager;
    private Logger logger;
    private ConfigManager configManager;
    private AsyncEventBus asyncEventBus;
    private CommandExecutor commandExecutor;
    private ManagerRegistry managerRegistry;
    private HashMap<String, PluginMessageManager> pluginMessageManagerHashMap = new HashMap<>();

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    /**
     * Get the ReportManager for this Plugin
     *
     * @return The ReportManager
     */
    public ReportManager getReportManager() {
        if(reportManager == null) {
            reportManager = new ReportManager(this);
        }

        return reportManager;
    }

    /**
     * Get the Logger for this Plugin
     *
     * @return The Logger *wonder*
     */
    public Logger getPluginLogger() {
        if(logger == null) {
            logger = new Logger(this);
        }

        return logger;
    }

    /**
     * Get the ConfigManager which holds all Configs for this Plugin
     *
     * @return
     */
    public ConfigManager getConfigManager() {
        if(configManager == null) {
            configManager = new ConfigManager(this);
        }

        return configManager;
    }

    /**
     * Get the Async Event Bus which is run in an extra Thread
     * @return
     */
    public AsyncEventBus getAsyncEventBus() {
        if(asyncEventBus == null) {
            asyncEventBus = new AsyncEventBus(this);
        }

        return asyncEventBus;
    }

    /**
     * Get the Command Executor
     * @return
     */
    public CommandExecutor getCommandExecutor() {
        if(commandExecutor == null) {
            commandExecutor = new CommandExecutor(this);
        }

        return commandExecutor;
    }

    /**
     * Get the Manager Registry where all Managers are registered into
     * @return
     */
    public ManagerRegistry getManagerRegistry() {
        if(managerRegistry == null) {
            managerRegistry = new ManagerRegistry(this);
        }

        return managerRegistry;
    }

    /**
     * Get the correct PluginMessageManager for the channel
     * @param channel
     * @return
     */
    public PluginMessageManager getPluginMessageManager(String channel) {
        if(!pluginMessageManagerHashMap.containsKey(channel)) {
            pluginMessageManagerHashMap.put(channel, new PluginMessageManager(this, channel));
        }

        return pluginMessageManagerHashMap.get(channel);
    }

    /**
     * A SimpleDateFormat for all Loggers and ReportManagers to use
     *
     * @return A SimpleDateFormat in "dd.MM.yyyy HH:mm:ss" notation
     */
    public SimpleDateFormat getSimpleDateFormat() {
        return simpleDateFormat;
    }
}
