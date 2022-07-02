/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.MathUtils;

public class TimeAction<T extends Action<T>> extends Action<T> {

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
		this.duration = MathUtils.clamp(duration, 0, Float.MAX_VALUE);
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
		if(!isRunning()) return false;
		if(isPaused()) return true;
		
		if((timer += delta) >= duration) {
			end();
		}
		
		return isRunning();
	}
	
	@Override
	protected void startLogic() {
		setTime(0);
	}
	
	@Override
	protected void restartLogic() {
		setTime(0);
	}
	
	@Override
	protected void endLogic() {
		timer = duration;
	}
	
	@Override
	public void clear() {
		super.clear();
		timer = 0;
	}
	
	@Override
	public void reset() {
		super.reset();
		duration = 0;
	}

}
