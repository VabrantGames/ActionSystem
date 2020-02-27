package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;

/**
 * 
 * 
 * @author John Barton
 *
 */
public class ActionManager {
	
	private Array<Action> unmanagedActions;
	private Array<Action> actions;
	private final ActionLogger logger = ActionLogger.getLogger(ActionManager.class);
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
		unmanagedActions = new Array<>(2);
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public Array<Action> getUnmanagedActions(){
		return unmanagedActions;
	}
	
	public Array<Action> getActions(){
		return actions;
	}
	
	void startUnmanagedAction(Action action) {
		int index = unmanagedActions.indexOf(action, false);
		Action a = unmanagedActions.get(index);
		
		if(a.getRootAction() == null) {
			a.setRoot();
		}

		actions.add(unmanagedActions.get(index));
	}
	
	void poolUnmanagedAction(Action unmanagedAction) {
		int index = unmanagedActions.indexOf(unmanagedAction, false);
		Action action = unmanagedActions.removeIndex(index);
		ActionPools.free(action);
	}
	
	/**
	 * Adds an action to the action manager.
	 * @param action
	 */
	public void addAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		
		action.setActionManager(this);
		
		if(action.isManaged()) {
			action.setRoot();
			action.setRootAction(action);
			actions.add(action);
		}
		else {
			unmanagedActions.add(action);
		}
		
		if(logger != null) logger.info("Added" + action.getLogger().getActionName(), action.getLogger().getClassName());
	}
	
	public void update(float delta) {
		for(int i = actions.size - 1; i >= 0; i--) {
			Action action = actions.get(i);
			if(!action.update(delta)) {
				action.end();
				ActionPools.free(actions.removeIndex(i));
			}
		}
	}
	
	public void endAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null) continue;
			action.end();
		}
	}
	
	public void killAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null) continue;
			action.kill();
		}
	}
	
	public void pauseAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null) continue;
			action.pause();
		}
	}
	
	public void resumeAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null) continue;
			action.resume();
		}
	}

	public void freeAll() {
		if(logger != null) logger.debug("Free All");
		for(int i = actions.size - 1; i >= 0; i--) {
			ActionPools.free(actions.removeIndex(i));
		}
		
		for(int i = unmanagedActions.size - 1; i >= 0; i--) {
			unmanagedActions.get(i).poolUnmanagedAction();
		}
	}

}
