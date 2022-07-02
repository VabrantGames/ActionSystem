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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.*;
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.test.TestObject;
import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class RotateActionTest extends ActionSystemTestListener {
	
	private TestObject testObject;

	private LabelTextFieldFloatWidget rotationStartWidget;
	private LabelTextFieldFloatWidget rotationEndWidget;
	private LabelTextFieldFloatWidget amountWidget;
	private LabelTextFieldFloatWidget durationWidget;
	
	private DoubleLabelWidget targetEndRotationWidget;
	private DoubleLabelWidget currentRotationWidget;

	private ActionListener endListener = new ActionListener() {
		@Override
		public void onEvent(ActionEvent e) {
			currentRotationWidget.setValue(testObject.getRotation());
		}
	};
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		testObject.setSize(200);
		testObject.useDeg(false);
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		LabelStyle blackTextStyle = new LabelStyle(skin.get(LabelStyle.class));
		blackTextStyle.fontColor = Color.BLACK;
		
		Label valuesLabel = new Label("Values", blackTextStyle);
		root.add(valuesLabel).left();
		root.row();
		
		rotationStartWidget = new LabelTextFieldFloatWidget("start: ", skin, root, 0);
		rotationStartWidget.allowNegativeValues();
		rotationEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
		rotationEndWidget.allowNegativeValues();
		root.row();
		amountWidget = new LabelTextFieldFloatWidget("amount: ", skin, root, 1f);
		amountWidget.allowNegativeValues();
		root.row();
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1f);
		root.row();
		
		Label metricsLabel = new Label("Metrics", blackTextStyle);
		root.add(metricsLabel).left().padTop(10);
		root.row();
		targetEndRotationWidget = new DoubleLabelWidget("TargetEndRotation: ", skin, root);
		root.row();
		currentRotationWidget = new DoubleLabelWidget("CurrentRotation: ", skin, root);
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("RotateBy") {
			@Override
			public Action<?> run() {
				//Set start rotation
				testObject.setRotation(rotationStartWidget.getValue());

				float end = testObject.getRotation() + amountWidget.getValue();
				
				//Set test end value
				targetEndRotationWidget.setValue(end);
				
				return RotateAction.rotateBy(testObject, amountWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.subscribeToEvent(ActionEvent.END_EVENT, endListener);
			}
		});
		
		addTest(new ActionTest("RotateTo") {
			@Override
			public Action<?> run() {
				//Set start rotation
				testObject.setRotation(rotationStartWidget.getValue());

				//Set test end value
				targetEndRotationWidget.setValue(rotationEndWidget.getValue());
				
				return RotateAction.rotateTo(testObject, rotationEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.subscribeToEvent(ActionEvent.END_EVENT, endListener);
			}
		});
		
		addTest(new ActionTest("SetRotation") {
			@Override
			public Action<?> run() {
				//Set start rotation
				testObject.setRotation(rotationStartWidget.getValue());

				//Set test end value
				targetEndRotationWidget.setValue(rotationEndWidget.getValue());
				
				return RotateAction.setRotation(testObject, rotationEndWidget.getValue());
			}
		});
		
		addTest(new ActionTest("RestartTest (Custom)") {
			@Override
			public Action<?> run() {
				//Set start rotation
				testObject.setRotation(0);
				
				//Set test end value
				targetEndRotationWidget.setValue(-1);
				
				GroupAction group = GroupAction.sequence(
						RotateAction.rotateTo(testObject, 225, 1f, Interpolation.linear), 
						RotateAction.rotateBy(testObject, 45, 1f, Interpolation.linear));

				ActionListener restartListener = new ActionListener() {
					boolean restart = true;

					@Override
					public void onEvent(ActionEvent e) {
						if (restart) {
							System.out.println(testObject.getRotation());
							restart = false;
							group.restart();
						}
					}
				};
				((RotateAction)group.getActions().first()).subscribeToEvent(ActionEvent.END_EVENT, restartListener);

				return group;
			}
		});
		
		addTest(new ActionTest("RotateByFromEnd (Custom)") {
			@Override
			public Action<?> run() {
				//Set start rotation
				testObject.setRotation(0);
				
				//Set test end value
				targetEndRotationWidget.setValue(-1);
				
				GroupAction group = GroupAction.sequence(
						RotateAction.rotateBy(testObject, 45, 0.5f, Interpolation.bounceOut).startRotateByFromEnd(), 
						DelayAction.delay(0.2f));
				
				return RepeatAction.repeat(group, 3);
			}
		});
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float x = (width - testObject.getWidth()) / 2;
		float y = (height - testObject.getHeight()) / 2;
		testObject.setPosition(x, y);
	}

	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
