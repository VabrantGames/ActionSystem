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
	
//	private TestObjectController testObjectController;
	private float[] startValues = new float[3];
	private float[] endValues = new float[3];
	private float alphaStart;
	private float alphaEnd;
	private Color endColor;
	private TestObject testObject;
	private ActionListener<ColorAction> rgbListener;
	private ActionListener<ColorAction> hsbListener;
	
	@Override
	public void create() {
		super.create();
		endColor = new Color(0, 0, 0, 1);
		testObject = new TestObject();
		rgbListener = createRGBListener();
		hsbListener = createHSBListener();
		reset();
	}
	
//	private TestObject getTestObject() {
//		TestObject object = testObjectController.create();
//		object.setSize(100, 100);
//		testObjectController.center(object, viewport);
//		return object;
//	}
//	
//	private GroupAction wrap(Action<?> a) {
//		return GroupAction.sequence(
//				DelayAction.delay(0.5f),
//				a,
//				DelayAction.delay(1f));
//	}
	
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
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		ActionSystemTestConstantsAndUtils.centerTestObject(testObject, hudViewport);
	}

	@Override
	public void reset() {
		testObject.setColor(Color.BLACK);
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
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
//				changeColorTest();
//				changeColorRGBTest();
				changeColorRGBATest();
				break;
			case Keys.NUMPAD_1:
//				changeRedTest();
//				changeGreenTest();
				changeBlueTest();
				break;
			case Keys.NUMPAD_2:
//				changeColorHSBTest();
				changeColorHSBATest();
				break;
			case Keys.NUMPAD_3:
				changeHueTest();
//				changeSaturationTest();
//				changeBrightnessTest();
				break;
			case Keys.NUMPAD_4:
				changeAlphaTest();
				break;
			case Keys.NUMPAD_5:
				rgbSoloTest();
				
				break;
			case Keys.NUMPAD_6:
				pingPongTest();
				break;
			case Keys.NUMPAD_7:
				break;
			case Keys.NUMPAD_8:
				restartTest();
				break;
			case Keys.R:
				reset();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void changeColorTest() {
		reset();

		testObject.setColor(Color.RED);
		endColor.set(Color.GREEN);
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeColor(testObject, endColor, 1f, Interpolation.linear)
				.setName("Change Color Test")
				.addListener(rgbListener));
	}
	
	public void changeColorRGBTest() {
		reset();
		
		testObject.setColor(Color.RED);
		endColor.set(ColorAction.normalize(20), ColorAction.normalize(50), ColorAction.normalize(150), 1);
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeColorRGB(testObject, endColor.r, endColor.g, endColor.b, 1f, Interpolation.linear)
				.setName("Change Color RGB Test")
				.addListener(rgbListener));
	}
	
	public void changeColorRGBATest() {
		reset();

		testObject.setColor(Color.BLACK);
		endColor.set(0.20f, 0.50f, 0.10f, 0.2f);
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeColorRGBA(testObject, endColor.r, endColor.g, endColor.b, endColor.a, 1f, Interpolation.exp5Out)
				.setName("Change Color RGBA Test")
				.addListener(rgbListener));
	}
	
	public void changeRedTest() {
		reset();

		Color startColor = testObject.getColor();
		startColor.set(0, ColorAction.normalize(171), ColorAction.normalize(130), 1);
		endColor.set(1, startColor.g, startColor.b, startColor.a);
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeRed(testObject, endColor.r, 1f, Interpolation.linear)
				.solo(true)
				.setName("Change Red Test")
				.addListener(rgbListener));
	}
	
	public void changeGreenTest() {
		reset();
		
		Color startColor = testObject.getColor();
		startColor.set(1, 0, 0, 1);
		endColor.set(startColor.r, 1, startColor.b, startColor.a);
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeGreen(testObject, endColor.g, 1f, Interpolation.linear)
				.solo(true)
				.setName("Change Green Test")
				.addListener(rgbListener));
	}
	
	public void changeBlueTest() {
		reset();
		
		Color startColor = testObject.getColor();
		startColor.set(1, 0, 0, 1);
		endColor.set(startColor.r, startColor.g, 1, startColor.a);
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeBlue(testObject, endColor.b, 1f, Interpolation.linear)
				.solo(true)
				.setName("Change Blue Test")
				.addListener(rgbListener));
	}
	
	private void changeAlphaTest() {
		reset();

		testObject.setColor(Color.RED);
		testObject.getColor().a = 1;
		endColor.set(testObject.getColor());
		endColor.a = 0.1f;
		setTestValues(testObject.getColor(), endColor, true);
		
		actionManager.addAction(
				ColorAction.changeAlpha(testObject, endColor.a, 1f, Interpolation.linear)
				.setName("Change Alpha Test")
				.addListener(rgbListener));
	}
	
	public void changeColorHSBTest() {
		reset();
		
		testObject.setColor(Color.RED);
		ColorAction.HSBToRGB(endColor, 190f, 0.8f, 1f);
		setTestValues(testObject.getColor(), endColor, false);

		actionManager.addAction(
				ColorAction.changeColorHSB(testObject, endValues[0], endValues[1], endValues[2], 1f, Interpolation.linear, true)
				.setName("Change Color HSB Test")
				.addListener(hsbListener));
	}
	
	public void changeColorHSBATest() {
		reset();
		
		testObject.setColor(Color.FOREST);
		ColorAction.HSBToRGB(endColor, 190f, 0.8f, 1f);
		endColor.a = 0.4f;
		setTestValues(testObject.getColor(), endColor, false);
		
		actionManager.addAction(
				ColorAction.changeColorHSBA(testObject, endValues[0], endValues[1], endValues[2], endColor.a, 1f, Interpolation.linear, false)
				.setName("Change Color HSBA Test")
				.addListener(hsbListener));
	}
	
	private void changeHueTest() {
		reset();
		
		Color startColor = testObject.getColor();
		startColor.set(Color.FOREST);
		ColorAction.HSBToRGB(endColor, 250f, ColorAction.getSaturation(startColor), ColorAction.getBrightness(startColor));
		setTestValues(startColor, endColor, false);
		
		actionManager.addAction(
				ColorAction.changeHue(testObject, endValues[0], 1f, Interpolation.linear, true)
				.solo(true)
				.setName("Change Hue Test")
				.addListener(hsbListener));
	}
	
	private void changeSaturationTest() {
		reset();

		Color startColor = ColorAction.HSBToRGB(testObject.getColor(), 30f, 1f, 1f);
		ColorAction.HSBToRGB(endColor, ColorAction.getHue(startColor), 0.3f, ColorAction.getBrightness(startColor));
		setTestValues(startColor, endColor, false);
		
		actionManager.addAction(
				ColorAction.changeSaturation(testObject, endValues[1], 1f, Interpolation.linear, true)
				.solo(true)
				.setName("Change Saturation Test")
				.addListener(hsbListener));
	}
	
	private void changeBrightnessTest() {
		reset();
		
		Color startColor = ColorAction.HSBToRGB(testObject.getColor(), 0, 0.3f, 1f);
		ColorAction.HSBToRGB(endColor, ColorAction.getHue(startColor), ColorAction.getSaturation(startColor), 0.5f);
		setTestValues(startColor, endColor, false);
		
		actionManager.addAction(
				ColorAction.changeBrightness(testObject, endValues[2], 1f, Interpolation.linear, true)
				.solo(true)
				.setName("Change Brightness Test")
				.addListener(hsbListener));
	}
	
	private void hsbSoloTest() {
		reset();

		float duration = 1f;
		ColorAction.HSBToRGB(testObject.getColor(), 0, 1f, 1);

		actionManager.addAction(
				GroupAction.parallel(
						ColorAction.changeHue(testObject, 259, duration, Interpolation.linear, true).solo(true),
						ColorAction.changeSaturation(testObject, 0.2f, duration, Interpolation.linear, true).solo(true)));
	}
	
	private void rgbSoloTest() {
		reset();
		
		testObject.getColor().set(0, 0, 0, 1f);
		
		actionManager.addAction(
				GroupAction.parallel(
						ColorAction.changeRed(testObject, 1, 1f, Interpolation.bounceOut)
						.solo(true),
						ColorAction.changeGreen(testObject, 1, 0.5f, Interpolation.linear)
						.solo(true)
						.reverseBackToStart(false),
						ColorAction.changeBlue(testObject, 1f, 1f, Interpolation.bounceOut)
						.solo(true)));
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
//		testObjectController.draw(shapeDrawer);
		testObject.draw(shapeDrawer);
	}

}
