package net.cubespace.CloudChat.Module.IRC.Permission;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 02.01.14 21:16
 */
public class WhoisResolver {
    private WhoisResolverState resolverState = WhoisResolverState.UNRESOLVED;
    private String auth = null;

    public void onResolveMessage(int code, String auth) {
        if(code == 318)
            this.resolverState = WhoisResolverState.RESOLVED;
        if(code == 330 && this.resolverState.equals(WhoisResolverState.UNRESOLVED))
            this.auth = auth;
    }

    public boolean isResolved() {
        return this.resolverState.equals(WhoisResolverState.RESOLVED);
    }

    public String getAuth() {
        return auth;
    }
}
