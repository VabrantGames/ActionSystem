package com.vabrant.actionsystem.test.rotatetests;

import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class SetRotationTest extends ActionSystemTestScreen {

	public final float end = 360;
	
	public SetRotationTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - testObject.width / 2);
		testObject.setY(screenCenterY - testObject.height / 2);
	}

	@Override
	public void runTest() {
		actionManager.addAction(RotateAction.setRotation(testObject, end));			
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getRotation()));
		log("End: ", Float.toString(end));
	}

}
