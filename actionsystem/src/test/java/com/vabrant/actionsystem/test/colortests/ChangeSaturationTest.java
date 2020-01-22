package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class ChangeSaturationTest extends ActionSystemTestScreen {

	private final float end = 0.2f;
	
	public ChangeSaturationTest(ActionSystemTestSelector screen) {
		super(screen);
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
	public void reset() {
		ColorAction.HSBToRGB(testObject.getColor(), 300, 1f, 1, 1);
	}
	
	@Override
	public void runTest() {
		actionManager.addAction(
				ColorAction.changeSaturation(testObject, end, 1f, Interpolation.linear)
					.setName("ChangeSaturationTest")
				);
	}
	
	@Override
	public void runCheck1() {
		log("End: ", Float.toString(end));
		log("Current: ", Float.toString(ColorAction.getSaturation(testObject.getColor())));
	}
	
}
