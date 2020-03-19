package com.vabrant.actionsystem.actions;

public class DelayAction extends TimeAction<DelayAction> {
	
	public static DelayAction getAction() {
		return obtain(DelayAction.class);
	}
	
	public static DelayAction delay(float delay) {
		DelayAction action = getAction();
		action.setDuration(delay);
		return action;
	}

}
