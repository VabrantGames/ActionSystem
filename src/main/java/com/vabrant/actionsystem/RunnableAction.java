package com.vabrant.actionsystem;

public class RunnableAction extends Action<RunnableAction>{

	private Runnable runnable;
	
	public void set(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused()) return false;
		if(!isRunning()) start();
		runnable.run();
		end();
		return isFinished;
	}
	
	@Override
	public void reset() {
		super.reset();
		runnable = null;
	}
	
	public static RunnableAction runnable(Runnable runnable) {
		RunnableAction action = getAction(RunnableAction.class);
		action.set(runnable);
		return action;
	}
}
