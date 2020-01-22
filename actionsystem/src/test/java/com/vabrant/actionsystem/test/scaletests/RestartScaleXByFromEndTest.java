package com.vabrant.actionsystem.test.scaletests;

import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class RestartScaleXByFromEndTest extends ActionSystemTestScreen {

	public final int scaleAmount = 1;
	public final int repeatAmount = 3;
	
	public RestartScaleXByFromEndTest(ActionSystemTestSelector screen) {
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
		testObject.setScaleX(1);
	}
	
	private ActionListener getListener() {
		return new ActionListener() {
			@Override
			public void actionStart(Action a) {
				log(a.getName(), "Start");
			}

			@Override
			public void actionEnd(Action a) {
				log(a.getName(), "End");
			}

			@Override
			public void actionKill(Action a) {
				
			}

			@Override
			public void actionRestart(Action a) {
				log(a.getName(), "Restart");
			}
			
			@Override
			public void actionComplete(Action a) {
			}
		};
	}
	
	//TODO Finish group action testing.
	@Override
	public void runTest() {
		ScaleAction scale = ScaleAction.scaleXBy(testObject, scaleAmount, 1f, Interpolation.linear);
		scale.setName("FromEnd");
		scale.addListener(getListener());
		scale.restartScaleXByFromEnd();
		
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(scale)
				.add(DelayAction.delay(0.3f));
		actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
	}
	
}
