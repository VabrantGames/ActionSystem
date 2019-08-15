package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Action implements Poolable{
	
	boolean isManaged = true;
	//TODO still needed?
	private boolean hasBeenPooled;
	protected boolean isFinished;
	protected boolean isRunning;
	protected boolean isPaused;
	private String name;
	private Array<ActionListener> listeners;
	private PauseCondition pauseCondition;
	private ActionManager actionManager;
	
	public Action() {
		listeners = new Array<>();
	}
	
	void setPooled(boolean pooled) {
		hasBeenPooled = pooled;
	}
	
	public boolean hasBeenPooled() {
		return hasBeenPooled;
	}
	
	/**
	 * Makes this Action managed by the user. Useful when an action needs to be permanent. Will not be pooled when finished.
	 */
	public void unmanage() {
		isManaged = false;
	}
	
	public boolean isManaged() {
		return isManaged;
	}
	
	void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public boolean isFinished() {
		return isFinished;
	}
	
	public void setName(String name) {
		if(name == null) throw new IllegalArgumentException("Name can't be null.");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setPause(boolean pause) {
		if(isFinished) return;
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
		if(listener == null) throw new IllegalArgumentException("Listener is null.");
		listeners.add(listener);
	}
	
	public boolean removeListener(ActionListener listener) {
		return listeners.removeValue(listener, false);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	public boolean update(float delta) {
		return false;
	}
	
	public void poolUnmanagedAction() {
		if(!isManaged && actionManager != null) {
			isManaged = true;
			actionManager.poolUnmanagedAction(this);
			actionManager = null;
		}
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
	
	public void start() {
		if(!isManaged()) {
			if(actionManager == null) throw new GdxRuntimeException("Unmanaged Actions need to be added to an Action Manager.");
			actionManager.startUnmanagedAction(this);
		}
		
		isRunning = true;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart(this);
		}
	}
	
	public void restart() {
		isRunning = false;
		isFinished = false;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart(this);
		}
	}
	
	/**
	 * Ends the action as if it's completed.
	 */
	public void end() {
		if(isFinished) return;
		
		isRunning = false;
		isFinished = true;
		name = null;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionEnd(this);
		}
	}
	
	/**
	 * Ends the action at it's current position but not as if it were completed.
	 */
	public void kill() {
		if(isFinished) return;
		
		isRunning = false;
		isFinished = true;
		name = null;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionKill(this);
		}
	}
	
	public static <T extends Action> T getAction(Class<T> c) {
		return ActionPools.obtain(c);
	}
	
}
