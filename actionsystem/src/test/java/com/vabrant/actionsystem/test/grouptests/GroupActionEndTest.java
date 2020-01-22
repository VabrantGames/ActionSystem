package com.vabrant.actionsystem.test.grouptests;

import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class GroupActionEndTest extends ActionSystemTestScreen{

	private final String actionName = "Group";
	
	public GroupActionEndTest(ActionSystemTestSelector screen) {
		super(screen);
	}

	private ActionListener getEndListener() {
		return new ActionAdapter() {
			@Override
			public void actionEnd(Action a) {
				Action action = ActionWatcher.get(actionName);
				if(action != null) {
					action.end();
				}
			}
		};
	}
	
	@Override
	public void runTest() {
		DelayAction one = DelayAction.delay(0.3f);
		one.setName("One");
		one.addListener(getEndListener());
		DelayAction two = DelayAction.delay(0.3f);
		two.setName("Two");
		DelayAction three = DelayAction.delay(0.3f);
		three.setName("Three");
		
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(one)
				.add(two)
				.add(three)
				.setName(actionName)
				.watchAction();
		actionManager.addAction(group);
	}

}
