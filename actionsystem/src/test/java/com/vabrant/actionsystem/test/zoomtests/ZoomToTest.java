package com.vabrant.actionsystem.test.zoomtests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ZoomAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class ZoomToTest extends ActionSystemTestScreen{
	
	private final float end = 2;

	public ZoomToTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		testObject.setZoom(1);
	}
	
	@Override
	public void runTest() {
		ZoomAction action = ZoomAction.zoomTo(testObject, end, 1f, Interpolation.linear);
		action.setName("ZoomTo");
		actionManager.addAction(action);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getZoom()));
		log("End: ", Float.toString(end));
	}
	
}
