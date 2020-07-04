package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.badlogic.gdx.utils.reflect.Method;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.GroupAction;
import com.vabrant.actionsystem.actions.MoveAction;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.test.TestObject;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class MoveActionTest extends ActionSystemTestListener{
	
	boolean reverseBackToStart = false;
	boolean reverse = false;
	float startX;
	float startY;
	float endX;
	float endY;
	float amountX;
	float amountY;
	TestObject testObject;
	ActionListener<MoveAction> listener;
	
	public MoveActionTest() {
		ActionLogger.useSysOut();
		ActionPools.logger.setLevel(ActionLogger.DEBUG);
		
		testObject = new TestObject();
		testObject.setSize(50, 50);
		listener = getEndListener();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		reset();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.R:
				reset();
				break;
			case Keys.NUMPAD_1:
				moveYByTest();
				break;
			case Keys.NUMPAD_2:
				moveXByTest();
				break;
			case Keys.NUMPAD_3:
				moveByTest();
				break;
			case Keys.NUMPAD_4:
				moveByAngleTest();
				break;
			case Keys.NUMPAD_5:
				moveByAngleRadTest();
				break;
			case Keys.NUMPAD_6:
				moveXToTest();
				break;
			case Keys.NUMPAD_7:
				moveYToTest();
				break;
			case Keys.NUMPAD_8:
				moveToTest();
				break;
			case Keys.NUMPAD_9:
				try {
					hasConflictTest();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				break;
			case Keys.NUMPAD_0:
				pingPongTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	private void reset() {
		float x = (viewport.getWorldWidth() - testObject.width) / 2;
		float y = (viewport.getWorldHeight() - testObject.height) / 2;
		
		testObject.setX(x);
		testObject.setY(y);
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		amountX = 0;
		amountY = 0;
	}

	private ActionListener<MoveAction> getEndListener() {
		return new ActionAdapter<MoveAction>() {
			@Override
			public void actionEnd(MoveAction a) {
				System.out.println();
				System.out.println(a.getName());
				System.out.println("StartX: " + startX);
				System.out.println("StartY: " + startY);
				System.out.println("EndX: " + endX);
				System.out.println("EndY: " + endY);
				System.out.println("X: " + testObject.getX());
				System.out.println("Y: " + testObject.getY());
			}
		};
	}
	
	public void moveYByTest() {
		reset();
		amountY = 50;
		startY = testObject.getY();
		endY = startY + amountY; 
//		actionManager.addAction(
//				MoveAction.moveYBy(testObject, amountY, 1f, Interpolation.exp5Out)
//					.addListener(listener)
//					.setName("MoveYBy")
//					.reverseBackToStart(reverseBackToStart)
//					.setReverse(reverse)
//					.setLogLevel(ActionLogger.DEBUG)
//				);
		actionManager.addAction(
				GroupAction.sequence(
						MoveAction.moveXBy(testObject, 50, 1f, Interpolation.exp5Out)
							.setName("MoveXBy")
							.setLogLevel(ActionLogger.DEBUG),
						MoveAction.moveYBy(testObject, 50, 1f, Interpolation.exp5Out)
							.setName("MoveYBy")
							.setLogLevel(ActionLogger.DEBUG))
				);
	}
	
	public void moveXByTest() {
		reset();
		amountX = 50;
		startX = testObject.getX();
		endX = startX + amountX;
		actionManager.addAction(
				MoveAction.moveXBy(testObject, amountX, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveXBy")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void moveByTest() {
		reset();
		amountX = 50;
		amountY = 20;
		startX = testObject.getX();
		startY = testObject.getY();
		endX = startX + amountX;
		endY = startY + amountY;
		actionManager.addAction(
				MoveAction.moveBy(testObject, amountX, amountY, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveBy")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void moveByAngleTest() {
		reset();
		float angle = 135;
		float amount = 50;
		amountX = amount * MathUtils.cosDeg(angle);
		amountY = amount * MathUtils.sinDeg(angle);
		startX = testObject.getX();
		startY = testObject.getY();
		endX = startX + amountX;
		endY = startY + amountY;
		actionManager.addAction(
				MoveAction.moveByAngleDeg(testObject, angle, amount, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveByAngle")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void moveByAngleRadTest() {
		reset();
		float amount = 50;
		float rad = 2.356194f;
		float angle = MathUtils.radiansToDegrees * rad;
		amountX = amount * MathUtils.cosDeg(angle);
		amountY = amount * MathUtils.sinDeg(angle);
		startX = testObject.getX();
		startY = testObject.getY();
		endX = startX + amountX;
		endY = startY + amountY;
		actionManager.addAction(
				MoveAction.moveByAngleRad(testObject, rad, amount, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveByAngleRadians")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void moveXToTest() {
		reset();
		startX = testObject.getX();
		endX = 600;
		actionManager.addAction(
				MoveAction.moveXTo(testObject, endX, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveXto")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void moveYToTest() {
		reset();
		startY = testObject.getY();
		endY = 200;
		actionManager.addAction(
				MoveAction.moveYTo(testObject, endY, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveYTo")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void moveToTest() {
		reset();
		startX = testObject.getX();
		startY = testObject.getY();
		endX = 0;
		endY = 0;
		actionManager.addAction(
				MoveAction.moveTo(testObject, endX, endY, 1f, Interpolation.exp5Out)
					.addListener(listener)
					.setName("MoveTo")
					.reverseBackToStart(reverseBackToStart)
					.setReverse(reverse)
				);
	}
	
	public void hasConflictTest() throws Exception {
		reset();
		
		MoveAction action = MoveAction.obtain();
		MoveAction moveXAction = MoveAction.obtain()
				.moveXTo(0);
		MoveAction moveYAction = MoveAction.obtain()
				.moveYTo(0);
		MoveAction moveAction = MoveAction.obtain()
				.moveTo(0, 0);
		
		Method hasConflictMethod = ClassReflection.getDeclaredMethod(MoveAction.class, "hasConflict", Action.class);
		hasConflictMethod.setAccessible(true);
		
		System.out.println();
		
		//Check if the x is already being used
		System.out.println("x is being used");
		action.moveXTo(0);
		System.out.println("HasConflictWithActionMovingX: " + (boolean)hasConflictMethod.invoke(action, moveXAction));
		System.out.println("HasConflictWithActionMovingY: " + (boolean)hasConflictMethod.invoke(action, moveYAction));
		System.out.println("HasConflictWithActionMoveXY: " + (boolean)hasConflictMethod.invoke(action, moveAction));
		
		ActionPools.free(action);

		System.out.println("y is being used");
		action = MoveAction.obtain().moveYTo(0);
		
		System.out.println("HasConflictWithActionMovingX: " + (boolean)hasConflictMethod.invoke(action, moveXAction));
		System.out.println("HasConflictWithActionMovingY: " + (boolean)hasConflictMethod.invoke(action, moveYAction));
		System.out.println("HasConflictWithActionMovingXY: " + (boolean)hasConflictMethod.invoke(action, moveAction));
		
		//No need to test anymore should work
		
		ActionPools.free(action);
		ActionPools.free(moveAction);
		ActionPools.free(moveXAction);
		ActionPools.free(moveYAction);
	}
	
	public void pingPongTest() {
		reset();
		
		actionManager.addAction(
				RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 100, 1f, Interpolation.linear), 
						2)
				.pingPong(true));
	}
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
