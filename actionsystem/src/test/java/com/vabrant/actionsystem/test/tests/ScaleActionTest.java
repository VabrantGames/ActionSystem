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
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.ActionSystemTestConstantsAndUtils;
import com.vabrant.actionsystem.test.TestObject;

/**
 * @author John Barton
 *
 */
public class ScaleActionTest extends ActionSystemTestListener {
	
	private LabelTextFieldFloatWidget xStartWidget;
	private LabelTextFieldFloatWidget yStartWidget;
	private LabelTextFieldFloatWidget xEndWidget;
	private LabelTextFieldFloatWidget yEndWidget;
	private LabelTextFieldFloatWidget xAmountWidget;
	private LabelTextFieldFloatWidget yAmountWidget;
	private LabelTextFieldFloatWidget durationWidget;
	private LabelCheckBoxWidget reverseBackToStartWidget;
	private LabelCheckBoxWidget reverseWidget;
	private LabelCheckBoxWidget startXByFromEndWidget;
	private LabelCheckBoxWidget startYByFromEndWidget;
	
	//Metrics
	private DoubleLabelWidget testXEndWidget;
	private DoubleLabelWidget testYEndWidget;
	private DoubleLabelWidget currentXWidget;
	private DoubleLabelWidget currentYWidget;
	
	private TestObject testObject;
	
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
		xStartWidget = new LabelTextFieldFloatWidget("xStart: ", skin, root, 1);
		xStartWidget.allowNegativeValues();
		xEndWidget = new LabelTextFieldFloatWidget("xEnd: ", skin, root, 1);
		xEndWidget.allowNegativeValues();
		root.row();
		yStartWidget = new LabelTextFieldFloatWidget("yStart: ", skin, root, 1);
		yStartWidget.allowNegativeValues();
		yEndWidget = new LabelTextFieldFloatWidget("yEnd: ", skin, root, 1);
		yEndWidget.allowNegativeValues();
		root.row();
		xAmountWidget = new LabelTextFieldFloatWidget("xAmount: ", skin, root, 0);
		xAmountWidget.allowNegativeValues();
		yAmountWidget = new LabelTextFieldFloatWidget("yAmount: ", skin, root, 0);
		yAmountWidget.allowNegativeValues();
		root.row();
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1);
		root.row();
		reverseBackToStartWidget = new LabelCheckBoxWidget("reverseBackToStart: ", skin, root);
		root.row();
		reverseWidget = new LabelCheckBoxWidget("reverse: ", skin, root);
		root.row();
		startXByFromEndWidget = new LabelCheckBoxWidget("startXByFromEnd: ", skin, root);
		root.row();
		startYByFromEndWidget = new LabelCheckBoxWidget("startYByFromEnd: ", skin, root);
		root.row();
		
		Label metricsLabel = new Label("Metrics", headerStyle);
		root.add(metricsLabel).left().padTop(10);
		root.row();
		testXEndWidget = new DoubleLabelWidget("testXEnd: ", skin, root);
		root.row();
		testYEndWidget = new DoubleLabelWidget("testYEnd: ", skin, root);
		root.row();
		currentXWidget = new DoubleLabelWidget("currentX: ", skin, root);
		root.row();
		currentYWidget = new DoubleLabelWidget("currentY: ", skin, root);
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("ScaleXBy") {
			@Override
			public Action<?> run() {
				if(!startXByFromEndWidget.isChecked()) testObject.setScaleX(xStartWidget.getValue());
				if(!startYByFromEndWidget.isChecked()) testObject.setScaleY(yStartWidget.getValue());

				testXEndWidget.setValue(testObject.getScaleX() + xAmountWidget.getValue());
				testYEndWidget.setValue(testObject.getScaleY());

				return ScaleAction.scaleXBy(testObject, xAmountWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear)
						.addListener(metricsListener)
						.setName("ScaleXBy")
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ScaleYBy") {
			@Override
			public Action<?> run() {
				if(!startXByFromEndWidget.isChecked()) testObject.setScaleX(xStartWidget.getValue());
				if(!startYByFromEndWidget.isChecked()) testObject.setScaleY(yStartWidget.getValue());
				
				testXEndWidget.setValue(testObject.getScaleX());
				testYEndWidget.setValue(testObject.getScaleY() + yAmountWidget.getValue());
				
				return ScaleAction.scaleYBy(testObject, yAmountWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear)
						.addListener(metricsListener)
						.setName("ScaleYBy")
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ScaleXTo") {
			@Override
			public Action<?> run() {
				testObject.setScale(xStartWidget.getValue(), yStartWidget.getValue());
				
				testXEndWidget.setValue(xEndWidget.getValue());
				testYEndWidget.setValue(testObject.getScaleY());
				
				return ScaleAction.scaleXTo(testObject, xEndWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear) 
						.addListener(metricsListener)
						.setName("ScaleXTo")
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ScaleYTo") {
			@Override
			public Action<?> run() {
				testObject.setScale(xStartWidget.getValue(), yStartWidget.getValue());
				
				testXEndWidget.setValue(testObject.getScaleX());
				testYEndWidget.setValue(yEndWidget.getValue());
				
				return ScaleAction.scaleYTo(testObject, yEndWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear) 
						.addListener(metricsListener)
						.setName("ScaleYTo")
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ScaleBy") {
			@Override
			public Action<?> run() {
				if(!startXByFromEndWidget.isChecked()) testObject.setScaleX(xStartWidget.getValue());
				if(!startYByFromEndWidget.isChecked()) testObject.setScaleY(yStartWidget.getValue());
				
				testXEndWidget.setValue(testObject.getScaleX() + xAmountWidget.getValue());
				testYEndWidget.setValue(testObject.getScaleY() + yAmountWidget.getValue());
				
				return ScaleAction.scaleBy(testObject, xAmountWidget.getValue(), 
						yAmountWidget.getValue(), durationWidget.getValue(), Interpolation.linear) 
						.addListener(metricsListener)
						.setName("ScaleBy")
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
		
		addTest(new ActionTest("ScaleTo") {
			@Override
			public Action<?> run() {
				testObject.setScale(xStartWidget.getValue(), yStartWidget.getValue());
				
				testXEndWidget.setValue(xEndWidget.getValue());
				testYEndWidget.setValue(yEndWidget.getValue());
				
				return ScaleAction.scaleTo(testObject, xEndWidget.getValue(), yEndWidget.getValue(), 
						durationWidget.getValue(), Interpolation.linear) 
						.setName("ScaleTo")
						.reverseBackToStart(reverseBackToStartWidget.isChecked())
						.setReverse(reverseWidget.isChecked());
			}
		});
	}

	@Override
	public void drawWithShapeRenderer(ShapeRenderer renderer) {
		testObject.draw(renderer);
	}

}
