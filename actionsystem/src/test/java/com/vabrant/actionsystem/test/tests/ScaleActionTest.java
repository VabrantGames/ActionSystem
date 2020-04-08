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
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RunnableAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class ScaleActionTest extends ActionSystemTestListener {

	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private float amountX;
	private float amountY;
	private TestObject testObject;
	private ShapeRenderer renderer;
	private ActionListener<ScaleAction> listener;
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		shapeDrawer.setDefaultSnap(false);
		shapeDrawer.setPixelSize(2);
		listener = createListener();
		reset();
	}

	private void reset() {
		testObject.setScaleX(1);
		testObject.setScaleY(1);
		TestObjectController.getInstance().center(testObject, viewport);
	}

	private ActionListener<ScaleAction> createListener(){
		return new ActionAdapter<ScaleAction>() {
			@Override
			public void actionEnd(ScaleAction a) {
				System.out.println();
				System.out.println(a.getName());
				System.out.println("StartX: " + startX);
				System.out.println("StartY: " + startY);
				System.out.println("EndX: " + endX);
				System.out.println("EndY: " + endY);
				System.out.println("X: " + testObject.getScaleX());
				System.out.println("Y: " + testObject.getScaleY());
			}
		};
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
		reset();
		
		amountX = 0.5f;
		startX = testObject.getScaleX();
		endX = startX + amountX;
		startY = endY = testObject.getScaleY();
		
		actionManager.addAction(
				ScaleAction.scaleXBy(testObject, amountX, 0.5f, Interpolation.linear)
				.setName("ScaleXBy")
				.addListener(listener));
	}

	public void scaleYByTest() {
		reset();
		
		amountY = -0.5f;
		startY = testObject.getScaleY();
		endY = startY + amountY;
		startX = endX = testObject.getScaleX();
		
		actionManager.addAction(
				ScaleAction.scaleYBy(testObject, amountY, 0.5f, Interpolation.linear)
				.setName("ScaleYBy")
				.addListener(listener));
	}
	
	public void scaleXToTest() {
		reset();
		
		startX = testObject.getScaleX();
		endX = 2;
		endY = startY = testObject.getScaleY();
		
		actionManager.addAction(
				ScaleAction.scaleXTo(testObject, endX, 0.5f, Interpolation.linear)
				.setName("ScaleXTo")
				.addListener(listener));
	}
	
	public void scaleYToTest() {
		reset();
		
		startY = testObject.getScaleY();
		endY = 3;
		endX = startX = testObject.getScaleY();
		
		actionManager.addAction(
				ScaleAction.scaleYTo(testObject, endY, 0.5f, Interpolation.linear)
				.setName("ScaleYTo")
				.addListener(listener));
	}

	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		batch.end();
		
		renderer.begin();
		testObject.draw(renderer);
		renderer.end();
		
		batch.begin();
	}
	
}
