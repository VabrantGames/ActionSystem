package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class MoveYByTest extends ActionSystemTestScreen {
	
	float start;
	final int amount = 50;

	public MoveYByTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}

	@Override
	public void runTest() {
		start = testObject.getY();
		actionManager.addAction(MoveAction.moveYBy(testObject, amount, 1f, false, Interpolation.linear));
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		reset();
	}
	
	@Override
	public void reset() {
		testObject.setY(20);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getY()));
		log("End: ", Float.toString(start + amount));
	}

}
