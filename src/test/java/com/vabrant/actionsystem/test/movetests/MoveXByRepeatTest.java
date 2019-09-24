package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemBaseTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class MoveXByRepeatTest extends ActionSystemBaseTestScreen {
	
	final int repeatAmount = 3;
	final int amount = 50;
	float start;

	public MoveXByRepeatTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}

	@Override
	public void runTest() {
		start = testObject.getX();
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(MoveAction.moveXBy(testObject, amount, 1f, false, Interpolation.linear).restartMoveXByFromEnd())
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
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
