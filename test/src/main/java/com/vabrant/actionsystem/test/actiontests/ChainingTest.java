package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.PauseCondition;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ChainingTest extends ActionSystemTestScreen {

	public ChainingTest(ActionSystemTestSelector screen) {
		super(screen);
		createTestObject();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}
	
	@Override
	public void reset() {
		super.reset();
	}
	
	private ActionListener createActionListener() {
		return new ActionAdapter() {
			@Override
			public void actionStart(Action a) {
				log("This is just the begining", "");
			}
		};
	}
	
	private PauseCondition createPauseCondition() {
		return new PauseCondition() {
			@Override
			public boolean shouldPause() {
				return true;
			}

			@Override
			public boolean shouldResume() {
				return true;
			}
		};
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				ScaleAction.scaleBy(testObject, 2, 2, 1f, Interpolation.linear)
					.setName("ScaleAction")
					.addListener(createActionListener())
					.setPauseCondition(createPauseCondition())
				);
	}

}
