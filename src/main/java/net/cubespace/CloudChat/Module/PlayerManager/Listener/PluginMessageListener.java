package net.cubespace.CloudChat.Module.PlayerManager.Listener;

import com.iKeirNez.PluginMessageApiPlus.PacketHandler;
import com.iKeirNez.PluginMessageApiPlus.PacketListener;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerChangeAFKEvent;
import net.cubespace.CloudChat.Module.PlayerManager.Message.AFKMessage;
import net.cubespace.CloudChat.Module.PlayerManager.Message.AffixMessage;
import net.cubespace.CloudChat.Module.PlayerManager.Message.WorldMessage;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 04:09
 */
public class PluginMessageListener implements PacketListener {
    private PlayerManager playerManager;
    private CloudChatPlugin plugin;

    public PluginMessageListener(CloudChatPlugin plugin) {
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
        this.plugin = plugin;
    }

    @PacketHandler
    public void onAffixMessage(AffixMessage affixMessage){
        ProxiedPlayer player = affixMessage.getSender().getBungeePlayer();

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        playerDatabase.Prefix = affixMessage.getPrefix();
        playerDatabase.Suffix = affixMessage.getSuffix();

        plugin.getPluginLogger().debug("Got new Affix Message for " + player.getName() + " - " + affixMessage.getPrefix() + "/" + affixMessage.getSuffix());
    }

    @PacketHandler
    public void onWorldMessage(WorldMessage worldMessage){
        ProxiedPlayer player = worldMessage.getSender().getBungeePlayer();

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
        playerDatabase.World = worldMessage.getName();
        playerDatabase.WorldAlias = worldMessage.getAlias();

        plugin.getPluginLogger().debug("Got new World Message for " + player.getName() + " - " + worldMessage.getName() + "/" + worldMessage.getAlias());
    }

    @PacketHandler
    public void onAFKMessage(AFKMessage afkMessage){
        ProxiedPlayer player = afkMessage.getSender().getBungeePlayer();

        PlayerDatabase playerDatabase = playerManager.get(player.getName());
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
}