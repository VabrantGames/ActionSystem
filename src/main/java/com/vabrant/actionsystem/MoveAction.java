package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MoveAction extends PercentAction {
	
	public static MoveAction getAction() {
		return getAction(MoveAction.class);
	}
	
	public static MoveAction moveXBy(Movable movable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveXBy(movable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveYBy(Movable movable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveYBy(movable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveBy(Movable movable, float xAmount, float yAmount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveBy(movable, xAmount, yAmount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveByAngle(Movable movable, float angle, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveByAngle(movable, angle, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveXTo(Movable movable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveXTo(movable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveYTo(Movable movable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveYTo(movable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction moveTo(Movable movable, float xEnd, float yEnd, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveTo(movable, xEnd, yEnd);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static MoveAction setX(Movable movable, float x) {
		MoveAction action = getAction();
		action.moveXTo(movable, x);
		action.set(0, false, null);
		return action;
	}
	
	public static MoveAction setY(Movable movable, float y) {
		MoveAction action = getAction();
		action.moveYTo(movable, y);
		action.set(0, false, null);
		return action;
	}
	
	public static MoveAction setPosition(Movable movable, float x, float y) {
		MoveAction action = getAction();
		action.moveTo(movable, x, y);
		action.set(0, false, null);
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
	private Movable movable;
	private XMoveType xMoveType = XMoveType.NONE;
	private YMoveType yMoveType = YMoveType.NONE;
	
	public void moveXTo(Movable movable, float end) {
		this.movable = movable;
		this.xEnd = end;
		xMoveType = XMoveType.MOVE_X_TO;
	}
	
	public void moveYTo(Movable movable, float end) {
		this.movable = movable;
		this.yEnd = end;
		yMoveType = YMoveType.MOVE_Y_TO;
	}
	
	public void moveTo(Movable movable, float xEnd, float yEnd) {
		this.movable = movable;
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xMoveType = XMoveType.MOVE_X_TO;
		yMoveType = YMoveType.MOVE_Y_TO;
	}

	public MoveAction moveByAngle(Movable movable, float angle, float amount) {
		this.movable = movable;
		angle %= 360;
		xAmount = amount * MathUtils.cosDeg(angle);
		yAmount = amount * MathUtils.sinDeg(angle);
		xMoveType = XMoveType.MOVE_X_BY;
		yMoveType = YMoveType.MOVE_Y_BY;
		return this;
	}
	
	public MoveAction moveXBy(Movable movable, float amount) {
		this.movable = movable;
		xAmount = amount;
		xMoveType = XMoveType.MOVE_X_BY;
		return this;
	}

	public MoveAction moveYBy(Movable movable, float amount) {
		this.movable = movable;
		yAmount = amount;
		yMoveType = YMoveType.MOVE_Y_BY;
		return this;
	}
	
	public MoveAction moveBy(Movable movable, float xAmount, float yAmount) {
		this.movable = movable;
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
				movable.setX(MathUtils.lerp(xStart, xEnd, percent));
				break;
		}
		
		switch(yMoveType) {
			case MOVE_Y_BY:
			case MOVE_Y_TO:
				movable.setY(MathUtils.lerp(yStart, yEnd, percent));
				break;
		}
	}
	
	@Override
	public void start() {
		super.start();
		if(setupX) {
			switch(xMoveType) {
				case MOVE_X_BY:
					xStart = movable.getX();
					xEnd = xStart + xAmount;
					break;
				case MOVE_X_TO:
					xStart = movable.getX();
					break;
			}
		}
		
		if(setupY) {	
			switch(yMoveType) {
				case MOVE_Y_BY:
					yStart = movable.getY();
					yEnd = yStart + yAmount;
					break;
				case MOVE_Y_TO:
					yStart = movable.getY();
					break;
			}
		}
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
		movable = null;
		setupX = true;
		setupY = true;
		restartMoveXByFromEnd = false;
		restartMoveYByFromEnd = false;
		xMoveType = XMoveType.NONE;
		yMoveType = YMoveType.NONE;
	}

}
