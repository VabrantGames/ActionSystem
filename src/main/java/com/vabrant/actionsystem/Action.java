package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Action implements Poolable{
	
	boolean hasBeenReset;
	boolean isManaged = true;
	//TODO still needed?
	private boolean hasBeenPooled;
	
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
	
	public void setLoggingLevel(int level) {
		logger.setLevel(level);
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public void addPreAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null.");
		preActions.add(action);
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
	
	public void setName(String name) {
		if(name == null) throw new IllegalArgumentException("Name can't be null.");
		this.name = name;
		logger.setActionName(name);
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
	
	@Override
	public void reset() {
		if(logger != null) logger.info("Reset");
		if(isManaged) actionManager = null;
		preActions.clear();
		listeners.clear();
		pauseCondition = null;
		name = null;
		isPaused = false;
		isRunning = false;
		isFinished = false;
		logger.clearActionName();
	}
	
	public void clear() {
		if(logger != null) logger.info("Clear");		
		isRunning = false;
		isFinished = false;
		isPaused = false;
	}
	
	public void start() {
		if(logger != null) logger.info("Start");		
		
		if(!isManaged()) {
			if(actionManager == null) throw new GdxRuntimeException("Unmanaged Actions need to be added to an Action Manager.");
			actionManager.startUnmanagedAction(this);
		}
		
		if(preActions.size > 0) {
			//exception will be thrown when if the action is managed and start is called
			//TODO add exception message
			if(actionManager == null) throw new ActionSystemRuntimeException("");
			for(int i = preActions.size - 1; i >= 0; i--) {
				actionManager.addAction(preActions.pop());
			}
		}
		
		isRunning = true;
		isFinished = false;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart(this);
		}
	}
	
	public void restart() {
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
	
	protected void onComplete() {
		if(logger != null) logger.info("Completed");	
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
	
	public static <T extends Action> T getAction(Class<T> c) {
		return ActionPools.obtain(c);
	}
	
}
