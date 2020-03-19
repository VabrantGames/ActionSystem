package com.vabrant.actionsystem.test.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.ActionWatcher;

public class ActionWatcherTest {
	
	private static class TestAction extends Action<TestAction> {
		
		public static TestAction getAction() {
			return obtain(TestAction.class);
		}
	}
	
	@Before
	public void init() {
		ActionLogger.useSysOut();
		ActionWatcher.getLogger().setLevel(ActionLogger.DEBUG);
	}
	
	@Test
	public void cleanupTest() {
		ActionManager manager = new ActionManager();
		manager.getLogger().setLevel(ActionLogger.DEBUG);

		//create test action
		String name = "Cleanup";
		TestAction action = TestAction.getAction();
		action.setName(name);
		action.setLogLevel(ActionLogger.DEBUG);
		action.watchAction();
		action.start();
		
		//add action
		manager.addAction(action);
		
		//pools action instantly
		manager.freeAll();
		
		assertTrue(ActionWatcher.get(name) == null);
	}

}
