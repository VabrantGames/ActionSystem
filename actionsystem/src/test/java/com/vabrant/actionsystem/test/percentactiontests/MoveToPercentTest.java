package com.vabrant.actionsystem.test.percentactiontests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class MoveToPercentTest extends ActionSystemTestScreen {
	
	private final float percent = 0.5f;

	public MoveToPercentTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		reset();
	}
	
	@Override
	public void reset() {
		testObject.setX(10);
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

	@Override
	public void runTest() {
		MoveAction action = MoveAction.moveXBy(testObject, 400, 3f, Interpolation.linear)
				.setName("MoveToPercentTest")
				.setup()
				.setPercent(0.5f)
				.setTime(0.2f)
				.moveToPercent();
		actionManager.addAction(GroupAction.sequence(DelayAction.delay(0.5f), action));
	}

}
