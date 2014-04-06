package net.cubespace.CloudChat.Module.CloudChat.Command;

import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.MessageFormat;
import net.cubespace.CloudChat.Module.Mute.MuteManager;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickAction;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Chat.MessageBuilder.ClickEvent.ClickEvent;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class Playermenu implements CLICommand {
    private final CubespacePlugin plugin;
    private final MuteManager muteManager;
    private final PlayerManager playerManager;
    private final Pattern pattern = Pattern.compile("\\{(.*?)\\}", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public Playermenu(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.muteManager = plugin.getManagerRegistry().getManager("muteManager");
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @SuppressWarnings("unused")
    @Command(command = "cc:playermenu", arguments = 1)
    public void playerMenuCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        boolean muted = muteManager.isPlayerMute(sender.getName(), args[0]);

        MessageBuilder messageBuilder = new MessageBuilder();
        String message = messages.Playermenu_Complete;
        Integer amt = 0;

        for(String line : message.split("\n")) {
            Matcher regexMatcher = pattern.matcher(line);
            while (regexMatcher.find()) {
                HashMap<String, String> found = new HashMap<>();

                String p = regexMatcher.group(1);
                String[] temp = p.split(";");

                for(String t : temp) {
                    String[] temp1 = t.split(":");

                    found.put(temp1[0].trim(), temp1[1]);
                }

                if (found.containsKey("permission") && found.containsKey("command") && found.containsKey("message")) {
                    if (!plugin.getPermissionManager().has(sender, found.get("permission"))) {
                        plugin.getPluginLogger().debug("No Permission for " + regexMatcher.group(0));
                        line = line.replace(regexMatcher.group(0), "");
                        continue;
                    }

                    if (found.containsKey("status")) {
                        switch (found.get("status")) {
                            case "unmuted":
                                if (muted) {
                                    line = line.replace(regexMatcher.group(0), "");
                                    plugin.getPluginLogger().debug("Wrong status for " + regexMatcher.group(0));
                                    continue;
                                }
                                break;

                            case "muted":
                                if (!muted) {
                                    line = line.replace(regexMatcher.group(0), "");
                                    plugin.getPluginLogger().debug("Wrong status for " + regexMatcher.group(0));
                                    continue;
                                }
                                break;
                            default:
                                line = line.replace(regexMatcher.group(0), "");
                                plugin.getPluginLogger().debug("Wrong status for " + regexMatcher.group(0));
                                continue;
                        }
                    }

                    ClickAction clickAction = ClickAction.RUN_COMMAND;

                    if (found.containsKey("mode")) {
                        if (found.get("mode").trim().equals("suggest")) {
                            clickAction = ClickAction.SUGGEST_COMMAND;
                        }
                    }

                    ClickEvent clickEvent = new ClickEvent();
                    clickEvent.setAction(clickAction);
                    clickEvent.setValue(found.get("command").replace("%player", args[0]).replace("%sender", sender.getName()));

                    messageBuilder.addEvent("c" + amt, clickEvent);
                    line = line.replace(regexMatcher.group(0), found.get("message").replace("[click]", "{click:c" + amt +"}"));

                    amt++;
                } else {
                    line = line.replace(regexMatcher.group(0), "");
                }
            }

            messageBuilder.setText(MessageFormat.format(line, null, playerManager.get(args[0]))).send(sender);
        }
    }
}