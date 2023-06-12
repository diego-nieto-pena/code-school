package com.patterns.behavioral.observer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManagerR {

    private Map<String, List<Listener>> subscribers = new HashMap<>();

    public void subscribe(String topic, Listener listener) {
        final List<Listener> listeners = subscribers.get(topic);
        listeners.add(listener);
    }

    public void unsubscribe(String topic, Listener listener) {
        final List<Listener> listeners = subscribers.get(topic);
        listeners.remove(listener);
    }

    public void notify(String topic) {
        final List<Listener> listeners = subscribers.get(topic);
        for (Listener listener : listeners) {
            listener.notify();
        }
    }
}
