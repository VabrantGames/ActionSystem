package com.vabrant.actionsystem.test.zoomtests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ZoomAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class RestartZoomByFromEndTest extends ActionSystemTestScreen {
	
	private final float amount = 1;

	public RestartZoomByFromEndTest(TestSelectScreen screen) {
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
		testObject.setZoom(1);
	}

	@Override
	public void runTest() {
		GroupAction group = GroupAction.getAction()
				.add(ZoomAction.zoomBy(testObject, amount, 1f, Interpolation.linear).restartZoomByFromEnd())
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(RepeatAction.repeat(group, 2));
	}

}
