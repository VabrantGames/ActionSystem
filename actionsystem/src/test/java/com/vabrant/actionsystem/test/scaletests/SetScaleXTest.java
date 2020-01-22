package com.vabrant.actionsystem.test.scaletests;

import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class SetScaleXTest extends ActionSystemTestScreen {

	public float start;
	public final float end = -5f;
	
	public SetScaleXTest(ActionSystemTestSelector screen) {
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
		super.reset();
	}

	@Override
	public void runTest() {
		start = testObject.getScaleX();
		ScaleAction action = ScaleAction.setScaleX(testObject, end);
		action.setName("SetScaleX");
		actionManager.addAction(action);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getScaleX()));
		log("End: ", Float.toString(end));
	}

}
