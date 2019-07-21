package com.vabrant.actionsystem;

public class DelayAction extends Action {

	private float timer;
	private float duration;
	
	public DelayAction bob() {
		return this;
	}
	
	public void set(float duration) {
		this.duration = duration;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public float getCurrentTime() {
		return timer;
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
	
	public static DelayAction getAction() {
		return getAction(DelayAction.class);
	}
	
	public static DelayAction delay(float delay) {
		DelayAction action = getAction();
		action.set(delay);
		return action;
	}
}
