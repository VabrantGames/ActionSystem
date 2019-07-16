package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Action implements Poolable{
	
	public boolean hasBeenPooled;
	protected boolean isFinished;
	protected boolean isRunning;
	protected boolean isPaused;
	private String name;
	private Array<ActionListener> listeners;
	private PauseCondition pauseCondition;
	
	public Action() {
		listeners = new Array<>();
	}
	
	public boolean hasBeenPooled() {
		return hasBeenPooled;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setPause(boolean pause) {
		if(pause) {
			if(isPaused || pauseCondition != null && !pauseCondition.shouldPause()) return;
			isPaused = pause;
		}
		else {
			if(!isPaused || pauseCondition != null && !pauseCondition.shouldResume()) return;
			isPaused = false;
		}
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void setPauseCondition(PauseCondition pauseCondition) {
		if(pauseCondition == null) throw new IllegalArgumentException("PauseCondition is null.");
		this.pauseCondition = pauseCondition;
	}
	
	public void addListener(ActionListener listener) {
		listeners.add(listener);
	}
	
	public boolean removeListener(ActionListener listener) {
		return listeners.removeValue(listener, false);
	}
	
	@Override
	public void reset() {
		listeners.clear();
		pauseCondition = null;
		name = null;
		isPaused = false;
		isRunning = false;
		isFinished = false;
	}
	
	protected void start() {
		isRunning = true;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart();
		}
	}
	
	public boolean update(float delta) {
		return false;
	}
	
	public void restart() {
		isRunning = false;
		isFinished = false;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart();
		}
	}
	
	/**
	 * Ends the action as if it completed.
	 */
	public void end() {
		isRunning = false;
		isFinished = true;
		name = null;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionEnd();
		}
	}
	
	/**
	 * Ends the action at it's current position but not as if it were completed.
	 */
	public void kill() {
		isRunning = false;
		isFinished = true;
		name = null;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionKill();
		}
	}
	
	public static <T extends Action> T getAction(Class<T> c) {
		return Pools.obtain(c);
	}
	
}
