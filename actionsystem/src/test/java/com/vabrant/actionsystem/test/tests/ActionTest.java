package com.vabrant.actionsystem.test.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ActionTest {

    static TestAction action = null;

    @BeforeClass
    public static void init() throws ReflectionException {
        ActionLogger.useSysOut();

        action = new TestAction();
        action.setLogLevel(ActionLogger.DEBUG);

        //set this action as the root action
        Method m = ClassReflection.getDeclaredMethod(Action.class, "setRoot", null);
        m.setAccessible(true);
    }

    @Before
    public void clearAction() {
        action.clear();
        action.clearListeners();
        action.setLogLevel(ActionLogger.DEBUG);
        System.out.println();
    }

    public void printTestHeader(String name) {
        String pattern = "//----------//";
        System.out.println(pattern + ' ' + name + ' ' + pattern);
    }

	/**
	 * Single cycle test
	 */
	@Test
	public void a_singleCycleTest() {
		printTestHeader("Single Cycle Test");
		
		//---------// Run Cycle 1 //----------//
		action.start();
		action.update(0);
		action.endCycle();
		
		//will be called by the action manager
		action.end();
		
		assertFalse("Action is running", action.isRunning());
	}
	
	/**
	 * Multi cycle test. Mimics what multi cycles would look like. 
	 */
	@Test
	public void b_multiCycleTest() {
		printTestHeader("Multi Cycle Test");
		
		final int amountOfCyclesToRun = 3;
		
		//---------// Run Cycle 1 //----------//
		action.start();
		action.update(0);
		action.endCycle();
		
		//----------// Run Cycle 2 //----------//
		action.startCycle();
		action.update(0);
		action.endCycle();
		
		//----------// Run Cycle 3 //----------//
		action.startCycle();
		action.update(0);
		action.endCycle();
		
		action.end();

		assertFalse("Action is running", action.isRunning());
		assertEquals("Amount of cycles don't match", amountOfCyclesToRun, action.getCycle());
	}
	
	/**
	 * Single cycle test that gets ended while running.
	 */
	@Test 
	public void c_endWhileRunningTest() {
		printTestHeader("End While Running Test");
		
		action.start();
		action.update(0);
		
		//end is called while the cycle is running;
		action.end();
		
		assertFalse("Cycle is running", action.isCycleRunning());
		assertFalse("Action is running", action.isRunning());
	}
	
	/**
	 * Single cycle test that gets killed while running.
	 */
	@Test 
	public void d_killWhileRunningTest() {
		printTestHeader("Kill While Running Test");
		
		action.start();
		action.update(0);
		
		//kill is called while the cycle is running
		action.kill();
		
		assertFalse("Cycle is running", action.isCycleRunning());
		assertFalse("Action is running", action.isRunning());
	}
	
	@Test
	public void e_listenerTest() {
		printTestHeader("Listener Test");
		
		action.addListener(getListener());
		action.setLogLevel(ActionLogger.INFO);
		
		//---------// Run Cycle 1 //----------//
		//This cycle is killed early
		action.start();
		action.update(0);
		action.killCycle();
		
		//---------// Run Cycle 2 //----------//
		//This cycle is ended early
		action.startCycle();
		action.update(0);
		action.endCycle();
		
		//---------// Run Cycle 3 //----------//
		//This cycle is restarted
		action.startCycle();
		action.update(0);
		action.restartCycle();
		action.endCycle();
		
		//---------// Run Cycle 1 //----------//
		//The action is restarted from the very beginning 
		//Cycle count gets reset
		action.restart();
		action.update(0);
		
		//Action is explicitly ended. 
		action.end();
		
		assertEquals("Cycles don't match", 1, action.getCycle());
		assertFalse("Cycle is running", action.isCycleRunning());
		assertFalse("Action is running", action.isRunning());

		//Clear, start, and then kill the action to test the kill listeners.
		action.clear();
		action.start();
		action.kill();
	}
	
	private ActionListener<TestAction> getListener() {
		return new ActionListener<TestAction>() {

			@Override
			public void actionStart(TestAction a) {
				a.getLogger().info("Listener start");
			}

			@Override
			public void actionEnd(TestAction a) {
				a.getLogger().info("Listener end");
			}

			@Override
			public void actionKill(TestAction a) {
				a.getLogger().info("Listener kill");
			}

			@Override
			public void actionRestart(TestAction a) {
				a.getLogger().info("Listener restart");
			}
			
			@Override
			public void actionCycleStart(TestAction a) {
				a.getLogger().info("Listener cycle start : " + a.getCycle());
			}

			@Override
			public void actionCycleEnd(TestAction a) {
				a.getLogger().info("Listener cycle end");
			}

			@Override
			public void actionCycleKill(TestAction a) {
				a.getLogger().info("Listener cycle kill");
			}

			@Override
			public void actionCycleRestart(TestAction a) {
				a.getLogger().info("Listener cycle restart");
			}
		};
	}
	
	private static class TestAction extends Action<TestAction> {

		@Override
		protected void startLogic() {
			getLogger().debug("Run start logic");
		}
		
		@Override
		protected void startCycleLogic() {
			getLogger().debug("Run start cycle logic");
		}
		
		@Override
		protected void restartLogic() {
			getLogger().debug("Run restart logic");
		}
		
		@Override
		protected void restartCycleLogic() {
			getLogger().debug("Run restart cycle logic");
		}
		
		@Override
		protected void endLogic() {
			getLogger().debug("Run end logic");
		}
		
		@Override
		protected void endCycleLogic() {
			getLogger().debug("Run end cycle logic");
		}
		
		@Override
		protected void killLogic() {
			getLogger().debug("Run kill logic");
		}
		
		@Override
		protected void killCycleLogic() {
			getLogger().debug("Run kill cycle logic");
		}
	}

}
