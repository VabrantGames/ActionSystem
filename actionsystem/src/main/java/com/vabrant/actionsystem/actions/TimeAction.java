package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.MathUtils;

public class TimeAction<T extends Action> extends Action<T> {

	protected float timer;
	protected float duration;

	/**
	 * Get the current time.
	 */
	public float getCurrentTime() {
		return timer;
	}
	
	/**
	 * Set the time in seconds or/and milliseconds. <br>
	 * e.g. 2.5f - 2f - 0.5f
	 * @param time
	 */
	public T setTime(float time) {
		timer = MathUtils.clamp(time, 0, duration);
		return (T)this;
	}
	
	/**
	 * Get the remaining time.
	 * @return remaining time
	 */
	public float getRemainingTime() {
		return duration - timer < 0 ? 0 : duration - timer;
	}
	
	/**
	 * Set the duration. 
	 * @param duration
	 */
	public T setDuration(float duration) {
//		this.duration = MathUtils.clamp(duration, 0, Float.MAX_VALUE);
		this.duration = duration;
		return (T)this;
	}
	
	/**
	 * Get the duration.
	 * @return duration
	 */
	public float getDuration() {
		return duration;
	}

	@Override
	public boolean update(float delta) {
		if(!isCycleRunning) return false;
		if(isPaused) return true;
		if(!isRunning) start();
		if((timer += delta) >= duration) {
			endCycle();
		}
		return isCycleRunning;
	}
	
	@Override
	protected void startCycleLogic() {
//		timer = 0;
		setTime(0);
	}
	
	@Override
	protected void restartCycleLogic() {
//		timer = 0;
		setTime(0);
	}
	
	@Override
	protected void endCycleLogic() {
		timer = duration;
	}
	
	@Override
	public void reset() {
		super.reset();
		timer = 0;
		duration = 0;
	}
	
	@Override
	public T clear() {
		super.clear();
		timer = 0;
		duration = 0;
		return (T)this;
	}

}
