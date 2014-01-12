package net.cubespace.CloudChat.Module.Twitter;

import net.cubespace.CloudChat.Config.Twitter;
import net.cubespace.CloudChat.Module.Twitter.Task.TwitterCheckTweetTask;
import net.cubespace.lib.CubespacePlugin;
import net.cubespace.lib.Module.Module;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class TwitterModule extends Module {
    private TwitterFactory twitterFactory;
    private TwitterManager twitterManager;
    private ScheduledTask task;

    public TwitterModule(CubespacePlugin plugin) {
        super(plugin);
    }

    public TwitterFactory getTwitterFactory() {
        return twitterFactory;
    }

    public TwitterManager getTwitterManager() {
        return twitterManager;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        if(((Twitter) plugin.getConfigManager().getConfig("twitter")).Enabled) {
            Twitter config = plugin.getConfigManager().getConfig("twitter");

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey(config.ConsumerKey)
                    .setOAuthConsumerSecret(config.ConsumerSecret)
                    .setOAuthAccessToken(config.AccessToken)
                    .setOAuthAccessTokenSecret(config.AccessTokenSecret);

            plugin.getPluginLogger().debug("Setting up Twitter Factory");
            twitterFactory = new TwitterFactory(cb.build());
            twitterManager = new TwitterManager();

            task = plugin.getProxy().getScheduler().schedule(plugin, new TwitterCheckTweetTask(this, plugin), 2, 60, TimeUnit.SECONDS);
        }
    }

    @Override
    public void onDisable() {
        if(task != null) {
            task.cancel();

            twitterFactory = null;
            twitterManager = null;
        }
    }
}
