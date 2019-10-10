package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;

public class GroupAction extends Action<GroupAction> {
	
	public static GroupAction getAction() {
		return getAction(GroupAction.class);
	}
	
	public static GroupAction parallel(Action a1, Action a2) {
		GroupAction action = getAction()
				.parallel()
				.add(a1)
				.add(a2);
		return action;
	}

	public static GroupAction sequence(Action a1, Action a2) {
		GroupAction action = getAction()
				.sequence()
				.add(a1)
				.add(a2);
		return action;
	}

	private boolean parallel;
	private boolean restartSequenceActions;
	private float timer;
	private float offset;
	private int index;
	private Array<Action> actions;
	
	public GroupAction() {
		actions = new Array<>(4);
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
				return;
			}
			if(restartSequenceActions) actions.get(index).restart();
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
	public GroupAction end() {
		super.end();
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).end();
		}
		return this;
	}
	
	@Override
	public GroupAction kill() {
		super.kill();
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).kill();
		}
		return this;
	}
	
	@Override
	public GroupAction restart() {
		super.restart();
		index = 0;
		timer = 0;
		
		if(parallel) {
			for(int i = 0; i < actions.size; i++) {
				actions.get(i).restart();
			}
		}
		else {
			restartSequenceActions = true;
			if(actions.size > 0) actions.first().restart();
		}
		return this;
	}
	
	@Override
	protected void complete() {
		super.complete();
		for(int i = 0; i < actions.size; i++) {
			actions.get(i).complete();
		}
	}
	
	@Override
	public GroupAction clear() {
		super.clear();
		actions.clear();
		offset = 0;
		index = 0;
		timer = 0;
		restartSequenceActions = false;
		return this;
	}
	
	@Override
	public void reset() {
		super.reset();
		actions.clear();
		offset = 0;
		index = 0;
		timer = 0;
		restartSequenceActions = false;
	}

}
