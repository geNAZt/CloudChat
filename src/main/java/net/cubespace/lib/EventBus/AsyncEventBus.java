package net.cubespace.lib.EventBus;

import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.12.13 00:07
 */
public class AsyncEventBus {
    private class SortListener implements Comparator<HandlerInfo> {
        private int calcCompare(HandlerInfo e) {
            int ret = 0;

            if(e.getAnnotation().canVeto()) ret += 1000;
            ret += e.getAnnotation().priority().getPrio();

            return 2000 - ret;
        }

        @Override
        public int compare(HandlerInfo a1, HandlerInfo a2) {
            return calcCompare(a1) - calcCompare(a2);
        }
    }

    private final HashMap<Class, ArrayList<HandlerInfo>> handlers = new HashMap<>();
    private final BlockingQueue<Event> queue = new LinkedBlockingQueue<>();
    private final BlockingQueue<HandlerInfo> killQueue = new LinkedBlockingQueue<>();

    private ScheduledTask killQueueTask;
    private ScheduledTask eventQueueTask;

    private final CubespacePlugin plugin;

    public AsyncEventBus(final CubespacePlugin plugin) {
        this.plugin = plugin;

        plugin.getPluginLogger().info("Creating new AsyncEventBus");

        //Start the killQueueTask
        killQueueTask = plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        HandlerInfo info = killQueue.take();
                        if (info.getSubscriber() == null) {
                            plugin.getPluginLogger().debug("Removing " + info.getMethod().getDeclaringClass().getName() + "." + info.getMethod().getName() + " from the Handler List");
                            synchronized (handlers) {
                                handlers.get(info.getEvent()).remove(info);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    plugin.getPluginLogger().warn("AsyncEventBus killTask got interrupted", e);
                    throw new RuntimeException(e);
                }
            }
        });

        //Start the eventQueueTask
        eventQueueTask = plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    while(true) {
                        handleEvent(queue.take());
                    }
                } catch (InterruptedException e) {
                    plugin.getPluginLogger().warn("AsyncEventBus eventQueueTask got interrupted", e);
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void callEvent(Event event) {
        queue.add(event);
    }

    public Event callEventSync(Event event) {
        return handleEvent(event);
    }

    private Event handleEvent(Event event) {
        long start = System.nanoTime();
        plugin.getPluginLogger().debug("Firing Event: " + event.getClass().getName());

        //Get all Handlers
        if(!handlers.containsKey(event.getClass())) {
            plugin.getPluginLogger().debug("Event: " + event.getClass().getName() + " has no Listeners. Skipped.");
            plugin.getPluginLogger().debug("Handled in " + (System.nanoTime() - start) + " ns");
            return event;
        }

        List<HandlerInfo> handlerInfos = handlers.get(event.getClass());

        if(handlerInfos.size() == 0) {
            plugin.getPluginLogger().debug("Event: " + event.getClass().getName() + " has no Listeners. Skipped.");
            plugin.getPluginLogger().debug("Handled in " + (System.nanoTime() - start) + " ns");
            return event;
        }

        //Call the Handlers
        boolean vetoed = false;
        for(HandlerInfo handlerInfo : handlerInfos) {
            if(!handlerInfo.getAnnotation().ignoreVeto() && vetoed) {
                plugin.getPluginLogger().debug("Event was vetoed and the Listener " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName() +" does not ignore it");
                continue;
            }

            if(handlerInfo.getAnnotation().canVeto()) {
                try {
                    plugin.getPluginLogger().debug("Calling Listener " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName());
                    boolean ret = (boolean) handlerInfo.getMethod().invoke(handlerInfo.getSubscriber(), event);
                    if(ret) {
                        vetoed = true;
                        plugin.getPluginLogger().debug("Listener " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName() + " has vetoed the Event by returning true");
                    }
                } catch (Exception e) {
                    vetoed = true;
                    plugin.getPluginLogger().debug("Listener " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName() + " has vetoed the Event by Exception", e);
                }
            } else {
                try {
                    plugin.getPluginLogger().debug("Calling Listener " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName());
                    System.out.println(handlerInfo.getSubscriber());
                    handlerInfo.getMethod().invoke(handlerInfo.getSubscriber(), event);
                } catch (Exception e) {
                    plugin.getPluginLogger().warn("Listener " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName() + " has thrown an Exception", e);
                }
            }
        }

        plugin.getPluginLogger().debug("Handled in " + (System.nanoTime() - start) + " ns");
        return event;
    }

    public void addListener(Object subscriber) {
        plugin.getPluginLogger().debug("Attempt to add a new Listener " + subscriber.getClass().getName());

        Method[] methods = subscriber.getClass().getDeclaredMethods();
        for (Method method : methods) {
            // look for the EventHandler annotation on the method, if it exists
            // if not, this returns null, and go to the next method
            EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) {
                plugin.getPluginLogger().debug("Method " + subscriber.getClass().getName() + "." + method.getName() + " has no EventHandler annotation. Skipped");
                continue;
            }

            // evaluate the parameters of the method.
            // only a single parameter of the Object type is allowed for the handler method.
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) {
                plugin.getPluginLogger().warn("Method " + subscriber.getClass().getName() + "." + method.getName() + " does not take exactly one Parameter. Skipped");
                continue;
            }

            //Check if first argument is a Event
            if (!(Event.class.isAssignableFrom(parameters[0]))) {
                plugin.getPluginLogger().warn("Method " + subscriber.getClass().getName() + "." + method.getName() + " does not take a Event as first Parameter. Skipped");
                continue;
            }

            //Check if the return type is correct if it can veto
            if(eh.canVeto() && !method.getReturnType().toString().equals("boolean")) {
                plugin.getPluginLogger().warn("Method " + subscriber.getClass().getName() + "." + method.getName() + " should veto but has no boolean return type. Skipped");
                continue;
            }

            //Check if this Handler has been registered
            boolean skipping = false;
            synchronized (handlers) {
                for(Map.Entry<Class, ArrayList<HandlerInfo>> handlerInfos : handlers.entrySet()) {
                    for(HandlerInfo handlerInfo : handlerInfos.getValue()) {
                        if (handlerInfo.getSubscriber() == null) {
                            try {
                                killQueue.put(handlerInfo);
                            } catch (InterruptedException e) {
                                plugin.getPluginLogger().warn("Could not add a HandlerInfo to the killQueue", e);
                            }

                            continue;
                        }

                        if(handlerInfo.getMethod().equals(method) && handlerInfo.getEvent().equals(parameters[0])) {
                            skipping = true;
                            break;
                        }
                    }

                    if(skipping) {
                        break;
                    }
                }
            }

            if(skipping) {
                plugin.getPluginLogger().warn("Method " + subscriber.getClass().getName() + "." + method.getName() + " can not be registered twice. Skipped", new Exception());
                continue;
            }

            // add the subscriber to the list
            HandlerInfo info = new HandlerInfo(parameters[0], method, subscriber, eh);

            if(handlers.containsKey(parameters[0])) {
                handlers.get(parameters[0]).add(info);
                Collections.sort(handlers.get(parameters[0]), new SortListener());
            }

            else {
                ArrayList<HandlerInfo> handlerInfos = new ArrayList<>();
                handlerInfos.add(info);
                handlers.put(parameters[0], handlerInfos);
            }

            plugin.getPluginLogger().debug("Method " + subscriber.getClass().getName() + "." + method.getName() + " registered for Event " + parameters[0].getName());

            for(HandlerInfo handlerInfo : handlers.get(parameters[0])) {
                plugin.getPluginLogger().debug(" " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName());
            }
        }
    }

    public void removeListener(Object subscriber) {
        List<HandlerInfo> killList = new ArrayList<>();

        synchronized (handlers) {
            for(Map.Entry<Class, ArrayList<HandlerInfo>> handlerInfos : handlers.entrySet()) {
                for(HandlerInfo handlerInfo : handlerInfos.getValue()) {
                    if (handlerInfo.getSubscriber() == null) {
                        plugin.getPluginLogger().debug("Adding Listener to the KillQueue " + handlerInfo.getMethod().getDeclaringClass().getName() + "." + handlerInfo.getMethod().getName());
                        try {
                            killQueue.put(handlerInfo);
                        } catch (InterruptedException e) {
                            plugin.getPluginLogger().warn("Could not add a HandlerInfo to the killQueue", e);
                        }

                        continue;
                    }

                    Object obj = handlerInfo.getSubscriber();
                    if (obj == null || obj == subscriber) {
                        killList.add(handlerInfo);
                    }
                }
            }
        }

        for (HandlerInfo kill : killList) {
            plugin.getPluginLogger().debug("Killed " + kill.getMethod().getDeclaringClass().getName() + "." + kill.getMethod().getName());
            handlers.get(kill.getEvent()).remove(kill);
        }
    }
}
