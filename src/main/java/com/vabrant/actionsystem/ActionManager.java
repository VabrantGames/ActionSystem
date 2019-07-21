package com.vabrant.actionsystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

public class ActionManager {
	
	private Array<Action> unmanagedActions;
	private Array<Action> actions;
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
		unmanagedActions = new Array<>(2);
	}
	
	public Array<Action> getUnmanagedActions(){
		return unmanagedActions;
	}
	
	public Array<Action> getActions(){
		return actions;
	}
	
	void startUnmanagedAction(Action action) {
		int index = unmanagedActions.indexOf(action, false);
		actions.add(unmanagedActions.removeIndex(index));
	}
	
	public void addUnmanagedAction(Action action) {
		if(action.isManaged()) return;
		action.setActionManager(this);
		unmanagedActions.add(action);
		Gdx.app.log("Action Manager", "Unmanaged action added");
	}
	
	void poolUnmanagedAction(Action unmanagedAction) {
		int index = unmanagedActions.indexOf(unmanagedAction, false);
		Action action = unmanagedActions.removeIndex(index);
		if(action != null) ActionPools.free(action);
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void update(float delta) {
		for(int i = actions.size - 1; i >= 0; i--) {
			Action action = actions.get(i);
			if(action.update(delta)) {
				Action completedAction = actions.removeIndex(i);
				
				if(!completedAction.isManaged()) {
					Gdx.app.log("Action Manager", "Put action back into unmanaged Array");
					unmanagedActions.add(completedAction);
				}
				else {
					Gdx.app.log("Action Manager", "Pool Action");
					ActionPools.free(completedAction);
				}
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
		if(action.isFinished()) return;
		action.kill();
	}
	
	public void pauseAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null || action.isFinished()) continue;
			action.setPause(true);
		}
	}
	
	public void resumeAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action == null || action.isFinished() || !action.isPaused()) continue;
			action.setPause(false);
		}
	}
	
	public void pauseAction(String name, boolean value) {
		Action action = getActionByName(name);
		if(action == null) return;
		if(action.isFinished()) return;
		action.setPause(value);
	}
	
	/**
	 * Pools all actions held. Running actions will be killed.
	 */
	public void dispose() {
		
	}

}
