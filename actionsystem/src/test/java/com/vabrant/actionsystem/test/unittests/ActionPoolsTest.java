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

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.utils.Pool;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.tests.ActionSystemTestListener;
import com.vabrant.actionsystem.test.unittests.MockActions.MockMultiParentAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockSingleParentAction;
import com.vabrant.actionsystem.test.unittests.MockActions.MockAction;

/**
 * @author John Barton
 *
 */
public class ActionPoolsTest extends ActionSystemTestListener {
	
	@BeforeClass
	public static void init() {
		ActionLogger.useSysOut();
		ActionPools.logger.setLevel(ActionLogger.DEBUG);
	}

	public void printTestHeader(String name) {
		System.out.println();
		String pattern = "//----------//";
		System.out.println(pattern + ' ' + name + ' ' + pattern);
    }
	
	@Test
	public void freeTest() {
		printTestHeader("Free Test");
		
		MockAction action = MockAction.obtain()
				.setName("Free")
				.setLogLevel(ActionLogger.DEBUG);
		
		ActionPools.free(action);
	}
	
//	@Test 
	public void freeSingeParentActionTest() {
		printTestHeader("Free Single Parent Test");
		
		MockAction child = MockAction.obtain()
				.setName("Child")
				.setLogLevel(ActionLogger.DEBUG);
		
		MockSingleParentAction parent = MockSingleParentAction.obtain()
				.set(child)
				.setName("Parent")
				.setLogLevel(ActionLogger.DEBUG);
		
		ActionPools.free(parent);
	}
	
	@Test
	public void freeMultiParentActionTest() {
		printTestHeader("Free MultiParent Action Test");
		
		MockAction child1 = MockAction.obtain()
				.setName("Child1")
				.setLogLevel(ActionLogger.DEBUG);
		
		MockAction child2 = MockAction.obtain()
				.setName("Child2")
				.setLogLevel(ActionLogger.DEBUG);
				
		MockMultiParentAction parent = MockMultiParentAction.obtain()
				.add(child1)
				.add(child2)
				.setName("Parent")
				.setLogLevel(ActionLogger.DEBUG);
		
		ActionPools.free(parent);
	}
	
//	@Test
	public void complexParentActionTest() {
		printTestHeader("Complex Parent Action Test");
		
		MockAction child1_P1 = MockAction.obtain()
				.setName("Child1_P1")
				.setLogLevel(ActionLogger.DEBUG);
		
		MockAction child2_P1 = MockAction.obtain()
				.setName("Child2_P1")
				.setLogLevel(ActionLogger.DEBUG);
				
		MockMultiParentAction parent1 = MockMultiParentAction.obtain()
				.add(child1_P1)
				.add(child2_P1)
				.setName("Parent1")
				.setLogLevel(ActionLogger.DEBUG);
		
		MockAction child1_P2 = MockAction.obtain()
				.setName("Child1_P2")
				.setLogLevel(ActionLogger.DEBUG);
		
		MockAction child2_P2 = MockAction.obtain()
				.setName("Child2_P2")
				.setLogLevel(ActionLogger.DEBUG);
				
		MockMultiParentAction parent2 = MockMultiParentAction.obtain()
				.add(child1_P2)
				.add(child2_P2)
				.add(parent1)
				.setName("Parent2")
				.setLogLevel(ActionLogger.DEBUG);
		
		ActionPools.free(parent2);
	}
	
	@Test
	public void createPoolTest() {
		printTestHeader("Create Pool Test");
		Pool<MoveAction> pool = ActionPools.create(MoveAction.class, 4, 10, true);
		
		assertTrue("Fill amount is incorrect", pool.getFree() == 4);
	}

}
