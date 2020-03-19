package com.vabrant.actionsystem.test.tests;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.PercentAction;
import com.vabrant.actionsystem.actions.Percentable;

public class PercentActionTest {

	static final float end = 10;
	static final int duration = 1;
	private TestClass testClass = new TestClass();
	
	@BeforeClass
	public static void init() throws ReflectionException{
		ActionLogger.useSysOut();
	}
	
	public TestAction getTestAction() {
		TestAction action = TestAction.set(testClass, end, duration)
				.setLogLevel(ActionLogger.DEBUG);

		//Make root action (Usually done by the action manager)
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
		
		return action;
	}

	public void printTestHeader(String name) {
		System.out.println();
		String pattern = "//----------//";
		System.out.println(pattern + ' ' + name + ' ' + pattern);
	}

	@Test
	public void singleRunTest() {
		printTestHeader("Single Run Test");

		TestAction action = getTestAction();
		
		action.start();

		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		
		//the after this update the timer will be the same as the duration
		//the action should end
		action.update(0.25f);
		
		ActionPools.free(action);

		assertEquals("Value is incorrect", end, testClass.getValue());
	}

	@Test
	public void multirunTest(){
		printTestHeader("Multi Run Test");
		
		TestAction action = getTestAction();

		//---------// Run 1 //----------//
		action.start();
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);

		//---------// Run 2 //----------//
		action.start();
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		
		assertEquals("Value is incorrect", end, testClass.getValue());
		
		ActionPools.free(action);
	}

	@Test
	public void restartRunTest(){
		printTestHeader("Restart Cycle Test");
		
		TestAction action = getTestAction();

		action.start();

		//action is updated half way
		action.update(0.25f);
		action.update(0.25f);

		//(percent * of) / 100
		final float expected = (50 * end) / 100;
		assertEquals("Value is incorrect", expected, testClass.getValue());

		action.restart();

		assertEquals("Percent is incorrent", 0f, action.getPercent());
		assertEquals("Value is incorrect", 0f, testClass.getValue());

		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		action.update(0.25f);
		
		assertFalse("Action is running", action.isRunning());
		assertEquals("End value is incorrect", end, testClass.getValue());
		
		ActionPools.free(action);
	}
	
	@Test
	public void setPercentTest() {
		printTestHeader("Set Percent Test");
		
		TestAction action = getTestAction();
		
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
		
		ActionPools.free(action);
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

		public static TestAction obtain(){
			return obtain(TestAction.class);
		}

		public static TestAction set(TestPercentable percentable, float end, float duration){
			return obtain()
					.moveTo(end)
					.set(percentable, duration, Interpolation.linear);
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
		public void reset() {
			super.reset();
			start = 0;
			end = 0;
			setup = false;
		}
	}

}
