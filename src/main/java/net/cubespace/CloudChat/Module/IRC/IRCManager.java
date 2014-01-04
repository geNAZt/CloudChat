package net.cubespace.CloudChat.Module.IRC;

import com.google.common.collect.ArrayListMultimap;
import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Module.IRC.Permission.PermissionManager;
import net.cubespace.CloudChat.Module.IRC.Permission.WhoisResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 13.12.13 21:22
 */
public class IRCManager {
    private CloudChatPlugin plugin;
    //Unresolved Whois
    private HashMap<String, WhoisResolver> unresolvedWhois = new HashMap<>();
    //Resolved Whois
    private HashMap<String, WhoisResolver> resolvedWhois = new HashMap<>();
    //Nicknames in Channels
    private ArrayListMultimap<String, String> nickJoinedChannels = ArrayListMultimap.create();
    //Bot joined Channels
    private ArrayList<String> botJoinedChannels = new ArrayList<>();
    //Permission Manager
    private PermissionManager permissionManager;
    //PM Sessions
    private HashMap<String, PMSession> nickPMSessions = new HashMap<>();
    private IRCModule ircModule;

    public IRCManager(CloudChatPlugin plugin, IRCModule ircModule) {
        this.plugin = plugin;
        this.permissionManager = new PermissionManager(plugin);
        this.ircModule = ircModule;
    }

    /**
     * Check if a Nickname has been resolved or is in the progress of resolving
     *
     * @param nickname
     * @return
     */
    public boolean isResolving(String nickname) {
        return unresolvedWhois.containsKey(nickname) || resolvedWhois.containsKey(nickname);
    }

    /**
     * Add a new Whois Request
     *
     * @param nickname
     * @param whoisResolver
     */
    public void addWhoIsResolver(String nickname, WhoisResolver whoisResolver) {
        unresolvedWhois.put(nickname, whoisResolver);
    }

    /**
     * Move auth information
     *
     * @param oldNick
     * @param newNick
     */
    public boolean moveWhois(String oldNick, String newNick) {
        WhoisResolver whoisResolver = resolvedWhois.get(oldNick);

        //There is a unresolved Whois => Remove it and ask the Server for a new One
        if(whoisResolver == null) {
            unresolvedWhois.remove(oldNick);
            return false;
        }

        resolvedWhois.remove(oldNick);
        resolvedWhois.put(newNick, whoisResolver);
        return true;
    }

    /**
     * Remove whois information for nick
     *
     * @param sourceNick
     */
    public void removeWhois(String sourceNick) {
        if(resolvedWhois.containsKey(sourceNick)) {
            resolvedWhois.remove(sourceNick);
        }

        if(unresolvedWhois.containsKey(sourceNick)) {
            unresolvedWhois.remove(sourceNick);
        }
    }

    /**
     * Process a new Whois Line for all resolvers
     *
     * @param code
     * @param line
     */
    public void newWhoisLine(int code, String line) {
        //Check if its a valid Whois Code
        if(code != 318 && code != 330) return;

        //Split the String ([0] is the nick, [1] is the auth when code is 330)
        String[] split = line.split(" ");

        //Check if there is a unresolved Whois
        if(!unresolvedWhois.containsKey(split[1])) return;
        unresolvedWhois.get(split[1]).onResolveMessage(code, (code == 330) ? split[2] : null);

        //Check if the Whois was resolved
        if(unresolvedWhois.get(split[1]).isResolved()) {
            WhoisResolver whoisResolver = unresolvedWhois.get(split[1]);
            plugin.getPluginLogger().info("Whois got resolved: " + split[1] + " -> " + whoisResolver.getAuth());
            permissionManager.load(split[1], whoisResolver.getAuth());
            unresolvedWhois.remove(split[1]);
            resolvedWhois.put(split[1], whoisResolver);
        }
    }

    /**
     * Add a new Channel for the Nick
     *
     * @param nick
     * @param channel
     */
    public void addJoinedChannel(String nick, String channel) {
        nickJoinedChannels.put(nick, channel);
    }

    /**
     * Remove all joined Channels for a Nick
     *
     * @param nick
     */
    public void removeJoinedChannels(String nick) {
        nickJoinedChannels.removeAll(nick);
    }

    /**
     * Remove a channel from all Nicks
     *
     * @param channel
     */
    public void removeChannel(String channel) {
        for(String nick : nickJoinedChannels.keySet())
            nickJoinedChannels.remove(nick, channel);
    }

    /**
     * Get all Channels a nick is in
     *
     * @param nick
     * @return
     */
    public List<String> getJoinedChannels(String nick) {
        return nickJoinedChannels.get(nick);
    }

    /**
     * Remove the Nick from the Channel
     *
     * @param nick
     * @param channel
     */
    public void removeChannelFromNick(String nick, String channel) {
        nickJoinedChannels.remove(nick, channel);

        if(!nickJoinedChannels.containsKey(nick)) {
            removeWhois(nick);
        }
    }

    /**
     * Check if a Nick is reachable
     *
     * @param nick
     * @return
     */
    public boolean isNickOnline(String nick) {
        return nickJoinedChannels.containsKey(nick);
    }

    /**
     * Check if bot has joined the channel
     *
     * @param channel
     * @return
     */
    public boolean botHasJoined(String channel) {
        return botJoinedChannels.contains(channel);
    }

    /**
     * The Bot has joined a new Channel
     *
     * @param channel
     */
    public void botJoinedChannel(String channel) {
        botJoinedChannels.add(channel);
    }

    /**
     * Get all Channels where the bot is in
     *
     * @return
     */
    public ArrayList<String> getBotJoinedChannels() {
        return botJoinedChannels;
    }

    /**
     * Remove a Channel from the Bot
     *
     * @param channel
     */
    public void removeBotJoinedChannel(String channel) {
        botJoinedChannels.remove(channel);
    }

    public PMSession getPmSession(String nick) {
        return nickPMSessions.get(nick);
    }

    public boolean hasPmSession(String nick) {
        return nickPMSessions.containsKey(nick);
    }

    public void newPMSession(String nick) {
        PMSession pmSession = new PMSession(ircModule, nick);
        nickPMSessions.put(nick, pmSession);
    }

    public void removePMSession(String nick) {
        nickPMSessions.remove(nick);
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
}
