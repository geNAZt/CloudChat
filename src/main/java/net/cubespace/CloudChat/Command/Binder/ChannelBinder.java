package net.cubespace.CloudChat.Command.Binder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Database.ChannelDatabase;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 30.11.13 13:45
 */
public class ChannelBinder extends Binder implements TabExecutor {
    public ChannelBinder(CloudChatPlugin plugin, String name, String permission, String... aliases) {
        super(plugin, name, permission, aliases);
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender commandSender, String[] args) {
        final String lastArg = ( args.length > 0 ) ? args[args.length - 1] : "";

        return Iterables.transform(Iterables.filter(plugin.getChannelManager().getChannels(), new Predicate<ChannelDatabase>() {
            @Override
            public boolean apply(ChannelDatabase database) {
                if(!commandSender.hasPermission("cloudchat.channel." + database.Name)) return false;

                return database.Name.startsWith(lastArg) || database.Short.startsWith(lastArg);
            }
        }), new Function<ChannelDatabase, String>() {
            @Override
            public String apply(ChannelDatabase database) {
                return database.Name;
            }
        });
    }
}
