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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ZoomAction;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.test.TestObject;

/**
 * @author John Barton
 *
 */
public class ZoomActionTest extends ActionSystemTestListener {
	
	private LabelTextFieldFloatWidget startWidget;
	private LabelTextFieldFloatWidget endWidget;
	private LabelTextFieldFloatWidget amountWidget;
	private LabelTextFieldFloatWidget durationWidget;
	private LabelCheckBoxWidget reverseBackToStartWidget;
	private LabelCheckBoxWidget reverseWidget;
	
	private DoubleLabelWidget testZoomEndWidget;
	private DoubleLabelWidget currentZoomWidget;
	
	private TestObject testObject;

	private ActionListener metricsListener = new ActionListener() {
		@Override
		public void onEvent(ActionEvent e) {
			currentZoomWidget.setValue(testObject.getZoom());
		}
	};
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		testObject.setSize(100);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float x = (width - testObject.getWidth()) * 0.5f;
		float y = (height - testObject.getHeight()) * 0.5f;
		testObject.setPosition(x, y);
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		LabelStyle headerStyle = new LabelStyle(skin.get(LabelStyle.class));
		headerStyle.fontColor = Color.BLACK;
		
		Label valuesLabel = new Label("Values", headerStyle);
		root.add(valuesLabel).left();
		root.row();
		startWidget = new LabelTextFieldFloatWidget("start: ", skin, root, 1f);
		root.row();
		endWidget = new LabelTextFieldFloatWidget("end: ", skin, root, 1f);
		root.row();
		amountWidget = new LabelTextFieldFloatWidget("amount: ", skin, root, 1f);
		root.row();
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1f);
		root.row();
		reverseBackToStartWidget = new LabelCheckBoxWidget("reverseBackToStart: ", skin, root);
		root.row();
		reverseWidget = new LabelCheckBoxWidget("reverse: ", skin, root);
		root.row();
		
		Label metricsLabel = new Label("Metrics", headerStyle);
		root.add(metricsLabel).left().padTop(10);
		root.row();
		testZoomEndWidget = new DoubleLabelWidget("testScaleEnd: ", skin, root);
		root.row();
		currentZoomWidget = new DoubleLabelWidget("currentScale: ", skin, root);
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("ZoomTo") {
			@Override
			public Action<?> run() {
				testObject.setZoom(startWidget.getValue());
				
				testZoomEndWidget.setValue(endWidget.getValue());
				
				return ZoomAction.zoomTo(testObject, endWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear)
						.subscribeToEvent(ActionEvent.END_EVENT, metricsListener)
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});

		addTest(new ActionTest("ZoomBy") {
			@Override
			public Action<?> run() {
				testObject.setZoom(startWidget.getValue());
				
				testZoomEndWidget.setValue(testObject.getZoom() + amountWidget.getValue());
				
				return ZoomAction.zoomBy(testObject, amountWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear)
						.subscribeToEvent(ActionEvent.END_EVENT, metricsListener)
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("Blank") {
			@Override
			public Action<?> run() {
				return null;
			}
		});
	}
	
//	public void reset() {
//		testObject.setZoom(1);
//		TestObjectController.getInstance().center(testObject, hudViewport);
//	}

//	private ActionListener createListener() {
//		return new ActionAdapter() {
//			@Override
//			public void actionEnd(Action a) {
//				System.out.println();
//				System.out.println(a.getName());
//				System.out.println("StartZoom: " + startZoom);
//				System.out.println("EndX: " + endZoom);
//				System.out.println("X: " + testObject.getZoom());
//			}
//		};
//	}
//	
//	@Override
//	public boolean keyDown(int keycode) {
//		switch(keycode) {
//			case Keys.NUMPAD_0:
//				zoomByTest();
//				break;
//			case Keys.NUMPAD_1:
//				zoomToTest();
//				break;
//			case Keys.NUMPAD_2:
//				startFromEndTest();
//				break;
//		}
//		return super.keyDown(keycode);
//	}
//	
//	public void zoomByTest() {
//		reset();
//		
//		amount = 0.5f;
//		startZoom = testObject.getZoom();
//		endZoom = startZoom + amount;
//		
//		actionManager.addAction(
//				ZoomAction.zoomBy(testObject, amount, 1f, Interpolation.linear)
//				.setName("ZoomBy")
//				.addListener(listener));
//	}
//	
//	public void zoomToTest() {
//		reset();
//		
//		startZoom = testObject.getZoom();
//		endZoom = 2;
//		
//		actionManager.addAction(
//				ZoomAction.zoomTo(testObject, endZoom, 1f, Interpolation.linear)
//				.setName("ZoomTo")
//				.addListener(listener));
//	}
//	
//	public void startFromEndTest() {
//		reset();
//		
//		int repeatAmount = 2;
//		
//		amount = 0.5f;
//		startZoom = testObject.getZoom();
//		endZoom = startZoom + (amount * (repeatAmount + 1));
//		
//		actionManager.addAction(
//				RepeatAction.repeat(
//						ZoomAction.zoomBy(testObject, amount, 1f, Interpolation.linear)
//						.startZoomByFromEnd(),
//						repeatAmount)
//				.setName("StartFromEnd")
//				.addListener(listener));
//	}
//	
	@Override
	public void drawWithShapeRenderer(ShapeRenderer renderer) {
		testObject.draw(renderer);
	}

}
