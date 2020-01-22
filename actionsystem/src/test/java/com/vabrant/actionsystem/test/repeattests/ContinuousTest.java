package com.vabrant.actionsystem.test.repeattests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class ContinuousTest extends ActionSystemTestScreen {
	
	private final int rotateAmount = 20;

	public ContinuousTest(ActionSystemTestSelector screen) {
		super(screen);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		createTestObject();
		reset();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void runTest() {
		RotateAction action = RotateAction.rotateBy(testObject, rotateAmount, 1f, Interpolation.linear);
		action.restartRotateByFromEnd();
		action.setName("ContinuousAction");
		actionManager.addAction(RepeatAction.continuous(action));
	}
	
	@Override
	public void reset() {
		super.reset();
		testObject.setRotation(0);
	}
	

}
