package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MoveByAction extends TimeAction {

	private boolean restartFromEnd;
	private boolean moveX;
	private boolean moveY;
	private float xAmount;
	private float yAmount;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private Movable movable;
	
	public void moveByAngle(Movable movable, float angle, float amount) {
		this.movable = movable;
		angle %= 360;
		xAmount = amount * MathUtils.cosDeg(angle);
		yAmount = amount * MathUtils.sinDeg(angle);
		moveX = true;
		moveY = true;
	}

	public MoveByAction moveXBy(Movable movable, float amount) {
		this.movable = movable;
		xAmount = amount;
		moveX = true;
		return this;
	}
	
	public void moveYBy(Movable movable, float amount) {
		this.movable = movable;
		yAmount = amount;
		moveY = true;
	}
	
	public void moveBy(Movable movable, float xAmount, float yAmount) {
		this.movable = movable;
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		moveX = true;
		moveY = true;
	}
	
	protected void setupX() {
		xStart = movable.getX();
		xEnd = xStart + xAmount;
	}
	
	protected void setupY() {
		yStart = movable.getY();
		yEnd = yStart + yAmount;
	}
	
	public MoveByAction restartXFromCurrentPosition() {
		restartFromEnd = true;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		if(moveX) movable.setX(MathUtils.lerp(xStart, xEnd, percent));
		if(moveY) movable.setY(MathUtils.lerp(yStart, yEnd, percent));
	}
	
	@Override
	protected void start() {
		super.start();
		
		if(moveX) setupX();
		if(moveY) setupY();
		
		if(xStart == 0 && xEnd == 0) {
			moveX = false;
		}
		else {
			moveX = true;
		}
		
		if(yStart == 0 && yEnd == 0) {
			moveY = false;
		}
		else {
			moveY = true;
		}
	}
	
	@Override
	public void restart() {
		super.restart();
		if(!restartFromEnd) {
			if(moveX) movable.setX(xStart);
			if(moveY) movable.setY(yStart);
		}
	}

	@Override
	public void reset() {
		super.reset();
		restartFromEnd = true;
		moveX = false;
		moveY = false;
		xAmount = 0;
		xStart = 0;
		xEnd = 0;
		yAmount = 0;
		yStart = 0;
		yEnd = 0;
		movable = null;
	}
	
	public static MoveByAction moveByAngle(Movable movable, float angle, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveByAction action = getAction(MoveByAction.class);
		action.moveByAngle(movable, angle, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveByAction moveXBy(Movable movable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveByAction action = getAction(MoveByAction.class);
		action.moveXBy(movable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}

	public static MoveByAction moveYBy(Movable movable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveByAction action = getAction(MoveByAction.class);
		action.moveYBy(movable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveByAction moveBy(Movable movable, float xAmount, float yAmount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveByAction action = getAction(MoveByAction.class);
		action.moveBy(movable, xAmount, yAmount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
}
