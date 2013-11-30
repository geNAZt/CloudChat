package net.cubespace.CloudChat.Command.Handler;

import net.cubespace.CloudChat.Command.Handler.CLICommand;
import net.cubespace.CloudChat.Command.Handler.Command;

import java.lang.reflect.Method;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 13.11.13 20:54
 */
public class CommandStruct {
    private Command annotation;
    private CLICommand instance;
    private Method command;

    public Command getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Command annotation) {
        this.annotation = annotation;
    }

    public Method getCommand() {
        return command;
    }

    public void setCommand(Method command) {
        this.command = command;
    }

    public CLICommand getInstance() {
        return instance;
    }

    public void setInstance(CLICommand instance) {
        this.instance = instance;
    }
}
