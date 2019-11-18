package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.ObjectMap;

public class ActionWatcher extends ActionAdapter {

	private final ActionLogger logger;
	private final ObjectMap<String, Action> actions;
	
	public ActionWatcher() {
		this(3);
	}
	
	public ActionWatcher(int size) {
		actions = new ObjectMap<>(size);
		logger = ActionLogger.getLogger(ActionWatcher.class, ActionLogger.NONE);
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
		action.addLibraryListener(this);
		actions.put(name, action);
		if(logger != null) logger.info("Watching", name);
	}
	
	private void removeAction(Action a) {
		if(!a.isManaged()) return;
		String key = actions.findKey(a, false);
		actions.remove(key);
		if(logger != null) logger.debug("Stopped Watching", key);
	}
	
	@Override
	public void actionKill(Action a) {
		removeAction(a);
	}
	
	@Override
	public void actionComplete(Action a) {
		removeAction(a);
	}

}
