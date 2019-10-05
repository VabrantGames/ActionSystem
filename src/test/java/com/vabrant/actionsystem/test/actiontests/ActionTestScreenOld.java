package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.Application;
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
import com.vabrant.actionsystem.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestObject;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class ActionTestScreenOld extends ActionSystemTestScreen {
	
	private Class<?>[] tests = {
			UnmanagedTest.class,
			PreActionTest.class,
			};
	ActionSystemTestObject testObject;
	
	public ActionTestScreenOld(TestSelectScreen screen) {
		super(screen);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		ActionPools.logger.setLevel(Logger.DEBUG);
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
		super.debug(renderer);
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width / 2, testObject.height / 2, testObject.width, testObject.height, testObject.getScaleX(), testObject.getScaleY(), testObject.getRotation());
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
			if(unmanagedAction.isRunning()) {
				unmanagedAction.restart();
			}
			else {
				unmanagedAction.start();
			}
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
	
	public class PreActionTest implements Test{

		ActionSystemTestObject preObject;
		
		public PreActionTest() {
			preObject = new ActionSystemTestObject();
			preObject.setColor(Color.RED);
			preObject.setPosition(testObject.getX() - preObject.width - 10, testObject.getY());
		}
		
		@Override
		public void runTest1() {
			ScaleAction scale = ScaleAction.scaleTo(testObject, 0.5f, 0.5f, 0.5f, true, Interpolation.linear);
			scale.setName("Main");
			
			ScaleAction preAction = ScaleAction.scaleTo(preObject, 0.5f, 0.5f, 2, true, Interpolation.linear);
			preAction.setName("PreAction");
			scale.addPreAction(preAction);
			actionManager.addAction(scale);
		}
		
		@Override
		public void check1() {
		}
		
		@Override
		public void debug(ShapeRenderer renderer) {
			if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
			renderer.setColor(preObject.getColor());
			renderer.rect(preObject.getX(), preObject.getY(), preObject.width / 2, preObject.height / 2, preObject.width, preObject.height, preObject.getScaleX(), preObject.getScaleY(), preObject.getRotation());
		}
	}
	
}
