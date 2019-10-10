package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Action<T extends Action> implements Poolable{
	
	boolean hasBeenReset;
	boolean isManaged = true;
	//TODO still needed?
	private boolean hasBeenPooled;
	
	protected boolean isCompleted;
	protected boolean isFinished;
	protected boolean isRunning;
	protected boolean isPaused;
	private String name;
	private PauseCondition pauseCondition;
	private ActionManager actionManager;
	private Array<ActionListener> listeners;
	final Array<Action> preActions;
	protected ActionLogger logger;
	
	public Action() {
		listeners = new Array<>(2);
		preActions = new Array<>(2);
		logger = new ActionLogger(this.getClass());
		
	//DEBUG remove
		logger.setLevel(Logger.DEBUG);
	}
	
	public T setLoggingLevel(int level) {
		logger.setLevel(level);
		return (T)this;
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public T addPreAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null.");
		preActions.add(action);
		return (T)this;
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
	public T unmanage() {
		isManaged = false;
		return (T)this;
	}
	
	public boolean isManaged() {
		return isManaged;
	}
	
	public void poolUnmanagedAction() {
		if(!isManaged && actionManager != null) {
			isManaged = true;
			actionManager.poolUnmanagedAction(this);
		}
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
	
	public T setName(String name) {
		if(name == null) throw new IllegalArgumentException("Name can't be null.");
		this.name = name;
		logger.setActionName(name);
		return (T)this;
	}
	
	public String getName() {
		return name;
	}

	public void setPause(boolean pause) {
		if(!isRunning) return;
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
	
	public T setPauseCondition(PauseCondition pauseCondition) {
		if(pauseCondition == null) throw new IllegalArgumentException("PauseCondition is null.");
		this.pauseCondition = pauseCondition;
		return (T)this;
	}
	
	public T addListener(ActionListener listener) {
		if(listener == null) throw new IllegalArgumentException("Listener is null.");
		listeners.add(listener);
		return (T)this;
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
		if(logger != null) logger.info("Reset");
		
		//TODO maybe use a flag for when this needs to be reset so it isn't used by accident with unmanaged actions
		if(isManaged) actionManager = null;
		preActions.clear();
		listeners.clear();
		pauseCondition = null;
		name = null;
		isCompleted = false;
		isPaused = false;
		isRunning = false;
		isFinished = false;
		logger.clearActionName();
	}
	
	public void clear() {
		if(logger != null) logger.info("Clear");		
		isCompleted = false;
		isRunning = false;
		isFinished = false;
		isPaused = false;
	}
	
	public void start() {
		if(logger != null) logger.info("Start");		
		
		if(!isManaged()) {
			if(actionManager == null) throw new ActionSystemRuntimeException("Unmanaged Actions need to be added to an Action Manager.");
			actionManager.startUnmanagedAction(this);
		}
		
		if(preActions.size > 0) {
			//exception will be thrown when if the action is managed and start is called
			//TODO add exception message. Can this be null?
			if(actionManager == null) throw new ActionSystemRuntimeException("");
			for(int i = preActions.size - 1; i >= 0; i--) {
				actionManager.addAction(preActions.pop());
			}
		}
		
		isCompleted = false;
		isRunning = true;
		isFinished = false;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart(this);
		}
	}
	
	public void restart() {
		if(isCompleted) return;
		if(logger != null) logger.info("Restart");		

		isRunning = true;
		isFinished = false;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart(this);
		}
	}
	
	/**
	 * Ends the action as if it's completed.
	 */
	public void end() {
		if(!isRunning) return;
		
		if(logger != null) logger.info("End");		
		
		isRunning = false;
		isFinished = true;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionEnd(this);
		}
	}
	
	/**
	 * Ends the action at it's current position but not as if it were completed.
	 */
	public void kill() {
		if(!isRunning) return;
		
		if(logger != null) logger.info("Kill");		
		
		isRunning = false;
		isFinished = true;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionKill(this);
		}
	}

	protected void complete() {
		if(logger != null) logger.info("Completed");	
		isCompleted = true;
	}
	
	public static <T extends Action> T getAction(Class<T> c) {
		return ActionPools.obtain(c);
	}
	
}
