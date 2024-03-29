
package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class EventEntry implements Pool.Poolable {

	private boolean isLocked;
	private Array<EventListener> listeners;

	public EventEntry () {
		listeners = new Array<>();
	}

	void lock () {
		isLocked = true;
	}

	public boolean isLocked () {
		return isLocked;
	}

	public void addListener (EventListener listener) {
		listeners.add(listener);
	}

	public void removeListener (EventListener listener) {
		listeners.removeValue(listener, false);
	}

	public boolean hasListener (EventListener listener) {
		return listeners.contains(listener, false);
	}

	public Array<EventListener> getListeners () {
		return listeners;
	}

	public <T extends Event> void fire (T e) {
		for (EventListener<T> l : listeners) {
			l.onEvent(e);
		}
	}

	@Override
	public void reset () {
		isLocked = false;
		listeners.clear();
	}
}
