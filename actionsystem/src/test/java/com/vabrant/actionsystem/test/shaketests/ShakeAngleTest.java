package com.vabrant.actionsystem.test.shaketests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ShakeAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ShakeAngleTest extends ActionSystemTestScreen {
	
	private final float maxAngle = 10f;

	public ShakeAngleTest(TestSelectScreen screen) {
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
		ShakeAction action = ShakeAction.shakeAngle(testObject, maxAngle, 1f, Interpolation.linear);
		action.setName("ShakeAngle");
		actionManager.addAction(action);
	}

}
