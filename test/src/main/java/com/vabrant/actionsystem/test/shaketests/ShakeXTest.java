package com.vabrant.actionsystem.test.shaketests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ShakeAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ShakeXTest extends ActionSystemTestScreen {
	
	private final float amount = 5;

	public ShakeXTest(ActionSystemTestSelector screen) {
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
		ShakeAction action = ShakeAction.shakeX(testObject, amount, 0.5f, Interpolation.linear);
		actionManager.addAction(action);
	}

}
