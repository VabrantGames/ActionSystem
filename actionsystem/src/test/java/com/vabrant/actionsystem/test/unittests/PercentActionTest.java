package com.vabrant.actionsystem.test.unittests;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.test.TestUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.actions.PercentAction;
import com.vabrant.actionsystem.actions.Percentable;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class PercentActionTest {

	@Parameterized.Parameter(0)
	public float end;

	@Parameterized.Parameter(1)
	public int duration;

	@Parameterized.Parameters()
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][]{
				{10,1}, {5, 2}};
		return Arrays.asList(data);
	}

	private PercentTestClass testClass = new PercentTestClass();
	private static Application application;
	
	@BeforeClass
	public static void init() throws ReflectionException{
		application = new HeadlessApplication(new ApplicationAdapter() {
		});
	}

	@After
	public void reset() {
		testClass.reset();
	}
	
	public PercentTestAction getTestAction() {
		PercentTestAction action = PercentTestAction.set(testClass, end, duration)
				.setLogLevel(ActionLogger.LogLevel.DEBUG);

		//Make root action (Usually done by the action manager)
		try {
			//set this action as the root action
			Method m = ClassReflection.getDeclaredMethod(Action.class, "setRoot", boolean.class);
			m.setAccessible(true);
			m.invoke(action, null);
		}
		catch(ReflectionException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return action;
	}

	public void printTestHeader(String name) {
		System.out.println();
		String pattern = "//----------//";
		System.out.println(pattern + ' ' + name + ' ' + pattern);
	}

	@Test
	public void singleRunTest() {
		TestUtils.printTestHeader("Single Run Test");

		System.out.println(end);
		System.out.println(duration);

		ActionManager manager = new ActionManager();
		PercentTestAction action = PercentTestAction.set(testClass, end, duration)
				.setLogLevel(ActionLogger.LogLevel.DEBUG);

		manager.addAction(action);

		manager.update(0.25f);
		manager.update(0.25f);

		assertEquals("Value is incorrect", end * action.getPercent(), testClass.getValue());
		
		manager.update(duration);

		assertEquals("Value is incorrect", end, testClass.getValue());
	}

	@Test
	public void unmanagedMultiRunTest(){
		TestUtils.printTestHeader("Multi Run Test");
		
		ActionManager manager = new ActionManager();
		PercentTestAction action = PercentTestAction.set(testClass, end, duration)
				.unmanage()
				.setLogLevel(ActionLogger.LogLevel.DEBUG);
		
		//Starts action
		manager.addAction(action);
		
		//Mock update to end
		manager.update(duration);

		assertEquals("Value is incorrect", end, testClass.getValue());
		assertEquals("Action is running", false, action.isRunning());
		
		manager.addAction(action);
		
		//Mock update
		manager.update(duration);

		assertEquals("Value is incorrect", end, testClass.getValue());
		assertEquals("Action is running", false, action.isRunning());
	}

	/**
	 * Ensure the percent is correctly changed when the time is changed
	 */
	@Test
	public void setTimeTest() {
		TestUtils.printTestHeader("Set Time Test");
		
		ActionManager manager = new ActionManager();
		PercentTestAction action = PercentTestAction.set(testClass, 10, 1)
				.setLogLevel(ActionLogger.LogLevel.DEBUG);

		manager.addAction(action);
		manager.update(0);
		
		action.setTime(0.5f);
		assertEquals("Percent is incorrect", 0.5f, action.getPercent());
	}

	/**
	 * Ensure the action is correctly reset when restarted
	 */
	@Test
	public void restartRunTest(){
		printTestHeader("Restart Cycle Test");
		
		ActionManager manager = new ActionManager();
		PercentTestAction action = PercentTestAction.set(testClass, end, duration)
				.setLogLevel(ActionLogger.LogLevel.DEBUG);

		manager.addAction(action);
		manager.update(0.5f);
		
		action.restart();

		assertEquals("Percent is incorrect", 0f, action.getPercent());
		assertEquals("Value is incorrect", 0f, testClass.getValue());

		manager.update(duration);
		
		assertEquals("End value is incorrect", end, testClass.getValue());
	}

	/**
	 * Ensure the time is correct when the percent changes
	 */
	@Test
	public void setPercentTest() {
		printTestHeader("Set Percent Test");
		
		ActionManager manager = new ActionManager();
		PercentTestAction action = PercentTestAction.set(testClass, end, duration);
		
		float expected = 0;
		float percent = 0;
		
		manager.addAction(action);
		
		manager.update(0.25f);
		
		//---------// Move to 20 Percent //----------//
		percent = 0.2f;
		action.setPercent(percent);
		expected = percent * duration;
		assertEquals("Time is not correct", expected, action.getCurrentTime());
		
		//---------// Move to 60 Percent //----------//
		percent = 0.6f;
		action.setPercent(percent);
		expected = percent * duration;
		assertEquals("Time is not correct", expected, action.getCurrentTime());
		
		//---------// Move to 0 Percent //----------//
		percent = 0f;
		action.setPercent(percent);
		expected = percent * duration;
		assertEquals("Time is not correct", expected, action.getCurrentTime());
		
		manager.update(duration);
	}

	//----------// Test classes and interfaces //----------//

	private interface TestPercentable extends Percentable {
		float getValue();
		void setValue(float value);
	}

	public static class PercentTestClass implements TestPercentable {

		private float value = 0;

		@Override
		public void setValue(float value) {
			this.value = value;
		}

		@Override
		public float getValue() {
			return value;
		}

		public void reset(){
			value = 0;
		}
	}

	/*
	 * The class does not depict an actual implementation of a class that extends PercentAction. It was created for testing purposes.
	 */
	private static class PercentTestAction extends PercentAction<TestPercentable, PercentTestAction> {

		public static PercentTestAction obtain(){
			return obtain(PercentTestAction.class);
		}

		public static PercentTestAction set(TestPercentable percentable, float end, float duration){
			return obtain()
					.moveTo(end)
					.set(percentable, duration, Interpolation.linear);
		}

		public float start;
		public float end;

		public PercentTestAction moveTo(float end){
			this.end = end;
			return this;
		}
		
		@Override
		public PercentTestAction setup() {
			super.setup();
			start = percentable.getValue();
			return this;
		}

		@Override
		protected void percent(float percent) {
			percentable.setValue(MathUtils.lerp(start, end, percent));
		}

		@Override
		public void reset() {
			super.reset();
			start = 0;
			end = 0;
		}
	}

}
