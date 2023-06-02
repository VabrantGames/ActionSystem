
package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.ObjectMap;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.events.*;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class EventManagerTest {

	private static Application application;

	@BeforeClass
	public static void init () {
		application = new HeadlessApplication(new ApplicationAdapter() {});
	}

	@Test
	public void basicTest () {
		final boolean[] wereEventsCalled = {false, false, false};

		TestAction action = new TestAction().subscribeToEvent(ActionEvent.START_EVENT, new ActionListener() {
			@Override
			public void onEvent (ActionEvent e) {
				wereEventsCalled[0] = true;
			}
		}).subscribeToEvent(ActionEvent.END_EVENT, new ActionListener() {
			@Override
			public void onEvent (ActionEvent e) {
				wereEventsCalled[1] = true;
			}
		}).subscribeToEvent(CountEvent.COUNT_EVENT, new CountListener() {
			@Override
			public void onEvent (CountEvent e) {
				wereEventsCalled[2] = true;
			}
		});

		action.fakeStart();
		action.fakeEnd();
		action.incrementCount();

		assertArrayEquals(new boolean[] {true, true, true}, wereEventsCalled);
	}

	@Test
	public void resetTest () {
		EventManager manager = new EventManager();

		EventListener<?> listener = new EmptyEventListener();

		manager.subscribe("one", listener);
		manager.subscribe("two", listener);
		manager.subscribe("#three", listener);

		ObjectMap<String, EventEntry> events = manager.getEvents();

		assertEquals(3, events.size);

		manager.reset();

		assertEquals(0, events.size);
	}

	@Test
	public void hasEventTest () {
		final String testEvent = "test";
		EventManager manager = new EventManager();

		manager.subscribe(testEvent, new EmptyEventListener());

		assertTrue(manager.hasEvent(testEvent));
	}

	@Test
	public void hasListenerTest () {
		final String testEvent = "test";
		EventManager manager = new EventManager();
		EventListener<?> listener = new EmptyEventListener();

		manager.subscribe(testEvent, listener);

		assertTrue(manager.hasListener(testEvent, listener));
	}

	@Test
	public void unsubscribeTest () {
		final String event = "event";
		EventManager manager = new EventManager();
		EventListener<?> listener = new EmptyEventListener();

		manager.subscribe(event, listener);
		manager.unsubscribe(event, listener);

		assertFalse(manager.hasListener(event, listener));
	}

	@Test
	public void clearListenersTest () {
		final String event = "event";
		final String lockedEvent = "#event";
		EventManager manager = new EventManager();

		manager.subscribe(event, new EmptyEventListener());
		manager.subscribe(event, new EmptyEventListener());
		manager.subscribe(lockedEvent, new EmptyEventListener());

		// Non locked events should be cleared
		manager.clearListeners(event);
		assertFalse(manager.hasEvent(event));

		// Locked events should not be cleared
		assertTrue(manager.hasEvent(lockedEvent));
	}

	@Test
	@Ignore
	public void TemplateListenerTest () {
		TestAction action = new TestAction();

		ActionListenerTemplate listener = new ActionListenerTemplate();
		listener.setEndListener(new ActionListener() {
			@Override
			public void onEvent (ActionEvent e) {
				System.out.println("Hello start event");
			}
		});
		listener.setStartListener(new ActionListener() {
			@Override
			public void onEvent (ActionEvent e) {
				System.out.println("Hello end event");
			}
		});

		action.subscribeToEvent(ActionEvent.START_EVENT, listener);
		action.subscribeToEvent(ActionEvent.END_EVENT, listener);
	}

	private class ActionListenerTemplate implements EventListener<ActionEvent> {

		private ActionListener startListener;
		private ActionListener endListener;

		@Override
		public void onEvent (ActionEvent e) {
			switch (e.getType()) {
			case ActionEvent.START_EVENT:
				if (startListener != null) startListener.onEvent(e);
				break;
			case ActionEvent.END_EVENT:
				if (endListener != null) endListener.onEvent(e);
				break;
			}
		}

		ActionListenerTemplate setStartListener (ActionListener listener) {
			startListener = listener;
			return this;
		}

		ActionListenerTemplate setEndListener (ActionListener listener) {
			endListener = listener;
			return this;
		}
	}

	private interface ActionListener extends EventListener<ActionEvent> {
	}

	private static class ActionEvent extends Event {

		private static final String START_EVENT = "start";
		private static final String END_EVENT = "end";

		public ActionEvent () {
			super(null);
		}

		public void setAsStart () {
			type = START_EVENT;
		}

		public void setAsEnd () {
			type = END_EVENT;
		}
	}

	private interface CountListener extends EventListener<CountEvent> {
	}

	private static class CountEvent extends Event {

		int count;
		public static final String COUNT_EVENT = "count_event";

		public CountEvent () {
			super(COUNT_EVENT);
		}

		public int getCount () {
			return count;
		}
	}

	private class TestAction extends Action {

		private int count;
		private EventManager eventManager = new EventManager();

		public void fakeStart () {
			ActionEvent event = new ActionEvent();
			event.setAsStart();
			eventManager.fire(event);
		}

		public void fakeEnd () {
			ActionEvent event = new ActionEvent();
			event.setAsEnd();
			eventManager.fire(event);
		}

		public void incrementCount () {
			CountEvent event = new CountEvent();
			event.count = count++;
			eventManager.fire(event);
		}

		public TestAction subscribeToEvent (String eventType, EventListener listener) {
			eventManager.subscribe(eventType, listener);
			return this;
		}

		public TestAction unsubscribeFromEvent (String eventType, EventListener listener) {
			eventManager.unsubscribe(eventType, listener);
			return this;
		}
	}

	private static class EmptyEventListener implements EventListener {
		@Override
		public void onEvent (Event e) {
		}
	};
}
