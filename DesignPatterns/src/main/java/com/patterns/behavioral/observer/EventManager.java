package com.patterns.behavioral.observer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    Map<String, List<EventListener>> listeners = new HashMap<>();

    public EventManager(String ... operations) {
        for (String operation: operations) {
            this.listeners.put(operation, new ArrayList<>());
        }
    }

    public void subscribe(String eventType, EventListener listener) {
        final List<EventListener> eventTypeListeners = listeners.get(eventType);
        eventTypeListeners.add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        final List<EventListener> eventTypeListeners = listeners.get(eventType);
        eventTypeListeners.remove(listener);
    }

    public void notify(String eventType, File file) {
        final List<EventListener> eventTypeListeners = listeners.get(eventType);
        for (EventListener listener: eventTypeListeners) {
            listener.update(eventType, file);
        }
    }
}
