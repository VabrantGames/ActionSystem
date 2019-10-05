package com.vabrant.actionsystem.test.actiontests;

import com.vabrant.actionsystem.Action;

public class TestAction extends Action {
	
	public static TestAction getAction() {
		return getAction(TestAction.class);
	}
	
	public static TestAction set(float duration) {
		TestAction action = getAction();
		action.setup(duration);
		return action;
	}

	public float timer;
	public float duration;
	
	public TestAction setup(float duration) {
		this.duration = duration;
		return this;
	}
	
	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		if((timer += delta) >= duration) {
			end();
		}
		return isFinished;
	}
	
	@Override
	public void restart() {
		super.restart();
		timer = 0;
	}

	@Override
	public void reset() {
		super.reset();
		timer = 0;
		duration = 0;
	}
}
