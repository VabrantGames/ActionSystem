package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ChangeHueTest extends ActionSystemTestScreen {

	private final float startHue = 0;
	private final float endHue = 270f;
	
	public ChangeHueTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}

	@Override
	public void runTest() {
		actionManager.addAction(ColorAction.changeHue(testObject, endHue, 1f, false, Interpolation.linear));
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		ColorAction.HSBToRGB(testObject.getColor(), startHue, 1, 1, 1);
	}

}
