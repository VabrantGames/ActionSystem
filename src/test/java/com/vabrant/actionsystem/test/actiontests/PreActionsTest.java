package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class PreActionsTest extends ActionSystemTestScreen {

	public PreActionsTest(TestSelectScreen screen) {
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
		actionManager.addAction(
				RotateAction.rotateBy(testObject, 180f, 1f, Interpolation.linear);
				);
	}

}
