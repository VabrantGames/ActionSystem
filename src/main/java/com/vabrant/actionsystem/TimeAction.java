package com.vabrant.actionsystem;

public class TimeAction extends Action {

	public float timer;
	protected float duration;

	protected void setDuration(float duration) {
		this.duration = duration;
	}

	public float getCurrentTime() {
		return timer;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public float getRemainingTime() {
		return duration - timer < 0 ? 0 : duration - timer;
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
