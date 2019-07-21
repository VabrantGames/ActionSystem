package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.DelayAction;
import com.vabrant.actionsystem.GroupAction;
import com.vabrant.actionsystem.MoveAction;
import com.vabrant.actionsystem.RepeatAction;

public class MoveToTest extends ActionSystemTestScreen{
	
	private int currentTest = 9;
	private int maxTest = 9;
	private ActionSystemTestObject testObject;
	
	public MoveToTest() {
		debug = true;
		testObject = new ActionSystemTestObject();
		testObject.setX(100);
		testObject.setY(100);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.SPACE:
				runTest(currentTest);
				break;
			case Keys.R:
				testObject.setX(100);
				testObject.setY(100);
				break;
		}
		return super.keyDown(keycode);
	}
	
	private void runTest(int testNum) {
		switch(testNum) {
			case 0:
				moveXToTest();
				break;
			case 1:
				moveYToTest();
				break;
			case 2:
				moveToTest();
				break;
			case 3:
				repeatMoveXToTest();
				break;
			case 4:
				repeatMoveYToTest();
				break;
			case 5:
				repeatMoveToTest();
				break;
			case 6:
				setPositionTest();
				break;
			case 7:
				setPositionMultipleTest();
				break;
			case 8:
				setXTest();
				break;
			case 9:
				setYTest();
				break;
		}
	}
	
	private void moveXToTest() {
		actionManager.addAction(MoveAction.moveXTo(testObject, 300, 0.5f, false, Interpolation.linear));
	}
	
	private void moveYToTest() {
		actionManager.addAction(MoveAction.moveYTo(testObject, 300, 0.5f, false, Interpolation.linear));
	}
	
	private void moveToTest() {
		actionManager.addAction(MoveAction.moveTo(testObject, 300, 200, 1f, false, Interpolation.linear));
	}
	
	private void repeatMoveXToTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveXTo(testObject, 200, 0.5f, false, Interpolation.linear))
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setRepeatAmount(2);
		actionManager.addAction(repeat);
	}
	
	private void repeatMoveYToTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveYTo(testObject, 200, 0.5f, false, Interpolation.linear))
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setRepeatAmount(2);
		actionManager.addAction(repeat);
	}
	
	private void repeatMoveToTest() {
		GroupAction group = GroupAction.getAction()
				.add(MoveAction.moveTo(testObject, 300, 200, 0.5f, false, Interpolation.linear))
				.add(DelayAction.delay(0.25f));
		RepeatAction repeat = RepeatAction.repeat(group).setRepeatAmount(2);
		actionManager.addAction(repeat);
	}
	
	private void setXTest() {
		actionManager.addAction(MoveAction.setX(testObject, 300));
	}
	
	private void setYTest() {
		actionManager.addAction(MoveAction.setY(testObject, 0));
	}
	
	private void setPositionTest() {
		actionManager.addAction(MoveAction.setPosition(testObject, 300, 300));
	}
	
	private void setPositionMultipleTest() {
		GroupAction group = GroupAction.getAction()
				.sequence()
				.add(MoveAction.setPosition(testObject, 300, 300))
				.add(DelayAction.delay(0.25f))
				.add(MoveAction.setPosition(testObject, 200, 300))
				.add(DelayAction.delay(0.25f))
				.add(MoveAction.setPosition(testObject, 200, 200))
				.add(DelayAction.delay(0.25f))
				.add(MoveAction.setPosition(testObject, 300, 200))
				.add(DelayAction.delay(0.25f));
		actionManager.addAction(group);
	}

	@Override
	public void debug(ShapeRenderer renderer) {
		renderer.set(ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		renderer.rect(testObject.getX(), testObject.getY(), 10, 10);
	}

}
