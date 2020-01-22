package com.vabrant.actionsystem.test.scaletests;

import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class SetScaleYTest extends ActionSystemTestScreen {
	
	public final float end = 7f;
	
	public SetScaleYTest(ActionSystemTestSelector screen) {
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
	public void runTest() {
		ScaleAction action = ScaleAction.setScaleY(testObject, end);
		action.setName("SetScaleY");
		actionManager.addAction(action);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getScaleY()));
		log("End: ", Float.toString(end));
	}

}
