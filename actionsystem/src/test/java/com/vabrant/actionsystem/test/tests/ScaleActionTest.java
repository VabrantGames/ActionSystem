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
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class ScaleActionTest extends ActionSystemTestListener {

	private TestObjectController testObjectController;
	
	public ScaleActionTest() {
		testObjectController = TestObjectController.getInstance();
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
				scaleXByTest();
				break;
			case Keys.NUMPAD_1:
				scaleYByTest();
				break;
			case Keys.NUMPAD_2:
				scaleXToTest();
				break;
			case Keys.NUMPAD_3:
				scaleYToTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void scaleXByTest() {
		TestObject object = getTestObject();
		ScaleAction action = ScaleAction.scaleXBy(object, -0.5f, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void scaleYByTest() {
		TestObject object = getTestObject();
		ScaleAction action = ScaleAction.scaleYBy(object, 0.5f, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void scaleXToTest() {
		TestObject object = getTestObject();
		ScaleAction action = ScaleAction.scaleXTo(object, 2, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void scaleYToTest() {
		TestObject object = getTestObject();
		ScaleAction action = ScaleAction.scaleYTo(object, 2, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObjectController.draw(shapeDrawer);
	}
	
}
