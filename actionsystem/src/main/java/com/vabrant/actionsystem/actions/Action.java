package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool.Poolable;

@SuppressWarnings("unchecked")
public class Action<T extends Action> implements Poolable{
	
	boolean isRoot;
	Action rootAction;
	protected boolean lastCycle;
	
	boolean isManaged = true;
	//TODO still needed?
	private boolean hasBeenPooled;
	
	//are all cycles complete
	//TODO is needed?
	protected boolean isComplete;
	
	//has the current cycle finished
	protected boolean isFinished;
	
	//is the current cycle running
	protected boolean isRunning;
	protected boolean isPaused;
	private String name;
	private PauseCondition pauseCondition;
	private ActionManager actionManager;
	private Array<ActionListener> listeners;
	final Array<Action> preActions;
	final Array<Action> postActions;
	protected ActionLogger logger;
	
	public Action() {
		listeners = new Array<>(2);
		preActions = new Array<>(1);
		postActions = new Array<>(1);
		logger = ActionLogger.getLogger(this.getClass(), ActionLogger.NONE);
		
		//DEBUG remove
		logger.setLevel(Logger.DEBUG);
	}
	
	public T setLoggingLevel(int level) {
		if(logger != null) logger.setLevel(level);
		return (T)this;
	}
	
//	public T soloLogger() {
//		if(logger != null) logger.solo();
//		return (T)this;
//	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public T watch(ActionWatcher watcher) {
		if(watcher == null) throw new IllegalArgumentException("ActionWatcher is null.");
		watcher.watch(this);
		return (T)this;
	}
	
	public T addPreAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null.");
		preActions.add(action);
		return (T)this;
	}
	
	public Array<Action> getPreActions(){
		return preActions;
	}
	
	public T addPostAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null.");
		postActions.add(action);
		return (T)this;
	}
	
	public Array<Action> getPostActions(){
		return postActions;
	}
	
	protected void lastCycle() {
		if(!isRoot) return;
		lastCycle = true;
	}
	
	void setPooled(boolean pooled) {
		hasBeenPooled = pooled;
	}
	
	//TODO remove this
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
	
	void setRoot() {
		isRoot = true;
	}
	
	void setRootAction(Action root) {
		if(!isRoot) rootAction = root;
	}
	
	public Action getRootAction() {
		return rootAction;
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
		if(logger != null) logger.setActionName(name);
		return (T)this;
	}
	
	public String getName() {
		return name;
	}
	
	public void pause() {
		if(!isRunning || isPaused || pauseCondition != null && !pauseCondition.shouldPause()) return;
		isPaused = true;
	}
	
	public void resume() {
		if(!isRunning || !isPaused || pauseCondition != null && !pauseCondition.shouldResume()) return;
		isPaused = false;
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
	
	public T removeListener(ActionListener listener) {
		listeners.removeValue(listener, false);
		return (T)this;
	}
	
	public T clearListeners() {
		listeners.clear();
		return (T)this;
	}
	
	public boolean update(float delta) {
		return false;
	}

	@Override
	public void reset() {
		if(logger != null) logger.info("Reset");
		lastCycle = false;
		listeners.clear();
		pauseCondition = null;
		name = null;
		isComplete = false;
		isPaused = false;
		isRunning = false;
		isFinished = false;
		lastCycle = false;
		isRoot = false;
		rootAction = null;
		if(logger != null) logger.clearActionName();
	}
	
	public T clear() {
//		isRoot = false;
//		rootAction = null;
		lastCycle = false;
		if(logger != null) logger.info("Clear");		
		isComplete = false;
		isRunning = false;
		isFinished = false;
		isPaused = false;
		return (T)this;
	}
	
	public T start() {
		if(logger != null) logger.info("Start");	
		
		if(!isManaged()) {
			if(actionManager == null) throw new ActionSystemRuntimeException("Unmanaged Actions need to be added to an Action Manager.");
			actionManager.startUnmanagedAction(this);
		}
		
		if(preActions.size > 0) {
			if(!isRoot && rootAction == null) throw new ActionSystemRuntimeException("Root Action has to be added to a Action Manager.");
			ActionManager manager = !isRoot ? rootAction.actionManager : actionManager;
			for(int i = preActions.size - 1; i >= 0; i--) {
				manager.addAction(preActions.pop());
			}
		}
		
		lastCycle = false;
		isComplete = false;
		isRunning = true;
		isFinished = false;
		
		lastCycle();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart(this);
		}
		return (T)this;
	}
	
	public T restart() {
		if(isComplete) return (T)this;
		if(logger != null) logger.info("Restart");		

		lastCycle = false;
		isRunning = true;
		isFinished = false;
		
		lastCycle();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart(this);
		}
		return (T)this;
	}
	
	/**
	 * Ends the action as if it's completed.
	 */
	public T end() {
		if(!isRunning) return (T)this;
		
		if(logger != null) logger.info("End");		
		
		isRunning = false;
		isFinished = true;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionEnd(this);
		}
		
		//check if this is the last cycle
		isComplete = !isRoot ? rootAction.lastCycle : this.lastCycle;
		if(isComplete) {
			for(int i = 0; i < listeners.size; i++) {
				listeners.get(i).actionComplete(this);
			}
			
			if(postActions.size > 0) {
				ActionManager manager = !isRoot ? rootAction.actionManager : actionManager;
				for(int i = postActions.size - 1; i >= 0; i--) {
					manager.addAction(postActions.pop());
				}
			}
		}
		return (T)this;
	}
	
	/**
	 * Ends the action at it's current position but not as if it were completed.
	 */
	public T kill() {
		if(!isRunning) return (T)this;
		
		if(logger != null) logger.info("Kill");		
		
		isRunning = false;
		isFinished = true;
		isComplete = true;
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionKill(this);
		}
		
		if(postActions.size > 0) {
			ActionManager manager = !isRoot ? rootAction.actionManager : actionManager;
			for(int i = postActions.size - 1; i >= 0; i--) {
				manager.addAction(postActions.pop());
			}
		}
		
		return(T)this;
	}

	public static <T extends Action> T getAction(Class<T> c) {
		return ActionPools.obtain(c);
	}
	
}
