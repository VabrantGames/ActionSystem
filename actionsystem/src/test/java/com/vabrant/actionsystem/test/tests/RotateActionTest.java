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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.Rotatable;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class RotateActionTest extends ActionSystemTestListener {

//	private TestClass testClass;
	
	private TestObject testObject;

	private LabelTextFieldFloatWidget rotationStartWidget;
	private LabelTextFieldFloatWidget rotationEndWidget;
	private LabelTextFieldFloatWidget amountWidget;
	private LabelTextFieldFloatWidget durationWidget;
	
	private DoubleLabelWidget targetEndRotationWidget;
	private DoubleLabelWidget currentRotationWidget;

	private ActionListener<RotateAction> endListener = new ActionAdapter<RotateAction>() {
		@Override
		public void actionEnd(RotateAction a) {
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
		
		rotationStartWidget = new LabelTextFieldFloatWidget("rotation: ", skin, root, 0);
		rotationStartWidget.setAllowNegativeValues(true);
		rotationEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
		rotationEndWidget.setAllowNegativeValues(true);
		root.row();
		amountWidget = new LabelTextFieldFloatWidget("amount: ", skin, root, 1f);
		amountWidget.setAllowNegativeValues(true);
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
						.addListener(endListener);
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
						.addListener(endListener);
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
		
		addTest(new ActionTest("RestartTest") {
			@Override
			public Action<?> run() {
				//Set start rotation
				testObject.setRotation(0);
				
				//Set test end value
				targetEndRotationWidget.setValue(-1);
				
				GroupAction group = GroupAction.sequence(
						RotateAction.rotateTo(testObject, 225, 1f, Interpolation.linear), 
						RotateAction.rotateBy(testObject, 45, 1f, Interpolation.linear));
				
				ActionListener<RotateAction> restartListener = new ActionAdapter<RotateAction>() {
					boolean restart = true;
					
					@Override
					public void actionEnd(RotateAction a) {
						if(restart) {
							System.out.println(testObject.getRotation());
							restart = false;
							group.restart();
						}
					}
				};
				
				((RotateAction)group.getActions().first()).addListener(restartListener);
				
				return group;
			}
		});
		
		addTest(new ActionTest("RotateByFromEnd") {
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

	public void rotateByFromEndTest() {
		reset();
		RotateAction action = RotateAction.rotateBy(testObject, 45, 1f, Interpolation.linear)
				.startRotateByFromEnd()
				.setLogLevel(ActionLogger.DEBUG);
		
		GroupAction sequence = GroupAction.sequence(
				action,
				DelayAction.delay(0.1f));
		
		actionManager.addAction(RepeatAction.repeat(sequence, 2));
	}
	
	public void capRadTest() {
		reset();
		testObject.useDeg(false);
		RotateAction action = RotateAction.rotateBy(testObject, MathUtils.PI2 * 3, 5f, Interpolation.fade)
				.capEndBetweenRevolutionRad()
				.addListener(new ActionAdapter<RotateAction>() {
					@Override
					public void actionEnd(RotateAction a) {
						System.out.println(testObject.getRotation());
					}
				});
		actionManager.addAction(action);
	}
	
	public void capDegTest() {
		reset();
		RotateAction action = RotateAction.rotateBy(testObject, 360 * 3, 5f, Interpolation.fade)
				.capEndBetweenRevolutionDeg()
				.addListener(new ActionAdapter<RotateAction>() {
					@Override
					public void actionEnd(RotateAction a) {
						System.out.println(testObject.getRotation());
					}
				});
		actionManager.addAction(action);
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
