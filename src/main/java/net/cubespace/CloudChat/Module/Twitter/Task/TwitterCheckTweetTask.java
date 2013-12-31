package net.cubespace.CloudChat.Module.Twitter.Task;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Twitter;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.CloudChat.Module.ChatHandler.Event.ChatMessageEvent;
import net.cubespace.CloudChat.Module.ChatHandler.Sender.Sender;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.Twitter.TwitterModule;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

import java.util.Date;
import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 17:35
 */
public class TwitterCheckTweetTask implements Runnable {
    private TwitterModule twitterModule;
    private CloudChatPlugin plugin;
    private ChannelManager channelManager;

    public TwitterCheckTweetTask(TwitterModule twitterModule, CloudChatPlugin plugin) {
        this.twitterModule = twitterModule;
        this.plugin = plugin;
        this.channelManager = plugin.getManagerRegistry().getManager("channelManager");
    }

    @Override
    public void run() {
        twitter4j.Twitter twitter = twitterModule.getTwitterFactory().getInstance();
        Twitter config = plugin.getConfigManager().getConfig("twitter");

        try {
            String[] srch = config.AccountToMonitor.toArray(new String[0]);
            ResponseList<User> users = twitter.lookupUsers(srch);
            for (User user : users) {
                if (user.getStatus() != null) {
                    List<Status> statusess = twitter.getUserTimeline(user.getScreenName());
                    if(!twitterModule.getTwitterManager().isRegistered(user.getName())) {
                        twitterModule.getTwitterManager().updateLastTweet(user.getName(), statusess.get(0).getCreatedAt());
                        continue;
                    }

                    Date lastTweet = twitterModule.getTwitterManager().getLastTweet(user.getName());
                    for (Status status3 : statusess) {
                        if(status3.getCreatedAt().after(lastTweet)) {
                            String message = config.Message.replace("%tweet", status3.getText());
                            PlayerDatabase twitterDatabase = new PlayerDatabase(plugin, "IRC");
                            twitterDatabase.Nick = config.IngameName + " " + status3.getUser().getScreenName();

                            for(String channel : config.PostToChannels) {
                                ChannelDatabase channelDatabase = channelManager.get(channel);
                                Sender sender1 = new Sender("Twitter", channelDatabase, twitterDatabase);

                                ChatMessageEvent chatMessageEvent = new ChatMessageEvent(sender1, message);
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