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
import com.vabrant.actionsystem.actions.DelayAction;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class ColorActionTest extends ActionSystemTestListener {
	
	private TestObjectController testObjectController;
	
	public ColorActionTest() {
		testObjectController = TestObjectController.getInstance();
	}
	
	private TestObject getTestObject() {
		TestObject object = testObjectController.create();
		object.setSize(100, 100);
		testObjectController.center(object, viewport);
		return object;
	}
	
	private GroupAction wrap(Action<?> a) {
		return GroupAction.sequence(
				DelayAction.delay(0.5f),
				a,
				DelayAction.delay(1f));
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_1:
				changeColorTest();
//				changeColorRGBTest();
//				changeColorRGBATest();
				break;
			case Keys.NUMPAD_2:
				changeRedTest();
//				changeGreenTest();
//				changeBlueTest();
				break;
			case Keys.NUMPAD_3:
//				changeColorHSBTest();
				changeColorHSBATest();
				break;
			case Keys.NUMPAD_4:
//				changeHueTest();
//				changeSaturationTest();
				changeBrightnessTest();
				break;
			case Keys.NUMPAD_5:
				changeAlphaTest();
				break;
			case Keys.NUMPAD_6:
				soloHSBGroup();
				break;
			case Keys.NUMPAD_7:
				break;
			case Keys.NUMPAD_8:
				break;
			case Keys.NUMPAD_9:
				restartTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void changeColorTest() {
		TestObject object = getTestObject();
		object.setColor(Color.RED);
		Color end = new Color(Color.GREEN);
		ColorAction action = ColorAction.changeColor(object, end, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeColorRGBTest() {
		TestObject object = getTestObject();
		ColorAction action = ColorAction.changeColorRGB(object, ColorAction.normalize(20), ColorAction.normalize(50), ColorAction.normalize(150), 1f, Interpolation.exp5Out);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeColorRGBATest() {
		TestObject object = getTestObject();
		ColorAction action = ColorAction.changeColorRGBA(object, 0.20f, 0.50f, 0.10f, 0.2f, 1f, Interpolation.exp5Out);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeRedTest() {
		TestObject object = getTestObject();
		object.setColor(new Color(0, 171f / 255f, 130f / 255f, 1));
		ColorAction action = ColorAction.changeRed(object, 1f, 1f, Interpolation.linear).solo();
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeGreenTest() {
		TestObject object = getTestObject();
		object.setColor(new Color(1, 0, 0, 1));
		ColorAction action = ColorAction.changeGreen(object, 1, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeBlueTest() {
		TestObject object = getTestObject();
		object.setColor(new Color(1, 0, 0, 1));
		ColorAction action = ColorAction.changeBlue(object, 1, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeColorHSBTest() {
		float[] hsb = {190, 0.8f, 1f};
		TestObject object = getTestObject();
		object.setColor(Color.RED);
		ColorAction action = ColorAction.changeColorHSB(object, hsb[0], hsb[1], hsb[2], 1f, Interpolation.linear, true);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void changeColorHSBATest() {
		float[] hsba = {190, 0.8f, 1f, 0.4f};
		TestObject object = getTestObject();
		object.setColor(Color.RED);
		ColorAction action = ColorAction.changeColorHSBA(object, ColorAction.temp(190, 0.8f, 1f, 0.1f), 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}

	private void changeAlphaTest() {
		TestObject object = getTestObject();
		ColorAction action = ColorAction.changeAlpha(object, 0.25f, 1f, Interpolation.linear);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	private void changeHueTest() {
		TestObject object = getTestObject();
		ColorAction.HSBToRGB(object.getColor(), 30, 1, 1);
		ColorAction action = ColorAction.changeHue(object, 250, 0.5f, Interpolation.linear, false);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	private void changeSaturationTest() {
		TestObject object = getTestObject();
		ColorAction.HSBToRGB(object.getColor(), 30, 1, 1);
		ColorAction action = ColorAction.changeSaturation(object, 0.3f, 0.5f, Interpolation.linear, true);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	private void changeBrightnessTest() {
		TestObject object = getTestObject();
		ColorAction.HSBToRGB(object.getColor(), 30, 1, 1);
		ColorAction action = ColorAction.changeBrightness(object, 0f, 0.5f, Interpolation.linear, true);
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	private void soloHSBGroup() {
		TestObject object = getTestObject();
		ColorAction.HSBToRGB(object.getColor(), 0, 1f, 1);
		
		GroupAction action = GroupAction.parallel(
				ColorAction.changeHue(object, 150, 0.5f, Interpolation.linear, true).solo(),
				ColorAction.changeSaturation(object, 0.2f, 0.5f, Interpolation.linear, true).solo());
		
//		GroupAction action = GroupAction.parallel(
//				ColorAction.changeRed(object, 1, 1f, Interpolation.linear).solo(),
//				ColorAction.changeGreen(object, 1, 1f, Interpolation.linear).solo(),
//				ColorAction.changeBlue(object, 1, 1f, Interpolation.linear).solo());
		
//		ColorAction action = ColorAction.obtain()
//				.set(object, 0.5f, Interpolation.linear)
//				.changeHue(150, true)
//				.changeSaturation(0.2f, true);
		
		GroupAction wrap = wrap(action);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	public void restartTest() {
		TestObject object = getTestObject();
		object.setColor(Color.RED);
		
		GroupAction sequence = GroupAction.sequence(
//				ColorAction.changeRed(object, 1, 1f, Interpolation.linear),
//				ColorAction.changeGreen(object, 1, 1f, Interpolation.linear),
//				ColorAction.changeBlue(object, 1, 1f, Interpolation.linear))
				ColorAction.changeBrightness(object, 0.5f, 1f, Interpolation.linear, true),
				ColorAction.changeHue(object, 50, 1f, Interpolation.linear, true),
				ColorAction.changeSaturation(object, 0.2f, 1f, Interpolation.linear, true))
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
		
		GroupAction wrap = wrap(sequence);
		wrap.addListener(object);
		actionManager.addAction(wrap);
	}
	
	private void conflitTest() {
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObjectController.draw(shapeDrawer);
	}

}
