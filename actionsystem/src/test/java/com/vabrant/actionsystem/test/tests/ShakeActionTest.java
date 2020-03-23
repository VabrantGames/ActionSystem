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
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.ShakeAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class ShakeActionTest extends ActionSystemTestListener {

	private ShapeRenderer renderer;
	private TestObjectController testObjectController;
	
	public ShakeActionTest() {
		testObjectController = TestObjectController.getInstance();
	}
	
	@Override
	public void create() {
		super.create();
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
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
				shakeXTest();
				break;
			case Keys.NUMPAD_1:
				shakeYTest();
				break;
			case Keys.NUMPAD_2:
				shakeAngleTest();
				break;
			case Keys.NUMPAD_3:
				shakeTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void shakeXTest() {
		TestObject object = getTestObject();
		ShakeAction action = ShakeAction.shakeX(object, 5, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void shakeYTest() {
		TestObject object = getTestObject();
		ShakeAction action = ShakeAction.shakeY(object, 5, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void shakeAngleTest() {
		TestObject object = getTestObject();
		ShakeAction action = ShakeAction.shakeAngle(object, 0.2f, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void shakeTest() {
		TestObject object = getTestObject();
		ShakeAction action = ShakeAction.shake(object, 5, 2, 0.2f, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObjectController.draw(shapeDrawer);
	}
}
