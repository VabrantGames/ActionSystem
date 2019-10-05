package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ChangeColorHSBTest extends ActionSystemTestScreen {

	private final float[] start = {130f, 0.25f, 0.9f};
	private final float[] end = {0f, 0.25f, 0.9f};
	
	public ChangeColorHSBTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void runTest() {
		actionManager.addAction(ColorAction.changeColor(testObject, end[0], end[1], end[2], 1, 1f, false, Interpolation.linear));
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		ColorAction.HSBToRGB(testObject.getColor(), start[0], start[1], start[2], 1);
	}

}
