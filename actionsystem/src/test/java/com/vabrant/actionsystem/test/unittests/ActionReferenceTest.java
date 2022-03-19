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
package com.vabrant.actionsystem.test.unittests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.ActionReference;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.unittests.MockActions.*;

/**
 * @author John Barton
 *
 */
public class ActionReferenceTest {

	private static Application application;
	
	@BeforeClass
	public static void init() {
		application = new HeadlessApplication(new ApplicationAdapter() {
		});
	}

	@Test
	public void basicTest() {
		MockAction action = MockAction.obtain();
		
		//Reference to the action. Likely a global var that different methods or whatever can access
		ActionReference<MockAction> ref = new ActionReference<MockAction>();
		ref.setAction(action);
		
		//MockAction m = ref.getAction();
		//m.doSomething();

		assertNotNull(ref.getAction());
	}
	
	@Test
	public void cleanupTest() {
		MockAction action = MockAction.obtain();
		ActionReference<MockAction> ref = new ActionReference<MockAction>(action);
		ActionPools.free(action);
		assertNull(ref.getAction());
	}
}
