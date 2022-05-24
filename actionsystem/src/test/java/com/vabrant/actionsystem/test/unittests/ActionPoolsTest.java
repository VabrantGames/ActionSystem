/**
 *	Copyright 2020 See AUTHORS file.
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.test.unittests;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.*;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.Event;
import com.vabrant.actionsystem.events.EventListener;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.badlogic.gdx.utils.Pool;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.test.tests.ActionSystemTestListener;
import com.vabrant.actionsystem.test.unittests.MockActions.MockMultiParentAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockSingleParentAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockAction;

import static org.junit.Assert.*;

/**
 * @author John Barton
 *
 */
public class ActionPoolsTest extends ActionSystemTestListener {

	private static Application application;
	
	@BeforeClass
	public static void init() {
		application = new HeadlessApplication(new ApplicationAdapter() {
		});
		ActionPools.logger.setLevel(ActionLogger.LogLevel.DEBUG);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	public boolean hasBeenPooled(Action<?> action) {
		try {
			Method m = ClassReflection.getDeclaredMethod(Action.class, "hasBeenPooled");
			m.setAccessible(true);
			return (boolean) m.invoke(action);
		}
		catch(ReflectionException e) {
			e.printStackTrace();
			System.exit(0);
		}

		throw new RuntimeException("I did something wrong");
	}

	public void printTestHeader(String name) {
		System.out.println();
		String pattern = "//----------//";
		System.out.println(pattern + ' ' + name + ' ' + pattern);
    }

	@Test
	public void createPoolTest() {
		Pool<MockAction> pool = ActionPools.create(MockAction.class, 4, 10, false);
		assertNotNull(pool);
		assertTrue(ActionPools.exists(MockAction.class));
	}

	@Test
	public void getPoolTest() {
		Pool<?> pool = ActionPools.get(MockAction.class);
		assertNotNull(pool);
		assertNotNull(ActionPools.get(MockAction.class));
	}

	@Test
	@Ignore
	public void freeTest() {
		//Free Action
		MockAction action = MockAction.obtain();
		ActionPools.free(action);
		assertTrue(hasBeenPooled(action));

		//Free non action
		ActionPools.create(TestClass.class);
		TestClass testClass = new TestClass();
		ActionPools.free(testClass);
		assertTrue(testClass.pooled);
	}

	@Test
	public void freeAllTest() {
		Array<MockAction> actions = new Array<>(3);
		for (int i = 0; i < 3; i++) {
			actions.add(MockAction.obtain());
		}

		ActionPools.freeAll(actions);
		for (int i = actions.size - 1; i >=0; i--) {
			Action a = actions.removeIndex(i);
			assertTrue(hasBeenPooled(a));
		}

		for (int i = 0; i < 3; i++) {
			actions.add(MockAction.obtain());
		}

		ActionPools.freeAll(actions.items);
		for (int i = actions.size - 1; i >= 0; i--) {
			Action a = actions.removeIndex(i);
			assertTrue(hasBeenPooled(a));
		}
	}

	@Test
	public void fillTest() {
		final int fillAmount = 10;
		int expected = ActionPools.get(MockAction.class).getFree() + fillAmount;
		ActionPools.fill(MockAction.class, 10);
		assertEquals(expected, ActionPools.get(MockAction.class).getFree());
	}

	@Test
	public void childFreeTest() {
		final boolean[] result = new boolean[6];

		class PooledListener implements EventListener {

			private int idx;

			PooledListener(int idx) {
				this.idx = idx;
			}

			@Override
			public void onEvent(Event e) {
				result[idx] = true;
			}
		}


		int idx = 0;
		MockMultiParentAction root = MockMultiParentAction.obtain();
		root.subscribeToEvent(ActionEvent.CLEANUP_EVENT, new PooledListener(idx++));

		MockSingleParentAction child1 = MockSingleParentAction.obtain();
		child1.subscribeToEvent(ActionEvent.CLEANUP_EVENT, new PooledListener(idx++));
		child1.set(MockAction.obtain()
				.subscribeToEvent(ActionEvent.CLEANUP_EVENT, new PooledListener(idx++)));
		root.add(child1);

		MockMultiParentAction child2 = MockMultiParentAction.obtain();
		child2.subscribeToEvent(ActionEvent.CLEANUP_EVENT, new PooledListener(idx++));
		child2.add(MockAction.obtain()
				.subscribeToEvent(ActionEvent.CLEANUP_EVENT, new PooledListener(idx++)));
		child2.add(MockAction.obtain()
				.subscribeToEvent(ActionEvent.CLEANUP_EVENT, new PooledListener(idx++)));
		root.add(child2);

		ActionPools.free(root);

		assertArrayEquals(new boolean[]{true, true, true, true, true, true}, result);
	}

	private static class TestClass implements Pool.Poolable {
		boolean pooled;

		@Override
		public void reset() {
			pooled = true;
		}
	}

}
