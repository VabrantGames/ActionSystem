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
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RotateAction;
import com.vabrant.actionsystem.actions.ScaleAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

/**
 * @author John Barton
 *
 */
public class GroupActionTest extends ActionSystemTestListener {
	
	private LabelTextFieldFloatWidget parallelOffsetWidget;
	private LabelCheckBoxWidget singleTestObjectWidget;

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
		singleTestObjectWidget = new LabelCheckBoxWidget("singleTestObject: ", skin, root);
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

				if(!singleTestObjectWidget.isChecked()) {
					for(int i = 0; i < testObjects.size; i++) {
						groupAction.add(MoveAction.moveYBy(testObjects.get(i), 100, 2f, Interpolation.exp5Out));
					}
				}
				else {
					float y = testObjects.get(0).getY();
					for(int i = 0; i < 5; i++) {
						groupAction.add(MoveAction.moveYTo(testObjects.get(0), y + 50, 1f, Interpolation.exp5Out));
					}
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
		
		addTest(new ActionTest("RestartTest(Fixed)") {
			@Override
			public Action<?> run() {
				resetY();
				
				for(int i = 0; i < testObjects.size; i++) {
					testObjects.get(i).setRotation(45);
				}
				
				GroupAction groupAction = GroupAction.obtain();
				groupAction.sequence();
				
				if(!singleTestObjectWidget.isChecked()) {
					for(int i = 0; i < testObjects.size; i++) {
						groupAction.add(MoveAction.moveYBy(testObjects.get(i), 150f, 0.5f, Interpolation.exp5Out));
					}
				}
				else {
					for(int i = 0; i < 5; i++) {
						groupAction.add(MoveAction.moveYBy(testObjects.get(0), 20, 0.5f, Interpolation.exp5Out));
					}
				}

				
//				ActionListener restartListener = new ActionAdapter() {
//					boolean restart = true;
//
//					@Override
//					public void actionEnd(Action a) {
//						if(restart) {
//							restart = false;
//							groupAction.restart();
//						}
//					}
//				};
//
//				groupAction.getActions().get(1).addListener(restartListener);

				ActionListener listener = new ActionListener() {
					boolean restart = true;

					@Override
					public void onEvent(ActionEvent e) {
						if (restart) {
							restart = false;
							groupAction.restart();
						}
					}
				};
				groupAction.getActions().get(1).subscribeToEvent(ActionEvent.END_EVENT, listener);

				return groupAction;
			}
		});

		addTest(new ActionTest("NestedParallelTest(Fixed)") {
			@Override
			public Action<?> run() {
				resetY();
				
				GroupAction g1 = GroupAction.obtain()
						.parallel()
						.add(MoveAction.moveYBy(testObjects.get(0), 50f, 0.5f, Interpolation.linear))
						.add(RotateAction.rotateBy(testObjects.get(0), 90, 0.5f, Interpolation.linear));
				
				GroupAction g2 = GroupAction.obtain()
						.parallel()
						.add(MoveAction.moveYBy(testObjects.get(1), 50f, 0.5f, Interpolation.linear))
						.add(RotateAction.rotateBy(testObjects.get(1), 90f, 0.5f, Interpolation.linear));
				
				return GroupAction.sequence(g1, g2);
			}
		});
		
		addTest(new ActionTest("NestedParallelTest(Fixed)") {
			@Override
			public Action<?> run() {
				resetY();
				
				GroupAction g1 = GroupAction.obtain()
						.parallel()
						.add(MoveAction.moveYBy(testObjects.get(0), 50f, 0.5f, Interpolation.linear))
						.add(RotateAction.rotateBy(testObjects.get(0), 90, 0.5f, Interpolation.linear));
				
				GroupAction g2 = GroupAction.obtain()
						.parallel()
						.add(MoveAction.moveYBy(testObjects.get(1), 50f, 0.5f, Interpolation.linear))
						.add(RotateAction.rotateBy(testObjects.get(1), 90f, 0.5f, Interpolation.linear));
				
				return GroupAction.sequence(g1, g2);
			}
		});
		
		addTest(new ActionTest("ReverseTest") {
			@Override
			public Action<?> run() {
				return GroupAction.obtain()
						.sequence()
						.add(MoveAction.moveYBy(testObjects.get(0), 50f, 0.5f, Interpolation.linear))
						.add(ScaleAction.scaleXBy(testObjects.get(0), 1f, 0.5f, Interpolation.linear).reverseBackToStart(true))
						.add(RotateAction.rotateBy(testObjects.get(0), 180f, 0.5f, Interpolation.elasticOut))
						.setReverse(true);
			}
		});
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		if(!singleTestObjectWidget.isChecked()) {
			for(int i = 0; i < testObjects.size; i++) {
				testObjects.get(i).draw(shapeDrawer);
			}
		}
		else {
			testObjects.get(0).draw(shapeDrawer);
		}
	}
	
}
