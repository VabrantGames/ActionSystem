package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.actions.*;
import com.vabrant.actionsystem.events.ActionEvent;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.test.unittests.MockActions.MockMultiParentAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockAction;

public class ActionTest {
	
	@Rule
	public TestName testName = new TestName();
	private static Application application;
	
    @BeforeClass
    public static void init() {
		application = new HeadlessApplication(new ApplicationAdapter() {
		});
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public void makeRoot(Action<?> action, boolean value) {
    	try {
	    	//set this action as the root action
	    	Method m = ClassReflection.getDeclaredMethod(Action.class, "setRoot", boolean.class);
	    	m.setAccessible(true);
	    	m.invoke(action, value);
	    	action.setRootAction(action);
    	}
    	catch(ReflectionException e) {
    		e.printStackTrace();
    		System.exit(0);
    	}
    }
    
    public boolean hasBeenPooled(Action<?> action) {
    	try {
    		Method m = ClassReflection.getDeclaredMethod(Action.class, "hasBeenPooled");
    		m.setAccessible(true);
    		return (boolean)m.invoke(action);
    	}
    	catch(ReflectionException e) {
    		e.printStackTrace();
    		System.exit(0);
    	}
		
    	throw new RuntimeException("I did something wrong");
    }
    
    public void printTestHeader(String name) {
    	System.out.println();
    	String pattern = "//----------//";
    	System.out.println(pattern + ' ' + name + ' ' + pattern);
    }

	@Test
	public void basicTest() {
		printTestHeader(testName.getMethodName());
		
		MockAction action = MockAction.obtain()
				.setName("Action")
				.setLogLevel(ActionLogger.LogLevel.DEBUG);

		action.setCustomUpdateCode(new Runnable() {
			@Override
			public void run() {
				action.end();
			}
		});
		
		ActionManager manager = new ActionManager();
		
		manager.addAction(action);
		
		assertTrue(action.isRunning());
		assertTrue(action.isRoot());
		assertTrue(action.inUse());
		
		manager.update(0);

		assertFalse(action.isRunning());
	}

	@Test
	public void listenerTest() {
		printTestHeader(testName.getMethodName());

		//----------// Normal start to end cycle //----------//
		MockAction action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.START_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				System.out.println("Hello start event");
			}
		});

		action.subscribeToEvent(ActionEvent.END_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				System.out.println("Hello end event");
			}
		});

		makeRoot(action, true);
		action.start();
		action.end();
		makeRoot(action, false);
		ActionPools.free(action);

		//---------// Kill cycle //----------//
		action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.KILL_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				System.out.println("Hello kill event");
			}
		});

		makeRoot(action, true);
		action.start();
		action.kill();
		makeRoot(action, false);
		ActionPools.free(action);

		//---------// Restart //----------//
		action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.RESTART_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				System.out.println("Hello restart event");
			}
		});

		makeRoot(action, true);
		action.start();
		action.restart();
		action.end();
		makeRoot(action, false);

		ActionPools.free(action);
	}
	
	@Test
	public void basicUnmanagedTest() {
		printTestHeader("Basic Unmanaged Test");
		
		MockAction action = MockAction.obtain()
				.setName("BasicUnmanaged")
				.setLogLevel(ActionLogger.LogLevel.DEBUG)
				.unmanage();
		
		//Normal cycle
		makeRoot(action, true);
		action.start();
		action.update(0);
		action.end();
		makeRoot(action, false);
		
		ActionPools.free(action);
		
		//Unmanaged actions should not be returned to the pool or reset
		assertFalse(hasBeenPooled(action));
		
		//Free an unmanaged action. Resetting it and putting it in a pool.
		action.free();
		
		assertTrue(hasBeenPooled(action));
	}
	
	@Test
	public void attemptToUseManagedPooledActionTest() {
		printTestHeader(testName.getMethodName());

		MockAction action = MockAction.obtain()
				.setName("Action")
				.setLogLevel(ActionLogger.LogLevel.DEBUG);
		
		makeRoot(action, true);
		action.start();
		action.update(0);
		action.end();
		makeRoot(action, false);
		
		ActionPools.free(action);

		boolean exceptionThrown = false;
		
		try {
			action.start();
		}
		catch(Exception e) {
			exceptionThrown = true;
			System.out.println("Attempt to start");
			System.err.println(e.getMessage());
		}
		
		assertTrue(exceptionThrown);
	}
	
	@Test
	public void poolRootActionWhileInUseTest() {
		printTestHeader(testName.getMethodName());
		
		MockAction action = MockAction.obtain()
				.setName("Action")
				.setLogLevel(ActionLogger.LogLevel.DEBUG);

		//Make action root and mock a normal cycle
		makeRoot(action, true);
		action.start();
		action.update(0);
		action.end();
		
		//An exception should be thrown if an attempt is made to pool an action that is in use
		
		boolean exceptionThrown = false;
		
		try {
			ActionPools.free(action);
		}
		catch(Exception e) {
			exceptionThrown = true;
			System.err.println(e.getMessage());
		}
		
		assertTrue(exceptionThrown);
		
		makeRoot(action, false);
		
		ActionPools.free(action);
	}

	@Test
	public void restartTest() {
		printTestHeader(testName.getMethodName());

		ActionListener listener = new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				assertFalse(e.getAction().isRunning());
			}
		};

		//Parent 1
		MockMultiParentAction p1 = MockMultiParentAction.obtain()
				.setName("p1")
				.setLogLevel(ActionLogger.LogLevel.INFO)
				.subscribeToEvent(ActionEvent.RESTART_EVENT, listener);

		//Child 1 of parent 1
		MockAction p1C1 = MockAction.obtain()
				.setName("P1C1")
				.setLogLevel(ActionLogger.LogLevel.INFO)
				.subscribeToEvent(ActionEvent.RESTART_EVENT, listener);
		p1.add(p1C1);

		//Child 2 of parent 1. Parent 2
		MockMultiParentAction p2 = MockMultiParentAction.obtain()
				.setName("P2")
				.setLogLevel(ActionLogger.LogLevel.INFO)
				.subscribeToEvent(ActionEvent.RESTART_EVENT, listener);
		p1.add(p2);

		//Child 3 of parent 1
		MockAction p1C3 = MockAction.obtain()
				.setName("P1C3")
				.setLogLevel(ActionLogger.LogLevel.INFO)
				.subscribeToEvent(ActionEvent.RESTART_EVENT, listener);
		p1.add(p1C3);

		//Child 1 of parent 2
		MockAction p2C1 = MockAction.obtain()
				.setName("p2C1")
				.setLogLevel(ActionLogger.LogLevel.INFO)
				.subscribeToEvent(ActionEvent.RESTART_EVENT, listener);
		p2.add(p2C1);

		//Child 2 of parent 2
		MockAction p2C2 = MockAction.obtain()
				.setName("p2C2")
				.setLogLevel(ActionLogger.LogLevel.INFO)
				.subscribeToEvent(ActionEvent.RESTART_EVENT, listener);
		p2.add(p2C2);

		makeRoot(p1, true);

		p1.start();
		p1C1.start();
		p2.start();
		p1C3.start();
		p2C1.start();
		p2C2.start();
		p1.restart();

		assertTrue(p1.isRunning());
	}

	@Test
	public void cleanupEventTest() {
		ActionListener listener = new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				e.getAction().getLogger().print("Hello cleanup event");
			}
		};

		MockAction action = MockAction.obtain();
		action.setName(testName.getMethodName());
		action.subscribeToEvent(ActionEvent.RESET_EVENT, listener);

		//Mock cycle
		makeRoot(action, true);
		action.setRootAction(action);
		action.start();
		action.end();
		makeRoot(action, false);

		action.reset();
	}

}
