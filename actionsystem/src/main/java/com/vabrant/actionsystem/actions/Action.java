package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * The Action class is the base class for all actions... add more
 * 
 * @author John Barton
 */
@SuppressWarnings("all")
public class Action<T extends Action> implements Poolable{

	/** Whether this action is the root action. */
	boolean isRoot;
	
	/** A reference to the root action for non root action. Root actions will use themselves if requested. */
	Action rootAction;
	
	/** Whether this action is managed by the {@link ActionManager} or by the user.*/
	boolean isManaged = true;
	
	//TODO still needed?
	private boolean hasBeenPooled;
	
	private boolean forceKill;
	private boolean forceEnd;
	
	/** The amount of cycles this action has ran since {@link #start} or {@link #restart} was called. */
	private int cycle;
	
	/** Whether the current cycle is finished or not. */ 
	protected boolean isCycleRunning;
	protected boolean isRunning;
	protected boolean isPaused;
	
	private String name;
	private PauseCondition pauseCondition;
	private ActionManager actionManager;
	private Array<ActionListener> listeners;
	
	/** Listeners that can't be removed by the user. */
	private Array<CleanupListener> cleanupListeners;
	final Array<Action> preActions;
	final Array<Action> postActions;
	private ConflictChecker conflictWatcher;
	
	protected final ActionLogger logger;
	
	public Action() {
		listeners = new Array<>(2);
		cleanupListeners = new Array<>(3);
		preActions = new Array<>(1);
		postActions = new Array<>(1);
		logger = ActionLogger.getLogger(this.getClass(), ActionLogger.NONE);
	}
	
	public T setLogLevel(int level) {
		if(logger != null) logger.setLevel(level);
		return (T)this;
	}
	
	public T soloLogger() {
		if(logger != null) logger.solo();
		return (T)this;
	}
	
	public ActionLogger getLogger() {
		return logger;
	}

	public T watchAction() {
		ActionWatcher.watch(this);
		return (T)this;
	}
	
	public T setConflictChecker(ConflictChecker conflictChecker) {
		if(conflictChecker == null) throw new IllegalArgumentException("ConflictChecker is null.");
		this.conflictWatcher = conflictChecker;
		return (T)this;
	}
	
