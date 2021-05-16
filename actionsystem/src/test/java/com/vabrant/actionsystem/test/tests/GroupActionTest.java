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
import com.badlogic.gdx.utils.Array;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class GroupActionTest extends ActionSystemTestListener {
	
	private LabelTextFieldFloatWidget parallelOffsetWidget;

	private final float startY = 150;
	private final int amountOfTestObjects = 5;
	private Array<TestObject> testObjects;
	
	public void create() {
		super.create();
		
		testObjects = new Array<>(amountOfTestObjects);
		for(int i = 0; i < amountOfTestObjects; i++) {
			testObjects.add(new TestObject());
		}
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		float x = 0;
		final float offset = 20;
		float size = 50 + offset;
		float fullWidth = size * amountOfTestObjects;
		x = (1280 - fullWidth) * 0.5f;
		for(int i = 0; i < testObjects.size; i++) {
			TestObject testObject = testObjects.get(i);
			testObject.setX(x);
			testObject.setY(startY);
			x += size;
		}
	}
	
	private void resetY() {
		for(int i = 0; i < testObjects.size; i++) {
			testObjects.get(i).setY(startY);
		}
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		Label valuesLabel = new Label("Values", new LabelStyle(skin.get(LabelStyle.class)));
		valuesLabel.getStyle().fontColor = Color.BLACK;
		root.add(valuesLabel).left();
		root.row();
		parallelOffsetWidget = new LabelTextFieldFloatWidget("parallelOffset: ", skin, root, 0);
		root.row();
	}
	
	@Override
	public void createTests() {
		addTest(new ActionTest("Parallel") {
			@Override
			public Action<?> run() {
				resetY();
				
				GroupAction groupAction = GroupAction.obtain();
				groupAction.parallel(parallelOffsetWidget.getValue());

				for(int i = 0; i < testObjects.size; i++) {
					groupAction.add(MoveAction.moveYBy(testObjects.get(i), 100, 2f, Interpolation.exp5Out));
				}
				
				return groupAction;
			}
		});
		
		addTest(new ActionTest("Sequence") {
			@Override
			public Action<?> run() {
				resetY();
				
				GroupAction groupAction = GroupAction.obtain();
				groupAction.sequence();
				
				for(int i = 0; i < testObjects.size; i++) {
					groupAction.add(MoveAction.moveYBy(testObjects.get(i), 100, 1f, Interpolation.bounceOut));
				}
				
				return groupAction;
			}
		});
		
		addTest(new ActionTest("RestartTest") {
			@Override
			public Action<?> run() {
				resetY();
				
				GroupAction groupAction = GroupAction.obtain();
				groupAction.sequence();
				
				for(int i = 0; i < testObjects.size; i++) {
//					groupAction.add(MoveAction.moveYBy(testObjects.get(i), 150f, 0.5f, Interpolation.exp5Out));
					groupAction.add(ScaleAction.scaleYBy(testObjects.get(i), 1f, 0.5f, Interpolation.exp5Out));
				}

				
				ActionListener restartListener = new ActionAdapter() {
					boolean restart = true;
					
					@Override
					public void actionEnd(Action a) {
						if(restart) {
							restart = false;
							groupAction.restart();
						}
					}
				};
		
				groupAction.getActions().get(1).addListener(restartListener);
				return groupAction;
			}
		});
	}

//	@Override
//	public boolean keyDown(int keycode) {
//		switch(keycode) {
//			case Keys.NUMPAD_1:
//				parallelTest();
//				break;
//			case Keys.NUMPAD_2:
//				parallelWithOffsetTest();
//				break;
//			case Keys.NUMPAD_3:
//				sequenceTest();
//				break;
//			case Keys.NUMPAD_4:
//				restartTest();
//				break;
//			case Keys.NUMPAD_5:
//				nestedTest();
//				break;
//			case Keys.NUMPAD_6:
//				parallelReverseTest();
//				break;
//			case Keys.NUMPAD_7:
//				sequenceReverseTest();
//				break;
//		}
//		return super.keyDown(keycode);
//	}
//	
//	public void parallelTest() {
//		TestObject ob1 = testObjectController.create();
//		testObjectController.center(ob1, hudViewport);
//		
//		GroupAction parallel = GroupAction.parallel(
//				MoveAction.moveXBy(ob1, 50, 0.5f, Interpolation.exp5Out),
//				MoveAction.moveYBy(ob1, 50, 0.5f, Interpolation.exp5Out))
//		.setName("Parallel")
//		.setLogLevel(ActionLogger.DEBUG)
//		.addListener(ob1);
//		
//		actionManager.addAction(parallel);
//	}
//	
//	public void parallelWithOffsetTest() {
//		TestObject testObject = testObjectController.create();
//		testObjectController.center(testObject, hudViewport);
//		
//		GroupAction parallelWithOffset = GroupAction.parallel(
//				0.5f,
//				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear),
//				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.linear))
//		.addListener(testObject);
//		
//		actionManager.addAction(parallelWithOffset);
//	}
//	
//	public void sequenceTest() {
//		TestObject testObject = testObjectController.create();
//		testObjectController.center(testObject, hudViewport);
//		
//		GroupAction sequence = GroupAction.sequence(
//				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.exp5Out),
//				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.exp5Out),
//				MoveAction.moveXBy(testObject, -50, 0.5f, Interpolation.exp5Out))
//		.setName("Sequence")
//		.setLogLevel(ActionLogger.DEBUG)
//		.addListener(testObject);
//		
//		actionManager.addAction(sequence);
//	}
//	
//	public void restartTest() {
//		TestObject testObject = testObjectController.create();
//		testObjectController.center(testObject, hudViewport);
//		
//		GroupAction sequence = GroupAction.sequence(
//				MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.exp5Out),
//				MoveAction.moveYBy(testObject, 50, 0.5f, Interpolation.exp5Out),
//				MoveAction.moveXBy(testObject, -50, 0.5f, Interpolation.exp5Out))
//		.setName("Sequence")
//		.addListener(testObject)
//		.setLogLevel(ActionLogger.DEBUG);
//		
//		ActionListener<MoveAction> restartListener = new ActionAdapter<MoveAction>() {
//			boolean restart = true;
//			
//			@Override
//			public void actionEnd(MoveAction a) {
//				if(restart) {
//					restart = false;
//					sequence.restart();
//				}
//			}
//		};
//
//		((MoveAction)sequence.getActions().get(1)).addListener(restartListener);
//		
//		actionManager.addAction(sequence);
//	}
//	
//	public void nestedTest() {
//		TestObject ob1 = testObjectController.create();
//		testObjectController.center(ob1, hudViewport);
//		ob1.setX(ob1.getX() + 25 + 10);
//		
//		TestObject ob2 = testObjectController.create();
//		testObjectController.center(ob2, hudViewport);
//		ob2.setX(ob2.getX() - 25 - 10);
//		
//		GroupAction one = GroupAction.parallel(
//				MoveAction.moveXBy(ob1, 50, 1f, Interpolation.exp5Out),
//				MoveAction.moveYBy(ob1, 50, 1f, Interpolation.exp5Out));
//		
//		GroupAction two = GroupAction.parallel(
//				MoveAction.moveXBy(ob2, -50, 1f, Interpolation.exp5Out),
//				MoveAction.moveYBy(ob2, 50, 1f, Interpolation.exp5Out));
//		
//		GroupAction group = GroupAction.parallel(one, two);
//		group.addListener(ob1);
//		group.addListener(ob2);
//		
//		actionManager.addAction(group);
//	}
//	
//	public void parallelReverseTest() {
//		TestObject ob1 = testObjectController.create();
//		TestObject ob2 = testObjectController.create();
//		
//		testObjectController.center(ob1, hudViewport);
//		testObjectController.center(ob2, hudViewport);
//		
//		ob1.setX(ob1.getX() - ob1.width - 10);
//		ob2.setX(ob2.getX() + ob1.width + 10);
//		
//		GroupAction parallel = GroupAction.parallel(
//				0.8f,
//				MoveAction.moveYBy(ob1, 100, 1f, Interpolation.linear),
//				MoveAction.moveYBy(ob2, -100, 1f, Interpolation.linear))
//		.addListener(ob1)
//		.addListener(ob2)
//		.setReverse(false);
//		
//		actionManager.addAction(parallel);
//	}
//	
//	public void sequenceReverseTest() {
//		TestObject ob1 = testObjectController.create();
//				
//		testObjectController.center(ob1, hudViewport);
//		
//		GroupAction sequence = GroupAction.sequence(
//				MoveAction.moveXBy(ob1, 100, 0.5f, Interpolation.linear),
//				MoveAction.moveYBy(ob1, -100, 0.5f, Interpolation.linear),
//				MoveAction.moveXBy(ob1, -100, 0.5f, Interpolation.linear),
//				MoveAction.moveYBy(ob1, 100, 0.5f, Interpolation.linear))
//		.addListener(ob1)
//		.setReverse(true);
//
//		actionManager.addAction(sequence);
//	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		for(int i = 0; i < testObjects.size; i++) {
			testObjects.get(i).draw(shapeDrawer);
		}
	}
	
}
