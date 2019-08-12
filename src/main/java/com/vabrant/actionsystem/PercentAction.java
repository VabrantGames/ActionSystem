package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;

public abstract class PercentAction extends TimeAction {

	protected boolean reverseBackToStart;
	protected Interpolation interpolation;
	
	public void set(float duration, boolean reverseBackToStart, Interpolation interpolation) {
		setDuration(duration);
		this.reverseBackToStart = reverseBackToStart;
		this.interpolation = interpolation;
	}
	
	@Override
	public void reset() {
		super.reset();
		reverseBackToStart = false;
		interpolation = null;
	}
	
	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		isFinished = (timer += delta) >= duration;
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
	
	protected abstract void percent(float percent);
}
