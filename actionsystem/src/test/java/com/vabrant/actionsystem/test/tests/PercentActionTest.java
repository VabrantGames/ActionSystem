package com.vabrant.actionsystem.test.tests;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.PercentAction;
import com.vabrant.actionsystem.actions.Percentable;

public class PercentActionTest {

	static final float end = 10;
	static final int duration = 1;
	static TestClass testClass = null;
	static TestAction action = null;
	
	@BeforeClass
	public static void init() throws ReflectionException{
		ActionLogger.useSysOut();

		testClass = new TestClass();
		action = TestAction.getAction();
		action.setLogLevel(ActionLogger.DEBUG);
		
		//set this action as the root action
		Method m = ClassReflection.getDeclaredMethod(Action.class, "setRoot", null);
		m.setAccessible(true);
		m.invoke(action, null);
	}

	@Before
	public void clearAction(){
		testClass.reset();
		action.clear();
		TestAction.set(action, testClass, end, duration);
		action.setLogLevel(ActionLogger.DEBUG);
		System.out.println();
	}

	public void printTestHeader(String name) {
		String pattern = "//----------//";
		System.out.println(pattern + ' ' + name + ' ' + pattern);
	}

	@Test
	public void singleCycleTest() {
		printTestHeader("Single Cycle Test");

		action.start();

		//Somewhat mimic updating. Update values will most likely be a lot smaller. Duration is 1 so update with 0.25f is called 4 times.
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		
		assertFalse("Cycle is running", action.isCycleRunning());

		//End will be called by the Action Manager
		action.end();

		assertEquals("Value is incorrect", end, testClass.getValue());
	}

	@Test
	public void multiCycleTest(){
		printTestHeader("Multi Cycle Test");

		//---------// Run Cycle 1 //----------//
		action.start();
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);

		assertFalse("Cycle is running", action.isCycleRunning());

		//---------// Run Cycle 2 //----------//
		action.startCycle();
		
		assertEquals("Value is incorrect", 0f, testClass.getValue());
		
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);

		assertFalse("Cycle is running", action.isCycleRunning());

		action.end();

		assertEquals("Value is incorrect", end, testClass.getValue());
	}

	@Test
	public void restartCycleTest(){
		printTestHeader("Restart Cycle Test");

		action.start();

		//action is updated half way
		action.update(0.25f);
		action.update(0.25f);

		//(percent * of) / 100
		final float expected = (50 * end) / 100;
		assertEquals("Value is incorrect", expected, testClass.getValue());

		//this cycle should be reset to the beginning
		action.restartCycle();

		assertEquals("Value is incorrect", 0f, testClass.getValue());

		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);

		assertFalse("Cycle is running", action.isCycleRunning());
		
		action.end();
		
		assertFalse("Action is running", action.isRunning());
		assertEquals("End value is incorrect", end, testClass.getValue());
	}
	
	@Test
	public void setPercentTest() {
		printTestHeader("Set Percent Test");
		
		float expected = 0;
		float percent = 0;
		
		action.start();
		
		//Just an update
		action.update(0.25f);
		
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
		
		action.end();
	}

	public static class TestClass implements TestPercentable {
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
	
	private interface TestPercentable extends Percentable {
		float getValue();
		void setValue(float value);
	}

	/*
	 * The class does not depict an actual implementation of a class that extends PercentAction. It was created for testing purposes.
	 */
	private static class TestAction extends PercentAction<TestPercentable, TestAction> {

		public static TestAction getAction(){
			return getAction(TestAction.class);
		}

		public static void set(TestAction action, TestPercentable percentable, float end, float duration){
			action.moveTo(end);
			action.set(percentable, duration, Interpolation.linear);
		}

		private boolean setup;
		public float start;
		public float end;

		public TestAction moveTo(float end){
			this.end = end;
			return this;
		}
		
		@Override
		public TestAction setup() {
			if(setup) return this;
			setup = true;
			start = percentable.getValue();
			return this;
		}

		@Override
		protected void percent(float percent) {
			percentable.setValue(MathUtils.lerp(start, end, percent));
		}

		@Override
		public TestAction clear() {
			super.clear();
			start = 0;
			end = 0;
			setup = false;
			return this;
		}
	}

}
