package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.ObjectMap;

public class ActionWatcher {

	private final ActionLogger logger;
	private final ObjectMap<String, Action> actions;
	private final ActionListener listener;
	
	public ActionWatcher() {
		this(3);
	}
	
	public ActionWatcher(int size) {
		actions = new ObjectMap<>(size);
		logger = ActionLogger.getLogger(ActionWatcher.class, ActionLogger.NONE);
		listener = createActionListener();
	}
	
	private ActionListener createActionListener() {
		return new ActionAdapter() {
			@Override
			public void actionKill(Action a) {
//				removeAction(a);
			}
			
			@Override
			public void actionComplete(Action a) {
				removeAction(a);
			}
		};
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public Action getAction(String name) {
		return actions.get(name);
	}

	public void watch(Action action) {
		if(action == null) return;
		if(action.getName() == null) throw new IllegalArgumentException("Action name is null");
		watch(action.getName(), action);
	}
	
	public void watch(String name, Action action) {
		if(action == null) return;
		action.addLibraryListener(listener);
		actions.put(name, action);
		if(logger != null) logger.info("Watching", name);
	}
	
	private void removeAction(Action a) {
		if(!a.isManaged()) return;
		String key = actions.findKey(a, false);
		actions.remove(key);
		if(logger != null) logger.debug("Stopped Watching", key);
	}

}
