package com.vabrant.actionsystem.actions;

public class RunnableAction extends Action<RunnableAction>{

	public static RunnableAction runnable(Runnable runnable) {
		RunnableAction action = obtain(RunnableAction.class);
		action.set(runnable);
		return action;
	}
	
	private Runnable runnable;
	
	public void set(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public boolean update(float delta) {
		if(isDead() || !isRunning()) return false;
		if(isPaused()) return true;
		runnable.run();
		end();
		return isRunning();
	}
	
	@Override
	public void reset() {
		super.reset();
		runnable = null;
	}
	
}
