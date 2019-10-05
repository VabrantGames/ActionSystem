package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class RestartMoveYByFromEndTest extends ActionSystemTestScreen {
	
	final int repeatAmount = 3;
	final int amount = 30;
	float start;
	
	public RestartMoveYByFromEndTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}

	@Override
	public void runTest() {
		start = testObject.getY();
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(MoveAction.moveYBy(testObject, amount, 1f, false, Interpolation.linear).restartMoveYByFromEnd())
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
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
		float end = (start + amount) + (amount * repeatAmount);
		log("Current: ", Float.toString(testObject.getY()));
		log("End: ", Float.toString(end));
	}

}
