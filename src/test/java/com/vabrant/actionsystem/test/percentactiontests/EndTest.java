package com.vabrant.actionsystem.test.percentactiontests;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class EndTest extends ActionSystemTestScreen {
	
	private final String actionName = "End Test";
	private float start;
	private final float duration = 2f;
	private final float endTime = 1f;
	private final float amount = 50;

	public EndTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(20);
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		super.reset();
		testObject.setX(20);
	}

	@Override
	public void runTest() {
		start = testObject.getX();
		actionManager.addAction(
				MoveAction.moveXBy(testObject, amount, duration, Interpolation.linear)
					.setName(actionName)
				);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		MoveAction action = (MoveAction)actionManager.getActionByName(actionName);
		if(action != null) {
			if(action.getCurrentTime() > endTime) {
				action.end();
			}
		}
	}
	
	@Override
	public void runCheck1() {
		float end = MathUtils.lerp(start, start + amount, 1);
		log("Current: ", Float.toString(testObject.getX()));
		log("End: ", Float.toString(end));
	}

}
