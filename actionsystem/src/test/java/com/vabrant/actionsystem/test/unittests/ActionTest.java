package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Array;
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
import com.vabrant.actionsystem.test.unittests.MockActions.MockSingleParentAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockAction;

import static org.junit.Assert.*;

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
	public void startEventTest() {
		final boolean[] result = {false};
		MockAction action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.START_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				result[0] = true;
			}
		});

		makeRoot(action, true);
		action.start();

		assertArrayEquals(new boolean[]{true}, result);
	}

	@Test
	public void endEventTest() {
		final boolean[] result = {false};
		MockAction action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.END_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				result[0] = true;
			}
		}) ;

		makeRoot(action, true);
		action.start();
		action.end();

		assertArrayEquals(new boolean[]{true}, result);
	}

	@Test
	public void killEventTest() {
		final boolean[] result = {false};
		MockAction action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.KILL_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				result[0] = true;
			}
		});

		makeRoot(action, true);
		action.start();
		action.kill();

		assertArrayEquals(new boolean[]{true}, result);
	}

	@Test
	public void restartEventTest() {
		final boolean[] result = {false};
		MockAction action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.RESTART_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				result[0] = true;
			}
		});

		makeRoot(action, true);
		action.start();
		action.restart();

		assertArrayEquals(new boolean[]{true}, result);
	}

	@Test
	public void resetEventTest() {
		final boolean[] result = {false};
		MockAction action = MockAction.obtain();

		action.subscribeToEvent(ActionEvent.RESET_EVENT, new ActionListener() {
			@Override
			public void onEvent(ActionEvent e) {
				result[0] = true;
			}
		});

		makeRoot(action, true);
		action.start();
		action.end();
		makeRoot(action, false);
		action.reset();

		assertArrayEquals(result, result);
	}
	
	@Test
	public void unmanagedTest() {
		MockAction action = MockAction.obtain()
				.setName("unmanaged")
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
		action.manage();
		
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
		MockAction action = MockAction.obtain()
				.subscribeToEvent(ActionEvent.RESTART_EVENT, new ActionListener() {
					@Override
					public void onEvent(ActionEvent e) {
						assertFalse(e.getAction().isRunning());
					}
				});

		MockSingleParentAction singleParentAction = MockSingleParentAction.obtain()
				.set(MockAction.obtain());
				singleParentAction.subscribeToEvent(ActionEvent.RESTART_EVENT, new ActionListener() {
					@Override
					public void onEvent(ActionEvent e) {
						assertFalse(((MockSingleParentAction)e.getAction()).getAction().isRunning());
					}
				});

		MockMultiParentAction multiParentAction = MockMultiParentAction.obtain()
				.add(MockAction.obtain())
				.add(MockAction.obtain())
				.subscribeToEvent(ActionEvent.RESTART_EVENT, new ActionListener() {
					@Override
					public void onEvent(ActionEvent e) {
						Array<Action<?>> actions = ((MockMultiParentAction) e.getAction()).getActions();

						for (Action a : actions) {
							assertFalse(a.isRunning());
						}
					}
				});

		makeRoot(action, true);
		makeRoot(singleParentAction, true);
		makeRoot(multiParentAction, true);

		action.start();
		singleParentAction.start();
		multiParentAction.start();

		action.restart();
		singleParentAction.restart();
		multiParentAction.restart();

		assertTrue(action.isRunning());
		assertTrue(singleParentAction.isRunning());
		assertTrue(multiParentAction.isRunning());
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
