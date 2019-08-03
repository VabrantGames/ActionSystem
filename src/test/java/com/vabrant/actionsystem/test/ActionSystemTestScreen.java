package com.vabrant.actionsystem.test;

import com.vabrant.actionsystem.ActionManager;
import com.vabrant.testbase.TestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ActionSystemTestScreen extends TestScreen {
	
	ActionManager actionManager;
	
	public ActionSystemTestScreen(TestSelectScreen screen) {
		super(screen);
		actionManager = new ActionManager(5);
	}
	
	@Override
	public void update(float delta) {
		actionManager.update(delta);
	}

}
