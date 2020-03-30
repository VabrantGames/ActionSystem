package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class PercentAction<P extends Percentable, A extends Action<A>> extends TimeAction<A> {

	protected boolean reverse;
	protected boolean reverseBackToStart;
	protected float percent;
	protected Interpolation interpolation;
	protected P percentable;
	
	/**
	 * Get the percentable object.
	 * @return percentable object
	 */
	public P getPercentable() {
		return percentable;
	}
	
	/**
	 * Sets the percent but does not guarantee that the percent will stay the same. {@link com.badlogic.gdx.math.Interpolation Interpolation}, {@link #reverseBackToStart}, and {@link #reverse} can change the percent. 
	 * The timer will be moved to the correct position. 
	 * @param percent
	 */
	public A setPercent(float percent) {
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
		return (A)this;
	}
	
	/**
	 * {@inheritDoc} <br>
	 * The percent will be moved to the correct position.
	 */
	@Override
	public A setTime(float time) {
		super.setTime(time);
		calculatePercent();
		return (A)this;
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
	
	public A moveToPercent() {
		percent(percent);
		return (A)this;
	}
	
	public float getPercent() {
		return percent;
	}
	
	public A set(P percentable, float duration, Interpolation interpolation) {
		setDuration(duration);
		this.percentable = percentable;
		this.interpolation = interpolation;
		return (A)this;
	}
	
	public A reverseBackToStart(boolean reverseBackToStart) {
		this.reverseBackToStart = reverseBackToStart;
		return (A)this;
	}
	
	public A setReverse(boolean reverse) {
		this.reverse = reverse;
		return (A)this;
	}
	
	public A setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
		return (A)this;
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
	protected void startLogic() {
		super.startLogic();
		setup();
		percent(percent);
	}

	@Override
	public boolean update(float delta) {
		if(isDead() || !isRunning()) return false;
		if(isPaused()) return true;
		
		timer += delta;
		
		calculatePercent();
		percent(percent);
		if(timer >= duration) end();
		return isRunning();
	}

	/**
	 * Sets up the action for the current cycle. This method is called every time {@link #startLogic} is called.
	 * If the action doesn't need to be setup per cycle a boolean can be used stop it from setting up.
	 */
	public abstract A setup();
	
	protected abstract void percent(float percent);
}
