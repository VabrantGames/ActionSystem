package com.vabrant.actionsystem.test.actiontests;

import com.vabrant.actionsystem.Action;
import com.vabrant.actionsystem.ActionAdapter;
import com.vabrant.actionsystem.ActionListener;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ActionListenerTest extends ActionSystemTestScreen {

	public ActionListenerTest(TestSelectScreen screen) {
		super(screen);
	}
	
	private ActionListener createKillListener() {
		return new ActionAdapter() {
			@Override
			public void actionRestart(Action a) {
				Action root = a.getRootAction();
				if(root != null) root.kill();
			}
		};
	}
	
	private ActionListener createActionListener() {
		return new ActionListener() {

			@Override
			public void actionStart(Action a) {
				log("Start", "");
			}

			@Override
			public void actionEnd(Action a) {
				log("End", "");
			}

			@Override
			public void actionKill(Action a) {
				log("Kill", "");
			}

			@Override
			public void actionRestart(Action a) {
				log("Restart", "");
			}

			@Override
			public void actionComplete(Action a) {
				log("Complete", "");
			}
		};
	}

	@Override
	public void runTest() {
		actionManager.addAction(
				RepeatAction.repeat(
						DelayAction.delay(0.5f)
							.addListener(createActionListener())
//							.addListener(createKillListener())
						, 2)
					.setName("ActionListenerTest")
				);
		
	}

}
