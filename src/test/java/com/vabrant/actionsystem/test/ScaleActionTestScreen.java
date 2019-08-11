package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.ScaleAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class ScaleActionTestScreen extends ActionSystemBaseTestScreen {

	private final Class<?>[] tests = {
			ScaleXByTest.class,
			ScaleYByTest.class,
			ScaleByTest.class,
			ScaleXToTest.class,
			ScaleYToTest.class,
			ScaleToTest.class,
			SetScaleTest.class
			};
	private ActionSystemTestObject testObject;
	
	public ScaleActionTestScreen(TestSelectScreen screen) {
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
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width / 2, testObject.height / 2, testObject.width, testObject.height, testObject.getScaleX(), testObject.getScaleY(), 0);
	}
	
	public class ScaleXByTest implements Test{
		
		private final float amount = 0.5f;
		
		public ScaleXByTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1,1);
		}
		 
		public void runTest1() {
			actionManager.addAction(ScaleAction.scaleXBy(testObject, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndScaleX", Float.toString(testObject.getScaleX() + amount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentScaleX", Float.toString(testObject.getScaleX()));
		}
		
		@Override
		public void runTest2() {
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(ScaleAction.scaleXBy(testObject, amount, 0.5f, false, Interpolation.linear).restartScaleXByFromEnd())
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, 3));
		}
	}
	
	public class ScaleYByTest implements Test{

		private final float amount = 0.5f;
		
		public ScaleYByTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1, 1);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ScaleAction.scaleYBy(testObject, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void runTest2() {
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(ScaleAction.scaleYBy(testObject, amount, 0.5f, false, Interpolation.linear).restartScaleYByFromEnd())
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, 3));
		}
	}
	
	public class ScaleByTest implements Test{
		
		private final float xAmount = 0.5f;
		private final float yAmount = 0.8f;

		public ScaleByTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1, 1);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ScaleAction.scaleBy(testObject, xAmount, yAmount, 0.5f, false, Interpolation.linear));
		}
	}
	
	public class ScaleXToTest implements Test{
		
		private final float end = 1.5f;
		
		public ScaleXToTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1, 1);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ScaleAction.scaleXTo(testObject, end, 0.5f, false, Interpolation.linear));
		}
	}
	
	public class ScaleYToTest implements Test{
		
		private final float end = 1.5f;
		
		public ScaleYToTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1, 1);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ScaleAction.scaleYTo(testObject, end, 0.5f, false, Interpolation.linear));
		}		
	}
	
	public class ScaleToTest implements Test{
		
		private final float xEnd = 4f;
		private final float yEnd = 2f;
		
		public ScaleToTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1, 1);
		}
		
		 @Override
		public void runTest1() {
			 actionManager.addAction(ScaleAction.scaleTo(testObject, xEnd, yEnd, 0.5f, false, Interpolation.linear));
		}
	}
	
	public class SetScaleTest implements Test{
		
		private final float xEnd = 2f;
		private final float yEnd = 4f;
		
		public SetScaleTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setScale(1, 1);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ScaleAction.setScaleX(testObject, xEnd));
		}
		
		@Override
		public void runTest2() {
			actionManager.addAction(ScaleAction.setScaleY(testObject, yEnd));
		}
		
		@Override
		public void runTest3() {
			actionManager.addAction(ScaleAction.setScale(testObject, xEnd, yEnd));
		}
	}
}
