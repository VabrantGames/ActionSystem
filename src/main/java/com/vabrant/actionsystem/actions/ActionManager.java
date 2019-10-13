package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class ActionManager {
	
	private Array<Action> unmanagedActions;
	private Array<Action> actions;
	private final ActionLogger logger;
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
		unmanagedActions = new Array<>(2);
		logger = ActionLogger.getLogger(this.getClass(), ActionLogger.NONE);
		
		//DEBUG remove
		logger.setLevel(Logger.DEBUG);
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
		actions.add(unmanagedActions.get(index));
	}
	
	public void addUnmanagedAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		if(action.isManaged()) throw new IllegalArgumentException("Action is not unmanaged.");
		action.setActionManager(this);
		unmanagedActions.add(action);
		if(logger != null) logger.info("Unmanaged action added");
	}
	
	void poolUnmanagedAction(Action unmanagedAction) {
		int index = unmanagedActions.indexOf(unmanagedAction, false);
		Action action = unmanagedActions.removeIndex(index);
		ActionPools.free(action);
	}
	
	public void addAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		action.setRoot();
		action.setRootAction(action);
		action.setActionManager(this);
		actions.add(action);
		if(logger != null) logger.debug(action.getLogger().getClassName() + " added");
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
	
	public void pauseAction(String name) {
		Action action = getActionByName(name);
		if(action == null) return;
		action.pause();
	}
	
	public void resumeAction(String name) {
		Action action = getActionByName(name);
		if(action == null) return;
		action.resume();
	}
	
	public void freeAll() {
		for(int i = actions.size - 1; i >= 0; i--) {
			ActionPools.free(actions.removeIndex(i));
		}
		
		for(int i = unmanagedActions.size - 1; i >= 0; i--) {
			unmanagedActions.get(i).poolUnmanagedAction();
		}
	}

}
