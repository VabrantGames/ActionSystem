package com.vabrant.actionsystem.test.percentactiontests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ReverseBackToStartTest extends ActionSystemTestScreen {

	public ReverseBackToStartTest(TestSelectScreen screen) {
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
		
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				ScaleAction.scaleBy(testObject, 2, 2, 1f, Interpolation.linear)
					.reverseBackToStart(true)
					.setName("ReverseBackToStartTest")
				);
	}

}
