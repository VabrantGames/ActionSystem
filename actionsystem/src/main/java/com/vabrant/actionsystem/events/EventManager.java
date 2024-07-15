
package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.vabrant.actionsystem.actions.ActionPools;

/** A simple reusable event system. Some events are vital to ensure things function properly and therefore can be locked. Locked
 * events will only be removed when explicitly removed with {@link #clearAllListeners(boolean)} or when {@link #reset} is
 * called. */
public class EventManager implements Pool.Poolable {

	private ObjectMap<Class<? extends Event>, EventEntry> events;

	public EventManager () {
		events = new ObjectMap<>(4);
	}

	public ObjectMap<Class<? extends Event>, EventEntry> getEvents () {
		return events;
	}

	public boolean hasEvent (Class<? extends Event> eventType) {
		return events.containsKey(eventType);
	}

	public EventEntry getEntry (Class<? extends Event> entry) {
		return events.get(entry);
	}

	public boolean hasListener (Class<? extends Event> eventType, EventListener<?> listener) {
		if (eventType == null) throw new IllegalArgumentException("Event type is null");
		if (listener == null) throw new IllegalArgumentException("Listener is null");

		EventEntry entry = events.get(eventType);

		if (entry == null) return false;

		return entry.hasListener(listener);
	}

	/** Clears all listeners of an {@link Event}
	 * @param eventType */
	public void clearListeners (Class<? extends Event> eventType) {
		if (eventType == null) throw new IllegalArgumentException("Event type is null");

		EventEntry entry = events.remove(eventType);

		if (entry != null && !entry.isLocked()) {
			ActionPools.free(entry);
		}
	}

	/** Clears listeners of all {@link Event}'s excluding locked events. */
	public void clearAllListeners () {
		clearAllListeners(false);
	}

	private void clearAllListeners (boolean clearLockedEvents) {
		ObjectMap.Entries<Class<? extends Event>, EventEntry> it = events.iterator();

		while (it.hasNext()) {
			ObjectMap.Entry<Class<? extends Event>, EventEntry> e = it.next();
			if (!clearLockedEvents && e.value.isLocked()) continue;
			it.remove();
			ActionPools.free(e.value);
		}
	}

	public void subscribe (Class<? extends Event> eventType, EventListener<?> listener) {
		if (eventType == null) throw new IllegalArgumentException("Event type is null");
		if (listener == null) throw new IllegalArgumentException("Listener is null");

		EventEntry entry = events.get(eventType);

		if (entry == null) {
			entry = ActionPools.obtain(EventEntry.class);
			events.put(eventType, entry);

// if (eventType.charAt(0) == '#') {
// entry.lock();
// }
		}

		entry.addListener(listener);
	}

	public void unsubscribe (Class<? extends Event> eventType, EventListener<?> listener) {
		if (eventType == null) throw new IllegalArgumentException("Event type is null");
		if (listener == null) throw new IllegalArgumentException("Listener is null");

		EventEntry entry = events.get(eventType);

		if (entry != null) {
			entry.removeListener(listener);
		}
	}

	public void fire (Event event) {
		EventEntry entry = events.get(event.getClass());

		if (entry != null) {
			entry.fire(event);
		}
	}

	@Override
	public void reset () {
		clearAllListeners(true);
	}
}
