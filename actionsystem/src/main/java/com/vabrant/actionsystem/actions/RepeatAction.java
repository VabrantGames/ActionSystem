package com.vabrant.actionsystem.actions;

/**
 * Repeats an {@link Action} a specified number of times or indefinitely. 
 * @author John Barton
 */
public class RepeatAction extends Action<RepeatAction> implements SingleParentAction {

	public static RepeatAction obtain() {
		return obtain(RepeatAction.class);
	}
	
	/**
	 * Repeats an action one time.
	 * @param repeatAction Action to repeat.
	 * @return An action that runs once.
	 */
	public static RepeatAction repeat(Action<?> repeatAction) {
		return obtain()
				.set(repeatAction)
				.setAmount(1);
	}

	/**
	 * Repeats an action a specified number of times.
	 * @param repeatAction Action to repeat.
	 * @param amount Times to repeat.
	 * @return An action that runs a specified number of times.
	 */
	public static RepeatAction repeat(Action<?> repeatAction, int amount) {
		return obtain()
				.set(repeatAction)
				.setAmount(amount);
	}
	
	/**
	 * Repeats an action indefinitely. 
	 * @param repeatAction Action to repeat.
	 * @return An action that runs indefinitely.
	 */
	public static RepeatAction continuous(Action<?> repeatAction) {
		return obtain()
				.set(repeatAction)
				.setContinuous(true);
	}
	
	/** How many times the action has been played */
	private int count = 0;
	
	/** How many times to play the action. Includes the initial non repeat play. */
	private int amount = 0;
	private boolean isContinuous;
	private Action<?> action;

	/**
	 * 
	 * @param continuous
	 * @return This action for chaining.
	 */
	public RepeatAction setContinuous(boolean continuous) {
		count = 0;
		amount = 0;
		isContinuous = continuous;
		return this;
	}
	
	/**
	 * Times to repeat.
	 * @return This action for chaining.
	 */
	public RepeatAction setAmount(int amount) {
		//times to repeat + initial play
		this.amount = amount + 1;
		return this;
	}
	
	public RepeatAction set(Action<?> action) {
		//Can't change actions while the action is running
		if(isRunning()) return this;
		
		if(action == null) throw new IllegalArgumentException("Action is null.");
		
		//Check if we are holding an action. If so pool it.
		if(this.action != null) {
			ActionPools.free(action);
			action = null;
		}
		
		this.action = action;
		return this;
	}
	
	public int getCount() {
		return count <= 0 ? 0 : count - 1;
	}
	
	public boolean isContinuous() {
		return isContinuous;
	}

	@Override
	public void setRootAction(Action<?> root) {
		super.setRootAction(root);
		if(action != null) action.setRootAction(root);
	}
	
	public Action<?> getAction() {
		return action;
	}

	@Override
	public boolean update(float delta) {
		if(isDead() || !isRunning()) return false;
		if(isPaused) return true;
		
		if(!action.update(delta)) {

			//Check if the inner action has been permanently ended or killed
			if(action.isDead()) {
//				permanentEnd();
//				end();
				return false;
			}
			
			if(isContinuous || count < amount) {
				if(!isContinuous) count++;
				if(logger != null && !isContinuous) logger.debug("Repeat", Integer.toString(getCount()));
				action.start();
			}
			else {
				end();
			}
		}
		return isRunning();
	}

	@Override
	protected void startLogic() {
		count = 0;
	}
	
	@Override
	protected void endLogic() {
		if(action != null) action.end();
	}
	
	@Override
	protected void killLogic() {
		if(action != null) action.kill();
	}
	
	@Override
	protected void restartLogic() {
		count = 0;
		if(action != null) action.restart();
	}

	@Override
	public void reset() {
		super.reset();
		action = null;
		amount = 0;
		count = 0;
		isContinuous = false;
	}
	
}
