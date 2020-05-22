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
package com.vabrant.actionsystem.test.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.ActionReference;
import com.vabrant.actionsystem.actions.MoveAction;

/**
 * @author John Barton
 *
 */
public class ActionReferenceTest {
	
	@BeforeClass
	public static void init() {
		ActionLogger.useSysOut();
	}

	@Test
	public void basicTest() {
		//Original move action
		MoveAction move = MoveAction.obtain();
		
		//Reference to the move action. Likely a global var that different method can or whatever can access 
		ActionReference<MoveAction> ref = new ActionReference<MoveAction>();
		ref.setAction(move);
		
		//Some method, sub, super class, etc that uses the reference
		
		if(ref != null) {
			MoveAction m = ref.getAction();
			m.moveBy(0, 0);
			//etc...
		}
		
		assertNotNull(ref.getAction());
	}
	
	@Test
	public void cleanupTest() {
		MoveAction move = MoveAction.obtain();
		
		ActionReference<MoveAction> ref = new ActionReference<MoveAction>(move);
		ref.throwException(true);
		
		ActionPools.free(move);
		
		assertNull(ref.getAction());
		
	}
}
