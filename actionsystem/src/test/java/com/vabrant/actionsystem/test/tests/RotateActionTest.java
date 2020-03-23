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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class RotateActionTest extends ActionSystemTestListener {

	private ShapeRenderer renderer;
	private TestObjectController testObjectController;
	
	public RotateActionTest() {
		testObjectController = TestObjectController.getInstance();
	}
	
	@Override
	public void create() {
		super.create();
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		shapeDrawer.setDefaultSnap(false);
		shapeDrawer.setPixelSize(2);
	}
	
	private TestObject getTestObject() {
		TestObject object = testObjectController.create();
		object.setSize(100, 100);
		testObjectController.center(object, viewport);
		return object;
	}
	
	private GroupAction wrap(Action<?> a) {
		return GroupAction.sequence(
				DelayAction.delay(0.5f),
				a,
				DelayAction.delay(1f));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				rotateToTest();
				break;
			case Keys.NUMPAD_1:
				rotateByTest();
				break;
			case Keys.NUMPAD_2:
				rotateByFromEndTest();
				break;
		}
		return super.keyDown(keycode);
	}

	public void rotateToTest() {
		TestObject object = getTestObject();
		RotateAction action = RotateAction.rotateTo(object, 90, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void rotateByTest() {
		TestObject object = getTestObject();
		RotateAction action = RotateAction.rotateBy(object, -180, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void rotateByFromEndTest() {
		TestObject object = getTestObject();
		RotateAction action = RotateAction.rotateBy(object, 45, 1f, Interpolation.linear)
				.restartRotateByFromEnd()
				.setLogLevel(ActionLogger.DEBUG);
		
		GroupAction sequence = GroupAction.sequence(
				action,
				DelayAction.delay(0.1f),
				action);
		
//		RepeatAction repeat = RepeatAction.repeat(action, 3);
		GroupAction wrap = wrap(sequence);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		batch.end();
		renderer.begin();
		testObjectController.draw(renderer);
		renderer.end();
		batch.begin();
	}
}
