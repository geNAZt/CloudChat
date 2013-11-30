package net.cubespace.CloudChat.Command.Binder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Collections;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:45
 */
public class JoinedChannelBinder extends Binder implements TabExecutor {
    public JoinedChannelBinder(CloudChatPlugin plugin, String name, String permission, String... aliases) {
        super(plugin, name, permission, aliases);
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender commandSender, String[] args) {
        final String lastArg = ( args.length > 0 ) ? args[args.length - 1] : "";

        if(!(commandSender instanceof ProxiedPlayer)) return Collections.EMPTY_LIST;
        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        return Iterables.transform(Iterables.filter(plugin.getPlayerManager().get(player).JoinedChannels, new Predicate<String>() {
            @Override
            public boolean apply(String channel) {
                return channel.startsWith(lastArg);
            }
        }), new Function<String, String>() {
            @Override
            public String apply(String channel) {
                return channel;
            }
        });
    }
}
