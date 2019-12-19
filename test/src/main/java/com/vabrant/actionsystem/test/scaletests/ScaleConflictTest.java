package com.vabrant.actionsystem.test.scaletests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ConflictChecker;
import com.vabrant.actionsystem.actions.ConflictChecker.ConflictActionType;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ScaleConflictTest extends ActionSystemTestScreen {

	private ConflictChecker conflictChecker;
	
	public ScaleConflictTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		conflictChecker = new ConflictChecker();
		conflictChecker.watch(ScaleAction.class, ConflictActionType.END_NEW);
	}
	
	@Override
	public void reset() {
		testObject.setScale(1, 1);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

	@Override
	public void runTest() {
		ScaleAction xAction = ScaleAction.scaleXTo(testObject, 4, 4f, Interpolation.linear)
				.setConflictChecker(conflictChecker)
				.setName("X Action");
		ScaleAction yAction = ScaleAction.scaleYTo(testObject, 4, 4f, Interpolation.linear)
				.setConflictChecker(conflictChecker)
				.setName("Y Action");
		ScaleAction conflictAction = ScaleAction.scaleTo(testObject, 4, 4, 4f, Interpolation.linear)
				.setConflictChecker(conflictChecker)
				.setName("ConflictAction");
		actionManager.addAction(xAction);
		actionManager.addAction(yAction);
		actionManager.addAction(DelayAction.delay(1.5f)
				.addPostAction(conflictAction));
	}

}
