package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.test.ActionSystemTestConstantsAndUtils;
import com.vabrant.actionsystem.test.tests.TestActions;
import com.vabrant.actionsystem.test.tests.TestActions.SingleParentTestAction;
import com.vabrant.actionsystem.test.tests.TestActions.TestAction;

public class ActionWatcherTest {
	
	private static ActionWatcher watcher;
	
	@BeforeClass
	public static void init() {
		ActionLogger.useSysOut();
		watcher = new ActionWatcher(2);
		watcher.getLogger().setLevel(ActionLogger.DEBUG);
		
	}
	
	public void makeRoot(Action<?> action) {
		try {
			Method m = ClassReflection.getDeclaredMethod(Action.class, "setRoot");
			m.setAccessible(true);
			m.invoke(action, null);
			action.setRootAction(action);
		}
		catch(ReflectionException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	@Test
	public void basicTest() {
		ActionSystemTestConstantsAndUtils.printTestHeader("Basic Test");
		
		final String tag = "basic";
		
		//Create parent and child actions
		SingleParentTestAction parent = SingleParentTestAction.obtain();
		TestAction child = TestAction.obtain()
				.setName(tag)
				.setLogLevel(ActionLogger.DEBUG)
				.watchAction(watcher);
		
		parent.set(child);
		
		makeRoot(parent);
		parent.start();

		//Get action from watcher
		Action<?> action = watcher.get(tag);
		
		//Do what ever you want with action
		if(action != null) {
			action.getLogger().debug("Hello World");
			action.end();
		}
		
		parent.end();
		ActionPools.free(action);
		
		assertFalse(watcher.contains(tag));
	}

	/**
	 * Creates a test action, starts the action and removes the action from the watcher while the action is running.
	 */
	@Test
	public void explicitRemoveTest() {
		ActionSystemTestConstantsAndUtils.printTestHeader("Explicit Remove Test");
		
		final String tag = "remove";
		
		TestAction action = TestAction.obtain()
				.setName(tag)
				.setLogLevel(ActionLogger.DEBUG)
				.watchAction(watcher);
		
		makeRoot(action);
		action.start();
		
		boolean removed = watcher.remove(tag);
		
		assertTrue(removed);
		
		action.end();
		ActionPools.free(action);
	}
}
