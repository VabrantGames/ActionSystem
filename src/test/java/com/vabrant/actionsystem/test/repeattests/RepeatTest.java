package com.vabrant.actionsystem.test.repeattests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class RepeatTest extends ActionSystemTestScreen {

	public RepeatTest(TestSelectScreen screen) {
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
		super.reset();
	}

	@Override
	public void runTest() {
		RotateAction action = RotateAction.rotateBy(testObject, 30f, 1f, false, Interpolation.linear);
		action.setName("RepeatAction");
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(action)
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(RepeatAction.repeat(group, 2));
	}

}
