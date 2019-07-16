package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;

public abstract class TimeAction extends Action {

	protected boolean reverseBackToStart;
	public float timer;
	protected float duration;
	protected Interpolation interpolation;

	public float getTimer() {
		return timer;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public float getRemainingTime() {
		return duration - timer < 0 ? 0 : duration - timer;
	}
	
	public void set(float duration, boolean reverseBackToStart, Interpolation interpolation) {
		this.duration = duration;
		this.reverseBackToStart = reverseBackToStart;
		this.interpolation = interpolation;
	}
	
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		timer += delta;
		isFinished = timer >= duration;
		float percent = 0;
		if(isFinished) {
			percent = reverseBackToStart ? 0 : 1;
		}
		else {
			percent = timer / duration;
			if(interpolation != null) percent = interpolation.apply(percent);
			if(reverseBackToStart) {
				percent *= 2;
				if(percent >= 1f) {
					percent = 2 - percent;
				}
			}
		}
		percent(percent);
		if(isFinished) end();
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
		interpolation = null;
		reverseBackToStart = false;
	}
	
	protected abstract void percent(float percent);

}
