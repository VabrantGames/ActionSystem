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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.Rotatable;
import com.vabrant.actionsystem.actions.RotateAction;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class RotateActionTest extends ActionSystemTestListener {

	private float x;
	private float y;
	private final int size = 100;
	private ShapeRenderer renderer;
	private TestClass testClass;
	
	@Override
	public void create() {
		super.create();
		testClass = new TestClass(); 
		renderer = new ShapeRenderer();
		renderer.setAutoShapeType(true);
		shapeDrawer.setDefaultSnap(false);
		shapeDrawer.setPixelSize(2);
	}

	public void reset() {
		testClass.useDeg(true);
		testClass.setRotation(0);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		x = (viewport.getWorldWidth() - size) / 2;
		y = (viewport.getWorldHeight() - size) / 2;
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
			case Keys.NUMPAD_3:
				capRadTest();
				break;
			case Keys.NUMPAD_4:
				capDegTest();
				break;
		}
		return super.keyDown(keycode);
	}

	public void rotateToTest() {
		reset();
		actionManager.addAction(RotateAction.rotateTo(testClass, 90, 1f, Interpolation.linear));
	}
	
	public void rotateByTest() {
		reset();
		actionManager.addAction(RotateAction.rotateBy(testClass, -180, 1f, Interpolation.linear));
	}
	
	public void rotateByFromEndTest() {
		reset();
		RotateAction action = RotateAction.rotateBy(testClass, 45, 1f, Interpolation.linear)
				.startRotateByFromEnd()
				.setLogLevel(ActionLogger.DEBUG);
		
		GroupAction sequence = GroupAction.sequence(
				action,
				DelayAction.delay(0.1f));
		
		actionManager.addAction(RepeatAction.repeat(sequence, 2));
	}
	
	public void capRadTest() {
		reset();
		testClass.useDeg(false);
		RotateAction action = RotateAction.rotateBy(testClass, MathUtils.PI2 * 3, 5f, Interpolation.fade)
				.capEndBetweenRevolutionRad()
				.addListener(new ActionAdapter<RotateAction>() {
					@Override
					public void actionEnd(RotateAction a) {
						System.out.println(testClass.getRotation());
					}
				});
		actionManager.addAction(action);
	}
	
	public void capDegTest() {
		reset();
		RotateAction action = RotateAction.rotateBy(testClass, 360 * 3, 5f, Interpolation.fade)
				.capEndBetweenRevolutionDeg()
				.addListener(new ActionAdapter<RotateAction>() {
					@Override
					public void actionEnd(RotateAction a) {
						System.out.println(testClass.getRotation());
					}
				});
		actionManager.addAction(action);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		batch.end();
		renderer.begin();
		
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		renderer.rect(x, y, size / 2, size / 2, size, size, 1, 1, testClass.getRotationDeg());
		
		renderer.end();
		batch.begin();
	}
	
	private class TestClass implements Rotatable {
		
		private boolean useDeg;
		private float rotation;
		
		public void useDeg(boolean useDeg) {
			this.useDeg = useDeg;
		}

		@Override
		public void setRotation(float rotation) {
			this.rotation = rotation;
		}

		@Override
		public float getRotation() {
			return rotation;
		}
		
		public float getRotationDeg() {
			return useDeg ? rotation : rotation * MathUtils.radiansToDegrees;
		}
		
	}
}
