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
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ZoomAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class ZoomActionTest extends ActionSystemTestListener {

	private float startZoom;
	private float endZoom;
	private float amount;
	private TestObject testObject;
	private ActionListener listener;
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		listener = createListener();
		reset();
	}
	
	public void reset() {
		testObject.setZoom(1);
		TestObjectController.getInstance().center(testObject, hudViewport);
	}

	private ActionListener createListener() {
		return new ActionAdapter() {
			@Override
			public void actionEnd(Action a) {
				System.out.println();
				System.out.println(a.getName());
				System.out.println("StartZoom: " + startZoom);
				System.out.println("EndX: " + endZoom);
				System.out.println("X: " + testObject.getZoom());
			}
		};
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				zoomByTest();
				break;
			case Keys.NUMPAD_1:
				zoomToTest();
				break;
			case Keys.NUMPAD_2:
				startFromEndTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void zoomByTest() {
		reset();
		
		amount = 0.5f;
		startZoom = testObject.getZoom();
		endZoom = startZoom + amount;
		
		actionManager.addAction(
				ZoomAction.zoomBy(testObject, amount, 1f, Interpolation.linear)
				.setName("ZoomBy")
				.addListener(listener));
	}
	
	public void zoomToTest() {
		reset();
		
		startZoom = testObject.getZoom();
		endZoom = 2;
		
		actionManager.addAction(
				ZoomAction.zoomTo(testObject, endZoom, 1f, Interpolation.linear)
				.setName("ZoomTo")
				.addListener(listener));
	}
	
	public void startFromEndTest() {
		reset();
		
		int repeatAmount = 2;
		
		amount = 0.5f;
		startZoom = testObject.getZoom();
		endZoom = startZoom + (amount * (repeatAmount + 1));
		
		actionManager.addAction(
				RepeatAction.repeat(
						ZoomAction.zoomBy(testObject, amount, 1f, Interpolation.linear)
						.startZoomByFromEnd(),
						repeatAmount)
				.setName("StartFromEnd")
				.addListener(listener));
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
