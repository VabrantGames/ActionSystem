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
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.test.TestUtils;
import org.junit.rules.TestName;

/**
 * @author John Barton
 *
 */
public class ActionLoggerTest {

	@Rule
	public TestName testName = new TestName();
	private static Application application;
	private static ActionLogger logger;
	
	@BeforeClass
	public static void init() {
		application = new HeadlessApplication(new ApplicationAdapter() {
		});
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		logger = ActionLogger.getLogger(ActionLoggerTest.class, "MyLogger", ActionLogger.DEBUG);
	}
	
	@Test 
	public void basicTest() {
		TestUtils.printTestHeader(testName.getMethodName());
		logger.info("Info", "Hello");
		logger.debug("Debug", "World");
		logger.error("Error", "Goodbye");
	}
	
	@Test
	public void resetTest() {
		TestUtils.printTestHeader(testName.getMethodName());
		logger.info("Before Reset");
		logger.reset();
		
		logger.setLevel(ActionLogger.DEBUG);
		logger.info("After Reset");

		logger = ActionLogger.getLogger(ActionLoggerTest.class, "MyLogger", ActionLogger.DEBUG);
	}
	
	@Test
	public void soloTest() {
		TestUtils.printTestHeader(testName.getMethodName());
		
		ActionLogger soloLogger = ActionLogger.getLogger(ActionLoggerTest.class, "Solo", ActionLogger.DEBUG);
		
		logger.info("Hello");
		soloLogger.info("Hello");
		
		soloLogger.solo(true);
		
		logger.info("Should not be logged");
		soloLogger.info("Solo me!!");
		
		soloLogger.solo(false);
		
		logger.info("Was i muted?");
		
	}

}
