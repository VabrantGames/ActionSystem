package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class MoveActionTestScreen extends ActionSystemTestScreen{
	
	private final Class<?>[] tests = {
			MoveXByTest.class,
			MoveYByTest.class,
			MoveByTest.class,
			MoveByAngleTest.class,
			MoveXToTest.class,
			MoveYToTest.class,
			MoveToTest.class,
			};
	private ActionSystemTestObject testObject;
	
	public MoveActionTestScreen(TestSelectScreen screen) {
		super(screen);
		testObject = new ActionSystemTestObject();
		testObject.setSize(20, 20);
		testObject.setColor(Color.BLACK);
		testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - 25);
		testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - 25);
		addTests(tests);
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width, testObject.height);
	}

	public class MoveXByTest implements Test {
		
		final int repeatAmount = 3;
		final float amount = 50;
		float start;
		
		public MoveXByTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			start = testObject.getX();
			actionManager.addAction(MoveAction.moveXBy(testObject, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndX", Float.toString(start + amount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentX", Float.toString(testObject.getX()));
		}
	
		@Override
		public void runTest2() {
			start = testObject.getX();
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(MoveAction.moveXBy(testObject, amount, 0.5f, false, Interpolation.linear).restartMoveXByFromEnd())
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
		}
		
		@Override
		public void check2() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndX", Float.toString(start + (amount * repeatAmount) + amount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentX", Float.toString(testObject.getX()));
		}
		
		@Override
		public void reset() {
			testObject.setX(20);
			testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - 25);
		}
	}
	
	public class MoveYByTest implements Test{

		final int repeatAmount = 3;
		final float amount = 50;
		float start;
		
		public MoveYByTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			start = testObject.getY();
			actionManager.addAction(MoveAction.moveYBy(testObject, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndY", Float.toString(start + amount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentY", Float.toString(testObject.getY()));
		}
	
		@Override
		public void runTest2() {
			start = testObject.getY();
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(MoveAction.moveYBy(testObject, amount, 0.5f, false, Interpolation.linear).restartMoveYByFromEnd())
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
		}
		
		@Override
		public void check2() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndY", Float.toString(start + amount + (repeatAmount * amount)));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentY", Float.toString(testObject.getY()));
		}
		
		@Override
		public void reset() {
			testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - 25);
			testObject.setY(20);
		}
	}
	
	public class MoveByTest implements Test{
		
		final float xAmount = 50;
		final float yAmount = 50;
		float xStart;
		float yStart;
		
		public MoveByTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			xStart = testObject.getX();
			yStart = testObject.getY();
			actionManager.addAction(MoveAction.moveBy(testObject, xAmount, yAmount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndX", Float.toString(xStart + xAmount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentX", Float.toString(testObject.getX()));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndY", Float.toString(yStart + yAmount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentY", Float.toString(testObject.getY()));
		}
		
		public void reset() {
			testObject.setX(20);
			testObject.setY(20);
		}
	}
	
	public class MoveByAngleTest implements Test{
		
		final float amount = 50;
		final float angle = 70;
		float xStart;
		float yStart;
		
		public MoveByAngleTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			xStart = testObject.getX();
			yStart = testObject.getY();
			actionManager.addAction(MoveAction.moveByAngle(testObject, angle, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			float x = amount * MathUtils.cosDeg(angle);
			float y = amount * MathUtils.sinDeg(angle);
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndX", Float.toString(xStart + x));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentX", Float.toString(testObject.getX()));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndY", Float.toString(yStart + y));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentY", Float.toString(testObject.getY()));
		}
		
		@Override
		public void reset() {
			testObject.setX(20);
			testObject.setY(20);
		}
	}
	
	public class MoveXToTest implements Test{
		
		final float end = 100;
		float start;
		
		public MoveXToTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			start = testObject.getX();
			actionManager.addAction(MoveAction.moveXTo(testObject, end, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndX", Float.toString(end));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentX", Float.toString(testObject.getX()));
		}
		
		@Override
		public void reset() {
			testObject.setX(20);
			testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - 25);
		}
	}
	
	public class MoveYToTest implements Test{
		
		final float end = 100;
		float start;
		
		public MoveYToTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			start = testObject.getY();
			actionManager.addAction(MoveAction.moveYTo(testObject, end, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndY", Float.toString(end));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentY", Float.toString(testObject.getY()));
		}
		
		@Override
		public void reset() {
			testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - 25);
			testObject.setY(20);
		}
	}
	
	public class MoveToTest implements Test{
		
		final float xEnd = 100;
		final float yEnd = 100;
		float xStart;
		float yStart;
		
		public MoveToTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			xStart = testObject.getX();
			yStart = testObject.getY();
			actionManager.addAction(MoveAction.moveTo(testObject, xEnd, yEnd, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndX", Float.toString(xEnd));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentX", Float.toString(testObject.getX()));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndY", Float.toString(yEnd));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentY", Float.toString(testObject.getY()));
		}
		
		@Override
		public void reset() {
			testObject.setX(20);
			testObject.setY(20);
		}
	}
	
}
