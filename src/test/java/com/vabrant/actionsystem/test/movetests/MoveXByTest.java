package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemBaseTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class MoveXByTest extends ActionSystemBaseTestScreen{
	
	final int amount = 50;
	float start;
	
	public MoveXByTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void runTest() {
		start = testObject.getX();
		actionManager.addAction(MoveAction.moveXBy(testObject, amount, 1f, false, Interpolation.linear));
	}
	
	@Override
	public void runCheck1() {
		log("EndX", Float.toString(start + amount));
		log("currentX", Float.toString(testObject.getX()));
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setY(screenCenterY - (testObject.height / 2));
		reset();
	}
	
	@Override
	public void reset() {
		testObject.setX(20);
	}

}
