package net.cubespace.CloudChat.Listener;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class TabCompleteListener implements Listener {
    private final CubespacePlugin plugin;
    private final PlayerManager playerManager;

    public TabCompleteListener(CubespacePlugin plugin) {
        this.plugin = plugin;
        this.playerManager = plugin.getManagerRegistry().getManager("playerManager");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTabCompleteRequest(final TabCompleteEvent tabCompleteEvent) {
        tabCompleteEvent.getSuggestions().clear();

        tabCompleteEvent.getSuggestions().addAll(Lists.newArrayList(Iterables.transform(Iterables.filter(plugin.getProxy().getPlayers(), new Predicate<ProxiedPlayer>() {
            @Override
            public boolean apply(ProxiedPlayer player) {
                PlayerDatabase playerDatabase = playerManager.get(player.getName());
                return playerDatabase != null && playerDatabase.Nick.toLowerCase().contains(tabCompleteEvent.getCursor().toLowerCase());
            }
        }), new Function<ProxiedPlayer, String>() {
            @Override
            public String apply(ProxiedPlayer player) {
                PlayerDatabase playerDatabase = playerManager.get(player.getName());
                return FontFormat.stripColor(playerDatabase.Nick);
            }
        })));
    }
}
