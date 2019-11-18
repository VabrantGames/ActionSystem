package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ActionWatcherTest extends ActionSystemTestScreen {

	private final String actionName = "continuousAction";
	private ActionWatcher actionWatcher;
	
	public ActionWatcherTest(TestSelectScreen testSelectScreen) {
		super(testSelectScreen);
		createTestObject();
		actionWatcher = new ActionWatcher();
		actionWatcher.getLogger().setLevel(ActionLogger.DEBUG);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

	@Override
	public void runTest() {
		RotateAction rotate = RotateAction.rotateBy(testObject, 50, 0.5f, Interpolation.linear).restartRotateByFromEnd();
		actionManager.addAction(RepeatAction.continuous(rotate)
				.setName(actionName)
				.watchAction(actionWatcher));
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		super.touchUp(screenX, screenY, pointer, button);
		
		Action action = actionWatcher.getAction(actionName);
		if(action != null) {
			action.kill();
		}
		
		return false;
	}

}
