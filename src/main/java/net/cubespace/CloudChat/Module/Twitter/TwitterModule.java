package net.cubespace.CloudChat.Module.Twitter;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Twitter;
import net.cubespace.CloudChat.Module.Twitter.Task.TwitterCheckTweetTask;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 17:17
 */
public class TwitterModule {
    private TwitterFactory twitterFactory;
    private TwitterManager twitterManager;

    public TwitterModule(CloudChatPlugin plugin) {
        if(((Twitter) plugin.getConfigManager().getConfig("twitter")).Enabled) {
            Twitter config = plugin.getConfigManager().getConfig("twitter");

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
              .setOAuthConsumerKey(config.ConsumerKey)
              .setOAuthConsumerSecret(config.ConsumerSecret)
              .setOAuthAccessToken(config.AccessToken)
              .setOAuthAccessTokenSecret(config.AccessTokenSecret);

            twitterFactory = new TwitterFactory(cb.build());
            twitterManager = new TwitterManager();

            plugin.getProxy().getScheduler().schedule(plugin, new TwitterCheckTweetTask(this, plugin), 30, 30, TimeUnit.SECONDS);
        }
    }

    public TwitterFactory getTwitterFactory() {
        return twitterFactory;
    }

    public TwitterManager getTwitterManager() {
        return twitterManager;
    }
}
