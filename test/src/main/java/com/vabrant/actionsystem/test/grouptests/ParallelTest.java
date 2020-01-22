package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ParallelTest extends ActionSystemTestScreen implements ActionListener{

	private final float delay = 0.5f;
	
	public ParallelTest(ActionSystemTestSelector screen) {
		super(screen);
	}

	@Override
	public void runTest() {
		DelayAction one = DelayAction.delay(delay);
		one.setName("One");
		one.addListener(this);
		DelayAction two = DelayAction.delay(delay);
		two.setName("Two");
		two.addListener(this);
		DelayAction three = DelayAction.delay(delay);
		three.setName("Three");
		three.addListener(this);
		
		GroupAction group = GroupAction.getAction()
				.parallel()
				.add(one)
				.add(two)
				.add(three);
		actionManager.addAction(group);
	}
	
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

}
