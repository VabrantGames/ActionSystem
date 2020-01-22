package com.vabrant.actionsystem.test.rotatetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class RestartRotateByFromEndTest extends ActionSystemTestScreen {
	
	private final int repeatAmount = 3;
	private final float amount = 30;

	public RestartRotateByFromEndTest(ActionSystemTestSelector screen) {
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
		RotateAction action = RotateAction.rotateBy(testObject, amount, 1f, Interpolation.linear);
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
