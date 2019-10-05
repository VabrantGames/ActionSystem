package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.ShakeAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class ShakeActionTestScreenOld extends ActionSystemTestScreen {
	
	private Class<?>[] tests = {
			ShakeXTest.class,
			ShakeYTest.class,
			ShakeAngleTest.class,
			ShakeTest.class
			};
	private ActionSystemTestObject testObject;
	
	public ShakeActionTestScreenOld(TestSelectScreen screen) {
		super(screen);
		testObject = new ActionSystemTestObject();
		testObject.setColor(Color.BLACK);
		testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - testObject.width / 2);
		testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - testObject.height / 2);
	}
	
	@Override
	public void runTest() {
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX() + testObject.getShakeX(), testObject.getY() + testObject.getShakeY(), testObject.width / 2, testObject.height / 2, testObject.width, testObject.height, 1, 1, testObject.getShakeAngle());
	}
	
	public class ShakeXTest implements Test{
		
		private final float amount = 4f;
		
		@Override
		public void runTest1() {
			actionManager.addAction(ShakeAction.shakeX(testObject, amount, 2, true, Interpolation.linear).usePercent(true));
		}
		
		@Override
		public void runTest2() {
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(ShakeAction.shakeX(testObject, amount, 1, false, Interpolation.linear))
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, 3));
		}
	}
	
	public class ShakeYTest implements Test{
		
		private final float amount = 4f;
		
		@Override
		public void runTest1() {
			actionManager.addAction(ShakeAction.shakeY(testObject, amount, 1f, false, Interpolation.linear));
		}
	}
	
	public class ShakeAngleTest implements Test{

		private final float amount = 4f;
		
		@Override
		public void runTest1() {
			actionManager.addAction(ShakeAction.shakeAngle(testObject, amount, 1f, false, Interpolation.linear));
		}
	}
	
	public class ShakeTest implements Test{

		private final float xAmount = 2f;
		private final float yAmount = 1f;
		private final float angleAmount = 0.75f;
		
		@Override
		public void runTest1() {
			actionManager.addAction(ShakeAction.shake(testObject, xAmount, yAmount, angleAmount, 1f, false, Interpolation.linear));
		}
	}

}
