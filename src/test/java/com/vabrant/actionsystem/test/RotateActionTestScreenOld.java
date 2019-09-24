package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class RotateActionTestScreenOld extends ActionSystemBaseTestScreen {
	
	private final Class<?>[] tests = {
			RotateToTest.class,
			RotateByTest.class,
			SetRotationTest.class,
			};
	private ActionSystemTestObject testObject;
	
	public RotateActionTestScreenOld(TestSelectScreen screen) {
		super(screen);
		testObject = new ActionSystemTestObject();
		testObject.setColor(Color.BLACK);
		testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - 25);
		testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - 25);
	}
	
	@Override
	public void runTest() {
		
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width / 2, testObject.height / 2, testObject.width, testObject.height, 1, 1, testObject.getRotation());
	}
	
	public class RotateToTest implements Test{

		public final float end = 270f;
		
		public RotateToTest() {
			reset();
		}
		
		@Override
		public void reset() {
			testObject.setRotation(0);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(RotateAction.rotateTo(testObject, end, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndRotation", Float.toString(end));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentRotation", Float.toString(testObject.getRotation()));
		}
	}
	
	public class RotateByTest implements Test{
		
		public final float amount = 60f;
		
		public void runTest1() {
			actionManager.addAction(RotateAction.rotateBy(testObject, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void reset() {
			testObject.setRotation(0);
		}
		
		@Override
		public void runTest2() {
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(RotateAction.rotateBy(testObject, amount, 1f, false, Interpolation.linear).restartRotateByFromEnd())
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, 3));
		}
	}

	public class SetRotationTest implements Test{
		
		public final float end = 225f;
		
		public SetRotationTest() {
			reset();
		}

		@Override
		public void reset() {
			testObject.setRotation(0);
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(RotateAction.setRotation(testObject, end));
		}
		 
	}
}
