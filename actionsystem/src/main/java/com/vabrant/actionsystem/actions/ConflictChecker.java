package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entries;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class ConflictChecker extends ActionAdapter{
	
	private final ActionLogger logger;
	
	public enum ConflictActionType{
		KILL_OLD,
		KILL_NEW,
		END_OLD,
		START_WHEN_OLD_FINISHES
	}

	private final ObjectMap<Class<?>, ConflictActionType> conflicts;
	private final ObjectMap<Action, Class<?>> runningActions;
	
	public ConflictChecker() {
		this(2);
	}
	
	public ConflictChecker(int size) {
		conflicts = new ObjectMap<>(size);
		runningActions = new ObjectMap<>(size);
		logger = ActionLogger.getLogger(ConflictChecker.class, ActionLogger.DEBUG);
	}
	
	public ActionLogger getLogger() {
		return logger;
	}
	
	public void watch(Class c, ConflictActionType type) {
		conflicts.put(c, type);
		if(logger != null) logger.debug("Watching for conflicts of type", c.getSimpleName());
	}
	
	private void addAction(Action action) {
		if(runningActions.containsKey(action)) return;
		action.addListener(this);
		runningActions.put(action, action.getClass());
		if(logger != null) logger.info("Watching" + action.getLogger().getActionName(), action.getLogger().getClassName());
	}
	
	private void removeAction(Action action) {
		runningActions.remove(action);
		if(logger != null) logger.debug("No longer watching" + action.getLogger().getActionName(), action.getLogger().getClassName());
	}

	public boolean checkForConflict(Action newAction) {
		Entries<Action, Class<?>> running = runningActions.entries();
		
		//if the class isn't being watched there are no conflicts
		if(!conflicts.containsKey(newAction.getClass())) return false;
		
		ConflictActionType type = conflicts.get(newAction.getClass());
		
		while(running.hasNext()) {
			Entry<Action, Class<?>> entry = running.next();
			Action oldAction = entry.key;
			
			if(oldAction.equals(newAction)) continue;
			
			if(oldAction.hasConflict(newAction)) {
				if(logger != null) logger.debug("Conflict" + oldAction.getLogger().getActionName() + newAction.getLogger().getActionName(), type.toString());
				doConflictAction(type, oldAction, newAction);
				return true;
			}
		}
		addAction(newAction);
		return false;
	}
	
	private void doConflictAction(ConflictActionType type, Action oldAction, Action newAction) {
		switch(type) {
			case KILL_NEW:
				newAction.forceKill();
				break;
			case KILL_OLD:
				oldAction.kill();
				break;
		}
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
