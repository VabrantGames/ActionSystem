package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class ActionManager {
	
	private Array<Action> unmanagedActions;
	private Array<Action> actions;
	private final Logger logger;
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
		unmanagedActions = new Array<>(2);
		logger = new Logger(this.getClass().getSimpleName(), Logger.DEBUG);
	}
	
	public Logger getLogger() {
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
		actions.add(unmanagedActions.get(index));
	}
	
	public void addUnmanagedAction(Action action) {
		if(action.isManaged()) throw new ActionSystemRuntimeException("Action is not unmanaged.");
		action.setActionManager(this);
		unmanagedActions.add(action);
		logger.info("Unmanaged action added");
	}
	
	void poolUnmanagedAction(Action unmanagedAction) {
		int index = unmanagedActions.indexOf(unmanagedAction, false);
		Action action = unmanagedActions.removeIndex(index);
		ActionPools.free(action);
	}
	
	public void addAction(Action action) {
		if(action.preActions.size > 0) action.setActionManager(this);
		actions.add(action);
		logger.debug(action.getClass().getSimpleName() + " added");
	}
	
	public void update(float delta) {
		for(int i = actions.size - 1; i >= 0; i--) {
			Action action = actions.get(i);
			if(action.update(delta)) {
				ActionPools.free(actions.removeIndex(i));
			}
		}
	}

	public Action getActionByName(String name) {
		for(int i = 0; i < actions.size; i++) {
			Action action = actions.get(i);
			if(action.getName() != null && action.getName().equals(name)) return action;
		}
		return null;
	}
	
	public void killAction(String name) {
		Action action = getActionByName(name);
		if(action == null) return;
		action.kill();
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
			action.setPause(true);
		}
	}
	
	public void resumeAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null) continue;
			action.setPause(false);
		}
	}
	
	public void pauseAction(String name, boolean value) {
		Action action = getActionByName(name);
		if(action == null) return;
		action.setPause(value);
	}
	
	/**
	 * Pools all actions held. Running actions will be killed.
	 */
	public void dispose() {
		for(int i = actions.size - 1; i >= 0; i--) {
			ActionPools.free(actions.removeIndex(i));
		}
		
		for(int i = unmanagedActions.size - 1; i >= 0; i--) {
			unmanagedActions.get(i).poolUnmanagedAction();
		}
	}

}
