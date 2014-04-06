package net.cubespace.CloudChat.Config.Sub;

import net.cubespace.CloudChat.Module.Spam.SpamAction;
import net.cubespace.Yamler.Config.Config;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class SpamEntry extends Config {
    public final Integer AmountOfMessages = 1000;
    public final Integer InHowMuchSeconds = 10;
    public final SpamAction ActionIfReached = SpamAction.MUTE;
    public final Integer HowLongToMuteInSeconds = 30;
    public final String MuteMessage = "You have been muted for %amount seconds due to spamming";
    public final String CommandToDispatch = "ban %player Dont spam so much";
    public final String KickMessage = "You have been kicked for Spamming";
    public final String ExcludePermission = "cloudchat.spam.rule1";
}
