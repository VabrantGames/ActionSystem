
package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.events.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class EventManagerTest {

	private static Application application;

	@BeforeClass
	public static void init () {
		application = new HeadlessApplication(new ApplicationAdapter() {});
	}

	@Test
	public void BasicTest () {
		EventManager manager = new EventManager();
		EventListener listener = new EmptyEventListener();

		manager.subscribe(CountEvent.class, listener);

		assertTrue(manager.hasEvent(CountEvent.class));
		assertTrue(manager.hasListener(CountEvent.class, listener));

		manager.unsubscribe(CountEvent.class, listener);

		assertFalse(manager.hasListener(CountEvent.class, listener));

		manager.reset();

		assertFalse(manager.hasEvent(CountEvent.class));
	}

	@Test
	public void clearListenersTest () {
		EventManager manager = new EventManager();

		class SomeEvent extends Event {
		}

		manager.subscribe(SomeEvent.class, new EmptyEventListener());
		manager.subscribe(CountEvent.class, new EmptyEventListener());

		// Non locked events should be cleared
		manager.clearListeners(SomeEvent.class);
		assertFalse(manager.hasEvent(SomeEvent.class));

		manager.clearAllListeners();
		assertFalse(manager.hasEvent(CountEvent.class));
	}

	private static class CountEvent extends Event {

		int count;

		public int getCount () {
			return count;
		}
	}

	private class TestAction extends Action<TestAction> {

		private int count;
		private EventManager eventManager = new EventManager();

		public void incrementCount () {
			CountEvent event = new CountEvent();
			event.count = count++;
			eventManager.fire(event);
		}
	}

	private static class EmptyEventListener implements EventListener {
		@Override
		public void onEvent (Event e) {
		}
	};
}
