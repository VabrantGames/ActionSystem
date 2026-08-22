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
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.logger.ActionLoggerManager;
import com.vabrant.actionsystem.test.TestUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/** @author John Barton */
public class ActionLoggerManagerTest {

	@Rule public TestName testName = new TestName();

	private static Application application;

	@BeforeClass
	public static void init () {
		application = new HeadlessApplication(new ApplicationAdapter() {});
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	@Test
	public void basicTest () {
		TestUtils.printTestHeader(testName.getMethodName());

		class Action1 {}
		class Action2 {}

		ActionLogger action1Logger = ActionLoggerManager.getLogger(Action1.class);
		ActionLogger action2Logger = ActionLoggerManager.getLogger(Action2.class);

		ActionLoggerManager.print(ActionLogger.LOGGER_INFO, ActionLogger.LOGGER_INFO, "customAction1", action1Logger, "Header", "Body", null);
		ActionLoggerManager.print(ActionLogger.LOGGER_DEBUG, ActionLogger.LOGGER_DEBUG, "customAction1", action1Logger, "Header", "Body", null);
		ActionLoggerManager.print(ActionLogger.LOGGER_ERROR, ActionLogger.LOGGER_ERROR, "customAction1", action1Logger, "Header", "Body", null);

		ActionLoggerManager.print(ActionLogger.LOGGER_INFO, ActionLogger.LOGGER_INFO, "customAction2", action2Logger, "Header", "Body", null);
		ActionLoggerManager.print(ActionLogger.LOGGER_DEBUG, ActionLogger.LOGGER_DEBUG, "customAction2", action2Logger, "Header", "Body", null);
		ActionLoggerManager.print(ActionLogger.LOGGER_ERROR, ActionLogger.LOGGER_ERROR, "customAction2", action2Logger, "Header", "Body", null);
	}

	@Test
	public void soloTest () {
		TestUtils.printTestHeader(testName.getMethodName());

		MockActions.MockAction action = MockActions.MockAction.obtain();
		action.setLogLevel(ActionLogger.LOGGER_DEBUG);

		MockActions.MockAction soloAction = MockActions.MockAction.obtain();
		soloAction.setLogLevel(ActionLogger.LOGGER_DEBUG);

		// Should print an error message since no action name was set.
		ActionLoggerManager.solo(soloAction);

		// Setup the solo action
		soloAction.setName("SoloAction");
		ActionLoggerManager.solo(soloAction);

		//Should not print
		action.getLogger().info(action, "Hello");

		soloAction.getLogger().info(soloAction, "World");
	}
}
