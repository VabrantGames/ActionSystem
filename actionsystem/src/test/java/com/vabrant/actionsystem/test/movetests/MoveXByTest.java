package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class MoveXByTest extends ActionSystemTestScreen{
	
	final int amount = 50;
	float start;
	
	public MoveXByTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void runTest() {
		start = testObject.getX();
		actionManager.addAction(MoveAction.moveXBy(testObject, amount, 1f, Interpolation.linear)
				.setName("MoveXBy")
				.setLogLevel(ActionLogger.DEBUG)
		);
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
