package com.vabrant.actionsystem.test.repeattests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class NestedRepeatTest extends ActionSystemTestScreen {

	public NestedRepeatTest(TestSelectScreen screen) {
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
	public void runTest() {
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(RotateAction.rotateBy(testObject, 30f, 1f, Interpolation.linear))
				.add(DelayAction.delay(0.3f));
		RepeatAction nestedRepeat = RepeatAction.repeat(group, 2);
		nestedRepeat.setName("Nested");
		RepeatAction outerRepeat = RepeatAction.repeat(nestedRepeat, 2);
		outerRepeat.setName("Outer");
		actionManager.addAction(outerRepeat);
	}

}
