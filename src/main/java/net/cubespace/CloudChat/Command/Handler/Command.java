package net.cubespace.CloudChat.Command.Handler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String command();
    int arguments();
}
