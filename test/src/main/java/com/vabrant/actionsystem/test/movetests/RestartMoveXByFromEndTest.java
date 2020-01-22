package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class RestartMoveXByFromEndTest extends ActionSystemTestScreen {
	
	final int repeatAmount = 2;
	final int amount = 50;
	float start;

	public RestartMoveXByFromEndTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
		reset();
	}

	@Override
	public void runTest() {
		start = testObject.getX();
		actionManager.addAction(
				RepeatAction.repeat(
						GroupAction.getAction()
							.sequence()
							.add(
								MoveAction.moveXBy(testObject, amount, 1f, Interpolation.linear)
									.setName("Move")
									.restartMoveXByFromEnd()
								)
							.add(
								DelayAction.delay(0.3f)
									.setName("Delay")
								)
						,repeatAmount)
				);
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
	
	@Override
	public void runCheck1() {
		float end = (start + amount) + (amount * repeatAmount);
		log("Current: ", Float.toString(testObject.getX()));
		log("End: ", Float.toString(end));
	}

}
