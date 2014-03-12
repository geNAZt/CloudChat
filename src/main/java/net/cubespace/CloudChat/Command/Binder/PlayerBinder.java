package net.cubespace.CloudChat.Command.Binder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

public class PlayerBinder extends Binder implements TabExecutor {
    private PlayerManager playerManager;

    public PlayerBinder(CubespacePlugin plugin, String name, String... aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        playerManager = plugin.getManagerRegistry().getManager("playerManager");

        final String lastArg = ( args.length > 0 ) ? args[args.length - 1] : "";

        return Iterables.transform(Iterables.filter(plugin.getProxy().getPlayers(), new Predicate<ProxiedPlayer>() {
            @Override
            public boolean apply(ProxiedPlayer player) {
                PlayerDatabase playerDatabase = playerManager.get(player.getName());
                return playerDatabase != null && playerDatabase.Nick.toLowerCase().contains(lastArg.toLowerCase());
            }
        }), new Function<ProxiedPlayer, String>() {
            @Override
            public String apply(ProxiedPlayer player) {
                PlayerDatabase playerDatabase = playerManager.get(player.getName());
                return FontFormat.translateString(playerDatabase.Nick);
            }
        });
    }
}
