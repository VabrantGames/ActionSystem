package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class PreActionsTest extends ActionSystemTestScreen {

	public PreActionsTest(TestSelectScreen screen) {
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
		testObject.setColor(Color.BLACK);
		testObject.setRotation(0);
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				GroupAction.getAction()
					.sequence()
					.add(
						RotateAction.rotateBy(testObject, 145f, 1f, Interpolation.linear)
							.addPreAction(ColorAction.changeColor(testObject, Color.RED, 1f, Interpolation.linear))
						)
					.addPreAction(DelayAction.delay(0.5f))
		);
	}

}
