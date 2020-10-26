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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestConstantsAndUtils;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 */
public class ScaleActionTest extends ActionSystemTestListener {

	private TestObject testObject;
	
	private LabelTextFieldFloatWidget xStartWidget;
	private LabelTextFieldFloatWidget xEndWidget;
	private LabelTextFieldFloatWidget yStartWidget;
	private LabelTextFieldFloatWidget yEndWidget;
	private LabelTextFieldFloatWidget xAmountWidget;
	private LabelTextFieldFloatWidget yAmountWidget;
	private LabelTextFieldFloatWidget durationWidget;
	
	private DoubleLabelWidget shouldEndXWidget;
	private DoubleLabelWidget shouldEndYWidget;
	private DoubleLabelWidget currentXWidget;
	private DoubleLabelWidget currentYWidget;
	
	private ShapeRenderer shapeRenderer;
	
	private ActionListener<ScaleAction> metricsListener = new ActionAdapter<ScaleAction>() {
		public void actionEnd(ScaleAction a) {
			currentXWidget.setValue(testObject.getScaleX());
			currentYWidget.setValue(testObject.getScaleY());
		}
	};
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float x = (hudViewport.getWorldWidth() - testObject.width) / 2;
		float y = (hudViewport.getWorldHeight() - testObject.height) / 2;
		testObject.setPosition(x, y);
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		LabelStyle labelHeaderStyle = new LabelStyle(skin.get(LabelStyle.class));
		labelHeaderStyle.fontColor = Color.BLACK;
		
		Label label = new Label("Set Values", labelHeaderStyle);
		root.add(label).left();
		root.row();
		
		//Set Values
		xStartWidget = new LabelTextFieldFloatWidget("x: ", skin, root, 1);
		xStartWidget.setAllowNegativeValues(true);
		xEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
		xEndWidget.setAllowNegativeValues(true);
		root.row();
		
		yStartWidget = new LabelTextFieldFloatWidget("y: ", skin, root, 1);
		yStartWidget.setAllowNegativeValues(true);
		yEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
		yEndWidget.setAllowNegativeValues(true);
		root.row();
		
		xAmountWidget = new LabelTextFieldFloatWidget("amount: ", skin, root, 1);
		yAmountWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
		root.row();
		
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1);
		root.row();
		
		//Metrics
		Label metricsLabel = new Label("Metrics", labelHeaderStyle);
		root.add(metricsLabel).left();
		root.row();
		
		shouldEndXWidget = new DoubleLabelWidget("TestEndX: ", skin, root);
		root.row();
		
		shouldEndYWidget = new DoubleLabelWidget("TestEndY: ", skin, root);
		root.row();
		
		currentXWidget = new DoubleLabelWidget("CurrentX: ", skin, root);
		root.row();
		
		currentYWidget = new DoubleLabelWidget("CurrentY: ", skin, root);
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("ScaleXBy") {
			@Override
			public Action<?> run() {
				testObject.setScaleX(xStartWidget.getValue());
				testObject.setScaleY(yStartWidget.getValue());
				
				shouldEndXWidget.setValue(xStartWidget.getValue() + xAmountWidget.getValue());
				shouldEndYWidget.setValue(yStartWidget.getValue());
				
				return ScaleAction.scaleXBy(testObject, xAmountWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(metricsListener);
			}
		});
		
		addTest(new ActionTest("ScaleYBy") {
			@Override
			public Action<?> run() {
				testObject.setScaleX(xStartWidget.getValue());
				testObject.setScaleY(yStartWidget.getValue());
				
				shouldEndXWidget.setValue(xStartWidget.getValue());
				shouldEndYWidget.setValue(yStartWidget.getValue() + yAmountWidget.getValue());
				
				return ScaleAction.scaleYBy(testObject, yAmountWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(metricsListener);
			}
		});
		
		addTest(new ActionTest("ScaleXTo") {
			@Override
			public Action<?> run() {
				testObject.setScaleX(xStartWidget.getValue());
				testObject.setScaleY(yStartWidget.getValue());
				
				shouldEndXWidget.setValue(xEndWidget.getValue());
				shouldEndYWidget.setValue(yStartWidget.getValue());

				return ScaleAction.scaleXTo(testObject, xEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(metricsListener);
			}
		});
		
		addTest(new ActionTest("ScaleYTo") {
			@Override
			public Action<?> run() {
				testObject.setScaleX(xStartWidget.getValue());
				testObject.setScaleY(yStartWidget.getValue());
				
				shouldEndXWidget.setValue(xStartWidget.getValue());
				shouldEndYWidget.setValue(yEndWidget.getValue());
				
				return ScaleAction.scaleYTo(testObject, yEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(metricsListener);
			}
		});
		
		//Unit test?
		addTest(new ActionTest("SoloTest") {
			@Override
			public Action<?> run() {
				//Set start values
				testObject.setScaleX(1f);
				testObject.setScaleY(1f);
				
				//First iteration 
				//Both actions start at the same time. Action1 repeats a specified amount of times while action2 plays once. Action2
				//finishes first while action1 still plays. While action2 is not playing, the restarting of action1 should not affect action2.
				
				//Second iteration
				//Same as first iteration but x and y switched
				
				GroupAction mainGroup = GroupAction.obtain().sequence();
				
				RepeatAction action1 = null;
				ScaleAction action2 = null;
				GroupAction actionGroup = null;
				
				action1 = RepeatAction.repeat(ScaleAction.scaleXTo(testObject, 2, 0.5f, Interpolation.linear).solo(true), 3);
				action2 = ScaleAction.scaleYTo(testObject, 5, 1, Interpolation.linear).solo(true);
				actionGroup = GroupAction.parallel(action1, action2);
				
				mainGroup.add(actionGroup);
				mainGroup.add(ScaleAction.setScale(testObject, 1, 1));
				mainGroup.add(DelayAction.delay(0.2f));
				
				action1 = RepeatAction.repeat(ScaleAction.scaleYTo(testObject, 2, 0.5f, Interpolation.linear).solo(true), 3);
				action2 = ScaleAction.scaleXTo(testObject, 5, 1, Interpolation.linear).solo(true);
				actionGroup = GroupAction.parallel(action1, action2);
				
				mainGroup.add(actionGroup);
				
				return mainGroup;
			}
		});
	}

	public void drawWithShapeRenderer() {
		shapeRenderer.begin(ShapeType.Filled);
		testObject.draw(shapeRenderer);
		shapeRenderer.end();
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
//		testObject.draw(shapeDrawer);
		batch.end();
		drawWithShapeRenderer();
		batch.begin();
	}

}
