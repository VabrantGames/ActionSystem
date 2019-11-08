package com.vabrant.actionsystem.test.rotatetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class RotateToTest extends ActionSystemTestScreen {

	public final float end = 120f;

	public RotateToTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
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
		testObject.setRotation(0);
	}
	
	@Override
	public void runTest() {
		actionManager.addAction(RotateAction.rotateTo(testObject, end, 1f, Interpolation.linear));
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getRotation()));
		log("End: ", Float.toString(end));
	}
}
