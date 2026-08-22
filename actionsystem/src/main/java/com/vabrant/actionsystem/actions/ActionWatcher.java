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
import com.vabrant.actionsystem.events.Event;
import com.vabrant.actionsystem.events.ActionResetEvent;
import com.vabrant.actionsystem.events.EventListener;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.logger.ActionLoggerManager;

/** Keeps track of actions that may be nested inside other actions or to provide global access to actions.
 * @author John Barton */
public class ActionWatcher {

	private static ActionWatcher instance = null;
	private static final ActionLogger logger = ActionLoggerManager.getLogger(ActionWatcher.class);

	public static ActionWatcher getInstance () {
		if (instance != null) return instance;

		instance = new ActionWatcher(20);
		instance.setLogID("DefaultActionWatcher");
		return instance;
	}

	private int logLevel = ActionLogger.LOGGER_NONE;
	private String logID;
	private final ObjectMap<String, Action<?>> watchActions;

	private EventListener cleanupListener = new EventListener() {
		@Override
		public void onEvent (Event e) {
			remove(e.getAction().getName());
		}
	};

	public ActionWatcher (int amount) {
		watchActions = new ObjectMap<>(amount);
	}

	public Action<?> get (String key) {
		return watchActions.get(key);
	}

	public void setLogLevel (int level) {
		logLevel = level;
	}

	public void setLogID (String logID) {
		this.logID = logID;
	}

	public ActionLogger getLogger () {
		return logger;
	}

	public void watch (Action<?> action) {
		String key = action.getName();

		if (key == null || key.isEmpty()) throw new IllegalArgumentException("Action needs a name to be identified.");

		// Check if the watcher contains the key or the same action instance
		if (watchActions.containsKey(key) || watchActions.containsValue(action, false)) {
			logger.debug(logLevel, logID, "Key or action is already added.", null, null);
			return;
		}

		action.subscribeToEvent(ActionResetEvent.class, cleanupListener);
		watchActions.put(key, action);
		logger.info(logLevel, logID, "Watching", key, null);
	}

	public boolean remove (Action<?> action) {
		return remove(action.getName());
	}

	/** Removes an action from the watcher.
	 * @param name
	 * @return Whether the action was removed. */
	public boolean remove (String name) {
		Action<?> action = watchActions.remove(name);

		if (action == null) {
			logger.info(logLevel, logID, "Action " + name + " doesn't exist", null, null);
			return false;
		}

		action.unsubscribeFromEvent(ActionResetEvent.class, cleanupListener);

		logger.info(logLevel, logID, "Stopped Watching", name, null);
		return true;
	}

	public boolean contains (String key) {
		return watchActions.containsKey(key);
	}

}
