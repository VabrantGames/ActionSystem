package com.vabrant.actionsystem.actions;

public class RunnableAction extends Action<RunnableAction>{

	private Runnable runnable;
	
	public void set(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public boolean update(float delta) {
		if(isCycleFinished) return true;
		if(isPaused()) return false;
		if(!isRunning()) start();
		runnable.run();
		end();
		return isCycleFinished;
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
