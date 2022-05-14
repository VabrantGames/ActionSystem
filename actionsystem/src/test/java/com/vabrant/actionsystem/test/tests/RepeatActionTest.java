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
import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;
import com.vabrant.actionsystem.logger.ActionLogger;
import com.vabrant.actionsystem.actions.ColorAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class RepeatActionTest extends ActionSystemTestListener {

	private LabelTextFieldIntWidget repeatAmountWidget;
	private TestObject testObject;
	
	@Override
	public void create() {
		super.create();
		testObject = new TestObject();
		testObject.setSize(200, 200);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		float x = (hudViewport.getWorldWidth() - testObject.width) / 2;
		float y = (hudViewport.getWorldHeight() - testObject.height) / 2;
		testObject.setX(x);
		testObject.setY(y);
	}
	
	@Override
	public void createHud(Table root, Skin skin) {
		LabelStyle headerStyle = new LabelStyle(skin.get(LabelStyle.class));
		headerStyle.fontColor = Color.BLACK;
		
		Label valuesLabel = new Label("Values", headerStyle);
		root.add(valuesLabel).left();
		root.row();
		repeatAmountWidget = new LabelTextFieldIntWidget("repeatAmount: ", skin, root, 1);
		root.row();
	}

	@Override
	public void createTests() {
		super.createTests();
		
		addTest(new ActionTest("RepeatTeset") {
			@Override
			public Action<?> run() {
				testObject.setColor(Color.BLACK);
				return RepeatAction.repeat(
						ColorAction.changeColor(testObject, Color.RED, 1f, Interpolation.linear),
						repeatAmountWidget.getValue())
						.setName("RepeatTest");
			}
		});
		
		addTest(new ActionTest("Continuous(Fixed)") {
			@Override
			public Action<?> run() {
				testObject.setColor(Color.BLACK);
				return RepeatAction.continuous(
						ColorAction.changeColor(testObject, Color.RED, 1.2f, Interpolation.smooth2)
							.reverseBackToStart(true))
						.setName("RepeatTest");
			}
		});
		
		addTest(new ActionTest("RestartTest(Fixed)") {
			@Override
			public Action<?> run() {
				testObject.setX((hudViewport.getWorldWidth() - testObject.width) / 2);
				RepeatAction repeat = RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear)
							.setName("Move")
							.setLogLevel(ActionLogger.LogLevel.DEBUG),
						2)
						.setName("Repeat")
						.setLogLevel(ActionLogger.LogLevel.DEBUG);

//				ActionListener<MoveAction> listener = new ActionAdapter<MoveAction>() {
//					boolean restart = true;
//
//					@Override
//					public void actionEnd(MoveAction a) {
//						if(restart && repeat.getCount() == 1) {
//							restart = !restart;
//							repeat.restart();
//						}
//					}
//				};
//				((MoveAction)repeat.getAction()).addListener(listener);

				ActionListener listener = new ActionListener() {
					boolean restart = true;

					@Override
					public void onEvent(ActionEvent e) {
						if (restart && repeat.getCount() == 1) {
							restart = !restart;
							repeat.restart();
						}
					}
				};
				((MoveAction)repeat.getAction()).subscribeToEvent(ActionEvent.END_EVENT, listener);

				return repeat;
			}
		});
		
		addTest(new ActionTest("PingPongTest(Fixed)") {
			@Override
			public Action<?> run() {
				return RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 100, 1f, Interpolation.linear), 
						2)
						.setName("PingPongTest")
						.pingPong(true);
			}
		});
		
		addTest(new ActionTest("NestedRepeatTest(Fixed)") {
			@Override
			public Action<?> run() {
				testObject.setColor(Color.BLACK);
				return RepeatAction.repeat(
						RepeatAction.repeat(
								ColorAction.changeColor(
										testObject, Color.RED, 1f, Interpolation.linear)
								.reverseBackToStart(false)));
			}
		});
	}

	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}
}
