/**
 *	Copyright 2019 John Barton
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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.badlogic.gdx.utils.reflect.ReflectionException;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.actionsystem.test.tests.TestActions.TestAction;

/**
 * @author John Barton
 *
 */
public class ActionManagerTest {

	@Rule
	public TestName testName = new TestName();
	
	@BeforeClass
	public static void init(){
		ActionLogger.useSysOut();
	}
	
	public void printTestHeader(String name) {
    	System.out.println();
    	String pattern = "//----------//";
    	System.out.println(pattern + ' ' + name + ' ' + pattern);
    }
	
	public boolean hasBeenPooled(Action<?> action) {
		try {
			Method m = ClassReflection.getDeclaredMethod(Action.class, "hasBeenPooled");
			m.setAccessible(true);
			return (boolean)m.invoke(action);
		}
		catch(ReflectionException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		return false;
	}
	
	@Test
	public void basicTest() {
		printTestHeader(testName.getMethodName());
		
		ActionManager manager = new ActionManager();
		
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
		
		assertTrue(hasBeenPooled(action));
	}
	
	@Test
	public void freeAllTest() {
		printTestHeader(testName.getMethodName());
		
		ActionManager manager = new ActionManager();
		
		final int amount = 10;
		
		//Add actions to manager
		for(int i = 0; i < amount; i++) {
			TestAction action = TestAction.obtain()
					.setName(Integer.toString(i))
					.setLogLevel(ActionLogger.INFO);
			manager.addAction(action);
		}
		
		//Mock cycle update
		manager.update(0);

		manager.freeAll(true);
	}
	
}
