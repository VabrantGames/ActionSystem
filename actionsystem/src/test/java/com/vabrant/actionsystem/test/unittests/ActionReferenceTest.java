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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.vabrant.actionsystem.actions.Action;
import org.junit.BeforeClass;
import org.junit.Test;

import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.ActionReference;
import com.vabrant.actionsystem.test.unittests.MockActions.*;

import static org.junit.Assert.*;

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
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
	}

	@Test
	public void basicTest() {
		//Action to reference
		MockAction action = MockAction.obtain();
		
		//Reference to the action. Likely a global var that different methods or whatever can access
		ActionReference<MockAction> ref = new ActionReference<MockAction>();
		ref.setAction(action);

		assertNotNull(ref.getAction());

		action.reset();

		assertNull(ref.getAction());
	}

	@Test
	public void  changeActionTest() {
		MockAction action1 = MockAction.obtain();
		MockAction action2 = MockAction.obtain();
		ActionReference<MockAction> ref = new ActionReference<MockAction>();

		ref.setAction(action1);
		Action oldAction = ref.setAction(action2);

		assertEquals(ref.getAction(), action2);
		assertEquals(oldAction, action1);

		ref.setAction(null);

		assertNull(ref.getAction());
	}
	
//	@Test
	public void cleanupTest() {
		MockAction action = MockAction.obtain();
		ActionReference<MockAction> ref = new ActionReference<MockAction>(action);
		ActionPools.free(action);
		assertNull(ref.getAction());
	}
}
