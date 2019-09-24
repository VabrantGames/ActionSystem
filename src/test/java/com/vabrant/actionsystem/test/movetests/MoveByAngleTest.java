package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemBaseTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class MoveByAngleTest extends ActionSystemBaseTestScreen {

	final int angle = 90;
	final int amount = 50;
	
	public MoveByAngleTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void runTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveByAngle(testObject, angle, amount, 1f, false, Interpolation.linear))
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(group);
	}
	
	@Override
	public void reset() {
		testObject.setX(20);
		testObject.setY(20);
	}

}
