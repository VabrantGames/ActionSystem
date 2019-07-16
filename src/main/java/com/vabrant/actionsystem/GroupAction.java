package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;

public class GroupAction extends Action {

	private float timer;
	private boolean parallel;
	private float offset;
	private int index;
	private Array<Action> actions;
	
	public GroupAction() {
		actions = new Array<>();
	}
	
	public Array<Action> getActions() {
		return actions;
	}
	
	public GroupAction parallel() {
		parallel(0);
		return this;
	}
	
	public GroupAction parallel(float offset) {
		this.offset = offset;
		parallel = true;
		return this;
	}
	
	public GroupAction sequence() {
		parallel = false;
		return this;
	}
	
	public GroupAction add(Action action) {
		actions.add(action);
		return this;
	}

	@Override
	public boolean update(float delta) {
		if(isFinished) return true;
		if(isPaused) return false;
		if(!isRunning) start();
		timer += delta;
		if(parallel) {
			updateGroup(delta);
		}
		else {
			updateSequence(delta);
		}
		return isFinished;
	}
	
	private void updateSequence(float delta) {
		if(actions.size == 0) {
			end();
			return;
		}
		if(actions.get(index).update(delta)) {
			if(++index == actions.size) {
				end();
			}
		}
	}
	
	private void updateGroup(float delta) {
		boolean finished = true;
		for(int i = 0; i < actions.size; i++) {
			float time = timer - (offset * i);
			if(time < 0) {
				finished = false;
				continue;
			}
			if(!actions.get(i).update(delta)) {
				finished = false;
			}
		}
		if(finished) end();
	}
	
	@Override
	public void end() {
		super.end();
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action.isRunning()) action.end();
		}
	}
	
	@Override
	public void kill() {
		super.kill();
		for(int i = 0, size = actions.size; i < size; i++) {
			Action action = actions.get(i);
			if(action.isRunning()) action.kill();
		}
	}
	
	@Override
	public void restart() {
		super.restart();
		index = 0;
		timer = 0;
		for(int i = 0; i < actions.size; i++) {
			actions.get(i).restart();
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		actions.clear();
		offset = 0;
		index = 0;
		timer = 0;
	}
	
	public static GroupAction getAction() {
		return getAction(GroupAction.class);
	}

}
