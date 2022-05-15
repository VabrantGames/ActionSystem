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
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.Movable;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.test.TestUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class RestartTest extends ActionSystemTestListener {
	
	private final int radius = 30;
	private Circle[] circles = new Circle[3];
	
	@Override
	public void create() {
		super.create();
		
		shapeDrawer.setColor(Color.BLACK);

		for(int i = 0; i < circles.length; i++) {
			circles[i] = new Circle();
		}
		reset();
	}
	
	public void reset() {
		int x = radius;
		float y = (TestUtils.DEFAULT_HEIGHT - (radius * 2)) / 2;
		for (int i = 0; i < circles.length; i++) {
			Circle c = circles[i];
			c.draw = true;
			c.setX(x);
			x += (radius * 2);			
			c.setY(y);
		}
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				multipleObjectsTest();
				break;
			case Keys.NUMPAD_1:
				singleObjectsTest();
		}
		return super.keyDown(keycode);
	}
	
	public void singleObjectsTest() {
		TestUtils.printTestHeader("Single Objects Test");
		reset();
		
		circles[1].draw = false;
		circles[2].draw = false;
		
		GroupAction sequence = GroupAction.sequence(
				MoveAction.moveXBy(circles[0], 100, 0.5f, Interpolation.linear)
				.setName("Right").setLogLevel(ActionLogger.LogLevel.DEBUG),
				MoveAction.moveYBy(circles[0], 100, 0.5f, Interpolation.linear)
				.setName("Up").setLogLevel(ActionLogger.LogLevel.DEBUG),
				MoveAction.moveXBy(circles[0], -100, 0.5f, Interpolation.linear)
				.setName("Left").setLogLevel(ActionLogger.LogLevel.DEBUG),
				DelayAction.delay(0.1f));

		ActionListener restartListener = new ActionListener() {
			boolean restart = true;

			@Override
			public void onEvent(ActionEvent e) {
				if (!restart) return;
				restart = false;
				System.out.println("Restart GroupAction");
				sequence.restart();
			}
		};
		((MoveAction)sequence.getActions().get(2)).subscribeToEvent(ActionEvent.END_EVENT, restartListener);

		actionManager.addAction(sequence);
	}
	
	public void multipleObjectsTest() {
		TestUtils.printTestHeader("Multiple Objects Test");
		
		reset();
		
		GroupAction sequence = GroupAction.sequence(
				MoveAction.moveXBy(circles[2], 200, 0.5f, Interpolation.linear)
				.setName("Three").setLogLevel(ActionLogger.LogLevel.DEBUG),
				MoveAction.moveXBy(circles[1], 200, 0.5f, Interpolation.linear)
				.setName("Two").setLogLevel(ActionLogger.LogLevel.DEBUG),
				MoveAction.moveXBy(circles[0], 200, 0.5f, Interpolation.linear)
				.setName("One").setLogLevel(ActionLogger.LogLevel.DEBUG),
				DelayAction.delay(0.1f));

		ActionListener restartListener = new ActionListener() {
			boolean restart = true;

			@Override
			public void onEvent(ActionEvent e) {
				if (!restart) return;
				restart = false;
				System.out.println("Restart GroupAction");
				sequence.restart();
			}
		};
		((MoveAction)sequence.getActions().get(2)).subscribeToEvent(ActionEvent.END_EVENT, restartListener);

		actionManager.addAction(sequence);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		for(int i = 0; i < circles.length; i++) {
			circles[i].draw(shapeDrawer, radius);
		}
	}
	
	private static class Circle implements Movable {
		
		private boolean draw = true;
		private float x;
		private float y;

		@Override
		public void setX(float x) {
			this.x = x;
		}

		@Override
		public void setY(float y) {
			this.y = y;
		}

		@Override
		public float getX() {
			return x;
		}

		@Override
		public float getY() {
			return y;
		}
		
		public void draw(ShapeDrawer shapeDrawer, float radius) {
			if(!draw) return;
			shapeDrawer.filledCircle(x, y, radius);
		}
		
	}

}
