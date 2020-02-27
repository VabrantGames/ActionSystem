package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public abstract class PercentAction<T extends Percentable, S extends Action> extends TimeAction<S> {

	protected boolean reverse;
	protected boolean reverseBackToStart;
	protected float percent;
	protected Interpolation interpolation;
	protected T percentable;
	
	/**
	 * Get the percentable object.
	 * @return percentable object
	 */
	public T getPercentable() {
		return percentable;
	}
	
	/**
	 * Sets the percent but does not guarantee that the percent will stay the same. {@link com.badlogic.gdx.math.Interpolation Interpolation}, {@link #reverseBackToStart}, and {@link #reverse} can change the percent. 
	 * The timer will be moved to the correct position. 
	 * @param percent
	 */
	public S setPercent(float percent) {
		this.percent = MathUtils.clamp(percent, 0f, 1f);
		
		//Action is finished. 
		if(percent == 1f) {
			percent = reverseBackToStart ? 0f : 1f;
			timer = duration;
		}
		else {
			timer = percent * duration;
			
			if(interpolation != null) percent = interpolation.apply(percent);
			
			if(reverseBackToStart) {
				percent *= 2f;
				
				if(percent >= 1f) {
					percent = 2f - percent;
				}
			}
		}
		
		percent = reverse ? 1f - percent : percent;
		return (S)this;
	}
	
	/**
	 * {@inheritDoc} <br>
	 * The percent will be moved to the correct position.
	 */
	@Override
	public S setTime(float time) {
		super.setTime(time);
		calculatePercent();
		return (S)this;
	}
	
	private void calculatePercent() {
		boolean finished = timer >= duration;
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
		
		percent = reverse ? 1f - percent : percent;
	}
	
	public S moveToPercent() {
		percent(percent);
		return (S)this;
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
		reverse = false;
		reverseBackToStart = false;
		interpolation = null;
		percent = 0;
		return (S)this;
	}

	@Override
	public void reset() {
		super.reset();
		reverse = false;
		percentable = null;
		reverseBackToStart = false;
		interpolation = null;
		percent = 0;
	}

	@Override
	protected void startCycleLogic() {
		super.startCycleLogic();
		setup();
		percent(percent);
	}

	@Override
	protected void restartCycleLogic() {
		super.restartCycleLogic();
		percent(percent);
	}

	@Override
	public boolean update(float delta) {
		if(!isRunning) start();
		if(!isCycleRunning) return false;
		if(isPaused) return true;
		
		timer += delta;
		
		calculatePercent();
		percent(percent);
		if(timer >= duration) endCycle();
		return isCycleRunning;
	}

	/**
	 * Sets up the action for the current cycle. This method is called every time {@link #startCycleLogic} is called.
	 * If the action doesn't need to be setup per cycle a boolean can be used stop it it from setting up.
	 */
	public abstract S setup();
	
	protected abstract void percent(float percent);
}
