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

public class MoveActionTest extends ActionSystemTestListener {
	
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
	public void createTests() {
		addTest(new ActionTest("MoveXBy") {
			@Override
			public Action<?> run() {
				reset();
				startX = testObject.getX();
				endX = 400;
				MoveAction action = MoveAction.moveXTo(testObject, endX, 1f, Interpolation.exp5Out)
							.addListener(listener)
							.setName("MoveXto")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
				actionManager.addAction(action);
				return action;
			}
		});
		
		//Is this test necessary?
		addTest(new ActionTest("MoveYBy") {
			@Override
			public Action<?> run() {
				reset();
				amountY = 50;
				startY = testObject.getY();
				endY = startY + amountY; 
				MoveAction action = MoveAction.moveYBy(testObject, amountY, 1f, Interpolation.exp5Out)
						.addListener(listener)
						.setName("MoveYBy")
						.reverseBackToStart(reverseBackToStart)
						.setReverse(reverse)
						.setLogLevel(ActionLogger.DEBUG);
				
				actionManager.addAction(action);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveBy") {
			@Override
			public Action<?> run() {
				reset();
				amountX = 50;
				amountY = 20;
				startX = testObject.getX();
				startY = testObject.getY();
				endX = startX + amountX;
				endY = startY + amountY;
				MoveAction action = MoveAction.moveBy(testObject, amountX, amountY, 1f, Interpolation.exp5Out)
							.addListener(listener)
							.setName("MoveBy")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
				actionManager.addAction(action);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveByAngle") {
			@Override
			public Action<?> run() {
				reset();
				float angle = 135;
				float amount = 50;
				amountX = amount * MathUtils.cosDeg(angle);
				amountY = amount * MathUtils.sinDeg(angle);
				startX = testObject.getX();
				startY = testObject.getY();
				endX = startX + amountX;
				endY = startY + amountY;
				MoveAction action = MoveAction.moveByAngleDeg(testObject, angle, amount, 1f, Interpolation.exp5Out)
							.addListener(listener)
							.setName("MoveByAngle")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
				actionManager.addAction(action);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveByAngleRad") {
			@Override
			public Action<?> run() {
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
				MoveAction action = MoveAction.moveByAngleRad(testObject, rad, amount, 1f, Interpolation.exp5Out)
							.addListener(listener)
							.setName("MoveByAngleRadians")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
				actionManager.addAction(action);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveXTo") {
			@Override
			public Action<?> run() {
				reset();
				testObject.setX(0);
				startX = testObject.getX();
				endX = 500;
				MoveAction action = MoveAction.moveXTo(testObject, endX, 1f, Interpolation.exp5Out)
							.addListener(listener)
							.setName("MoveXto")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
				actionManager.addAction(action);
				return action;
			}
		});
		
		addTest(new ActionTest("MoveYTo") {
			@Override

			public Action<?> run() {
				reset();
				startY = testObject.getY();
				endY = 200;
				MoveAction action = MoveAction.moveYTo(testObject, endY, 1f, Interpolation.exp5Out)
							.addListener(listener)
							.setName("MoveYTo")
							.reverseBackToStart(reverseBackToStart)
							.setReverse(reverse);
				actionManager.addAction(action);
				return action;
			}
		});
		
		addTest(new ActionTest("PingPong") {
			@Override
			public Action<?> run() {
				reset();
				RepeatAction action = RepeatAction.repeat(
						MoveAction.moveXBy(testObject, 100, 1f, Interpolation.linear), 6)
						.pingPong(true);
				actionManager.addAction(action);
				return action;
			}
		});
		
	}
	
	public void reset() {
		float x = (hudViewport.getWorldWidth() - testObject.width) / 2;
		float y = (hudViewport.getWorldHeight() - testObject.height) / 2;
		
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
	
	@Override
	public void draw(SpriteBatch batch, ShapeDrawer shapeDrawer) {
		testObject.draw(shapeDrawer);
	}

}
