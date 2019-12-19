package com.vabrant.actionsystem.test.actiontests;

import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class NotLastCycleEndTest extends ActionSystemTestScreen {

	private float timer;
	private final String actionName = "NotLastCycleEndTest";
	private ActionWatcher actionWatcher;
	private DelayAction unmanagedAction;
	
	public NotLastCycleEndTest(TestSelectScreen screen) {
		super(screen);
		actionWatcher = new ActionWatcher();
		unmanagedAction = DelayAction.delay(3f)
				.unmanage()
				.setName(actionName);
		actionManager.addAction(unmanagedAction);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		RepeatAction action = (RepeatAction)actionWatcher.getAction(actionName);
		if(action != null) {
			timer += delta;
			
			if(timer > 2f) {
				action.end();
//				action.kill();
			}
		}
	}
	
	@Override
	public void runTest() {
		GroupAction group = GroupAction.sequence(
				DelayAction.delay(2f),
				DelayAction.delay(2f));
		actionManager.addAction(RepeatAction.repeat(unmanagedAction, 2)
				.setName(actionName)
				.watchAction(actionWatcher));
	}

}
