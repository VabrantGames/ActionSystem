package com.vabrant.actionsystem.test.scaletests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ScaleYByTest extends ActionSystemTestScreen {

	public float start;
	public final float amount = 1;
	
	public ScaleYByTest(TestSelectScreen screen) {
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
		start = testObject.getScaleY();
		ScaleAction action = ScaleAction.scaleYBy(testObject, amount, 1f, Interpolation.linear);
		action.setName("ScaleYBy");
		actionManager.addAction(action);
	}
	
	@Override
	public void runCheck1() {
		log("Current: ", Float.toString(testObject.getScaleY()));
		log("End: ", Float.toString(start + amount));
	}

}