	protected boolean hasConflict(Action action) {
		return false;
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

	public int getCycle() {
		return cycle;
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
	
	void setRoot() {
		isRoot = true;
		if(logger != null) logger.debug("Is Root: " + isRoot);
	}
	
	protected void setRootAction(Action root) {
		if(!isRoot) rootAction = root;
	}

	public boolean isRoot() {
		return isRoot;
	}
	
	public Action getRootAction() {
		return isRoot ? this : rootAction;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public boolean isCycleRunning() {
		return isCycleRunning;
	}
	
	private void runPostActions() {
		if(postActions.size > 0) {
			ActionManager manager = getRootAction().actionManager;
			for(int i = postActions.size - 1; i >= 0; i--) {
				manager.addAction(postActions.pop());
			}
		}
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
	
	public final void pause() {
		if(!isRunning || isPaused || pauseCondition != null && !pauseCondition.shouldPause()) return;
		isPaused = true;
		pauseLogic();
	}
	
	protected void pauseLogic() {}
	
	public final void resume() {
		if(!isRunning || !isPaused || pauseCondition != null && !pauseCondition.shouldResume()) return;
		isPaused = false;
		resumeLogic();
	}
	
	protected void resumeLogic() {}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public T setPauseCondition(PauseCondition pauseCondition) {
		if(pauseCondition == null) throw new IllegalArgumentException("PauseCondition is null.");
		this.pauseCondition = pauseCondition;
		return (T)this;
	}
	
	protected boolean containsCleanupListener(CleanupListener listener) {
		return cleanupListeners.contains(listener, false);
	}
	
	protected T addCleanupListener(CleanupListener listener) {
		if(listener == null) throw new IllegalArgumentException("LibraryListener is null.");
		cleanupListeners.add(listener);
		return (T)this;
	}
	
	protected T removeCleanupListener(CleanupListener listener) {
		cleanupListeners.removeValue(listener, false);
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
		
		//clean up
		if(!hasBeenPooled) {
			if(logger != null) logger.debug("Cleanup");
			for(int i = 0; i < cleanupListeners.size; i++) {
				cleanupListeners.get(i).cleanup(this);
			}
		}
		
		if(logger != null) {
			logger.setLevel(ActionLogger.NONE);
		}
		cycle = 0;
		cleanupListeners.clear();
		listeners.clear();
		pauseCondition = null;
		name = null;
		isPaused = false;
		isRunning = false;
		isCycleRunning = false;
		isRoot = false;
		rootAction = null;
		forceKill = false;
		forceEnd = false;
		if(logger != null) logger.clearActionName();
	}
	
	public T clear() {
//		isRoot = false;
//		rootAction = null;
		cycle = 0;
		if(logger != null) logger.info("Clear");		
		isRunning = false;
		isCycleRunning = false;
		isPaused = false;
		return (T)this;
	}
	
	void forceKill() {
		forceKill = true;
		kill();
	}
	
	void forceEnd() {
		forceEnd = true;
		end();
	}
	
	/**
	 * Starts the action and the initial cycle.
	 */
	public final T start() {
		if(isRunning) return (T)this;
		
		if(conflictWatcher != null) {
			if(conflictWatcher.checkForConflict(this)) return null;
		}
		
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
		
		if(logger != null) logger.info("Start Action");	
		
		isRunning = true;
		
		startLogic();
		startCycle(false);
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart(this);
		}
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionCycleStart(this);
		}
		
		return (T)this;
	}

	/**
	 * FOR ACTION CREATION <br><br> 
	 * 
	 * The logic that will be ran at the start of the action. Will only be ran once.
	 */
	protected void startLogic() {}
	
	/**
	 * Starts the cycle.
	 */
	public final T startCycle() {
		if(!isRunning || isCycleRunning) return (T)this;
		startCycle(true);
		return (T)this;
	}
	
	//This is to ensure the listeners of the start and restart methods get called before the the startCycle listeners. 
	private final void startCycle(boolean runListeners) {
		++cycle;
		
		isCycleRunning = true;
		
		if(logger != null) logger.debug("Start Cycle: " + cycle);
		
		startCycleLogic();
		
		if(runListeners) {
			for(int i = 0; i < listeners.size; i++) {
				listeners.get(i).actionCycleStart(this);
			}
		}
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran at the start of every cycle. Including when {@link #start} is called. 
	 */
	protected void startCycleLogic() {}
	
	/**
	 * Restarts the action from its initial cycle. <br>
	 * <b>Note:</b> This method is not called internally but by the user.
	 */
	public final T restart() {
		if(!isRunning) return (T)this;
		if(logger != null) logger.info("Restart Action");		

		isRunning = true;
		cycle = 0;
		
		restartLogic();
		startCycle(false);
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart(this);
		}
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionCycleStart(this);
		}
		
		return (T)this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran if an action is restarted.
	 */
	protected void restartLogic() {}
	
	/**
	 * Restarts the current cycle from the beginning.<br>
	 * <b>Note:</b> This method is not called internally but by the user.
	 */
	public final T restartCycle() {
		if(!isRunning || !isCycleRunning) return (T)this;
		
		if(logger != null) logger.info("Restart Cycle");
		
		restartCycleLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionCycleRestart(this);
		}
		
		return (T)this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran every time a cycle is restarted.
	 */
	protected void restartCycleLogic() {}
	
	/**
	 * Ends the action. If the action is running it will be ended as if it were completed.
	 */
	public final T end() {
		if(!isRunning && !forceEnd) return (T)this;
		 
		//If the cycle is still running end it 
		if(isCycleRunning) endCycle();
		
		if(logger != null) logger.info("End Action");		
		
		isRunning = false;
		
		endLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionEnd(this);
		}
		
		runPostActions();
		
		return (T)this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran if the action is ended.
	 */
	protected void endLogic() {}
	
	/**
	 * Ends the current cycle. If this is called before the current cycle has finished it will be ended as if it has. <br> 
	 */
	public final T endCycle() {
		if(!isRunning || !isCycleRunning) return (T)this;
		
		if(logger != null) logger.debug("End Cycle");
		
		isCycleRunning = false;
		
		endCycleLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionCycleEnd(this);
		}
		
		return (T)this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran at the end of every cycle.
	 */
	protected void endCycleLogic() {}	
	
	/**
	 * Ends the action at it's current position. If the action is running it will end uncompleted. 
	 */
	public final T kill() {
		if(!isRunning && !forceKill) return (T)this;
		
		if(isCycleRunning) killCycle();
		
		if(logger != null) logger.info("Kill Action");	
		
		isRunning = false;
		
		killLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionKill(this);
		}
		
		runPostActions();
		
		return(T)this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran if an action is killed.
	 */
	protected void killLogic() {}
	
	/**
	 * Ends the current cycle at its current position. If this is called before the cycle has finished it will be ended in its unfinished state.
	 */
	public final T killCycle() {
		if(!isRunning || !isCycleRunning) return (T)this;
		
		if(logger != null) logger.debug("Kill Cycle");
		
		isCycleRunning = false;
		
		killCycleLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionCycleKill(this);
		}
		
		return (T)this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran every time a cycle is killed.
	 */
	protected void killCycleLogic() {}

	public static <T extends Action> T getAction(Class<T> c) {
		return ActionPools.obtain(c);
	}
	
}
