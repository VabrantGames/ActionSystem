package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

/**
 * The base class for all actions.
 * 
 * @author John Barton
 */
//@SuppressWarnings("all")
@SuppressWarnings("unchecked")
public class Action<T extends Action<T>> implements Poolable {
	
	/**
	 * Returns the specified action.
	 * @param c Class of the action.
	 */
	public static <T extends Action<T>> T obtain(Class<T> c) {
		return ActionPools.obtain(c);
	}

	/** Whether this action is the root action. */
	boolean isRoot;
	
	/** A reference to the root action for non root action. Root actions will use themselves if requested. */
	Action<?> rootAction;
	
	/** Whether this action is managed by the {@link ActionManager} or by the user.*/
	boolean isManaged = true;
	
	boolean canReset;
	private boolean hasBeenPooled;
	private boolean forceKill;
	private boolean forceEnd;
	protected boolean isDead;
	protected boolean isRunning;
	protected boolean isPaused;
	
	private String name;
	private Condition<T> pauseCondition;
	private Condition<T> resumeCondition;
	private ActionManager actionManager;
	private Array<ActionListener<T>> listeners;
	
	/** Listeners that can't be removed by the user. */
	private Array<CleanupListener> cleanupListeners;
	final Array<Action<?>> preActions;
	final Array<Action<?>> postActions;
	
	protected final ActionLogger logger;
	
