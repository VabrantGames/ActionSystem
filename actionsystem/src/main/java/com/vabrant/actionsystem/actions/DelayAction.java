package com.vabrant.actionsystem.actions;

public class DelayAction extends TimeAction<DelayAction> {
	
	public static DelayAction getAction() {
		return getAction(DelayAction.class);
	}
	
	public static DelayAction delay(float delay) {
		DelayAction action = getAction();
		action.set(delay);
		return action;
	}
	
	public void set(float duration) {
		this.duration = duration;
	}

	@Override
	public boolean update(float delta) {
		return super.update(delta);
	}

}
