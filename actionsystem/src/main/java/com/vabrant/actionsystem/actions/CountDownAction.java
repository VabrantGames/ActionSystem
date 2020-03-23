package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;

public class CountDownAction extends TimeAction<CountDownAction> {
	
	public static CountDownAction obtain() {
		return obtain(CountDownAction.class);
	}
	
	public static CountDownAction countDown(int duration) {
		return obtain()
				.count(duration);
	}
	
	private int nextCount;
	private final Array<CountDownActionListener> countDownListeners = new Array<>(1);
	
	private CountDownAction count(int duration) {
		setDuration(duration);
		nextCount = duration - 1;
		return this;
	}
	
	public CountDownAction addCountDownListener(CountDownActionListener listener) {
		if(listener == null) return this;
		countDownListeners.add(listener);
		return this;
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
	
	private void fireCount(int count) {
		for(int i = 0; i < countDownListeners.size; i++) {
			countDownListeners.get(i).currentCount(count);
		}
	}

	@Override
	public void reset() {
		super.reset();
		countDownListeners.clear();
		nextCount = 0;
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
	
	public interface CountDownActionListener{
		public void currentCount(int count);
	}

}
