package net.cubespace.lib.EventBus;

import net.cubespace.lib.Module.Module;

import java.lang.reflect.Method;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 18:06
 */
public class HandlerInfo {
    private final Method method;
    private final Object subscriber;
    private final EventHandler annotation;
    private final Class event;
    private Module module = null;

    public HandlerInfo(Class event, Method method, Object subscriber, EventHandler annotation, Module module) {
        this(event, method, subscriber, annotation);
        this.module = module;
    }

    public HandlerInfo(Class event, Method method, Object subscriber, EventHandler annotation) {
        this.event = event;
        this.method = method;
        this.subscriber = subscriber;
        this.annotation = annotation;
    }

    public Method getMethod() {
        return method;
    }

    public Object getSubscriber() {
        return subscriber;
    }

    public EventHandler getAnnotation() {
        return annotation;
    }

    public Class getEvent() {
        return event;
    }

    public Module getModule() {
        return module;
    }
}
