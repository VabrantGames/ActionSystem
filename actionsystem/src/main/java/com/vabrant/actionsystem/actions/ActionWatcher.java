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

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Keeps track of actions that may be nested inside other actions or to provide global access to actions.
 * @author John Barton
 */
public class ActionWatcher {

	private static ActionWatcher instance = null;
	
	public static ActionWatcher getInstance() {
		return instance != null ? instance : (instance = new ActionWatcher(20));
	}

	private final ActionLogger logger;
	private final ObjectMap<String, Action<? extends Action<?>>> watchActions;
	private final CleanupListener listener;
	
	public ActionWatcher(int amount) {
		watchActions = new ObjectMap<>(amount);
		logger = ActionLogger.getLogger(ActionWatcher.class, ActionLogger.NONE);
		listener = createActionListener();
	}
	
	private CleanupListener createActionListener() {
		return new CleanupListener() {
			@Override
			public void cleanup(Action a) {
				remove(a.getName());
			}
		};
	}
	
	public Action<?> get(String key){
		return watchActions.get(key);
	}

	public void watch(Action<?> action) {
		String key = action.getName();
		
		if(key == null || key.isEmpty()) throw new IllegalArgumentException("Action needs a name to be identified.");
		
		//Check if the watcher contains the key or the same action instance
		if(watchActions.containsKey(key) || watchActions.containsValue(action, false)) {
			logger.debug("Key or action is already added.");
			return;
		}
		
		action.addCleanupListener(listener);
		watchActions.put(key, action);
		logger.info("Watching", key);
	}
	
	public boolean remove(Action<?> action) {
		return remove(action.getName());
	}
	
	/**
	 * Removes an action from the watcher.
	 * @param name
	 * @return Whether or not the action was removed.
	 */
	public boolean remove(String name) {
		Action<?> action = watchActions.remove(name);
		
		if(action == null) {
			logger.info("Action " + name + " doesn't exist");
			return false;
		}
		
		action.removeCleanupListener(listener);
		
		logger.info("Stopped Watching", name);
		return true;
	}
	
	public boolean contains(String key) {
		return watchActions.containsKey(key);
	}
	
	public ActionLogger getLogger() {
		return logger;
	}

}
