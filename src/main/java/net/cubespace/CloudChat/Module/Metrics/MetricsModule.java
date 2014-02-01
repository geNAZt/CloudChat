package net.cubespace.CloudChat.Module.Metrics;

import net.cubespace.CloudChat.Module.IRC.IRCModule;
import net.cubespace.CloudChat.Module.Metrics.Listener.ChatMessageListener;
import net.cubespace.CloudChat.Module.Metrics.Listener.PMListener;
import net.cubespace.CloudChat.Module.Twitter.TwitterModule;
import net.cubespace.lib.Metrics;
import net.cubespace.lib.Module.Module;

import java.io.IOException;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class MetricsModule extends Module {
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        try {
            Metrics metrics = new Metrics(plugin);

            Metrics.Graph chatGraph = metrics.createGraph("Amount of Messages");

            chatGraph.addPlotter(new Metrics.Plotter("Ingame Chat") {
                @Override
                public int getValue() {
                    return ChatMessageListener.getIngameMessageCount();
                }

            });

            chatGraph.addPlotter(new Metrics.Plotter("IRC Chat") {
                @Override
                public int getValue() {
                    return ChatMessageListener.getIrcMessageCount();
                }
            });

            chatGraph.addPlotter(new Metrics.Plotter("PMs") {
                @Override
                public int getValue() {
                    return PMListener.getPMs();
                }
            });

            Metrics.Graph bots = metrics.createGraph("Bots");

            bots.addPlotter(new Metrics.Plotter("IRC") {
                @Override
                public int getValue() {
                    IRCModule ircModule = plugin.getModuleManager().getModule(IRCModule.class);
                    return (ircModule.getIrcBot() != null && ircModule.getIrcBot().isConnected()) ? 1 : 0;
                }
            });

            bots.addPlotter(new Metrics.Plotter("Twitter") {
                @Override
                public int getValue() {
                    TwitterModule twitterModule = plugin.getModuleManager().getModule(TwitterModule.class);
                    return (twitterModule.getTwitterFactory() != null) ? 1 : 0;
                }
            });

            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
            plugin.getPluginLogger().error("Could not start Metrics", e);
        }

        plugin.getAsyncEventBus().addListener(this, new ChatMessageListener());
        plugin.getAsyncEventBus().addListener(this, new PMListener());
    }

    @Override
    public void onDisable() {
        plugin.getAsyncEventBus().removeListener(this);
    }
}
