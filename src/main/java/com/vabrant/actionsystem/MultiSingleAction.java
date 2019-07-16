package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class MultiSingleAction extends TimeAction{
	
	private float offset;
	private Array<TimeAction> actions = new Array<>();

	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		timer += delta;
		float percent = 0;
		percent = timer / duration;
		for(int i = 0; i < actions.size; i++) {
			float time = timer - (offset * i);
			if(time < 0) {
				continue;
			}
			TimeAction action = actions.get(i);
			if(action.isFinished) continue;
			if(!action.isRunning()) action.start();
			
			if(time >= duration) {
				percent = 1;
				action.percent(percent);
				action.end();
			}
			else {
				float localPercent = time / duration;
				if(interpolation != null) percent = interpolation.apply(percent);
				float value = percent * localPercent;
				percent += value;
				percent = MathUtils.clamp(percent, 0, 1);
				percent = localPercent + percent;
				if(reverseBackToStart) {
					percent *= 2;
					if(percent >= 1f) {
						percent = 2 - percent;
					}
				}
				action.percent(percent);
			}
		}
		for(int i = 0; i < actions.size; i++) {
			if(!actions.get(i).isFinished) break;
			if(i == (actions.size - 1)) {
				end();
			}
		}
		return isFinished;
	}
	
	public void add(TimeAction action) {
		actions.add(action);
	}
	
	@Override
	protected void start() {
		super.start();
		duration = duration * actions.size;;
	}
	
	public void set(float offset, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		this.offset = offset;
		set(duration, reverseBackToStart, interpolation);
	}
	
	 @Override
	public void reset() {
		super.reset();
		actions.clear();
	}

	@Override
	protected void percent(float percent) {
	}
	

}
