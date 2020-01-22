package com.vabrant.actionsystem.test.repeattests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class RepeatTest extends ActionSystemTestScreen {

	public RepeatTest(ActionSystemTestSelector screen) {
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
		RotateAction action = RotateAction.rotateBy(testObject, 30f, 1f, Interpolation.linear);
		action.setName("RepeatAction");
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(action)
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(RepeatAction.repeat(group, 2));
	}

}
