package com.vabrant.actionsystem.test.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.test.tests.TestActions.MultiParentTestAction;
import com.vabrant.actionsystem.test.tests.TestActions.TestAction;

public class ActionTest {
	
	@Rule
	public TestName testName = new TestName();
	
    @BeforeClass
    public static void init() {
        ActionLogger.useSysOut();
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
		
		TestAction action = TestAction.obtain()
				.setName("Action")
				.setLogLevel(ActionLogger.DEBUG);
				
		makeRoot(action, true);
		
		assertTrue(action.isRoot());
		assertTrue(action.inUse());
		
		action.start();
		
		assertTrue(action.isRunning());
		
		action.update(0);
		action.end();
		
		assertFalse(action.isRunning());
		
		makeRoot(action, false);
		
		ActionPools.free(action);
		
		assertTrue(hasBeenPooled(action));
	}

	@Test
	public void listenerTest() {
		printTestHeader(testName.getMethodName());
		
		ActionListener<TestAction> listener = new ActionAdapter<TestAction>() {
			@Override
			public void actionStart(TestAction a) {
				a.getLogger().info("Listener Start");
			}
			
			public void actionEnd(TestAction a) {
				a.getLogger().info("Listener End");
			}
			
			@Override
			public void actionKill(TestAction a) {
				a.getLogger().info("Listener Kill");
			}
			
			@Override
			public void actionRestart(TestAction a) {
				a.getLogger().info("Listener Restart");
			}
		};
		
		TestAction action = null;
		
		//---------// End //----------//
		action = TestAction.obtain()
				.addListener(listener)
				.setLogLevel(ActionLogger.INFO);
		
		makeRoot(action, true);
		action.start();
		action.update(0);
		action.end();
		makeRoot(action, false);
		
		ActionPools.free(action);
		
		//---------// Kill //----------//
		action = TestAction.obtain()
				.addListener(listener)
				.setLogLevel(ActionLogger.INFO);
		
		makeRoot(action, true);
		action.start();
		action.update(0);
		action.kill();
		makeRoot(action, false);
		
		ActionPools.free(action);
		
		//---------// Restart //----------//
		action = TestAction.obtain()
				.addListener(listener)
				.setLogLevel(ActionLogger.INFO);
		
		makeRoot(action, true);
		action.start();
		action.update(0);
		action.restart();
		action.end();
		makeRoot(action, false);
		
		ActionPools.free(action);
	}
	
