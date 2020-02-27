package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class RepeatedParallelTest extends ActionSystemTestScreen {

	private final int repeatAmount = 2;
	private final float delay = 0.3f;
	
	public RepeatedParallelTest(ActionSystemTestSelector screen) {
		super(screen);
	}
	
	public ActionAdapter getListener() {
		return new ActionAdapter() {
			@Override
			public void actionStart(Action a) {
				log(a.getName(), "Start");
			}
			
			@Override
			public void actionRestart(Action a) {
				log(a.getName(), "Restart");
			}
			
			@Override
			public void actionEnd(Action a) {
				log(a.getName(), "End");
			}
		};
	}

	@Override
	public void runTest() {
		ActionListener listener = getListener();
		DelayAction one = DelayAction.delay(delay);
		one.setName("One");
		one.addListener(listener);
		DelayAction two = DelayAction.delay(delay);
		two.setName("Two");
		two.addListener(listener);
		DelayAction three = DelayAction.delay(delay);
		three.setName("Three");
		three.addListener(listener);
		
		GroupAction group = GroupAction.getAction()
				.parallel()
				.add(one)
				.add(two)
				.add(three);
		actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
	}

}
