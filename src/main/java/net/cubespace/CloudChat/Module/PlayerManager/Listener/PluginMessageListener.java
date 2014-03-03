package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.Module.ChatHandler.ChatHandlerModule;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerChangeAFKEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.PluginMessages.AFKMessage;
import net.cubespace.PluginMessages.AffixMessage;
import net.cubespace.PluginMessages.IgnoreMessage;
import net.cubespace.PluginMessages.OutputMessage;
import net.cubespace.PluginMessages.PermissionRequest;
import net.cubespace.PluginMessages.WorldMessage;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class PluginMessageListener implements PacketListener {
    private PlayerManager playerManager;
    private CubespacePlugin plugin;

    public PluginMessageListener(CubespacePlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @PacketHandler
    public void onAffixMessage(AffixMessage affixMessage){
        ProxiedPlayer player = affixMessage.getSender().getBungeePlayer();
        if(player == null) {
            plugin.getPluginLogger().error("Could not resolve ProxiedPlayer");
            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        if(playerDatabase == null) {
            plugin.getPluginLogger().error("Could not get Player Database for " + player.getName());
            return;
        }

        if(affixMessage.getPrefix() != null)
            playerDatabase.Prefix = affixMessage.getPrefix();

        if(affixMessage.getSuffix() != null)
            playerDatabase.Suffix = affixMessage.getSuffix();

        playerDatabase.Town = affixMessage.getTown();
        playerDatabase.Nation = affixMessage.getNation();
        playerDatabase.Faction = affixMessage.getFaction();

        //Get new Permissions
        //plugin.getPluginMessageManager("CubespaceLibrary").sendPluginMessage(player, new PermissionRequest(plugin.getPermissionManager().getPrefix()));
        plugin.getPluginLogger().debug("Got new Affix Message for " + player.getName() + " - " + affixMessage.getPrefix() + "/" + affixMessage.getSuffix());
    }

    @PacketHandler
    public void onWorldMessage(WorldMessage worldMessage){
        ProxiedPlayer player = worldMessage.getSender().getBungeePlayer();
        if(player == null) {
            plugin.getPluginLogger().error("Could not resolve ProxiedPlayer");
            return;
        }

        if(worldMessage.getName() != null && worldMessage.getAlias() != null) {
            PlayerDatabase playerDatabase = playerManager.get(player.getName());
            if(playerDatabase == null) {
                plugin.getPluginLogger().error("Could not get Player Database for " + player.getName());
                return;
            }

            playerDatabase.World = worldMessage.getName();
            playerDatabase.WorldAlias = worldMessage.getAlias();

            //Get new Permissions
            plugin.getPluginMessageManager("CubespaceLibrary").sendPluginMessage(player, new PermissionRequest(plugin.getPermissionManager().getPrefix()));
        }

        plugin.getPluginLogger().debug("Got new World Message for " + player.getName() + " - " + worldMessage.getName() + "/" + worldMessage.getAlias());
    }

    @PacketHandler
    public void onAFKMessage(AFKMessage afkMessage){
        ProxiedPlayer player = afkMessage.getSender().getBungeePlayer();
        if(player == null) {
            plugin.getPluginLogger().error("Could not resolve ProxiedPlayer");
            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        if(playerDatabase == null) {
            plugin.getPluginLogger().error("Could not get Player Database for " + player.getName());
            return;
        }

        if(afkMessage.isAfk() && !playerDatabase.AFK) {
            //The Player has gone AFK
            plugin.getAsyncEventBus().callEvent(new PlayerChangeAFKEvent(player, true));
        } else if(!afkMessage.isAfk() && playerDatabase.AFK) {
            //The Player has got out of AFK
            plugin.getAsyncEventBus().callEvent(new PlayerChangeAFKEvent(player, false));
        }

        playerDatabase.AFK = afkMessage.isAfk();
        plugin.getPluginLogger().debug("Got new AFK Message for " + player.getName() + " - " + afkMessage.isAfk());
    }

    @PacketHandler
    public void onIgnoreMessage(IgnoreMessage ignoreMessage) {
        ProxiedPlayer player = ignoreMessage.getSender().getBungeePlayer();
        if(player == null) {
            plugin.getPluginLogger().error("Could not resolve ProxiedPlayer");
            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        if(playerDatabase == null) {
            plugin.getPluginLogger().error("Could not get Player Database for " + player.getName());
            return;
        }

        playerDatabase.Ignore = ignoreMessage.getIgnore();
    }

    @PacketHandler
    public void onOutputMessage(OutputMessage outputMessage) {
        ProxiedPlayer player = outputMessage.getSender().getBungeePlayer();
        if(player == null) {
            plugin.getPluginLogger().error("Could not resolve ProxiedPlayer");
            return;
        }

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        if(playerDatabase == null) {
            plugin.getPluginLogger().error("Could not get Player Database for " + player.getName());
            return;
        }

        playerDatabase.Output = outputMessage.isOutput();

        ChatHandlerModule chatHandlerModule = plugin.getModuleManager().getModule(ChatHandlerModule.class);
        if(chatHandlerModule.getChatBuffer().getBuffer(player.getName()) != null) {
            for(MessageBuilder messageBuilder : chatHandlerModule.getChatBuffer().getBuffer(player.getName())) {
                messageBuilder.send(player);
            }
        }

        chatHandlerModule.getChatBuffer().removeBuffer(player.getName());
    }
}