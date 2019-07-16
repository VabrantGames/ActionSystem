package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;

public class ActionManager {
	
	private Array<Action> actions;
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
	}
	
	public Array<Action> getActions(){
		return actions;
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public void update(float delta) {
		for(int i = actions.size - 1; i >= 0; i--) {
			Action action = actions.get(i);
			if(action.update(delta)) {
				Pools.freeAction(actions.removeIndex(i));
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

}
