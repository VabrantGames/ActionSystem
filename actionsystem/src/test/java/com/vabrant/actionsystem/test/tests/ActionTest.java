package com.vabrant.actionsystem.test.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
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
import com.vabrant.actionsystem.actions.CleanupListener;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.Condition;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.tests.TestActions.MultiParentTestAction;
import com.vabrant.actionsystem.test.tests.TestActions.TestAction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActionTest {
	
	private static ActionManager manager;

    @BeforeClass
    public static void init() {
    	manager = new ActionManager();
        ActionLogger.useSysOut();
    }

    public void makeRoot(Action<?> action) {
    	try {
	    	//set this action as the root action
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
    
    public void printTestHeader(String name) {
    	System.out.println();
    	String pattern = "//----------//";
    	System.out.println(pattern + ' ' + name + ' ' + pattern);
    }

	@Test
	public void basicTest() {
		printTestHeader("Basic Test");
		
		TestAction action = TestAction.obtain();
		makeRoot(action);
		
		//---------// Run 1 //----------//
		action.start();
		
		assertTrue(action.isRunning());
		
		action.update(0);
		action.end();
		
		assertFalse(action.isRunning());
		
		ActionPools.free(action);
	}

	@Test
	public void listenerTest() {
		printTestHeader("Listener Test");
		
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
		
		TestAction action = TestAction.obtain();
		makeRoot(action);
		
		action.addListener(listener);
		action.setLogLevel(ActionLogger.INFO);
		
		//---------// Run //----------//
		action.start();
		action.update(0);
		action.end();
		
		//---------// Kill //----------//
		action.start();
		action.update(0);
		action.end();
		
		//---------// Restart //----------//
		//This cycle is restarted
		action.start();
		action.update(0);
		action.restart();
		action.end();
		
		ActionPools.free(action);
	}
	
	@Test
	public void basicUnmanagedTest() {
		printTestHeader("Basic Unmanaged Test");
		
		TestAction action = TestAction.obtain()
				.setName("BasicUnmanaged")
				.setLogLevel(ActionLogger.INFO)
				.unmanage();
		makeRoot(action);
		
		action.start();
		
		//An action that is managed by the ActionManager can not be used again is permanently ended or killed.
		//Unmanaged actions however can be started again.
		action.permanentEnd();
		
		action.start();
		
		assertTrue(action.isRunning());
		
		action.end();
		
		action.free();
	}
	
	@Test
	public void resetButNotPoolUnmanagedTest() {
		printTestHeader("Reset But Not Pool UnManaged Test");
		
		TestAction action = TestAction.obtain()
				.setName("ResetButNotPool")
				.setLogLevel(ActionLogger.INFO)
				.unmanage();
		makeRoot(action);
		
		action.start();
		action.end();

		//Since the action is unmanaged it should not be pooled but reset
		//If an unmanaged action is nested inside another action and that action is pooled unmanaged actions
		//are just reset. All values are not reset.
		ActionPools.free(action);
			
		action.free();
	}

	@Test 
	public void restartTest() {
		printTestHeader("Restart Test");
		
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
		
		makeRoot(p1);
		
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
		
		p1.start();
		p1C1.start();
		p2.start();
		p1C3.start();
		p2C1.start();
		p2C2.start();
		
		p1.restart();
	}

}
