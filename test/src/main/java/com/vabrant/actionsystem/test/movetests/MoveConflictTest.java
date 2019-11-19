package com.vabrant.actionsystem.test.movetests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ConflictChecker;
import com.vabrant.actionsystem.actions.ConflictChecker.ConflictActionType;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.actions.ShakeAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class MoveConflictTest extends ActionSystemTestScreen {

	private ConflictChecker conflictChecker;
	
	public MoveConflictTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		conflictChecker = new ConflictChecker();
		conflictChecker.watch(MoveAction.class, ConflictActionType.END_OLD);
		conflictChecker.watch(RotateAction.class, ConflictActionType.END_NEW);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		reset();
	}
	
	@Override
	public void reset() {
		testObject.setPosition(10, 10);
	}

	@Override
	public void runTest() {
//		MoveAction xAction = MoveAction.moveXTo(testObject, 100, 4f, Interpolation.linear)
//				.setConflictChecker(conflictChecker)
//				.setName("X Action");
//		MoveAction yAction = MoveAction.moveYTo(testObject, 100, 4f, Interpolation.linear)
//				.setConflictChecker(conflictChecker)
//				.setName("Y Action");
//		MoveAction conflictAction = MoveAction.moveTo(testObject, 100, 100, 4f, Interpolation.linear)
//				.setConflictChecker(conflictChecker)
//				.setName("ConflictAction");
//		actionManager.addAction(xAction);
//		actionManager.addAction(yAction);
//		actionManager.addAction(DelayAction.delay(1.5f)
//				.addPostAction(conflictAction));
		
		ShakeAction rotate = ShakeAction.shakeX(testObject, 4, 4f, Interpolation.linear)
				.setConflictChecker(conflictChecker)
				.setName("Old Action");
		ShakeAction conflictAction = ShakeAction.shakeX(testObject, 4, 4f, Interpolation.linear)
				.setConflictChecker(conflictChecker)
				.setName("New Action");
		actionManager.addAction(rotate);
		actionManager.addAction(DelayAction.delay(1.5f)
			.addPostAction(conflictAction));
		
	}

}
