package net.cubespace.CloudChat.Config.Sub;

import net.cubespace.CloudChat.Module.Spam.SpamAction;
import net.cubespace.Yamler.Config.Config;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class SpamEntry extends Config {
    @SuppressWarnings("CanBeFinal")
    public Integer AmountOfMessages = 1000;
    @SuppressWarnings("CanBeFinal")
    public Integer InHowMuchSeconds = 10;
    @SuppressWarnings("CanBeFinal")
    public SpamAction ActionIfReached = SpamAction.MUTE;
    @SuppressWarnings("CanBeFinal")
    public Integer HowLongToMuteInSeconds = 30;
    @SuppressWarnings("CanBeFinal")
    public String MuteMessage = "You have been muted for %amount seconds due to spamming";
    @SuppressWarnings("CanBeFinal")
    public String CommandToDispatch = "ban %player Dont spam so much";
    @SuppressWarnings("CanBeFinal")
    public String KickMessage = "You have been kicked for Spamming";
    @SuppressWarnings("CanBeFinal")
    public String ExcludePermission = "cloudchat.spam.rule1";
}
