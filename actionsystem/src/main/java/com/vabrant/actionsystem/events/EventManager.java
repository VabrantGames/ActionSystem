package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

/**
 * A simple reusable event system. Some events are vital to ensure things function properly and therefore can be
 * locked. Locked events will only be removed when explicitly removed with {@link #removeListener} or when {@link #reset} is called.
 */
public class EventManager implements Pool.Poolable {

    private ObjectMap<String, EventEntry> events;

    public EventManager() {
        events = new ObjectMap<>(4);
    }

    public ObjectMap<String, EventEntry> getEvents() {
        return events;
    }

    public boolean hasEvent(String eventType) {
        return events.containsKey(eventType);
    }

    public boolean hasListener(String eventType, EventListener<?> listener) {
        if (eventType == null) throw new IllegalArgumentException("Event type is null");
        if (listener == null) throw new IllegalArgumentException("Listener is null");

        EventEntry entry = events.get(eventType);

        if (entry == null) return false;

        return entry.hasListener(listener);
    }

    /**
     * Clears all listeners of an {@link Event}
     * @param eventType
     */
    public void clearListeners(String eventType) {
        if (eventType == null) throw new IllegalArgumentException("Event type is null");

        EventEntry entry = events.remove(eventType);

        if (entry != null && !entry.isLocked()) {
            Pools.free(entry);
        }
    }

    /**
     * Clears listeners of all {@link Event}'s excluding locked events.
     */
    public void clearAllListeners() {
        clearAllListeners(false);
    }

    private void clearAllListeners(boolean clearLockedEvents) {
        ObjectMap.Entries<String, EventEntry> it = events.iterator();

        while (it.hasNext()) {
            ObjectMap.Entry<String, EventEntry> e = it.next();
            if (!clearLockedEvents && e.value.isLocked()) continue;
            it.remove();
            Pools.free(e.value);
        }
    }

    public void subscribe(String eventType, EventListener<?> listener) {
        if (eventType == null) throw new IllegalArgumentException("Event type is null");
        if (listener == null) throw new IllegalArgumentException("Listener is null");

        EventEntry entry = events.get(eventType);

        if (entry == null) {
            entry = Pools.obtain(EventEntry.class);
            events.put(eventType, entry);

            if (eventType.charAt(0) == '#') {
                entry.lock();
            }
        }

        entry.addListener(listener);
    }

    public void unsubscribe(String eventType, EventListener<?> listener) {
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
        clearAllListeners(true);
    }

}
