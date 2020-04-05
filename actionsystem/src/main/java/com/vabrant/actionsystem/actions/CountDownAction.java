package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;

public class CountDownAction extends TimeAction<CountDownAction> {
	
	public interface CountDownActionListener{
		public void currentCount(int count);
	}
	
	public static CountDownAction obtain() {
		return obtain(CountDownAction.class);
	}
	
	public static CountDownAction countDown(int duration) {
		return obtain()
				.setDuration(duration);
	}
	
	private int nextCount;
	private final Array<CountDownActionListener> countDownListeners = new Array<>(2);

	@Override
	public CountDownAction setDuration(float duration) {
		int newDuration = (int)duration;
		
		//Set the timer to the correct position
		if(newDuration > this.duration) {
			timer = 0;
			super.setDuration(newDuration);
		}
		else {
			timer = (int)this.duration - newDuration;
		}
		
		nextCount = newDuration - 1;
		
		if(isRunning()) fireCount(nextCount + 1);
		return this;
	}
	
	public CountDownAction addCountDownListener(CountDownActionListener listener) {
		if(listener == null) return this;
		countDownListeners.add(listener);
		return this;
	}
	
	public void fireCount(int count) {
		for(int i = countDownListeners.size - 1; i >= 0; i--) {
			countDownListeners.get(i).currentCount(count);
		}
	}
	
	@Override
	protected void startLogic() {
		super.startLogic();
		nextCount = (int)duration - 1;
		fireCount(nextCount + 1);
	}

	@Override
	public void endLogic() {
		super.endLogic();
		fireCount(0);
	}
	
	@Override
	public void clear() {
		super.clear();
		nextCount = 0;
	}

	@Override
	public void reset() {
		super.reset();
		countDownListeners.clear();
	}
	
	@Override
	public boolean update(float delta) {
		if(isDead() || !isRunning()) return false;
		if(isPaused()) return true;
		
		timer += delta;
		
		if((duration - timer) < nextCount) {
			if(--nextCount < 0) {
				end();
			}
			else {
				fireCount(nextCount + 1);
			}
		}
		return isRunning();
	}

}
