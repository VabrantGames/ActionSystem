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
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.logger.Logger;
import com.vabrant.actionsystem.logger.LoggerManager;
import com.vabrant.actionsystem.logger.LoggerPrinter;
import com.vabrant.actionsystem.test.TestUtils;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.util.logging.Level;

/** @author John Barton */
public class ActionLoggerTest {

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

		// ActionLogger logger = ActionLogger.getLogger(ActionLoggerTest.class, testName.getMethodName(),
		// ActionLogger.LogLevel.DEBUG);
		Logger logger = LoggerManager.getLogger(ActionLoggerTest.class);
//		logger.setLevel(ActionLogger.LogLevel.DEBUG);

//		logger.info("Hello");
//		logger.info("Hello", "Info");
//		logger.debug("Hello", "Debug");
//		logger.error("Hello", "Error");
		LoggerManager.print(Logger.INFO, Logger.INFO, "Action", logger, "Header", "Body", null);
		LoggerManager.print(Logger.DEBUG, Logger.DEBUG, "Action", logger, "Header", "Body", null);
		LoggerManager.print(Logger.ERROR, Logger.ERROR, "Action", logger, "Header", "Body", null);
	}

//	@Test
//	public void levelTest () {
//		TestUtils.printTestHeader(testName.getMethodName());
//
//		ActionLogger logger = ActionLogger.getLogger(ActionLoggerTest.class);
//
//		logger.setLevel(ActionLogger.LogLevel.NONE);
//		logger.info("Hello");
//		logger.debug("Hello");
//		logger.error("Hello");
//
//		logger.setLevel(ActionLogger.LogLevel.ERROR);
//		logger.print("Level", "Error");
//		logger.info("Don't print info");
//		logger.debug("Don't print debug");
//
//		logger.setLevel(ActionLogger.LogLevel.INFO);
//		logger.print("Level", "Info");
//		logger.debug("Don't print debug");
//
//		logger.setLevel(ActionLogger.LogLevel.DEBUG);
//		logger.print("Level", "Debug");
//		logger.info("Print info");
//		logger.error("Print error");
//		logger.debug("Print debug");
//	}

	@Test
	public void soloTest () {
		TestUtils.printTestHeader(testName.getMethodName());

		class BasicClass {
		}

		class SoloClass {
		}

		MockActions.MockAction action = MockActions.MockAction.obtain();
		MockActions.MockAction soloAction = MockActions.MockAction.obtain();
		action.setLogLevel(Logger.DEBUG);
		soloAction.setLogLevel(Logger.DEBUG);

		// Should print an error message since no action name was set.
		LoggerManager.solo(soloAction);

		soloAction.setName("SoloAction");
		LoggerManager.solo(soloAction);

		//Should not print
		action.getLogger().info(action, "Hello");

		soloAction.getLogger().info(soloAction, "World");



//		ActionLogger logger1 = ActionLogger.getLogger(ActionLoggerTest.class, "logger1", ActionLogger.LogLevel.DEBUG);
//		ActionLogger logger2 = ActionLogger.getLogger(ActionLoggerTest.class, "logger2", ActionLogger.LogLevel.DEBUG);
//
//		LoggerPrinter errorPrinter = new LoggerPrinter() {
//			@Override
//			public void print (ActionLogger logger, String message, String body, ActionLogger.LogLevel level) {
//				throw new RuntimeException("This logger should not print");
//			}
//		};
//
//		logger1.info("Hello", "logger1");
//		logger2.info("Hello", "logger2");
//
//		logger1.solo(true);
//
//		logger2.setPrinter(errorPrinter);
//		logger2.info("Hello", "World");
//		logger2.setPrinter(null);
//
//		logger2.solo(true);
//		logger1.setPrinter(errorPrinter);
//		logger1.info("Hello", "World");
//
//		logger2.solo(false);
	}
}
