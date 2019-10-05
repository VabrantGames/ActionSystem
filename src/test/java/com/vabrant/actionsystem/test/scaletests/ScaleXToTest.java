package com.vabrant.actionsystem.test.scaletests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ScaleXToTest extends ActionSystemTestScreen {

	public final float end = 10;
	
	public ScaleXToTest(TestSelectScreen screen) {
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
		testObject.setScaleX(1);
	}

	@Override
	public void runTest() {
		ScaleAction action = ScaleAction.scaleXTo(testObject, end, 1f, false, Interpolation.linear);
		action.setName("ScaleXTo");
		actionManager.addAction(action);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getScaleX()));
		log("End: ", Float.toString(end));
	}

}
