
package com.vabrant.actionsystem.events;

public interface EventListener<T extends Event> {
	void onEvent (T e);
}
