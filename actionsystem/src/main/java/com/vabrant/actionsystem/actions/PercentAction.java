package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;

public abstract class PercentAction<T extends Percentable, S extends Action> extends TimeAction<S> {

	protected boolean reverse;
	protected boolean reverseBackToStart;
	protected float percent;
	protected Interpolation interpolation;
	protected T percentable;
	
	public T getPercentable() {
		return percentable;
	}
	
	public float getPercent() {
		return percent;
	}
	
	public S set(T percentable, float duration, Interpolation interpolation) {
		setDuration(duration);
		this.percentable = percentable;
		this.interpolation = interpolation;
		return (S)this;
	}
	
	public S reverseBackToStart(boolean reverseBackToStart) {
		this.reverseBackToStart = reverseBackToStart;
		return (S)this;
	}
	
	public S setReverse(boolean reverse) {
		this.reverse = reverse;
		return (S)this;
	}
	
	public S setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
		return (S)this;
	}
	
	@Override
	public S clear() {
		super.clear();
		reverseBackToStart = false;
		interpolation = null;
		percent = 0;
		return (S)this;
	}

	@Override
	public void reset() {
		super.reset();
		percentable = null;
		reverseBackToStart = false;
		interpolation = null;
		percent = 0;
	}

	@Override
	public S end() {
		percent(percent);
		super.end();
		return (S)this;
	}
	
	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		boolean finished = (timer += delta) >= duration;
		percent = 0;
		if(finished) {
			percent = reverseBackToStart ? 0 : 1f;
		}
		else {
			percent = timer / duration;
			if(interpolation != null) percent = interpolation.apply(percent);
			if(reverseBackToStart) {
				percent *= 2f;
				if(percent >= 1f) {
					percent = 2f - percent;
				}
			}
		}
		percent = reverse ? 1 - percent : percent;
		percent(percent);
		if(finished) end();
		return isFinished;
	}
	
	protected abstract void percent(float percent);
}
