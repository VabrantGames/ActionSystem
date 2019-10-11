package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.actionsystem.Action;
import com.vabrant.actionsystem.ActionListener;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class RepeatedSequenceTest extends ActionSystemTestScreen implements ActionListener{

	private final int repeatAmount = 2;
	private final float delay = 0.5f;
	
	public RepeatedSequenceTest(TestSelectScreen screen) {
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
				.sequence()
				.add(one)
				.add(two)
				.add(three);
		actionManager.addAction(RepeatAction.repeat(group, repeatAmount));
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
