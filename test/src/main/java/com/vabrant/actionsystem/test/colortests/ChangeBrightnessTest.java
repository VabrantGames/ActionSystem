package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ChangeBrightnessTest extends ActionSystemTestScreen {

	public ChangeBrightnessTest(ActionSystemTestSelector screen) {
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
		ColorAction.HSBToRGB(testObject.getColor(), 200, 1, 1, 1);
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				ColorAction.changeBrightness(testObject, 0.6f, 1f, Interpolation.linear)
					.setName("ChangeBrightnessTest")
				);
	}

}
