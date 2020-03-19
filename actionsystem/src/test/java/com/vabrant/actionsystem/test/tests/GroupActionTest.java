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

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class GroupActionTest extends ActionSystemTestListener {
	
	private TestObjectController testObjectController;
	
	public GroupActionTest() {
		testObjectController = TestObjectController.getInstance();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_1:
				parallelTest();
				break;
			case Keys.NUMPAD_2:
				parallelWithOffsetTest();
				break;
			case Keys.NUMPAD_3:
				sequenceTest();
				break;
			case Keys.NUMPAD_4:
				restartTest();
				break;
			case Keys.NUMPAD_5:
				nestedTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void parallelTest() {
		TestObject testObject = testObjectController.create();
		testObjectController.center(testObject, viewport);
		
		GroupAction parallel = GroupAction.parallel(
				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.exp5Out),
				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.exp5Out))
		.setName("Parallel")
		.setLogLevel(ActionLogger.DEBUG)
		.addListener(testObject);
		
		actionManager.addAction(parallel);
	}
	
	public void parallelWithOffsetTest() {
		TestObject testObject = testObjectController.create();
		testObjectController.center(testObject, viewport);
		
		GroupAction parallelWithOffset = GroupAction.parallel(
				0.5f,
				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear),
				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.linear))
		.addListener(testObject);
		
		actionManager.addAction(parallelWithOffset);
	}
	
	public void sequenceTest() {
		TestObject testObject = testObjectController.create();
		testObjectController.center(testObject, viewport);
		
		GroupAction sequence = GroupAction.sequence(
				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.exp5Out),
				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.exp5Out),
				MoveAction.moveXBy(testObject, -50, 0.5f, Interpolation.exp5Out))
		.setName("Sequence")
		.setLogLevel(ActionLogger.DEBUG)
		.addListener(testObject);
		
		actionManager.addAction(sequence);
	}
	
	public void restartTest() {
		TestObject testObject = testObjectController.create();
		testObjectController.center(testObject, viewport);
		
		GroupAction sequence = GroupAction.sequence(
				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.exp5Out),
				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.exp5Out),
				MoveAction.moveXBy(testObject, -50, 0.5f, Interpolation.exp5Out))
		.setName("Sequence")
		.addListener(testObject)
		.setLogLevel(ActionLogger.DEBUG);
		
		ActionListener<MoveAction> restartListener = new ActionAdapter<MoveAction>() {
			boolean restart = true;
			
			@Override
			public void actionEnd(MoveAction a) {
				if(restart) {
					restart = false;
					sequence.restart();
				}
			}
		};

		((MoveAction)sequence.getActions().get(1)).addListener(restartListener);
		
		actionManager.addAction(sequence);
	}
	
	public void nestedTest() {
		TestObject ob1 = testObjectController.create();
		testObjectController.center(ob1, viewport);
		ob1.setX(ob1.getX() + 25 + 10);
		
		TestObject ob2 = testObjectController.create();
		testObjectController.center(ob2, viewport);
		ob2.setX(ob2.getX() - 25 - 10);
		
		GroupAction one = GroupAction.parallel(
				MoveAction.moveXBy(ob1, 50, 1f, Interpolation.exp5Out),
				MoveAction.moveYBy(ob1, 50, 1f, Interpolation.exp5Out));
		
		GroupAction two = GroupAction.parallel(
				MoveAction.moveXBy(ob2, -50, 1f, Interpolation.exp5Out),
				MoveAction.moveYBy(ob2, 50, 1f, Interpolation.exp5Out));
		
		GroupAction group = GroupAction.parallel(one, two);
		group.addListener(ob1);
		group.addListener(ob2);
		
		actionManager.addAction(group);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObjectController.draw(shapeDrawer);
	}
	
}
