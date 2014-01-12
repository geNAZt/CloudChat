package net.cubespace.CloudChat.Module.CloudChat.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Logger.Level;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 07:02
 */
public class Admin implements CLICommand {
    private CubespacePlugin plugin;
    private ScheduledTask cancelTask;

    public Admin(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(arguments = 1, command = "cc:report")
    public void onReportCommand(CommandSender sender, String[] args) {
        if(args[0].equals("on") && !plugin.getReportManager().isSessionOpen()) {
            String file = "CloudChat-" + System.currentTimeMillis();
            plugin.getReportManager().openSession(file);
            plugin.getPluginLogger().setLogLevel(Level.DEBUG);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(((Messages) plugin.getConfigManager().getConfig("messages")).Command_CC_Report_On.replace("%file", file))).send(sender);

            cancelTask = plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getReportManager().closeSession();
                    plugin.getPluginLogger().setLogLevel(Level.INFO);
                }
            }, 10, TimeUnit.MINUTES);
        } else if(args[0].equals("off") && plugin.getReportManager().isSessionOpen()) {
            cancelTask.cancel();
            plugin.getReportManager().closeSession();
            plugin.getPluginLogger().setLogLevel(Level.INFO);

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(((Messages) plugin.getConfigManager().getConfig("messages")).Command_CC_Report_Off)).send(sender);
        }
    }
}
