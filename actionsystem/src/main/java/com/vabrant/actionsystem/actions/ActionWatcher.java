package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * 
 * @author John
 *
 */
public class ActionWatcher {

	private static final ActionWatcher instance = new ActionWatcher();
	
	public static void watch(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		instance.watchAction(action);
	}
	
	public static Action stopWatching(String name) {
		return instance.removeAction(name);
	}
	
	public static Action get(String name) {
		return instance.actions.get(name);
	}
	
	public static ActionLogger getLogger() {
		return instance.logger;
	}
	
	private final ActionLogger logger;
	private final ObjectMap<String, Action<? extends Action>> actions;
	private final CleanupListener listener;
	
	private ActionWatcher() {
		actions = new ObjectMap<>(20);
		logger = ActionLogger.getLogger(ActionWatcher.class, ActionLogger.NONE);
		listener = createActionListener();
	}
	
	private CleanupListener createActionListener() {
		return new CleanupListener() {
			@Override
			public void cleanup(Action a) {
				removeAction(a.getName());
			}
		};
	}

	private void watchAction(Action action) {
		if(action.getName() == null) throw new IllegalArgumentException("Action name is null");
		action.addCleanupListener(listener);
		actions.put(action.getName(), action);
		if(logger != null) logger.info("Watching", action.getName());
	}
	
	private Action removeAction(String name) {
		Action action = actions.remove(name);
		
		if(action == null) {
			if(logger != null) logger.info("Action " + name + " doesn't exist");
			return null;
		}
		
		action.removeCleanupListener(listener);
		
		if(logger != null) logger.debug("Stopped Watching", name);
		
		return action;
	}

}
