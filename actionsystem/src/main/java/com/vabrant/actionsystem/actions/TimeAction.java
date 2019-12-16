package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.MathUtils;

public class TimeAction<T extends Action> extends Action<T> {

	public float timer;
	protected float duration;

	public float getCurrentTime() {
		return timer;
	}
	
	public T setTime(float time) {
		timer = MathUtils.clamp(time, 0, duration);
		return (T)this;
	}
	
	public T setDuration(float duration) {
		this.duration = duration;
		if(this.duration < 0) this.duration = 0;
		return (T)this;
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
	public T start() {
		super.start();
//		timer = 0;
		return (T)this;
	}
	
	@Override
	public T restart() {
		super.restart();
		timer = 0;
		return (T)this;
	}
	
	@Override
	public T kill() {
		super.kill();
		if(!isManaged() && getRootAction().isLastCycle()) {
			timer = 0;
		}
		return (T)this;
	}
	
	@Override
	protected T complete() {
		super.complete();
		timer = 0;
		return (T)this;
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
