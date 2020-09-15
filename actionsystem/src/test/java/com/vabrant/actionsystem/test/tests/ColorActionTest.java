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
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.ActionSystemTestConstantsAndUtils;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class ColorActionTest extends ActionSystemTestListener {
	
//	private boolean isRGB;
	private float[] startValues = new float[3];
	private float[] endValues = new float[3];
	private float alphaStart;
	private float alphaEnd;
	private Color endColor;
	private TestObject testObject;
	
	private ActionListener<ColorAction> rgbListener;
	private ActionListener<ColorAction> hsbListener;
	
	//Start and end values
	private LabelTextFieldFloatWidget redStartWidget;
	private LabelTextFieldFloatWidget redEndWidget;
	private LabelTextFieldFloatWidget greenStartWidget;
	private LabelTextFieldFloatWidget greenEndWidget;
	private LabelTextFieldFloatWidget blueStartWidget;
	private LabelTextFieldFloatWidget blueEndWidget;
	private LabelTextFieldFloatWidget hueStartWidget;
	private LabelTextFieldFloatWidget hueEndWidget;
	private LabelTextFieldFloatWidget saturationStartWidget;
	private LabelTextFieldFloatWidget saturationEndWidget;
	private LabelTextFieldFloatWidget brightnessStartWidget;
	private LabelTextFieldFloatWidget brightnessEndWidget;
	private LabelTextFieldFloatWidget alphaStartWidget;
	private LabelTextFieldFloatWidget alphaEndWidget;
	private LabelTextFieldFloatWidget durationWidget;
	
	//Copy of the end values that is immutable during the test 
	private DoubleLabelWidget testRedEndWidget;
	private DoubleLabelWidget testGreenEndWidget;
	private DoubleLabelWidget testBlueEndWidget;
	private DoubleLabelWidget testAlphaEndWidget;
	private DoubleLabelWidget testHueEndWidget;
	private DoubleLabelWidget testSaturationEndWidget;
	private DoubleLabelWidget testBrightnessEndWidget;
	
	//Displays the color values after execution 
	private DoubleLabelWidget currentRedWidget;
	private DoubleLabelWidget currentGreenWidget;
	private DoubleLabelWidget currentBlueWidget;
	private DoubleLabelWidget currentHueWidget;
	private DoubleLabelWidget currentSaturationWidget;
	private DoubleLabelWidget currentBrightnessWidget;
	private DoubleLabelWidget currentAlphaWidget;
	
	private LabelCheckBoxWidget useHSBCheckbox;
	private LabelCheckBoxWidget soloCheckbox;
	
	private ActionListener<ColorAction> listener = new ActionAdapter<ColorAction>() {
		public void actionEnd(ColorAction a) {
			Color c = testObject.getColor();
			
			currentAlphaWidget.setValue(c.a);
			
			if(!useHSBCheckbox.isChecked()) {
				currentRedWidget.setValue(c.r);
				currentGreenWidget.setValue(c.g);
				currentBlueWidget.setValue(c.b);
				currentHueWidget.setValue(-1);
				currentSaturationWidget.setValue(-1);
				currentBrightnessWidget.setValue(-1);
			}
			else {
				currentRedWidget.setValue(-1);
				currentGreenWidget.setValue(-1);
				currentBlueWidget.setValue(-1);
				currentHueWidget.setValue(ColorAction.getHue(c));
				currentSaturationWidget.setValue(ColorAction.getSaturation(c));
				currentBrightnessWidget.setValue(ColorAction.getBrightness(c));
			}
		}
	};
	
	@Override
	public void create() {
		super.create();
		endColor = new Color(0, 0, 0, 1);
		testObject = new TestObject();
		testObject.setSize(200);
		rgbListener = createRGBListener();
		hsbListener = createHSBListener();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float x = (width - testObject.getWidth()) / 2;
		float y = (height - testObject.getHeight()) / 2;
		testObject.setX(x);
		testObject.setY(y);
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		//Values
		Label valuesLabel = new Label("Values", new LabelStyle(skin.get(LabelStyle.class)));
		valuesLabel.getStyle().fontColor = Color.BLACK;
		root.add(valuesLabel).left();
		root.row();
		redStartWidget = new LabelTextFieldFloatWidget("red: ", skin, root, 0);
		redEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
		root.row();
		greenStartWidget = new LabelTextFieldFloatWidget("green: ", skin, root, 0);
		greenEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
		root.row();
		blueStartWidget = new LabelTextFieldFloatWidget("blue: ", skin, root, 0);
		blueEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
		root.row();
		hueStartWidget = new LabelTextFieldFloatWidget("hue: ", skin, root, 0);
		hueEndWidget = new LabelTextFieldFloatWidget("", skin, root, 0);
		root.row();
		saturationStartWidget = new LabelTextFieldFloatWidget("saturation: ", skin, root, 1);
		saturationEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
		root.row();
		brightnessStartWidget = new LabelTextFieldFloatWidget("brightness: ", skin, root, 1);
		brightnessEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
		root.row();
		alphaStartWidget = new LabelTextFieldFloatWidget("alpha: ", skin, root, 1);
		alphaEndWidget = new LabelTextFieldFloatWidget("", skin, root, 1);
		root.row();
		durationWidget = new LabelTextFieldFloatWidget("duration: ", skin, root, 1f);
		root.row();
		useHSBCheckbox = new LabelCheckBoxWidget("useHSBValues", skin, root);
		root.row();
		soloCheckbox = new LabelCheckBoxWidget("solo", skin, root);
		root.row();
		
		//Metrics
		Label metricsLabel = new Label("Metrics", new LabelStyle(skin.get(LabelStyle.class)));
		metricsLabel.getStyle().fontColor = Color.BLACK;
		root.add(metricsLabel).left();
		root.row();
		testRedEndWidget = new DoubleLabelWidget("EndRed: ", skin, root);
		root.row();
		testGreenEndWidget = new DoubleLabelWidget("EndGreen: ", skin, root);
		root.row();
		testBlueEndWidget = new DoubleLabelWidget("EndBlue: ", skin, root);
		root.row();
		testHueEndWidget = new DoubleLabelWidget("EndHue: ", skin, root);
		root.row();
		testSaturationEndWidget = new DoubleLabelWidget("EndSaturation: ", skin, root);
		root.row();
		testBrightnessEndWidget = new DoubleLabelWidget("EndBrightness: ", skin, root);
		root.row();
		testAlphaEndWidget = new DoubleLabelWidget("EndAlpha: ", skin, root);
		root.row();
		
		currentRedWidget = new DoubleLabelWidget("CurrentRed: ", skin, root);
		root.row();
		currentGreenWidget = new DoubleLabelWidget("CurrentGreen: ", skin, root);
		root.row();
		currentBlueWidget = new DoubleLabelWidget("CurrentBlue: ", skin, root);
		root.row();
		currentHueWidget = new DoubleLabelWidget("CurrentHue: ", skin, root);
		root.row();
		currentSaturationWidget = new DoubleLabelWidget("CurrentSaturation: ", skin, root);
		root.row();
		currentBrightnessWidget = new DoubleLabelWidget("CurrentBrightness: ", skin, root);
		root.row();
		currentAlphaWidget = new DoubleLabelWidget("CurrentAlpha: ", skin, root);
	}
	
	private ActionListener<ColorAction> createRGBListener(){
		return new ActionAdapter<ColorAction>() {
			@Override
			public void actionEnd(ColorAction a) {
				ActionSystemTestConstantsAndUtils.printTestHeader(a.getName());
				StringBuilder builder = new StringBuilder(100);
				
				//Start Values
				builder.append("redStart: " + startValues[0]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("greenStart: " + startValues[1]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("blueStart: " + startValues[2]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("alphaStart: " + alphaStart);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				
				//End Values
				builder.append("redEnd: " + endValues[0]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("greenEnd: " + endValues[1]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("blueEnd: " + endValues[2]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("alphaEnd: " + alphaEnd);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				
				//Current Values
				Color c = testObject.getColor();
				
				builder.append("red: " + c.r);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("green: " + c.g);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("blue: " + c.b);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("alpha: " + c.a);
				
				System.out.println(builder.toString());
			}
		};
	}
	
	private ActionListener<ColorAction> createHSBListener(){
		return new ActionAdapter<ColorAction>() {
			@Override
			public void actionEnd(ColorAction a) {
				ActionSystemTestConstantsAndUtils.printTestHeader(a.getName());
				StringBuilder builder = new StringBuilder(100);
				
				//Start Values
				builder.append("hueStart: " + startValues[0]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("saturationStart: " + startValues[1]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("brightnessStart: " + startValues[2]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("alphaStart: " + alphaStart);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				
				//End Values
				builder.append("hueEnd: " + endValues[0]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("saturationEnd: " + endValues[1]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("brightnessEnd: " + endValues[2]);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("alphaEnd: " + alphaEnd);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				
				
				//Current Values
				Color c = testObject.getColor();
				float hue = ColorAction.getHue(c);
				float saturation = ColorAction.getSaturation(c);
				float brightness = ColorAction.getBrightness(c);
				
				builder.append("hue: " + hue);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("saturation: " + saturation);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("brightness: " + brightness);
				builder.append(ActionSystemTestConstantsAndUtils.SEPARATOR);
				builder.append("alpha: " + c.a);
				
				System.out.println(builder.toString());
			}
		};
	}

	private void setTestValues(Color startColor, Color endColor, boolean rgb) {
		startValues[0] = rgb ? startColor.r : ColorAction.getHue(startColor);
		startValues[1] = rgb ? startColor.g : ColorAction.getSaturation(startColor);
		startValues[2] = rgb ? startColor.b : ColorAction.getBrightness(startColor);
		endValues[0] = rgb ? endColor.r : ColorAction.getHue(endColor);
		endValues[1] = rgb ? endColor.g : ColorAction.getSaturation(endColor);
		endValues[2] = rgb ? endColor.b : ColorAction.getBrightness(endColor);
		alphaStart = startColor.a;
		alphaEnd = endColor.a;
	}
	
	private void setupTest(boolean isRGB, float v1, float v2, float v3, float alpha) {
//		this.isRGB = isRGB;
		
		testAlphaEndWidget.setValue(alpha);
		
		if(isRGB) {
			testRedEndWidget.setValue(v1);
			testGreenEndWidget.setValue(v2);
			testBlueEndWidget.setValue(v2);
			testHueEndWidget.setValue(-1);
			testSaturationEndWidget.setValue(-1);
			testBrightnessEndWidget.setValue(-1);
		}
		else {
			testHueEndWidget.setValue(v1);
			testSaturationEndWidget.setValue(v2);
			testBrightnessEndWidget.setValue(v3);
			testRedEndWidget.setValue(-1);
			testGreenEndWidget.setValue(-1);
			testBlueEndWidget.setValue(-1);
		}
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("ChangeAlpha") {
			@Override
			public Action<?> run() {
//				isRGB = true;
				
				//Set start color
				testObject.getColor().set(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(), alphaStartWidget.getValue());
				
				//Set test end values
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(redStartWidget.getValue());
				testGreenEndWidget.setValue(greenStartWidget.getValue());
				testBlueEndWidget.setValue(blueStartWidget.getValue());
				testHueEndWidget.setValue(-1);
				testSaturationEndWidget.setValue(-1);
				testBrightnessEndWidget.setValue(-1);
				
				return ColorAction.changeAlpha(testObject, alphaEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(listener);
			}
		});

		addTest(new ActionTest("ChangeColorRGBA") {
			@Override
			public Action<?> run() {
//				isRGB = true;
				
				//Set start color
				testObject.getColor().set(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(), alphaStartWidget.getValue());
				
				//Set test end values
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(redEndWidget.getValue());
				testGreenEndWidget.setValue(greenEndWidget.getValue());
				testBlueEndWidget.setValue(blueEndWidget.getValue());
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testHueEndWidget.setValue(-1);
				testSaturationEndWidget.setValue(-1);
				testBrightnessEndWidget.setValue(-1);
				
				return ColorAction.changeColorRGBA(testObject, redEndWidget.getValue(), greenEndWidget.getValue(), blueEndWidget.getValue(), alphaEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(listener);
			}
		});
		
		addTest(new ActionTest("ChangeRed") {
			@Override
			public Action<?> run() {
//				isRGB = true;
				
				//Set start color
				testObject.getColor().set(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(), alphaStartWidget.getValue());
				
				//Set test end values
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(redEndWidget.getValue());
				testGreenEndWidget.setValue(greenStartWidget.getValue());
				testBlueEndWidget.setValue(blueStartWidget.getValue());
				testHueEndWidget.setValue(-1);
				testSaturationEndWidget.setValue(-1);
				testBrightnessEndWidget.setValue(-1);
				
				return ColorAction.changeRed(testObject, redEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(listener);
			}
		});
		
		addTest(new ActionTest("ChangeBlue") {
			@Override
			public Action<?> run() {
//				isRGB = true;
				
				//Set start color
				testObject.getColor().set(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(), alphaStartWidget.getValue());
				
				//Set test end values
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(redStartWidget.getValue());
				testGreenEndWidget.setValue(greenStartWidget.getValue());
				testBlueEndWidget.setValue(blueEndWidget.getValue());
				testHueEndWidget.setValue(-1);
				testSaturationEndWidget.setValue(-1);
				testBrightnessEndWidget.setValue(-1);
				
				return ColorAction.changeBlue(testObject, blueEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(listener);
			}
		});

		addTest(new ActionTest("ChangeGreen") {
			public Action<?> run() {
//				isRGB = true;
				
				//Set start color
				testObject.getColor().set(redStartWidget.getValue(), greenStartWidget.getValue(), blueStartWidget.getValue(), alphaStartWidget.getValue());
				
				//Set test end values
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(redStartWidget.getValue());
				testGreenEndWidget.setValue(greenEndWidget.getValue());
				testBlueEndWidget.setValue(blueStartWidget.getValue());
				testAlphaEndWidget.setValue(alphaStartWidget.getValue());
				testHueEndWidget.setValue(-1);
				testSaturationEndWidget.setValue(-1);
				testBrightnessEndWidget.setValue(-1);
				
				return ColorAction.changeGreen(testObject, greenEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear)
						.addListener(listener);
			}
		});
		
		addTest(new ActionTest("ChangeColorHSBA") {
			@Override
			public Action<?> run() {
//				isRGB = false;
				
				//Set start color
				ColorAction.HSBToRGB(testObject.getColor(), hueStartWidget.getValue(), saturationStartWidget.getValue(), brightnessStartWidget.getValue());
				testObject.getColor().a = alphaStartWidget.getValue();
				
				//Set test end values
				testHueEndWidget.setValue(hueEndWidget.getValue());
				testSaturationEndWidget.setValue(saturationEndWidget.getValue());
				testBrightnessEndWidget.setValue(brightnessEndWidget.getValue());
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(-1);
				testGreenEndWidget.setValue(-1);
				testBlueEndWidget.setValue(-1);
				
				return ColorAction.changeColorHSBA(testObject, hueEndWidget.getValue(), saturationEndWidget.getValue(), brightnessEndWidget.getValue(), alphaEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear, useHSBCheckbox.isChecked())
						.addListener(listener);
			}
		});
		
		addTest(new ActionTest("ChangeHue") {
			@Override
			public Action<?> run() {
//				isRGB = false;
				
				//Set the start color
				ColorAction.HSBToRGB(testObject.getColor(), hueStartWidget.getValue(), saturationStartWidget.getValue(), brightnessStartWidget.getValue());
				testObject.getColor().a = alphaStartWidget.getValue();
				
				//Set test end values
				testHueEndWidget.setValue(hueEndWidget.getValue());
				testSaturationEndWidget.setValue(saturationEndWidget.getValue());
				testBrightnessEndWidget.setValue(brightnessEndWidget.getValue());
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(-1);
				testGreenEndWidget.setValue(-1);
				
				testBlueEndWidget.setValue(-1);
				
				return ColorAction.changeHue(testObject, hueEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear, useHSBCheckbox.isChecked())
						.addListener(listener);
			}
		});
		
		addTest(new ActionTest("ChangeSaturation") {
			@Override
			public Action<?> run() {
//				isRGB = false;
				
				//Set the start color
				ColorAction.HSBToRGB(testObject.getColor(), hueStartWidget.getValue(), saturationStartWidget.getValue(), brightnessStartWidget.getValue());
				testObject.getColor().a = alphaStartWidget.getValue();
				
				//Set test end values
				testHueEndWidget.setValue(hueEndWidget.getValue());
				testSaturationEndWidget.setValue(saturationEndWidget.getValue());
				testBrightnessEndWidget.setValue(brightnessEndWidget.getValue());
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(-1);
				testGreenEndWidget.setValue(-1);
				testBlueEndWidget.setValue(-1);
				
				return ColorAction.changeSaturation(testObject, saturationEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear, useHSBCheckbox.isChecked())
						.addListener(listener);
			}
		});
		
		addTest(new ActionTest("ChangeBrightness") {
			@Override
			public Action<?> run() {
//				isRGB = false;
				
				//Set the start color
				ColorAction.HSBToRGB(testObject.getColor(), hueStartWidget.getValue(), saturationStartWidget.getValue(), brightnessStartWidget.getValue());
				testObject.getColor().a = alphaStartWidget.getValue();
				
				//Set test end values
				testHueEndWidget.setValue(hueEndWidget.getValue());
				testSaturationEndWidget.setValue(saturationEndWidget.getValue());
				testBrightnessEndWidget.setValue(brightnessEndWidget.getValue());
				testAlphaEndWidget.setValue(alphaEndWidget.getValue());
				testRedEndWidget.setValue(-1);
				testGreenEndWidget.setValue(-1);
				testBlueEndWidget.setValue(-1);
				
				return ColorAction.changeBrightness(testObject, brightnessEndWidget.getValue(), durationWidget.getValue(), Interpolation.linear, useHSBCheckbox.isChecked())
						.addListener(listener);
			}
		});
		
	}
	
	public void restartTest() {
		reset();
		
		testObject.setColor(Color.RED);
		
		GroupAction sequence = GroupAction.sequence(
//				ColorAction.changeRed(object, 1, 1f, Interpolation.linear),
//				ColorAction.changeGreen(object, 1, 1f, Interpolation.linear),
//				ColorAction.changeBlue(object, 1, 1f, Interpolation.linear))
				ColorAction.changeBrightness(testObject, 0.5f, 1f, Interpolation.linear, true),
				ColorAction.changeHue(testObject, 50, 1f, Interpolation.linear, true),
				ColorAction.changeSaturation(testObject, 0.2f, 1f, Interpolation.linear, true))
		.setName("Sequence")
		.setLogLevel(ActionLogger.DEBUG);
		
		ActionListener restartListener = new ActionAdapter() {
			boolean restart = true;
			
			@Override
			public void actionEnd(Action a) {
				if(restart) {
					restart = false;
					sequence.restart();
					
				}
			}
		};

		((ColorAction)sequence.getActions().get(1)).addListener(restartListener);
		
		actionManager.addAction(sequence);
	}
	
	private void conflitTest() {
	}
	
	private void pingPongTest() {
		reset();
		
//		testObject.getColor().set(0, 1, 0, 1);
//		actionManager.addAction(
//				RepeatAction.repeat(
//						ColorAction.changeRed(testObject, 1, 1f, Interpolation.linear), 
//						2)
//				.pingPong(true));
		
//		testObject.getColor().set(1, 0, 0, 1);
//		actionManager.addAction(
//				RepeatAction.repeat(
//						ColorAction.changeHue(testObject, 50, 1f, Interpolation.linear), 
//						3)
//				.pingPong(true));

		testObject.getColor().set(1, 0, 0, 1);
		actionManager.addAction(
				RepeatAction.repeat(
						ColorAction.changeColorRGBA(testObject, ColorAction.normalize(255), ColorAction.normalize(213), 0, 0.5f, 1f, Interpolation.linear),
						3)
				.pingPong(true));
		
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
