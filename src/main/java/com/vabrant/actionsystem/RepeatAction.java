package com.vabrant.actionsystem;

public class RepeatAction extends Action {

	private int count;
	private int amount;
	private boolean isContinuous;
	private Action action;

	public RepeatAction setContinuous() {
		isContinuous = true;
		return this;
	}
	
	public boolean isContinuous() {
		return isContinuous;
	}
	
	public RepeatAction setRepeatAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public void set(Action action) {
		this.action = action;
	}
	
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		if(action.update(delta)) {
			if(!isContinuous) count++;
			if(isContinuous || count <= amount) {
				action.restart();
			}
			else {
				end();
			}
		}
		return isFinished;
	}
	
	public Action getRepeatAction() {
		return action;
	}
	
	@Override
	public void end() {
		super.end();
		if(action.isRunning()) action.end();
	}
	
	@Override
	public void kill() {
		super.kill();
		if(action.isRunning()) action.kill();
	}
	
	@Override
	public void restart() {
		super.restart();
		count = 0;
		action.restart();
	}
	
	@Override
	public void reset() {
		super.reset();
		action = null;
		amount = 0;
		count = 0;
		isContinuous = false;
	}
	
	public static RepeatAction getAction() {
		return getAction(RepeatAction.class);
	}
	
	public static RepeatAction repeat(Action repeatAction) {
		RepeatAction action = getAction();
		action.set(repeatAction);
		return action;
	}
}
