package com.vabrant.actionsystem.test.scaletests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ScaleYToTest extends ActionSystemTestScreen {

	public final float end = 0.5f;
	
	public ScaleYToTest(TestSelectScreen screen) {
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
		testObject.setScaleY(1);
	}

	@Override
	public void runTest() {
		ScaleAction action = ScaleAction.scaleYTo(testObject, end, 1f, false, Interpolation.linear);
		action.setName("ScaleYTo");
		actionManager.addAction(action);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getScaleY()));
		log("End: ", Float.toString(end));
	}

}
