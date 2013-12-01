package net.cubespace.CloudChat.Command.Binder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.PlayerDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:45
 */
public class PlayerBinder extends Binder implements TabExecutor {
    public PlayerBinder(CloudChatPlugin plugin, String name, String... aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        final String lastArg = ( args.length > 0 ) ? args[args.length - 1] : "";
        return Iterables.transform(Iterables.filter(plugin.getProxy().getPlayers(), new Predicate<ProxiedPlayer>() {
            @Override
            public boolean apply(ProxiedPlayer player) {
                PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);
                if (playerDatabase == null) return false;

                return playerDatabase.Nick.startsWith(lastArg);
            }
        }), new Function<ProxiedPlayer, String>() {
            @Override
            public String apply(ProxiedPlayer player) {
                PlayerDatabase playerDatabase = plugin.getPlayerManager().get(player);
                return playerDatabase.Nick;
            }
        });
    }
}
