package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pools;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.events.*;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EventManagerTest {

    private static Application application;

    @BeforeClass
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {
        });
    }

    @Test
    public void basicTest() {
        TestAction action = new TestAction()
                .subscribeToEvent(ActionEvent.START_EVENT, new ActionListener() {
                    @Override
                    public void onEvent(ActionEvent e) {
                        System.out.println("Action has started");
                    }
                })
                .subscribeToEvent(ActionEvent.END_EVENT, new ActionListener() {
                    @Override
                    public void onEvent(ActionEvent e) {
                        System.out.println("Action has ended");
                    }
                })
                .subscribeToEvent(CountEvent.COUNT_EVENT, new CountListener() {
                    @Override
                    public void onEvent(CountEvent e) {
                        System.out.println(e.getCount());
                    }

                });

        TestActionAdapter ad = new TestActionAdapter();
        ad.setEndListener(new ActionListener() {
            @Override
            public void onEvent(ActionEvent e) {

            }
        });
        ad.setStartListener(new ActionListener() {
            @Override
            public void onEvent(ActionEvent e) {

            }
        });

        action.subscribeToEvent("", ad);
        action.subscribeToEvent("", ad);

        action.fakeStart();
        action.fakeEnd();
        action.incrementCount();
        action.incrementCount();
        action.incrementCount();
    }

    @Test
    public void resetTest() {
        EventManager manager = new EventManager();

        EventListener listener = new EventListener() {
            @Override
            public void onEvent(Event e) {
            }
        };

        manager.subscribe("one", listener);
        manager.subscribe("two", listener);
        manager.subscribe("three", listener);

        ObjectMap<String, EventEntry> events = manager.getEvents();

        assertEquals(3, events.size);

        manager.reset();

        assertEquals(0, events.size);
    }

    private interface CountListener extends EventListener<CountEvent> {
    }

    private class TestActionAdapter implements EventListener<ActionEvent> {

        private ActionListener startListener;
        private ActionListener endListener;

        @Override
        public void onEvent(ActionEvent e) {
            switch (e.getType()) {
                case ActionEvent.START_EVENT:
                    if (startListener != null) startListener.onEvent(e);
                    break;
                case ActionEvent.END_EVENT:
                    if (endListener != null) endListener.onEvent(e);
                    break;
            }
        }

        public TestActionAdapter setStartListener(ActionListener listener) {
            startListener = listener;
            return this;
        }

        public TestActionAdapter setEndListener(ActionListener listener) {
            endListener = listener;
            return this;
        }
    }

    private class TestAction extends Action {

        private int count;
        private EventManager eventManager = new EventManager();

        public void fakeStart() {
            ActionEvent event = new ActionEvent();
            event.setAsStart();
            event.setAction(this);
            eventManager.fire(event);
        }

        public void fakeEnd() {
            ActionEvent event = new ActionEvent();
            event.setAsEnd();
            event.setAction(this);
            eventManager.fire(event);
        }

        public void fakeKill() {
            ActionEvent event = new ActionEvent()
                    .setAsKill()
                    .setAction(this);
            eventManager.fire(event);
        }

        public void fakeRestart() {
            ActionEvent event = new ActionEvent()
                    .setAsRestart()
                    .setAction(this);
            eventManager.fire(event);
        }

        public void incrementCount() {
            CountEvent event = new CountEvent();
            event.count = count++;
            eventManager.fire(event);
        }

        public TestAction subscribeToEvent(String eventType, EventListener listener) {
            eventManager.subscribe(eventType, listener);
            return this;
        }

        public TestAction unsubscribeFromEvent(String eventType, EventListener listener) {
            eventManager.unsubscribe(eventType, listener);
            return this;
        }

    }

    private static class CountEvent extends Event {

        int count;
        public static final String COUNT_EVENT = "count_event";

        public CountEvent() {
            super(COUNT_EVENT);
        }

        public int getCount() {
            return count;
        }
    }


}