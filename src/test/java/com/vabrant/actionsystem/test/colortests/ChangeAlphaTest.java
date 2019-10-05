package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ChangeAlphaTest extends ActionSystemTestScreen {

	private final int startAlpha = 1;
	private final int endAlpha = 0;
	
	public ChangeAlphaTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		testObject.setColor(Color.RED);
		reset();
	}

	@Override
	public void runTest() {
		actionManager.addAction(ColorAction.changeAlpha(testObject, endAlpha, 1f, false, Interpolation.linear));
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		testObject.getColor().a = startAlpha;
	}

}
