package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;

public class CountDownAction extends TimeAction<CountDownAction> {
	
	public static CountDownAction getAction() {
		return getAction(CountDownAction.class);
	}
	
	public static CountDownAction CountDown(int duration) {
		CountDownAction action = getAction();
		action.countDown(duration);
		return action;
	}
	
	private int nextCount;
	private final Array<CountDownActionListener> countDownListeners = new Array<>(1);
	
	private CountDownAction countDown(int duration) {
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
		fireCount(nextCount + 1);
	}
	
	@Override
	public CountDownAction restart() {
		super.restart();
		nextCount = (int)duration - 1;
		fireCount(nextCount + 1);
		return this;
	}
	
	@Override
	public CountDownAction end() {
		super.end();
		fireCount(0);
		return this;
	}
	
	private void fireCount(int count) {
		for(int i = 0; i < countDownListeners.size; i++) {
			countDownListeners.get(i).currentCount(count);
		}
	}
	
	@Override
	public CountDownAction clear() {
		super.clear();
		nextCount = 0;
		return this;
	}

	@Override
	public void reset() {
		super.reset();
		countDownListeners.clear();
		nextCount = 0;
	}
	
	@Override
	public boolean update(float delta) {
		if(isCycleFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		
		timer += delta;
		
		if((duration - timer) < nextCount) {
			if(--nextCount < 0) {
				end();
			}
			else {
				fireCount(nextCount + 1);
			}
		}
			
		return isCycleFinished;
	}
	
	public interface CountDownActionListener{
		public void currentCount(int count);
	}

}