	public Action() {
		listeners = new Array<>(2);
		cleanupListeners = new Array<>(3);
		preActions = new Array<>(2);
		postActions = new Array<>(2);
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
	
	public boolean hasConflict(Action<T> action) {
		return false;
	}
	
	public T addPreAction(Action<?> action) {
		if(action == null) throw new IllegalArgumentException("Action is null.");
		preActions.add(action);
		return (T)this;
	}
	
	public Array<Action<?>> getPreActions(){
		return preActions;
	}
	
	public T addPostAction(Action<?> action) {
		if(action == null) throw new IllegalArgumentException("Action is null.");
		postActions.add(action);
		return (T)this;
	}
	
	public Array<Action<?>> getPostActions(){
		return postActions;
	}
	
	void setPooled(boolean pooled) {
		hasBeenPooled = pooled;
	}
	
	boolean hasBeenPooled() {
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

	void setActionManager(ActionManager actionManager) {
		this.actionManager = actionManager;
	}

	void setRoot() {
		isRoot = true;
	}
	
	public void setRootAction(Action<?> root) {
		rootAction = root;
	}

	public boolean isRoot() {
		return isRoot;
	}
	
	public Action<?> getRootAction() {
		return rootAction;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public boolean isRunning() {
		return isRunning;
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
		this.name = name;
		if(logger != null) logger.setActionName(name);
		return (T)this;
	}
	
	public String getName() {
		return name;
	}
	
	public final void pause() {
		if(!isRunning || isPaused || pauseCondition != null && !pauseCondition.isTrue((T)this)) return;
		isPaused = true;
		pauseLogic();
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran every time pause is called.
	 */
	protected void pauseLogic() {}
	
	public final void resume() {
		if(!isRunning || !isPaused || resumeCondition != null && !resumeCondition.isTrue((T)this)) return;
		isPaused = false;
		resumeLogic();
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran every time resume is called.
	 */
	protected void resumeLogic() {}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public T setPauseCondition(Condition<T> pauseCondition) {
		this.pauseCondition = pauseCondition;
		return (T)this;
	}
	
	public T setResumeCondition(Condition<T> resumeCondition) {
		this.resumeCondition = resumeCondition;
		return (T)this;
	}
	
	protected boolean containsCleanupListener(CleanupListener<T> listener) {
		return cleanupListeners.contains(listener, false);
	}
	
	protected T addCleanupListener(CleanupListener<T> listener) {
		if(listener == null) throw new IllegalArgumentException("LibraryListener is null.");
		cleanupListeners.add(listener);
		return (T)this;
	}
	
	protected T removeCleanupListener(CleanupListener<T> listener) {
		cleanupListeners.removeValue(listener, false);
		return (T)this;
	}
	
	public T addListener(ActionListener<T> listener) {
		if(listener == null) throw new IllegalArgumentException("Listener is null.");
		listeners.add(listener);
		return (T)this;
	}
	
	public T removeListener(ActionListener<T> listener) {
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

	void forceKill() {
		forceKill = true;
		kill();
	}
	
	void forceEnd() {
		forceEnd = true;
		end();
	}
	
	public void permanentKill() {
		isDead = true;
		kill();
	}
	
	public void permanentEnd() {
		isDead = true;
		end();
	}

	/**
	 * Pools an unmanaged action. 
	 */
	public void free() {
		if(isManaged) return;
		isManaged = true;
		ActionPools.free(this);
	}

	public void clear() {}
	
	@Override
	public void reset() {
		if(!canReset && isManaged) throw new RuntimeException("Reset can't be called externally.");
		
		if(logger != null) logger.info("Reset");
		
		//clean up
		if(!hasBeenPooled) {
			if(logger != null) logger.debug("Cleanup");
			for(int i = 0; i < cleanupListeners.size; i++) {
				cleanupListeners.get(i).cleanup(this);
			}
		}
		
		isDead = false;
		isPaused = false;
		isRunning = false;
		isRoot = false;
		rootAction = null;
		forceKill = false;
		forceEnd = false;
		canReset = false;
		
		if(isManaged) {
			if(logger != null) logger.setLevel(ActionLogger.NONE);
			if(logger != null) logger.clearActionName();
			pauseCondition = null;
			resumeCondition = null;
			cleanupListeners.clear();
			listeners.clear();
			name = null;
		}
	}
	
	/**
	 * Starts the action
	 */
	public final T start() {
		if(isRunning) return (T)this;
		if(isDead) {
			if(isManaged) return (T)this;
			
			//Reset some values if an unmanaged action is dead
			isDead = false;
			forceKill = false;
			forceEnd = false;
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
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionStart((T)this);
		}

		return (T)this;
	}

	/**
	 * FOR ACTION CREATION <br><br> 
	 * 
	 * The logic that will be ran at the start of the action. 
	 */
	protected void startLogic() {}

	/**
	 * Restarts the action and its children. <br>
	 */
	public final T restart() {
		if(!getRootAction().isRunning()) return (T)this;
		restartChildren(this);
		restart(true);
		return (T)this;
	}
	
	protected final void restart(boolean invokedAction) {
		if(logger != null) logger.info("Restart Action");	
		
		boolean start = !invokedAction ? false : isRunning;
		
		isRunning = false;
		restartLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionRestart((T)this);
		}
		
		if(start) start();
	}
	
	/**
	 *  Recursively restart all children and it's children of an action.
	 *  
	 * @param action
	 */
	protected void restartChildren(Action<?> action) {
		if(action instanceof SingleParentAction) {
			Action<?> child = ((SingleParentAction)action).getAction();
			if(child == null) return;
			restartChildren(child);
			child.restart(false);
		}
		else if(action instanceof MultiParentAction) {
			Array<Action<?>> children = ((MultiParentAction)action).getActions();
			for(int i = 0, size = children.size; i < size; i++) {
				Action<?> child = children.get(i);
				if(child == null) continue;
				restartChildren(child);
				child.restart(false);
			}
		}
	}
		
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran if an action is restarted.
	 */
	protected void restartLogic() {}

	/**
	 * Ends the action. If the action is running it will be ended as if it were completed.
	 */
	public final T end() {
		if(!isRunning && !forceEnd) return (T)this;
		
		if(isRoot()) isDead = true;
		isRunning = false;
		endLogic();
		
		if(logger != null) logger.info("End Action");		
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionEnd((T)this);
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
	 * Ends the action at it's current position. If the action is running it will end uncompleted. 
	 */
	public final T kill() {
		if(!isRunning && !forceKill) return (T)this;
		
		if(logger != null) logger.info("Kill Action");	
		
		if(isRoot()) isDead = true;
		isRunning = false;
		
		killLogic();
		
		for(int i = 0; i < listeners.size; i++) {
			listeners.get(i).actionKill((T)this);
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

}
