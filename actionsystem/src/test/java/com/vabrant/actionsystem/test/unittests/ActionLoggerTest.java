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

import org.junit.BeforeClass;
import org.junit.Test;

import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.test.TestUtils;

/**
 * @author John Barton
 *
 */
public class ActionLoggerTest {
	
	private static ActionLogger logger;
	
	@BeforeClass
	public static void init() {
		ActionLogger.useSysOut();
		logger = ActionLogger.getLogger(ActionLoggerTest.class, "MyLogger", ActionLogger.DEBUG);
	}
	
	@Test 
	public void basicTest() {
		TestUtils.printTestHeader("Basic Test");
		logger.info("Info", "Hello");
		logger.debug("Debug", "World");
		logger.error("Error", "Goodbye");
	}
	
	@Test
	public void resetTest() {
		TestUtils.printTestHeader("Reset Test");
		logger.info("Before Reset");
		logger.reset();
		
		logger.setLevel(ActionLogger.DEBUG);
		logger.info("After Reset");
		
		init();
	}
	
	@Test
	public void soloTest() {
		TestUtils.printTestHeader("Solo Test");
		
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