	@Test
	public void basicUnmanagedTest() {
		printTestHeader("Basic Unmanaged Test");
		
		TestAction action = TestAction.obtain()
				.setName("BasicUnmanaged")
				.setLogLevel(ActionLogger.DEBUG)
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

		TestAction action = TestAction.obtain()
				.setName("Action")
				.setLogLevel(ActionLogger.DEBUG);
		
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
		
		TestAction action = TestAction.obtain()
				.setName("Action")
				.setLogLevel(ActionLogger.DEBUG);

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
	public void preActionTest() {
		printTestHeader(testName.getMethodName());
		
		ActionManager manager = new ActionManager();
		
		TestAction mainAction = TestAction.obtain()
				.setName("Main")
				.setLogLevel(ActionLogger.DEBUG);
		
		TestAction preAction = TestAction.obtain()
				.setName("Pre")
				.setLogLevel(ActionLogger.DEBUG);
		
		mainAction.addPreAction(preAction);

		//Set root and call start on the main action 
		//When the main action is started the pre action should be started
		//The pre action is removed from the main action and added to the manager as its own action
		manager.addAction(mainAction);
		
		assertTrue(preAction.isRoot());
		assertTrue(preAction.isRunning());

		//Mock cycle
		manager.update(0);
		mainAction.end();
		preAction.end();
		
		//Cleans up the ended actions
		manager.update(0);
		
		assertTrue(hasBeenPooled(mainAction));
		assertTrue(hasBeenPooled(preAction));
	}
	
	@Test
	public void preActionEndEarlyTest() {
		printTestHeader(testName.getMethodName());
		
		TestAction mainAction = TestAction.obtain()
				.setName("Main")
				.setLogLevel(ActionLogger.DEBUG);
		
		TestAction preAction = TestAction.obtain()
				.setName("Pre")
				.setLogLevel(ActionLogger.DEBUG);
		
		mainAction.addPreAction(preAction);
		
		ActionPools.free(mainAction);
		
		assertTrue(hasBeenPooled(mainAction));
		assertTrue(hasBeenPooled(preAction));
	}
	
	@Test
	public void unmanagedPreActionTest() {
		printTestHeader(testName.getMethodName());
		
		ActionManager manager = new ActionManager();
		
		TestAction mainAction = TestAction.obtain()
				.setName("Main")
				.unmanage()
				.setLogLevel(ActionLogger.DEBUG);
		
		TestAction preAction = TestAction.obtain()
				.setName("Pre")
				.setLogLevel(ActionLogger.DEBUG);
		
		mainAction.addPreAction(preAction);
		
		manager.addAction(mainAction);
		
		assertTrue(mainAction.isRunning());
		assertTrue(preAction.isRunning());
		
		//Mock cycle
		manager.update(0);
		mainAction.end();
		preAction.end();
		
		manager.update(0);
		
		assertTrue(hasBeenPooled(preAction));
	}
	
	@Test
	public void postActionTest() {
		printTestHeader(testName.getMethodName());

		ActionManager manager = new ActionManager();
		
		TestAction mainAction = TestAction.obtain()
				.setName("Main")
				.setLogLevel(ActionLogger.DEBUG);
		
		TestAction postAction = TestAction.obtain()
				.setName("Post")
				.setLogLevel(ActionLogger.DEBUG);
		
		mainAction.addPostAction(postAction);
		
		manager.addAction(mainAction);
		
		//Mock cycle
		//Post action starts when the main action is ended
		manager.update(0);
		mainAction.end();
		
		assertTrue(postAction.isRoot());
		assertTrue(postAction.isRunning());
		
		postAction.end();
		
		//Cleans up the ended actions
		manager.update(0);
		
		assertTrue(hasBeenPooled(mainAction));
		assertTrue(hasBeenPooled(postAction));
	}
	
	@Test
	public void postActionEndEarlyTest() {
		printTestHeader(testName.getMethodName());
		
		TestAction mainAction = TestAction.obtain()
				.setName("Main")
				.setLogLevel(ActionLogger.DEBUG);
		
		TestAction postAction = TestAction.obtain()
				.setName("post")
				.setLogLevel(ActionLogger.DEBUG);
		
		mainAction.addPostAction(postAction);
		ActionPools.free(mainAction);
		
		assertTrue(hasBeenPooled(mainAction));
		assertTrue(hasBeenPooled(postAction));
	}

	@Test 
	public void restartTest() {
		printTestHeader(testName.getMethodName());
		
		ActionListener listener = new ActionAdapter() {
			@Override
			public void actionRestart(Action a) {
				ActionLogger logger = a.getLogger();
				logger.info("Name: " + a.getName());
				logger.info("IsRunning: " + a.isRunning());
				System.out.println();
			}
		};
		
		//Parent 1
		MultiParentTestAction p1 = MultiParentTestAction.obtain()
				.setName("p1")
				.setLogLevel(ActionLogger.INFO)
				.addListener(listener);
		
		makeRoot(p1, true);
		
		//Child 1 of parent 1
		TestAction p1C1 = TestAction.obtain()
				.setName("P1C1")
				.setLogLevel(ActionLogger.INFO)
				.addListener(listener);
		
		p1.add(p1C1);
		
		//Child 2 of parent 1. Parent 2
		MultiParentTestAction p2 = MultiParentTestAction.obtain()
				.setName("P2")
				.setLogLevel(ActionLogger.INFO)
				.addListener(listener);
		
		p1.add(p2);
		
		//Child 3 of parent 1
		TestAction p1C3 = TestAction.obtain()
				.setName("P1C3")
				.setLogLevel(ActionLogger.INFO)
				.addListener(listener);
		
		p1.add(p1C3);
		
		//Child 1 of parent 2
		TestAction p2C1 = TestAction.obtain()
				.setName("p2C1")
				.setLogLevel(ActionLogger.INFO)
				.addListener(listener);
		
		p2.add(p2C1);
		
		//Child 2 of parent 2
		TestAction p2C2 = TestAction.obtain()
				.setName("p2C2")
				.setLogLevel(ActionLogger.INFO)
				.addListener(listener);
		
		p2.add(p2C2);

		makeRoot(p1, true);
		
		p1.start();
		p1C1.start();
		p2.start();
		p1C3.start();
		p2C1.start();
		p2C2.start();
		
		p1.restart();
	}

}
