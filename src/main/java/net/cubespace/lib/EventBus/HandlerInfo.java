package net.cubespace.lib.EventBus;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 18:06
 */
public class HandlerInfo {
    private final Method method;
    private final WeakReference<?> subscriber;
    private final EventHandler annotation;
    private final Class event;

    public HandlerInfo(Class event, Method method, Object subscriber, EventHandler annotation) {
        this.event = event;
        this.method = method;
        this.subscriber = new WeakReference<>(subscriber);
        this.annotation = annotation;
    }

    public Method getMethod() {
        return method;
    }

    public Object getSubscriber() {
        return subscriber.get();
    }

    public EventHandler getAnnotation() {
        return annotation;
    }

    public Class getEvent() {
        return event;
    }
}
