package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;

public abstract class PercentAction<T extends Percentable> extends TimeAction {

	protected boolean reverseBackToStart;
	protected Interpolation interpolation;
	protected T percentable;
	
	public T getPercentable() {
		return percentable;
	}
	
	public void set(T percentable, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		setDuration(duration);
		this.percentable = percentable;
		this.reverseBackToStart = reverseBackToStart;
		this.interpolation = interpolation;
	}

	@Override
	public void reset() {
		super.reset();
		percentable = null;
		reverseBackToStart = false;
		interpolation = null;
	}
	
	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		boolean isFinished = (timer += delta) >= duration;
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
