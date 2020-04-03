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
package com.vabrant.actionsystem.test.tests;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.test.tests.TestActions.TestAction;

/**
 * @author John Barton
 *
 */
public class ActionManagerTest {

	private static ActionManager manager;
	
	@BeforeClass
	public static void init(){
		manager = new ActionManager();
		ActionLogger.useSysOut();
	}
	
	public void printTestHeader(String name) {
    	System.out.println();
    	String pattern = "//----------//";
    	System.out.println(pattern + ' ' + name + ' ' + pattern);
    }
	
	@Test
	public void basicTest() {
		printTestHeader("Basic Test");
		
		TestAction action = TestAction.obtain()
				.setName("BasicTest")
				.setLogLevel(ActionLogger.DEBUG);
		
		//Adds the action to the manager
		//Makes this action the root action
		//Starts the action
		manager.addAction(action);
		
		assertTrue(action.isRunning());

		//Called every frame
		manager.update(0);
		
		action.end();

		//Last update
		manager.update(0);
	}
	
}
