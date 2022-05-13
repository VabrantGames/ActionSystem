package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.vabrant.actionsystem.actions.Action;

import java.util.Iterator;

public class EventManager implements Pool.Poolable {

    private ObjectMap<String, EventEntry> events;

    public EventManager() {
        events = new ObjectMap<>(4);
    }

    public ObjectMap<String, EventEntry> getEvents() {
        return events;
    }

    public void subscribe(String eventType, EventListener listener) {
        if (eventType == null) throw new IllegalArgumentException("Event type is null");
        if (listener == null) throw new IllegalArgumentException("Listener is null");

        EventEntry entry = events.get(eventType);

        if (entry == null) {
            entry = Pools.obtain(EventEntry.class);
            events.put(eventType, entry);
        }

        entry.addListener(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        if (eventType == null) throw new IllegalArgumentException("Event type is null");
        if (listener == null) throw new IllegalArgumentException("Listener is null");

        EventEntry entry = events.get(eventType);

        if (entry != null) {
            entry.removeListener(listener);
        }
    }

    public <T extends Event> void fire(T event) {
        EventEntry entry = events.get(event.getType());

        if (entry != null) {
            entry.fire(event);
        }
    }

    @Override
    public void reset() {
        ObjectMap.Entries<String, EventEntry> it = events.iterator();

        while (it.hasNext()) {
            ObjectMap.Entry<String, EventEntry> e = it.next();
            it.remove();
            Pools.free(e.value);
        }
    }
}
