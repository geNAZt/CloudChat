package net.cubespace.CloudChat.Module.IRC;

import com.google.common.collect.ArrayListMultimap;
import net.cubespace.CloudChat.Module.IRC.Permission.PermissionManager;
import net.cubespace.CloudChat.Module.IRC.Permission.WhoisResolver;
import net.cubespace.lib.CubespacePlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
public class IRCManager {
    private final CubespacePlugin plugin;
    //Unresolved Whois
    private final HashMap<String, WhoisResolver> unresolvedWhois = new HashMap<>();
    //Resolved Whois
    private final HashMap<String, WhoisResolver> resolvedWhois = new HashMap<>();
    //Nicknames in Channels
    private final ArrayListMultimap<String, String> nickJoinedChannels = ArrayListMultimap.create();
    //Bot joined Channels
    private final ArrayList<String> botJoinedChannels = new ArrayList<>();
    //Permission Manager
    private final PermissionManager permissionManager;
    //PM Sessions
    private final HashMap<String, PMSession> nickPMSessions = new HashMap<>();
    //Scmd Sessions
    private final ArrayList<ScmdSession> scmdSessions = new ArrayList<>();

    private final IRCModule ircModule;


    public IRCManager(CubespacePlugin plugin, IRCModule ircModule) {
        this.plugin = plugin;
        this.permissionManager = new PermissionManager(this, plugin);
        this.ircModule = ircModule;

        plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
            @Override
            public void run() {
                for(ScmdSession scmdSession : new ArrayList<>(scmdSessions)) {
                    if(System.currentTimeMillis() - scmdSession.getLastResponse() > 15 * 60 * 1000) {
                        scmdSessions.remove(scmdSession);
                    }
                }
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isResolving(String nickname) {
        return unresolvedWhois.containsKey(nickname) || resolvedWhois.containsKey(nickname);
    }

    public void addWhoIsResolver(String nickname, WhoisResolver whoisResolver) {
        unresolvedWhois.put(nickname, whoisResolver);
    }

    public boolean moveWhois(String oldNick, String newNick) {
        WhoisResolver whoisResolver = resolvedWhois.get(oldNick);

        //There is a unresolved Whois => Remove it and ask the Server for a new One
        if(whoisResolver == null) {
            unresolvedWhois.remove(oldNick);
            permissionManager.remove(oldNick);
            return false;
        }

        resolvedWhois.remove(oldNick);
        resolvedWhois.put(newNick, whoisResolver);

        permissionManager.move(oldNick, newNick);

        return true;
    }

    public void removeWhois(String sourceNick) {
        if(resolvedWhois.containsKey(sourceNick)) {
            resolvedWhois.remove(sourceNick);
        }

        if(unresolvedWhois.containsKey(sourceNick)) {
            unresolvedWhois.remove(sourceNick);
        }

        permissionManager.remove(sourceNick);
    }

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

    public void addJoinedChannel(String nick, String channel) {
        nickJoinedChannels.put(nick, channel);
    }

    public void removeJoinedChannels(String nick) {
        nickJoinedChannels.removeAll(nick);
    }

    public void removeChannel(String channel) {
        for(String nick : nickJoinedChannels.keySet())
            nickJoinedChannels.remove(nick, channel);
    }

    public List<String> getJoinedChannels(String nick) {
        return nickJoinedChannels.get(nick);
    }

    public void removeChannelFromNick(String nick, String channel) {
        nickJoinedChannels.remove(nick, channel);

        if(!nickJoinedChannels.containsKey(nick)) {
            removeWhois(nick);
        }
    }

    public boolean isNickOnline(String nick) {
        return nickJoinedChannels.containsKey(nick);
    }

    public boolean botHasJoined(String channel) {
        return botJoinedChannels.contains(channel);
    }

    public void botJoinedChannel(String channel) {
        botJoinedChannels.add(channel);
    }

    public ArrayList<String> getBotJoinedChannels() {
        return botJoinedChannels;
    }

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

    public ScmdSession getScmdSession(Integer scmdId) {
        for(ScmdSession scmdSession : scmdSessions) {
            if(scmdSession.getId().equals(scmdId)) {
                return scmdSession;
            }
        }

        return null;
    }

    public boolean hasScmdSessions(String nick) {
        for(ScmdSession scmdSession : scmdSessions) {
            if(scmdSession.getNickname().equals(nick)) {
                return true;
            }
        }

        return false;
    }

    public Integer newScmdSession(String nick, String channel, String command) {
        ScmdSession scmdSession = new ScmdSession(nick, channel, ((Long)System.currentTimeMillis()).intValue(), command);
        scmdSessions.add(scmdSession);

        return scmdSession.getId();
    }

    public void removeScmdSessions(String nick) {
        for(ScmdSession scmdSession : new ArrayList<>(scmdSessions)) {
            if(scmdSession.getNickname().equals(nick)) {
                scmdSessions.remove(scmdSession);
            }
        }
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }

    public void moveScmdSessions(String oldNick, String newNick) {
        for(ScmdSession scmdSession : new ArrayList<>(scmdSessions)) {
            if(scmdSession.getNickname().equals(oldNick)) {
                scmdSession.setNickname(newNick);

                if(scmdSession.getChannel().equals(oldNick)) {
                    scmdSession.setChannel(newNick);
                }
            }
        }
    }

    public String getNickForAuth(String auth) {
        for(Map.Entry<String, WhoisResolver> resolverEntry : resolvedWhois.entrySet()) {
            if(resolverEntry.getValue().getAuth().equals(auth)) {
                return resolverEntry.getKey();
            }
        }

        return null;
    }
}
