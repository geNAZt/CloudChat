package net.cubespace.CloudChat;

import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Config.Database;
import net.cubespace.CloudChat.Config.Factions;
import net.cubespace.CloudChat.Config.IRC;
import net.cubespace.CloudChat.Config.IRCPermissions;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Config.PermissionContainers;
import net.cubespace.CloudChat.Config.Spam;
import net.cubespace.CloudChat.Config.Towny;
import net.cubespace.CloudChat.Config.Twitter;
import net.cubespace.CloudChat.Config.UUIDMappings;
import net.cubespace.CloudChat.Listener.ChatListener;
import net.cubespace.CloudChat.Listener.PermissionChangedListener;
import net.cubespace.CloudChat.Listener.PermissionLoadedListener;
import net.cubespace.CloudChat.Listener.PlayerJoinListener;
import net.cubespace.CloudChat.Listener.PlayerQuitListener;
import net.cubespace.CloudChat.Listener.ServerConnectedListener;
import net.cubespace.CloudChat.Listener.TabCompleteListener;
import net.cubespace.CloudChat.Util.AutoComplete;
import net.cubespace.CloudChat.Util.FeatureDetector;
import net.cubespace.CloudChat.Util.Permissions;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Logger.Level;
import net.cubespace.lib.Module.ModuleDescription;

import java.io.File;
import java.util.HashSet;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class CloudChatPlugin extends CubespacePlugin {
    @Override
    public void onEnable() {
        //Setup the Logging Level
        getPluginLogger().setLogLevel(Level.INFO);

        //Load the Permission
        getPermissionManager().setup();

        //Load the Configs
        CommandAliases commandAliases = new CommandAliases(this);
        getConfigManager().initConfig("main", new Main(this));
        getConfigManager().initConfig("irc", new IRC(this));
        getConfigManager().initConfig("database", new Database(this));
        getConfigManager().initConfig("spam", new Spam(this));
        getConfigManager().initConfig("twitter", new Twitter(this));
        getConfigManager().initConfig("factions", new Factions(this));
        getConfigManager().initConfig("ircPermissions", new IRCPermissions(this));
        getConfigManager().initConfig("messages", new Messages(this));
        getConfigManager().initConfig("commandAliases", commandAliases);
        getConfigManager().initConfig("towny", new Towny(this));
        getConfigManager().initConfig("permissionContainers", new PermissionContainers(this));
        getConfigManager().initConfig("uuidMappings", new UUIDMappings(this));

        //Keep track of new Commands
        if (!commandAliases.BaseCommands.containsKey("channels"))
            commandAliases.BaseCommands.put("channels", "channels");

        if (!commandAliases.BaseCommands.containsKey("togglepm"))
            commandAliases.BaseCommands.put("togglepm", "togglepm");

        if (!commandAliases.BaseCommands.containsKey("irc:mute"))
            commandAliases.BaseCommands.put("irc:mute", "irc:mute");

        if (!commandAliases.BaseCommands.containsKey("irc:unmute"))
            commandAliases.BaseCommands.put("irc:unmute", "irc:unmute");

        if (!commandAliases.BaseCommands.containsKey("chatspy"))
            commandAliases.BaseCommands.put("chatspy", "chatspy");

        if (!commandAliases.BaseCommands.containsKey("conversation"))
            commandAliases.BaseCommands.put("conversation", "conversation");

        //Static init
        FeatureDetector.init(this);
        AutoComplete.init(this);
        Permissions.init(this);

        //Load the Modules
        getModuleManager().registerModule(new ModuleDescription("PlayerManager", "net.cubespace.CloudChat.Module.PlayerManager.PlayerManagerModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("ChannelManager", "net.cubespace.CloudChat.Module.ChannelManager.ChannelManagerModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("ChatHandler", "net.cubespace.CloudChat.Module.ChatHandler.ChatHandlerModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));

        getModuleManager().registerModule(new ModuleDescription("FormatHandler", "net.cubespace.CloudChat.Module.FormatHandler.FormatHandlerModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("ColorHandler", "net.cubespace.CloudChat.Module.ColorHandler.ColorHandlerModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("PM", "net.cubespace.CloudChat.Module.PM.PMModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("Logging", "net.cubespace.CloudChat.Module.Logging.LoggingModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("Mute", "net.cubespace.CloudChat.Module.Mute.MuteModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("IRC", "net.cubespace.CloudChat.Module.IRC.IRCModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("CloudChat", "net.cubespace.CloudChat.Module.CloudChat.CloudChatModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("Spam", "net.cubespace.CloudChat.Module.Spam.SpamModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("Twitter", "net.cubespace.CloudChat.Module.Twitter.TwitterModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));
        getModuleManager().registerModule(new ModuleDescription("Mail", "net.cubespace.CloudChat.Module.Mail.MailModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));

        getModuleManager().registerModule(new ModuleDescription("Metrics", "net.cubespace.CloudChat.Module.Metrics.MetricsModule", "1.0.0", "geNAZt", new HashSet<String>(), null, null));

        File moduleFolder = new File(getDataFolder(), "modules");
        if(!moduleFolder.exists()) {
            moduleFolder.mkdirs();
        }

        getModuleManager().detectModules(moduleFolder);
        getModuleManager().loadAndEnableModules();
        getPluginMessageManager("CloudChat").finish();

        //Register the Listeners
        getAsyncEventBus().addListener(null, new PermissionLoadedListener(this));
        getAsyncEventBus().addListener(null, new PermissionChangedListener(this));
        getProxy().getPluginManager().registerListener(this, new PlayerQuitListener(this));
        getProxy().getPluginManager().registerListener(this, new ChatListener(this));
        getProxy().getPluginManager().registerListener(this, new ServerConnectedListener(this));
        getProxy().getPluginManager().registerListener(this, new PlayerJoinListener(this));

        if (FeatureDetector.canUseTabCompleteListener()) {
            getProxy().getPluginManager().registerListener(this, new TabCompleteListener(this));
        }

        super.onEnable();
    }
}
