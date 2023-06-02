/**
 * Copyright 2019 John Barton
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.*;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.test.TestUtils;
import com.vabrant.actionsystem.test.unittests.MockActions.*;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/** @author John Barton */
public class ActionManagerTest {

	@Rule public TestName testName = new TestName();

	private ActionManager actionManager;
	private static Application application;

	@BeforeClass
	public static void init () {
		application = new HeadlessApplication(new ApplicationAdapter() {});
	}

	public boolean hasBeenPooled (Action<?> action) {
		try {
			Method m = ClassReflection.getDeclaredMethod(Action.class, "hasBeenPooled");
			m.setAccessible(true);
			return (boolean)m.invoke(action);
		} catch (ReflectionException e) {
			e.printStackTrace();
			System.exit(0);
		}

		return false;
	}

	@Test
	public void basicTest () {
		TestUtils.printTestHeader(testName.getMethodName());

		ActionManager manager = new ActionManager();

		MockAction a1 = MockAction.obtain();
		a1.setCustomUpdateCode(new Runnable() {
			@Override
			public void run () {
				a1.end();
			}
		});

		MockSingleParentAction a2 = MockSingleParentAction.obtain();
		MockAction a2Child = MockAction.obtain();
		a2Child.setCustomUpdateCode(new Runnable() {
			@Override
			public void run () {
				a2Child.end();
			}
		});
		a2.set(a2Child);

		// Adds the action to the manager
		// Makes this action the root action
		// Starts the action
		manager.addAction(a1);
		manager.addAction(a2);

		assertTrue(a1.isRunning());
		assertTrue(a2.isRunning());
		assertTrue(a2.getAction().isRunning());

		// End the action by passing in a large amount of time
		// The TimeAction should end when the timer is greater than the duration
		// The ActionManager should pool this action when it has been ended
		manager.update(Float.MAX_VALUE);

		assertFalse(a1.isRunning());
		assertFalse(a2.isRunning());
		assertFalse(a2Child.isRunning());

		assertTrue(hasBeenPooled(a1));
		assertTrue(hasBeenPooled(a2));
		assertTrue(hasBeenPooled(a2Child));
	}

	@Test
	public void freeAllTest () {
		TestUtils.printTestHeader(testName.getMethodName());

		final int amount = 10;

		Action[] actions = new Action[amount];
		ActionManager manager = new ActionManager(amount);
		manager.getLogger().setLevel(ActionLogger.LogLevel.INFO);

		// Add actions to manager
		for (int i = 0; i < amount; i++) {
			MockAction action = MockAction.obtain().setName(Integer.toString(i)).setLogLevel(ActionLogger.LogLevel.DEBUG);
			manager.addAction(action);
			actions[i] = action;
		}

		// Mock cycle update
		manager.update(Integer.MAX_VALUE);

		manager.freeAll(true);
	}
}
