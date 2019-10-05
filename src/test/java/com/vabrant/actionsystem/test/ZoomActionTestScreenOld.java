package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.ZoomAction;
import com.vabrant.testbase.Test;
import com.vabrant.testbase.TestBaseConstantsAndUtils;
import com.vabrant.testbase.TestSelectScreen;

public class ZoomActionTestScreenOld extends ActionSystemTestScreen {
	
	private Class<?>[] tests = {
			ZoomByTest.class,
			ZoomToTest.class,
			SetZoomTest.class,
			};
	private ActionSystemTestObject testObject;
	
	public ZoomActionTestScreenOld(TestSelectScreen screen) {
		super(screen);
		testObject = new ActionSystemTestObject();
		testObject.setColor(Color.BLACK);
		testObject.setX((TestBaseConstantsAndUtils.WORLD_WIDTH / 2) - testObject.width / 2);
		testObject.setY((TestBaseConstantsAndUtils.WORLD_HEIGHT / 2) - testObject.height / 2);
//		addTests(tests);
	}
	
	@Override
	public void runTest() {
		
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(testObject.getColor());
		renderer.rect(testObject.getX(), testObject.getY(), testObject.width / 2, testObject.height / 2, testObject.width, testObject.height, testObject.getZoom(), testObject.getZoom(), 0);
	}

	public class ZoomByTest implements Test{
		
		final int repeatAmount = 3;
		final float amount = 0.2f;
		float start;
		
		public ZoomByTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			start = testObject.getZoom();
			actionManager.addAction(ZoomAction.zoomBy(testObject, amount, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndZoom", Float.toString(start + amount));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentZoom", Float.toString(testObject.getZoom()));
		}
		
		@Override
		public void runTest2() {
			start = testObject.getZoom();
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(ZoomAction.zoomBy(testObject, amount, 0.5f, false, Interpolation.linear).restartZoomByFromEnd())
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
		}
		
		@Override
		public void check2() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndZoom", Float.toString(start + amount + (amount * repeatAmount)));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentZoom", Float.toString(testObject.getZoom()));
		}
		
		@Override
		public void reset() {
			testObject.setZoom(1);
		}
	}
	
	public class ZoomToTest implements Test{
		
		final float end = 1.5f;
		float start;
		
		public ZoomToTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			start = testObject.getZoom();
			actionManager.addAction(ZoomAction.zoomTo(testObject, end, 0.5f, false, Interpolation.linear));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndZoom", Float.toString(end));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentZoom", Float.toString(testObject.getZoom()));
		}
		
		@Override
		public void runTest2() {
			GroupAction group = GroupAction.getAction()
					.sequence()
					.add(ZoomAction.zoomTo(testObject, end, 0.5f, false, Interpolation.linear))
					.add(DelayAction.delay(0.5f));
			actionManager.addAction(RepeatAction.repeat(group, 3));
		}
		
		@Override
		public void reset() {
			testObject.setZoom(1);
		}
	}
	
	public class SetZoomTest implements Test{
		
		final float end = 2f;
		
		public SetZoomTest() {
			reset();
		}
		
		@Override
		public void runTest1() {
			actionManager.addAction(ZoomAction.setZoom(testObject, end));
		}
		
		@Override
		public void check1() {
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "EndZoom", Float.toString(end));
			TestBaseConstantsAndUtils.log(this.getClass().getSimpleName(), "CurrentZoom", Float.toString(testObject.getZoom()));
		}
		
		@Override
		public void reset() {
			testObject.setZoom(1);
		}
	}

}
