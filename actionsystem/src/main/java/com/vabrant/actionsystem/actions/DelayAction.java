package com.vabrant.actionsystem.actions;

public class DelayAction extends TimeAction<DelayAction> {
	
	public static DelayAction obtain() {
		return obtain(DelayAction.class);
	}
	
	public static DelayAction delay(float delay) {
		DelayAction action = obtain();
		action.setDuration(delay);
		return action;
	}

}
