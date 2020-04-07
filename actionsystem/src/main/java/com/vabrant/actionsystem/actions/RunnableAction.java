package com.vabrant.actionsystem.actions;

public class RunnableAction extends Action<RunnableAction>{

	public static RunnableAction runnable(Runnable runnable) {
		return obtain(RunnableAction.class)
				.set(runnable);
	}
	
	private Runnable runnable;
	
	public RunnableAction set(Runnable runnable) {
		this.runnable = runnable;
		return this;
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
