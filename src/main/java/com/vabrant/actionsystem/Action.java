package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Action implements Poolable{
	
	private boolean isManaged = true;
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
	
	/**
	 * For use with unmanaged actions.
	 */
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
	
	public void clearListeners() {
		listeners.clear();
	}
	
	public boolean update(float delta) {
		return false;
	}
	
	@Override
	public void reset() {
		if(!isManaged && actionManager != null) actionManager.poolUnmanagedAction(this);
		actionManager = null;
		isManaged = true;
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
			listeners.get(i).actionStart();
		}
	}
	
	public void restart() {
		isRunning = false;
		isFinished = false;
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart();
		}
	}
	
	/**
	 * Ends the action as if it's completed.
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
		return ActionPools.obtain(c);
	}
	
}
