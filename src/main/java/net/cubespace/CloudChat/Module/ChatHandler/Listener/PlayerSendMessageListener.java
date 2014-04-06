package net.cubespace.CloudChat.Module.ChatHandler.Listener;

import net.cubespace.CloudChat.Module.ChatHandler.ChatHandlerModule;
import net.cubespace.CloudChat.Module.ChatHandler.Event.PlayerSendMessageEvent;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.PlaySound;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.EventBus.EventHandler;
import net.cubespace.lib.EventBus.EventPriority;
import net.cubespace.lib.EventBus.Listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PlayerSendMessageListener implements Listener {
    private final PlayerManager playerManager;
    private final ChatHandlerModule chatHandlerModule;
    private final CubespacePlugin plugin;

    public PlayerSendMessageListener(ChatHandlerModule chatHandlerModule, CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.chatHandlerModule = chatHandlerModule;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSendMessage(PlayerSendMessageEvent event) {
        if(event.getPlayer() == null) {
            chatHandlerModule.getModuleLogger().warn("Could not send Message because the Player has been unloaded");
            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(event.getPlayer().getName());
        if(playerDatabase == null) {
            chatHandlerModule.getModuleLogger().warn("Could not send Message because the PlayerDatabase has been unloaded");
            return;
        }

        if (playerDatabase.Output) {
            if (playerDatabase.Highlight && event.getRawMessage() != null && !event.getSender().getPlayerDatabase().Realname.equals(event.getPlayer().getName())) {
                String message = event.getRawMessage();
                Pattern nickPattern = Pattern.compile(playerDatabase.Nick);
                playerDatabase.PatternCache.add(nickPattern);

                boolean sendHighlight = false;

                for (Pattern pattern : playerDatabase.PatternCache) {
                    Matcher matcher = pattern.matcher(message);

                    if (matcher.find()) {
                        sendHighlight = true;

                        String match = message.substring(matcher.start(), matcher.end());
                        String format = "";

                        for(int i = 0; i < matcher.start(); i++) {
                            if (message.charAt(i) == '§') {
                                format += "§" + message.charAt(i+1);
                            }
                        }

                        message = message.replace(match, FontFormat.translateString(playerDatabase.HighlightColor) + match + "§r" + format);
                    }
                }

                if (playerDatabase.HighlightSoundEnabled && sendHighlight) {
                    PlaySound playSound = new PlaySound(playerDatabase.HighlightSound);
                    plugin.getPluginMessageManager("CloudChat").sendPluginMessage(event.getPlayer(), playSound);
                }

                playerDatabase.PatternCache.remove(nickPattern);

                event.getMessage().setText(message).send(event.getPlayer());
                event.getMessage().setText(event.getRawMessage());
            } else {
                event.getMessage().send(event.getPlayer());
            }
            chatHandlerModule.getModuleLogger().debug("Sending Message to the Player directly");
        } else {
            chatHandlerModule.getModuleLogger().debug("Sending Message to the ChatBuffer");
            chatHandlerModule.getChatBuffer().addToBuffer(event.getPlayer().getName(), event.getMessage());
        }
    }
}
