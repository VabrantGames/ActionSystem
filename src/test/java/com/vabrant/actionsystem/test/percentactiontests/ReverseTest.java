package com.vabrant.actionsystem.test.percentactiontests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ReverseTest extends ActionSystemTestScreen {

	public ReverseTest(TestSelectScreen screen) {
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
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				MoveAction.moveXBy(testObject, 50f, 1f, Interpolation.linear)
					.setName("ReverseTest")
					.setReverse(true)
				);
	}

}
