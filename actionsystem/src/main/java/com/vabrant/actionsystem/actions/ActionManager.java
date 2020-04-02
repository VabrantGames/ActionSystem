package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;

/**
 * Manages actions.
 * 
 * @author John Barton
 *
 */
public class ActionManager {
	
	private final Array<Action<?>> actions;
	private final ActionLogger logger = ActionLogger.getLogger(ActionManager.class);
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public Array<Action<?>> getActions(){
		return actions;
	}

	/**
	 * Adds an action to the action manager.
	 * @param action
	 */
	public void addAction(Action<?> action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		action.setActionManager(this);
		action.setRoot();
		action.setRootAction(action);
		actions.add(action);
		action.start();
		if(logger != null) logger.info("Added" + action.getLogger().getActionName(), action.getLogger().getClassName());
	}
	
	public void update(float delta) {
		for(int i = actions.size - 1; i >= 0; i--) {
			if(!actions.get(i).update(delta)) {
				ActionPools.free(actions.removeIndex(i));
			}
		}
	}
	
	public void endAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action<?> action = actions.get(i);
			if(action == null) continue;
			action.end();
		}
	}
	
	public void killAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action<?> action = actions.get(i);
			if(action == null) continue;
			action.kill();
		}
	}
	
	public void pauseAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action<?> action = actions.get(i);
			if(action == null) continue;
			action.pause();
		}
	}
	
	public void resumeAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action<?> action = actions.get(i);
			if(action == null) continue;
			action.resume();
		}
	}

	/**
	 * Pools all action.
	 * @param freeUnmanaged
	 */
	public void freeAll(boolean freeUnmanaged) {
		if(logger != null) logger.debug("Free All");
		for(int i = actions.size - 1; i >= 0; i--) {
			Action<?> action = actions.removeIndex(i);
			
			if(action.isManaged()) {
				ActionPools.free(actions.removeIndex(i));
			}
			else {
				if(freeUnmanaged) action.free();
			}
		}
	}

}
