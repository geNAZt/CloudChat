package net.cubespace.CloudChat.Module.Twitter.Task;

import net.cubespace.CloudChat.Config.Twitter;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.Twitter.TwitterModule;
import net.cubespace.lib.CubespacePlugin;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class TwitterCheckTweetTask implements Runnable {
    private final TwitterModule twitterModule;
    private final CubespacePlugin plugin;
    private final ChannelManager channelManager;

    public TwitterCheckTweetTask(TwitterModule twitterModule, CubespacePlugin plugin) {
        this.twitterModule = twitterModule;
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");

        plugin.getPluginLogger().debug("Initialised Twitter Tweet Checker task");
    }

    @Override
    public void run() {
        twitter4j.Twitter twitter = twitterModule.getTwitterFactory().getInstance();
        Twitter config = plugin.getConfigManager().getConfig("twitter");

        try {
            String[] srch = config.AccountToMonitor.toArray(new String[config.AccountToMonitor.size()]);
            ResponseList<User> users = twitter.lookupUsers(srch);
            for (User user : users) {
                plugin.getPluginLogger().debug("Checking for new Tweets for " + user.getName());

                if (user.getStatus() != null) {
                    List<Status> statusess = twitter.getUserTimeline(user.getScreenName());
                    if(!twitterModule.getTwitterManager().isRegistered(user.getName())) {
                        plugin.getPluginLogger().debug("New Twitter user. Generating new entry in the Manager");
                        twitterModule.getTwitterManager().updateLastTweet(user.getName(), statusess.get(0).getCreatedAt());
                        continue;
                    }

                    Date lastTweet = twitterModule.getTwitterManager().getLastTweet(user.getName());
                    for (Status status3 : statusess) {
                        if(status3.getCreatedAt().after(lastTweet)) {
                            String message = config.Message.replace("%tweet", status3.getText());
                            PlayerDatabase twitterDatabase = new PlayerDatabase(plugin, "Twitter", "Twitter");
                            twitterDatabase.Nick = config.IngameName + " " + status3.getUser().getScreenName();

                            plugin.getPluginLogger().info("Found new Tweet: " + status3.getText());

                            for(String channel : config.PostToChannels) {
                                ChannelDatabase channelDatabase = channelManager.get(channel);
                                Sender sender1 = new Sender("Twitter", channelDatabase, twitterDatabase);

                                ChatMessageEvent chatMessageEvent = new ChatMessageEvent(sender1, message, new ArrayList<String>(){{
                                    add("§ALL");
                                }});
                                plugin.getAsyncEventBus().callEvent(chatMessageEvent);
                            }

                            twitterModule.getTwitterManager().updateLastTweet(user.getName(), status3.getCreatedAt());
                        }
                    }
                }
            }
        } catch (TwitterException e) {
            plugin.getPluginLogger().error("Could not get Twitter Tweets", e);
            throw new RuntimeException();
        }
    }
}
