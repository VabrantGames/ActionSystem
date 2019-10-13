package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.templates.ActionTemplate;
import com.vabrant.testbase.TestSelectScreen;

public class MoveByAngleTest extends ActionSystemTestScreen {

	final int angle = 60;
	final int amount = 50;
	
	public MoveByAngleTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void runTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveByAngle(testObject, angle, amount, 1f, Interpolation.linear)
						.setName("By Angle")
						)
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(group);
	}
	
	@Override
	public void reset() {
		testObject.setX(20);
		testObject.setY(20);
	}

}
