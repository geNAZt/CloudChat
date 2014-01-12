package net.cubespace.CloudChat.Command.Binder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;

public class JoinedChannelBinder extends Binder implements TabExecutor {
    public JoinedChannelBinder(CubespacePlugin plugin, String name, String... aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender commandSender, String[] args) {
        PlayerManager playerManager = plugin.getManagerRegistry().getManager("playerManager");

        final String lastArg = ( args.length > 0 ) ? args[args.length - 1] : "";

        if(!(commandSender instanceof ProxiedPlayer)) return Collections.EMPTY_LIST;
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        return Iterables.transform(Iterables.filter(playerManager.get(player.getName()).JoinedChannels, new Predicate<String>() {
            @Override
            public boolean apply(String channel) {
                return channel.toLowerCase().startsWith(lastArg.toLowerCase());
            }
        }), new Function<String, String>() {
            @Override
            public String apply(String channel) {
                return channel;
            }
        });
    }
}
