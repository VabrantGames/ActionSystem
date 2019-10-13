package com.vabrant.actionsystem;

public class RepeatAction extends Action<RepeatAction> {

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
	
	public RepeatAction setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public RepeatAction set(Action action) {
		this.action = action;
		return this;
	}
	
	@Override
	void setRootAction(Action root) {
		super.setRootAction(root);
		if(action != null) action.setRootAction(root);
	}
	
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		if(action.update(delta)) {
			if(!isContinuous) {
				count++;
			}
			if(isContinuous || count <= amount) {
				if(logger != null && !isContinuous) logger.debug("Repeat Count", Integer.toString(count));
				lastCycle();
				action.restart();
			}
			else {
				end();
			}
		}
		return isFinished;
	}
	
	@Override
	protected void lastCycle() {
		if(!isContinuous) {
			if(count == amount || amount == 0) {
				super.lastCycle();
			}
		}
	}
	
	public Action getRepeatAction() {
		return action;
	}
	
	@Override
	public RepeatAction end() {
		super.end();
		if(action != null) action.end();
		return this;
	}
	
	@Override
	public RepeatAction kill() {
		super.kill();
		if(action != null) action.kill();
		return this;
	}
	
	@Override
	public RepeatAction restart() {
		super.restart();
		count = 0;
		if(action != null) action.restart();
		return this;
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
	
	@Deprecated
	public static RepeatAction repeat(Action repeatAction) {
		RepeatAction action = getAction();
		action.set(repeatAction);
		return action;
	}
	
	public static RepeatAction repeat(Action repeatAction, int amount) {
		RepeatAction action = getAction();
		action.set(repeatAction);
		action.setAmount(amount);
		return action;
	}
	
	public static RepeatAction continuous(Action repeatAction) {
		RepeatAction action = getAction();
		action.set(repeatAction);
		action.setContinuous();
		return action;
	}
}
