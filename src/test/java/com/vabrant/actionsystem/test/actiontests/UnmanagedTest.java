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
		unmanagedAction = RotateAction.rotateBy(testObject, 180f, 1f, Interpolation.linear)
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
