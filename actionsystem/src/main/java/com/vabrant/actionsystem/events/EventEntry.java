package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.vabrant.actionsystem.actions.Action;

public class EventEntry implements Pool.Poolable {

    private Array<EventListener> listeners;

    public EventEntry() {
        listeners = new Array<>();
    }

    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        listeners.removeValue(listener, false);
    }

    public boolean hasListener(EventListener listener) {
        return listeners.contains(listener, false);
    }

    public Array<EventListener> getListeners() {
        return listeners;
    }

    public <T extends Event> void fire(T e) {
        for (EventListener<T> l : listeners) {
            l.onEvent(e);
        }
    }

    @Override
    public void reset() {
        listeners.clear();
    }
}
