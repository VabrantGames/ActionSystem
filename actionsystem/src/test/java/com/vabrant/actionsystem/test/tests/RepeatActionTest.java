package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class RepeatActionTest extends ActionSystemTestListener {

	private TestObject testObject;
	
	public RepeatActionTest() {
		testObject = new TestObject();
		testObject.setSize(50, 50);
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		reset();
	}
	
	public void reset() {
		float x = (hudViewport.getWorldWidth() - testObject.width) / 2;
		float y = (hudViewport.getWorldHeight() - testObject.height) / 2;
		testObject.setX(x);
		testObject.setY(y);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_1:
				repeatTest();
				break;
			case Keys.NUMPAD_2:
				continuousTest();
				break;
			case Keys.NUMPAD_3:
				restartTest();
				break;
			case Keys.NUMPAD_4:
				nestedRepeatTest();
				break;
			case Keys.NUMPAD_5:
				pingPongTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void repeatTest() {
		reset();
		int amount = 2;
		actionManager.addAction(
				RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 50, 1f, Interpolation.exp5Out)
						.startXByFromEnd()
						.setName("Move")
						.setLogLevel(ActionLogger.DEBUG),
				amount)
				.setName("Repeat")
				.setLogLevel(ActionLogger.DEBUG)
				);
	}
	
	public void continuousTest() {
		reset();
		actionManager.addAction(
				RepeatAction.continuous(
						MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear)
						.setName("Move")
						.setLogLevel(ActionLogger.DEBUG))
				.setName("Repeat")
				.setLogLevel(ActionLogger.DEBUG)
				);
	}

	public void restartTest() {
		reset();

		RepeatAction repeat = RepeatAction.repeat(
					MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear)
					.setName("Move")
					.setLogLevel(ActionLogger.DEBUG), 
		2)
		.setName("Repeat")
		.setLogLevel(ActionLogger.DEBUG);
		
		ActionListener<MoveAction> listener = new ActionAdapter<MoveAction>() {
			boolean restart = true;
			
			@Override
			public void actionEnd(MoveAction a) {
				if(restart && repeat.getCount() == 1) {
					restart = !restart;
					repeat.restart();
				}
			}
		};
		
		((MoveAction)repeat.getAction()).addListener(listener);
		
		actionManager.addAction(repeat);
	}
	
	public void nestedRepeatTest() {
		reset();
		
		RepeatAction move = RepeatAction.repeat(MoveAction.moveXBy(testObject, 50, 0.5f, Interpolation.linear), 2)
				.setName("Inner")
				.setLogLevel(ActionLogger.DEBUG);
		
		actionManager.addAction(
				RepeatAction.repeat(move)
				.setName("Outer")
				.setLogLevel(ActionLogger.DEBUG));
	}
	
	public void pingPongTest() {
		reset();
		
		actionManager.addAction(
				RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 100, 1f, Interpolation.linear), 
						2).pingPong(true));
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}
}
