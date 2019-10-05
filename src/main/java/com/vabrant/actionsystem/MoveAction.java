package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MoveAction extends PercentAction<Movable> {
	
	public static MoveAction getAction() {
		return getAction(MoveAction.class);
	}
	
	public static MoveAction moveXBy(Movable movable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveXBy(amount);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveYBy(Movable movable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveYBy(amount);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveBy(Movable movable, float xAmount, float yAmount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveBy(xAmount, yAmount);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveByAngle(Movable movable, float angle, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveByAngle(angle, amount);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveXTo(Movable movable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveXTo(end);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveYTo(Movable movable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveYTo(end);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveTo(Movable movable, float xEnd, float yEnd, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveTo(xEnd, yEnd);
		action.set(movable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction setX(Movable movable, float x) {
		MoveAction action = getAction();
		action.moveXTo(x);
		action.set(movable, 0, false, null);
		return action;
	}
	
	public static MoveAction setY(Movable movable, float y) {
		MoveAction action = getAction();
		action.moveYTo(y);
		action.set(movable, 0, false, null);
		return action;
	}
	
	public static MoveAction setPosition(Movable movable, float x, float y) {
		MoveAction action = getAction();
		action.moveTo(x, y);
		action.set(movable, 0, false, null);
		return action;
	}
	
	private enum XMoveType{
		MOVE_X_TO,
		MOVE_X_BY,
		NONE
	}
	
	private enum YMoveType{
		MOVE_Y_TO,
		MOVE_Y_BY,
		NONE
	}

	private boolean setupX = true;
	private boolean setupY = true;
	private boolean restartMoveXByFromEnd;
	private boolean restartMoveYByFromEnd;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private float yAmount;
	private float xAmount;
	private XMoveType xMoveType = XMoveType.NONE;
	private YMoveType yMoveType = YMoveType.NONE;
	
	public void moveXTo(float end) {
		this.xEnd = end;
		xMoveType = XMoveType.MOVE_X_TO;
	}
	
	public void moveYTo(float end) {
		this.yEnd = end;
		yMoveType = YMoveType.MOVE_Y_TO;
	}
	
	public void moveTo(float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xMoveType = XMoveType.MOVE_X_TO;
		yMoveType = YMoveType.MOVE_Y_TO;
	}

	public MoveAction moveByAngle(float angle, float amount) {
		angle %= 360;
		xAmount = amount * MathUtils.cosDeg(angle);
		yAmount = amount * MathUtils.sinDeg(angle);
		xMoveType = XMoveType.MOVE_X_BY;
		yMoveType = YMoveType.MOVE_Y_BY;
		return this;
	}
	
	public MoveAction moveXBy(float amount) {
		xAmount = amount;
		xMoveType = XMoveType.MOVE_X_BY;
		return this;
	}

	public MoveAction moveYBy(float amount) {
		yAmount = amount;
		yMoveType = YMoveType.MOVE_Y_BY;
		return this;
	}
	
	public MoveAction moveBy(float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xMoveType = XMoveType.MOVE_X_BY;
		yMoveType = YMoveType.MOVE_Y_BY;
		return this;
	}

	public MoveAction restartMoveXByFromEnd() {
		restartMoveXByFromEnd = true;
		return this;
	}
	
	public MoveAction restartMoveYByFromEnd() {
		restartMoveYByFromEnd = true;
		return this;
	}

	@Override
	protected void percent(float percent) {
		switch(xMoveType) {
			case MOVE_X_BY:
			case MOVE_X_TO:
				percentable.setX(MathUtils.lerp(xStart, xEnd, percent));
				break;
		}
		
		switch(yMoveType) {
			case MOVE_Y_BY:
			case MOVE_Y_TO:
				percentable.setY(MathUtils.lerp(yStart, yEnd, percent));
				break;
		}
	}
	
	private void setup() {
		if(setupX) {
			switch(xMoveType) {
				case MOVE_X_BY:
					xStart = percentable.getX();
					xEnd = xStart + xAmount;
					break;
				case MOVE_X_TO:
					xStart = percentable.getX();
					break;
			}
		}
		
		if(setupY) {	
			switch(yMoveType) {
				case MOVE_Y_BY:
					yStart = percentable.getY();
					yEnd = yStart + yAmount;
					break;
				case MOVE_Y_TO:
					yStart = percentable.getY();
					break;
			}
		}
	}
	
	@Override
	public void start() {
		super.start();
		setup();
	}
	
	@Override
	public void restart() {
		super.restart();
		setup();
	}
	
	@Override
	public void end() {
		super.end();
		if(!xMoveType.equals(XMoveType.MOVE_X_BY) || xMoveType.equals(XMoveType.MOVE_X_BY) && !restartMoveXByFromEnd) setupX = false;
		if(!yMoveType.equals(YMoveType.MOVE_Y_BY) || yMoveType.equals(YMoveType.MOVE_Y_BY) && !restartMoveYByFromEnd) setupY = false;
	}
	
	@Override
	public void reset() {
		super.reset();
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		setupX = true;
		setupY = true;
		restartMoveXByFromEnd = false;
		restartMoveYByFromEnd = false;
		xMoveType = XMoveType.NONE;
		yMoveType = YMoveType.NONE;
	}

}
