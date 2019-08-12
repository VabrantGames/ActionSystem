package com.vabrant.actionsystem;

public class DelayAction extends TimeAction {
	
	public void set(float duration) {
		this.duration = duration;
	}

	@Override
	public boolean update(float delta) {
		return super.update(delta);
	}

	public static DelayAction getAction() {
		return getAction(DelayAction.class);
	}
	
	public static DelayAction delay(float delay) {
		DelayAction action = getAction();
		action.set(delay);
		return action;
	}
}
