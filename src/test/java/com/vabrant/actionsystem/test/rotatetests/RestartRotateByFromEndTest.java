package com.vabrant.actionsystem.test.rotatetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class RestartRotateByFromEndTest extends ActionSystemTestScreen {
	
	private final int repeatAmount = 3;
	private final float amount = 30;

	public RestartRotateByFromEndTest(TestSelectScreen screen) {
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
		testObject.setRotation(0);
	}

	@Override
	public void runTest() {
		RotateAction action = RotateAction.rotateBy(testObject, amount, 1f, false, Interpolation.linear);
		action.setName("RestartRotate");
		action.restartRotateByFromEnd();
		actionManager.addAction(
				RepeatAction.repeat(
						GroupAction.getAction()
							.sequence()
							.add(action)
							.add(DelayAction.delay(0.3f))
				,repeatAmount));
	}

}
