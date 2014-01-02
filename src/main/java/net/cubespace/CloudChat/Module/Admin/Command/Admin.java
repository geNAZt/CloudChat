package net.cubespace.CloudChat.Module.Admin.Command;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.Logger.Level;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 07:02
 */
public class Admin implements CLICommand {
    private CloudChatPlugin plugin;
    private ScheduledTask cancelTask;

    public Admin(CloudChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Command(arguments = 1, command = "cc:report")
    public void onReportCommand(CommandSender sender, String[] args) {
        if(args[0].equals("on") && !plugin.getReportManager().isSessionOpen()) {
            String file = "CloudChat-" + System.currentTimeMillis();
            plugin.getReportManager().openSession(file);
            plugin.getPluginLogger().setLogLevel(Level.DEBUG);
            sender.sendMessage("Reporting has been enabled and the Report will be saved to " + file + ". Type /cc:report off to stop or it will automaticly stop after 10 Minutes");

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

            sender.sendMessage("Reporting is now off");
        }
    }
}
