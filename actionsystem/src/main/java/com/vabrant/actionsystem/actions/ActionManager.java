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
import com.vabrant.actionsystem.logger.ActionLogger;

/**
 * Manages actions.
 * 
 * @author John Barton
 *
 */
public class ActionManager {
	
	private final Array<Action<?>> actions;
	private final ActionLogger logger = ActionLogger.getLogger(ActionManager.class);
	
	public ActionManager() {
		this(10);
	}
	
	public ActionManager(int initialSize) {
		actions = new Array<>(initialSize);
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public Array<Action<?>> getActions(){
		return actions;
	}

	/**
	 * Adds an action to the action manager.
	 * @param action
	 */
	public void addAction(Action<?> action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		action.setActionManager(this);
		action.setRoot(true);
		action.setRootAction(action);
		actions.add(action);
		action.start();
		if(logger != null) logger.info("Added" + action.getLogger().getActionName(), action.getLogger().getClassName());
	}
	
	public void update(float delta) {
		for(int i = actions.size - 1; i >= 0; i--) {
			Action<?> action = actions.get(i);
			
			if(!action.update(delta)) {
				if(actions.size == 0) break;
				actions.removeIndex(i);
				action.setRoot(false);
//				Pool pool = action.pool;
//				pool.free(action);
				ActionPools.free(action);
			}
		}
	}
	
	public void endAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).end();
		}
	}
	
	public void killAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).kill();
		}
	}
	
	public void pauseAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).pause();
		}
	}
	
	public void resumeAllActions() {
		for(int i = 0, size = actions.size; i < size; i++) {
			actions.get(i).resume();
		}
	}
	
	public void freeAll() {
		freeAll(true);
	}

	/**
	 * Pools all action.
	 * @param freeUnmanaged
	 */
	public void freeAll(boolean freeUnmanaged) {
		if(logger != null) logger.debug("Free All");
		for(int i = actions.size - 1; i >= 0; i--) {
			Action<?> action = actions.removeIndex(i);
			action.setRoot(false);
			
			if(!action.isManaged() && freeUnmanaged) {
				action.free();
			}
			else {
				ActionPools.free(action);
			}
		}
	}

}
