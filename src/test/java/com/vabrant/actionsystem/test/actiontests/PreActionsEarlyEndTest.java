package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class PreActionsEarlyEndTest extends ActionSystemTestScreen {
	
	private final String actionName = "PreActionTest";
	
	public PreActionsEarlyEndTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void reset() {
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	private ActionListener createKillListener() {
		return new ActionAdapter() {
			@Override
			public void actionEnd(Action a) {
				actionManager.killAction(actionName);
			}
		};
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				GroupAction.getAction()
					.sequence()
					.setName(actionName)
					.add(DelayAction.delay(0.5f).addListener(createKillListener()))
					.add(
						RotateAction.rotateBy(testObject, 180f, 1f, Interpolation.linear)
							.addPreAction(ColorAction.changeColor(testObject, Color.RED, 1f, Interpolation.linear))
						)
				);
	}

}
