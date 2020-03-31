package com.vabrant.actionsystem.test.tests;

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
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.test.tests.TestActions.MultiParentTestAction;
import com.vabrant.actionsystem.test.tests.TestActions.TestAction;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActionTest {

    @BeforeClass
    public static void init() {
        ActionLogger.useSysOut();
    }

    public void makeRoot(Action<?> action) {
    	try {
	    	//set this action as the root action
	    	Method m = ClassReflection.getDeclaredMethod(Action.class, "setRoot", null);
	    	m.setAccessible(true);
	    	m.invoke(action, null);
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
	public void runTest() {
		printTestHeader("Single Cycle Test");
		
		TestAction action = TestAction.obtain();
		makeRoot(action);
		
		//---------// Run 1 //----------//
		action.start();
		action.update(0);
		action.end();
		
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
