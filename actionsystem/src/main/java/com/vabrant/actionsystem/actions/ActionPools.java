package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;

public class ActionPools {
	
	private static final int defaultPoolMaxCapacity = 100;
	private static final ObjectMap<Class<?>, Pool<?>> pools = new ObjectMap<>();
	public static final ActionLogger logger = ActionLogger.getLogger(ActionPools.class, ActionLogger.NONE);
	
	//DEBUG remove
	static {
		logger.setLevel(ActionLogger.DEBUG);
	}

	/**
	 * Where you want the pool to be filled to.
	 * @param type
	 * @param amount
	 */
	public static <T extends Action> void fill(Class<T> type, int amount) {
		if(amount == 0) throw new IllegalArgumentException("Fill amount can't be 0.");
		
		Pool<T> pool = get(type);
		if(logger != null) logger.debug("AmountToAdd", Integer.toString(amount - pool.getFree()));
		if(amount > pool.max) throw new IllegalArgumentException("Fill amount is greater than the Pool max.");
		if(pool.getFree() >= amount) return;
		
		for(int i = pool.getFree(); i < amount; i++) {
			try {
				T t = ClassReflection.newInstance(type);
				pool.free(t);
			} catch(ReflectionException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static <T extends Action> void add(Class<T> type, Pool<T> pool) {
		pools.put(type, pool);
	}
	
	public static <T> boolean exists(Class<T> type) {
		return pools.containsKey(type);
	}
	
	public static <T extends Action> Pool<T> create(Class<T> type, int initialCapacity, int max) {
		if(exists(type)) return get(type); 
		
		Pool<T> pool = new ReflectionPool<T>(type, initialCapacity, max);
		pools.put(type, pool);
		return pool;
	}
	
	public static <T extends Action> Pool<T> get(Class<T> type){
		Pool pool = pools.get(type);
		if(pool == null) pool = create(type, 4, defaultPoolMaxCapacity);
		return pool;
	}
	
	public static <T extends Action> T obtain(Class<T> type) {
		T action = get(type).obtain();
		action.setPooled(false);
		return action;
	}

	public static void free(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		if(action instanceof GroupAction) {
			freeGroup((GroupAction)action);
		}
		else if(action instanceof RepeatAction){
			freeRepeat((RepeatAction)action);
		}
		freeAction(action);
	}
	
	private static void freeAction(Action<?> action) {
		if(action.hasBeenPooled() || !action.isManaged()) return;
		
		if(action.getPreActions().size > 0) {
			for(int i = action.getPreActions().size - 1; i >= 0; i--) {
				free(action.getPreActions().pop());
			}
		}
		
		if(action.getPostActions().size > 0) {
			for(int i = action.getPostActions().size - 1; i >= 0; i--) {
				free(action.getPostActions().pop());
			}
		}
		
		
 		Pool pool = pools.get(action.getClass());
		if(pool == null) return;
		action.setPooled(true);
		if(logger != null) logger.info("Pooled" + action.getLogger().getActionName(), action.getLogger().getClassName());
		pool.free(action);
	}
	
	private static void freeGroup(GroupAction group) {
		Array<Action> actions = group.getActions();
		for(int i = 0; i < actions.size; i++) {
			Action action = actions.get(i);
			if(action instanceof GroupAction) freeGroup((GroupAction)action);
			else if(action instanceof RepeatAction) freeRepeat((RepeatAction)action);
			freeAction(action);
		}
	}
	
	private static void freeRepeat(RepeatAction repeat) {
		Action action = repeat.getRepeatAction();
		if(action instanceof GroupAction) freeGroup((GroupAction)action);
		else if(action instanceof RepeatAction) freeRepeat((RepeatAction)action);
		freeAction(action);
	}

}
