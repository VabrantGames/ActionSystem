/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.EventListener;
import com.vabrant.actionsystem.events.EventManager;
import com.vabrant.actionsystem.logger.ActionLogger;

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
		Pool<T> pool = ActionPools.get(c);
		T action = pool.obtain();
		action.setPooled(false);
		action.pool = pool;
		return action;
	}

	Pool<T> pool;
	
	/** Whether this action is currently being used by an {@link ActionManager} */
	boolean inUse;
	
	/** A reference to the root action.*/
	Action<?> rootAction;
	
	/** Whether this action is managed by the {@link ActionManager} or by the user.*/
	boolean isManaged = true;
	
	private boolean hasBeenPooled;
	protected boolean isDead;
	protected boolean isRunning;
	protected boolean isPaused;
	
	private String name;
	private Condition pauseCondition;
	private Condition resumeCondition;
	private ActionManager actionManager;
	protected EventManager eventManager;

	protected final ActionLogger logger;

	public Action() {
		logger = ActionLogger.getLogger(this.getClass(), ActionLogger.LogLevel.NONE);
	}
	
	public T setLogLevel(ActionLogger.LogLevel level) {
		logger.setLevel(level);
		return (T) this;
	}

	public T soloLogger(boolean solo) {
		logger.solo(solo);
		return (T) this;
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public T watchAction(ActionWatcher watcher) {
		watcher.watch(this);
		return (T)this;
	}

	public T watchAction() {
		ActionWatcher.getInstance().watch(this);
		return (T)this;
	}
	
	@Deprecated
	public boolean hasConflict(Action<?> action) {
		return false;
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

	/**
	 * Base of an Action hierarchy
	 * @param root
	 */
	void setRoot(boolean root) {
		inUse = root;
	}
	
	public void setRootAction(Action<?> root) {
		rootAction = root;
	}

	public boolean isRoot() {
		return this.equals(rootAction);
	}
	
	public Action<?> getRootAction() {
		return rootAction;
	}
	
	public Pool<T> getPool(){
		return pool;
	}

	public boolean isRunning() {
		if(rootAction != null && rootAction.isDead) return false;
		return isRunning;
	}
	
	public boolean inUse() {
		return rootAction != null && rootAction.inUse;
	}

	public T setName(String name) {
		this.name = name;
		logger.setName(name);
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
	 * The logic that will run every time pause is called.
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
	 * The logic that will run every time resume is called.
	 */
	protected void resumeLogic() {}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public T setPauseCondition(Condition pauseCondition) {
		this.pauseCondition = pauseCondition;
		return (T)this;
	}
	
	public T setResumeCondition(Condition resumeCondition) {
		this.resumeCondition = resumeCondition;
		return (T)this;
	}

	//TODO Use own pool
	public T subscribeToEvent(String eventType, EventListener<?> listener) {
		if (eventManager == null) eventManager = ActionPools.obtain(EventManager.class);
		eventManager.subscribe(eventType, listener);
		return (T) this;
	}

	public T unsubscribeFromEvent(String eventType, EventListener<?> listener) {
		if (eventManager == null) return (T) this;
		eventManager.unsubscribe(eventType, listener);
		return (T) this;
	}

	public T clearListeners(String eventType) {
		return (T) this;
	}

	public T clearAllListeners() {
		return (T) this;
	}
	
	public boolean update(float delta) {
		return false;
	}

	/**
	 * Pools an unmanaged action. Any references to the action should be nulled.
	 */
	public void free() {
		if(isManaged) return;
		isManaged = true;
		ActionPools.free(this);
	}

	//TODO Finish description 
	/**
	 * Clears an actions values but 
	 */
	public void clear() {
		if(inUse()) throw new IllegalStateException("Action can't be cleared while in use.");
		
		isDead = false;
		isPaused = false;
		isRunning = false;
		rootAction = null;
	}

	//Called when an unmanaged action is being cleanup by the ActionPool
	void unmanagedReset() {
		rootAction = null;
		isDead = false;
		isPaused = false;
		isRunning = false;
	}
	
	/**
	 * Resets an action to its default state.
	 */
	@Override
	public void reset() {
		if(inUse()) throw new IllegalStateException("Action can't be reset while in use.");
		
		logger.debug("Cleanup");

		if (eventManager != null && eventManager.hasEvent(ActionEvent.RESET_EVENT)) {
			ActionEvent event = ActionPools.obtain(ActionEvent.class);
			event.setAsReset();
			event.setAction(this);
			eventManager.fire(event);
		}
		
		logger.info("Reset");
		
		clear();
		logger.reset();
		pauseCondition = null;
		resumeCondition = null;
		name = null;

		if (eventManager != null) {
			ActionPools.free(eventManager);
			eventManager = null;
		}
	}
	
	/**
	 * FOR ACTION CREATION <br><br> 
	 * 
	 * The logic that will run at the start of the action.
	 */
	protected void startLogic() {}

	//TODO Use own pool
	/**
	 * Starts the action
	 */
	public final T start() {
		if(isRunning) return (T) this;
		if(isManaged && hasBeenPooled) throw new IllegalStateException("Managed actions may not be reused without being returned to a pool. To reuse an action make it unmanaged.");
		if(rootAction.isDead) throw new IllegalStateException("Action is dead.");

		logger.info("Start Action");	
		
		isRunning = true;
		startLogic();

		if (eventManager != null && eventManager.hasEvent(ActionEvent.START_EVENT)) {
			ActionEvent event = ActionPools.obtain(ActionEvent.class);
			event.setAsStart();
			event.setAction(this);
			eventManager.fire(event);
		}

		return (T) this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will run if an action is restarted.
	 */
	protected void restartLogic() {}

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
		logger.info("Restart Action");	
		
		boolean start = invokedAction;
		isRunning = false;
		restartLogic();

		if (eventManager != null && eventManager.hasEvent(ActionEvent.RESTART_EVENT)) {
			ActionEvent event = ActionPools.obtain(ActionEvent.class);
			event.setAsRestart();
			event.setAction(this);
			eventManager.fire(event);
		}
		
		if (start) start();
	}
	
	/**
	 *  Recursively restart all an actions children and their children.
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
	 * The logic that will run if the action is ended.
	 */
	protected void endLogic() {}

	/**
	 * Ends the action. If the action is running it will be ended as if it were completed.
	 */
	public final T end() {
		if (!isRunning()) return (T) this;

		logger.info("End Action");		
		
		if(isRoot()) isDead = true;
		isRunning = false;
		endLogic();

		if (eventManager != null && eventManager.hasEvent(ActionEvent.END_EVENT)) {
			ActionEvent event = ActionPools.obtain(ActionEvent.class);
			event.setAsEnd();
			event.setAction(this);
			eventManager.fire(event);
		}
		
		return (T) this;
	}
	
	/**
	 * FOR ACTION CREATION <br><br>
	 * 
	 * The logic that will be ran if an action is killed.
	 */
	protected void killLogic() {}
	
	/**
	 * Ends the action at its current position. If the action is running it will end uncompleted.
	 */
	public final T kill() {
		if (!isRunning()) return (T) this;

		logger.info("Kill Action");	
		
		if(isRoot()) isDead = true;
		isRunning = false;
		killLogic();

		if (eventManager != null && eventManager.hasEvent(ActionEvent.KILL_EVENT)) {
			ActionEvent event = ActionPools.obtain(ActionEvent.class);
			event.setAsKill();
			event.setAction(this);
			eventManager.fire(event);
		}

		return (T) this;
	}

}
