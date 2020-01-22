package com.vabrant.actionsystem.test.rotatetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class RotateByTest extends ActionSystemTestScreen {

	public float start;
	public final float amount = 50;
	
	public RotateByTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
	}
	
	@Override
	public void reset() {
		super.reset();
		testObject.setRotation(0);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

	@Override
	public void runTest() {
		start = testObject.getRotation();
		actionManager.addAction(RotateAction.rotateBy(testObject, amount, 1f, Interpolation.linear));
	}
	
	@Override
	public void runCheck1() {
		log("Current", Float.toString(testObject.getRotation())); 
		log("End: ", Float.toString(start + amount));
	}

}
