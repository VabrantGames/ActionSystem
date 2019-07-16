package com.vabrant.actionsystem;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

/**
 * @author John Barton
 * @author Nathan Sweet
 */
public class Pools {
	
	private static final ObjectMap<Class<?>, Pool<?>> pools = new ObjectMap<>();
	
	public static <T> Pool<T> get(Class<T> type, int max){
		Pool pool = pools.get(type);
		if(pool == null) {
			pool = new ReflectionPool<T>(type, 4, max);
			pools.put(type, pool);
		}
		return pool;
	}
	
	public static <T> void add(Class<T> type, Pool<T> pool) {
		pools.put(type, pool);
	}
	
	public static <T> boolean exists(Class<T> type) {
		return pools.containsKey(type);
	}
	
	public static <T> Pool<T> createPool(Class<T> type, int initialCapacity, int max) {
		Pool<T> pool = new ReflectionPool<T>(type, initialCapacity, max);
		pools.put(type, pool);
		return pool;
	}
	
	public static <T> Pool<T> get(Class<T> type){
		return get(type, 100);
	}
	
	public static <T> T obtain(Class<T> type) {
		T object = get(type).obtain();
		if(object instanceof Action) ((Action)object).hasBeenPooled = false;
		return object;
	}
	
	public static void free(Object object) {
		if(object == null) throw new IllegalArgumentException("Object is null.");
		Pool pool = pools.get(object.getClass());
		if(pool == null) return;
		if(object instanceof Action) {
			((Action)object).hasBeenPooled = true;
		}
		pool.free(object);
	}
	
	public static void freeAll(Array objects) {
		freeAll(objects, false);
	}
	
	public static void freeAll(Array objects, boolean samePool) {
		if (objects == null) throw new IllegalArgumentException("Objects cannot be null.");
		Pool pool = null;
		for (int i = 0, n = objects.size; i < n; i++) {
			Object object = objects.get(i);
			if (object == null) continue;
			if (pool == null) {
				pool = pools.get(object.getClass());
				if (pool == null) continue; // Ignore freeing an object that was never retained.
			}
			pool.free(object);
			if (!samePool) pool = null;
		}
	}
	
	public static void freeAction(Action action) {
		if(action == null) throw new IllegalArgumentException("Action is null");
		if(action instanceof GroupAction) {
			freeGroup((GroupAction)action);
		}
		else if(action instanceof RepeatAction){
			freeRepeat((RepeatAction)action);
		}
		free(action);
	}
	
	private static void freeGroup(GroupAction group) {
		Array<Action> actions = group.getActions();
		for(int i = 0; i < actions.size; i++) {
			Action action = actions.get(i);
			if(action instanceof GroupAction) freeGroup((GroupAction)action);
			else if(action instanceof RepeatAction) freeRepeat((RepeatAction)action);
			free(action);
		}
	}
	
	private static void freeRepeat(RepeatAction repeat) {
		Action action = repeat.getRepeatAction();
		if(action.hasBeenPooled) return;
		if(action instanceof GroupAction) freeGroup((GroupAction)action);
		else if(action instanceof RepeatAction) freeRepeat((RepeatAction)action);
		free(action);
	}

}
