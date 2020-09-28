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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestConstantsAndUtils;
import com.vabrant.actionsystem.test.TestObject;

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
	private ActionListener<ScaleAction> listener;
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		listener = createListener();
		reset();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		TestObjectController.getInstance().center(testObject, hudViewport);
	}

	@Override
	public void reset() {
		testObject.setScaleX(1);
		testObject.setScaleY(1);
	}

	private ActionListener<ScaleAction> createListener(){
		return new ActionAdapter<ScaleAction>() {
			@Override
			public void actionEnd(ScaleAction a) {
				ActionSystemTestConstantsAndUtils.printTestHeader(a.getName());
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
			case Keys.NUMPAD_4:
				pingPongTest();
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
	
	public void pingPongTest() {
		reset();
		
		actionManager.addAction(
				RepeatAction.continuous(
						ScaleAction.scaleXTo(testObject, 2, 0.25f, Interpolation.sineOut)) 
				.pingPong(true));
		
//		actionManager.addAction(
//				RepeatAction.repeat(
//						ScaleAction.scaleYTo(testObject, 2, 1f, Interpolation.linear),
//						4) 
//				.pingPong(true));
		
	}
	
	@Override
	public void drawWithShapeRenderer(ShapeRenderer renderer) {
		testObject.draw(renderer);
	}

}
