package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.RepeatAction;
import com.vabrant.testbase.TestSelectScreen;

public class MoveByTest extends ActionSystemTestScreen {
	
	private ActionSystemTestObject testObject;
	
	public MoveByTest(TestSelectScreen screen) {
		super(screen);
		
		debug = true;
		testObject = new ActionSystemTestObject();
		testObject.setX(100);
		testObject.setY(100);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.SPACE:
				repeatMoveByAngleFromEndTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	private void moveXByTest() {
		actionManager.addAction(MoveAction.moveXBy(testObject, 50, 0.5f, false, Interpolation.linear));
	}
	
	private void moveYByTest() {
		actionManager.addAction(MoveAction.moveYBy(testObject, 50, 0.5f, false, Interpolation.linear));
	}
	
	private void moveByTest() {
		actionManager.addAction(MoveAction.moveBy(testObject, 50, 50, 0.5f, false, Interpolation.linear));
	}
	
	private void moveByAngleTest() {
		actionManager.addAction(MoveAction.moveByAngle(testObject, 50, 50, 0.5f, false, Interpolation.linear));
	}
	
	private void repeatMoveXByTest() {
		 GroupAction group = GroupAction.getAction();
		 group.add(MoveAction.moveXBy(testObject, 50, 0.5f, false, Interpolation.linear));
		 group.add(DelayAction.delay(0.25f));
		 
		 RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		 
		 actionManager.addAction(repeat);
	}
	
	private void repeatMoveYByTest() {
		GroupAction group = GroupAction.getAction();
		group.add(MoveAction.moveYBy(testObject, 50, 0.5f, false, Interpolation.linear));
		group.add(DelayAction.delay(0.25f));
		 
		RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		 
		actionManager.addAction(repeat);
	}
	
	private void repeatMoveByTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveBy(testObject, 25, 50, 0.5f, false, Interpolation.linear))
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		actionManager.addAction(repeat);
	}
	
	private void repeatMoveByAngleTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveByAngle(testObject, 45, 50, 0.5f, false, Interpolation.linear))
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		actionManager.addAction(repeat);
	}
	
	private void repeatMoveXByFromEndTest() {
		 GroupAction group = GroupAction.getAction()
			 .add(MoveAction.moveXBy(testObject, 50, 0.5f, false, Interpolation.linear).restartMoveXByFromEnd())
			 .add(DelayAction.delay(0.25f));
		 RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		 actionManager.addAction(repeat);
	}

	private void repeatMoveYByFromEndTest() {
		 GroupAction group = GroupAction.getAction()
			 .add(MoveAction.moveYBy(testObject, 50, 0.5f, false, Interpolation.linear).restartMoveYByFromEnd())
			 .add(DelayAction.delay(0.25f));
		 RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		 actionManager.addAction(repeat);
	}
	
	private void repeatMoveByFromEndTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveBy(testObject, 25, 50, 0.5f, false, Interpolation.linear).restartMoveXByFromEnd().restartMoveYByFromEnd())
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		actionManager.addAction(repeat);
	}
	
	private void repeatMoveByAngleFromEndTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveByAngle(testObject, 45, 50, 0.5f, false, Interpolation.linear).restartMoveXByFromEnd().restartMoveYByFromEnd())
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setAmount(2);
		actionManager.addAction(repeat);
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		renderer.rect(testObject.getX(), testObject.getY(), 10, 10);
	}

}
