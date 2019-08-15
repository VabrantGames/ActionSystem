package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Logger;
import com.vabrant.actionsystem.Action;
import com.vabrant.actionsystem.ActionAdapter;
import com.vabrant.actionsystem.ActionPools;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class ActionTestScreen extends ActionSystemBaseTestScreen {
	
	private Class<?>[] tests = {
			UnmanagedTest.class
			};
	ActionSystemTestObject testObject;
	
	public ActionTestScreen(TestSelectScreen screen) {
		super(screen);
		ActionPools.logger.setLevel(Logger.INFO);
		testObject = new ActionSystemTestObject();
		testObject.setColor(Color.BLACK);
		testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - testObject.width / 2);
		testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - testObject.height / 2);
		addTests(tests);
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width / 2, testObject.height / 2, testObject.width, testObject.height, 1, 1, testObject.getRotation());
	}
	
	public class UnmanagedTest implements Test{
		
		RotateAction unmanagedAction;
		
		public UnmanagedTest() {
			unmanagedAction = RotateAction.rotateBy(testObject, 45f, 0.5f, false, Interpolation.circleOut).restartRotateByFromEnd();
			unmanagedAction.unmanage();
			unmanagedAction.addListener(getUnmanagedActionListener());
			actionManager.addUnmanagedAction(unmanagedAction);
		}
		
		private ActionAdapter getUnmanagedActionListener() {
			return new ActionAdapter() {
				@Override
				public void actionEnd(Action a) {
					System.out.println("Delay Over");
				}
			};
		}

		@Override
		public void runTest1() {
			unmanagedAction.restart();
			unmanagedAction.start();
		}
		
		@Override
		public void check1() {
			Gdx.app.log(getClass().getSimpleName(), "hasBeenPooled: " + unmanagedAction.hasBeenPooled());
		}
		
		@Override
		public void runTest2() {
			unmanagedAction.poolUnmanagedAction();
		}
		
	}
	
}
