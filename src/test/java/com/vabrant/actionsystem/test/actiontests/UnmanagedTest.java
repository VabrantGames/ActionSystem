package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class UnmanagedTest extends ActionSystemTestScreen {

	RotateAction unmanagedAction;
	
	public UnmanagedTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		unmanagedAction = RotateAction.rotateBy(testObject, 45f, 0.5f, Interpolation.linear)
				.setName("UnManagedAction")
				.unmanage();
		actionManager.addUnmanagedAction(unmanagedAction);
	}

	@Override
	public void runTest() {
		if(unmanagedAction.isRunning()) {
			unmanagedAction.restart();
		}
		else {
			unmanagedAction.clear();
			unmanagedAction
				.rotateBy(45)
				.setDuration(1f)
				.setInterpolation(Interpolation.circleOut);
			unmanagedAction.start();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

}
