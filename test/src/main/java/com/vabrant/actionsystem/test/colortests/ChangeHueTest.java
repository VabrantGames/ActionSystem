package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ChangeHueTest extends ActionSystemTestScreen {

	private float start;
	private final float startHue = 0;
	private final float endHue = 270f;
	
	public ChangeHueTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
		reset();
	}

	@Override
	public void runTest() {
		start = ColorAction.getSaturation(testObject.getColor());
		actionManager.addAction(
				ColorAction.changeHue(testObject, endHue, 1f, Interpolation.linear)
					.setName("Hue Test")
				);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		ColorAction.HSBToRGB(testObject.getColor(), startHue, 0.5f, 1, 1);
	}
	
	@Override
	public void runCheck1() {
		log("StartSaturation: ", Float.toString(start));
		log("EndSaturation: ", Float.toString(ColorAction.getSaturation(testObject.getColor())));
	}

}
