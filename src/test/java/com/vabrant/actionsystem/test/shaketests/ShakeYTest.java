package com.vabrant.actionsystem.test.shaketests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ShakeAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ShakeYTest extends ActionSystemTestScreen {

	private final float amount = 3;
	
	public ShakeYTest(TestSelectScreen screen) {
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
		ShakeAction action = ShakeAction.shakeY(testObject, amount, 1f, Interpolation.linear);
		action.setName("ShakeY");
		actionManager.addAction(action);
	}

}
