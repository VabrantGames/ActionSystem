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
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.logger.ActionLogger;

public class ActionPools {
	
	private static final ObjectMap<Class<?>, Pool<?>> pools = new ObjectMap<>();
	public static final ActionLogger logger = ActionLogger.getLogger(ActionPools.class, ActionLogger.LogLevel.NONE);

	/**
	 * Where you want the pool to be filled to.
	 * @param type
	 * @param amount
	 */
	public static <T> void fill(Class<T> type, int amount) {
		if(amount == 0) throw new IllegalArgumentException("Fill amount can't be 0.");
		
		Pool<T> pool = get(type);
		
		if(pool == null) throw new IllegalArgumentException("Pool of type " + type.getSimpleName() + " doesn't exist.");

		//Can't add if the pool if already full
		if(pool.getFree() == pool.max) {
			if(logger != null) logger.info(type.getSimpleName() + " pool is full.");
			return;
		}
		
		//Calculate how many actions to add
		int amountToAdd = (pool.getFree() + amount) < pool.max ? amount : pool.max - pool.getFree();
		
		if(logger != null) logger.debug("AmountToAdd", Integer.toString(amountToAdd));

		for(int i = 0; i < amountToAdd; i++) {
			try {
				T t = ClassReflection.newInstance(type);
//				((Action)t).canReset = true;
				pool.free(t);
			} 
			catch(ReflectionException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static<T extends Action<?>> void drain(Class<T> type, int amount) {
	}

	public static <T> boolean exists(Class<T> type) {
		return pools.containsKey(type);
	}

	public static <T> Pool<T> create(Class<T> type) {
		return create(type, false);
	}

	public static <T> Pool<T> create(Class<T> type, boolean fill) {
		return create(type, 10, Integer.MAX_VALUE, fill);
	}

	public static <T> Pool<T> create(Class<T> type, int initialCapacity, int max, boolean fill) {
		if (exists(type)) return get(type);

		Pool<T> pool = new ReflectionPool<T>(type, initialCapacity, max);
		pools.put(type, pool);

		if (fill) fill(type, initialCapacity);
		return pool;
	}
	
	public static <T> Pool<T> get(Class<T> type){
		Pool pool = pools.get(type);
		if (pool == null) {
			pool = create(type, 10, Integer.MAX_VALUE, false);
		}
		return pool;
	}

	public static <T> T obtain(Class<T> type) {
		return get(type).obtain();
	}

	public static void free(Object object) {
		if (object == null) throw new IllegalArgumentException("Object is null.");

		if (object instanceof Action) {
			Action<?> action = (Action<?>) object;

			if (action.inUse()) throw new IllegalArgumentException("Action can not be freed while in use.");
			if (action.hasBeenPooled()) return;

			if (action.isManaged()) {
				Pool pool = action.getPool();

				if(pool == null) {
					pool = ActionPools.get(action.getClass());
				}

				pool.free(action);
				action.setPooled(true);

				logger.info("Pooled" + action.getLogger().getActionName(), action.getLogger().getClassName());
			}
			else {
				action.unmanagedReset();
			}
		}
		else {
			Pool pool = pools.get(object.getClass());
			if (pool != null){
				pool.free(object);
			}
		}
	}

	public static <T> void freeAll(Array<T> objects) {
		if (objects == null) throw new IllegalArgumentException("Array is null");

		for (int i = 0, s = objects.size; i < s; i++) {
			free(objects.get(i));
		}
	}

	public static void freeAll(Action<?>[] actions) {
		if(actions == null) throw new IllegalArgumentException("Array is null");

		for(int i = 0; i < actions.length; i++) {
			free(actions[i]);
		}
	}
	
	private static void freeChildActions(Action<?> action) {
		if(action instanceof SingleParentAction) {
			freeSingleParentAction(action);
		}
		else if(action instanceof MultiParentAction) {
			freeMultiParentAction(action);
		}
	}
	
	/**
	 * Frees a child action of a {@link SingleParentAction}. Also frees any children it may have.
	 * @param action Parent action.
	 */
	private static void freeSingleParentAction(Action<?> action) {
		Action<?> child = ((SingleParentAction)action).getAction();
		if(child == null) return;
		freeChildActions(child);
		freeAction(child);
	}
	
	/**
	 * Frees multiple child actions of a {@link MultiParentAction}. Also frees any children it may have.
	 * @param action
	 */
	private static void freeMultiParentAction(Action<?> action) {
		Array<Action<?>> children = ((MultiParentAction)action).getActions();
		for(int i = children.size - 1; i >= 0; i--) {
			Action<?> child = children.pop();
			if(child == null) continue;
			freeChildActions(child);
			freeAction(child);
		}
	}
	
	/**
	 * Frees an action. 
	 */
	private static <T extends Action<?>> void freeAction(T action) {
		if (action.hasBeenPooled()) return;

		if (action.isManaged()) {
			Pool pool = action.getPool();

			if(pool == null) {
				pool = ActionPools.get(action.getClass());
			}
			
			pool.free(action);
			action.setPooled(true);
		}
		else {
			action.unmanagedReset();
		}

		logger.info("Pooled" + action.getLogger().getActionName(), action.getLogger().getClassName());
	}

}
