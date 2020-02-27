package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MoveAction extends PercentAction<Movable, MoveAction> {
	
	public static MoveAction getAction() {
		return getAction(MoveAction.class);
	}
	
	public static MoveAction moveXBy(Movable movable, float amount, float duration, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveXBy(amount);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction moveYBy(Movable movable, float amount, float duration, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveYBy(amount);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction moveBy(Movable movable, float xAmount, float yAmount, float duration, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveBy(xAmount, yAmount);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction moveByAngleRad(Movable movable, float radians, float amount, float duration, Interpolation interpolation) {
		return moveByAngleDeg(movable, MathUtils.radiansToDegrees * radians, amount, duration, interpolation);
	}
	
	public static MoveAction moveByAngleDeg(Movable movable, float degrees, float amount, float duration, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveByAngle(degrees, amount);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction moveXTo(Movable movable, float end, float duration, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveXTo(end);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction moveYTo(Movable movable, float end, float duration, Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveYTo(end);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction moveTo(Movable movable, float xEnd, float yEnd, float duration,  Interpolation interpolation) {
		MoveAction action = getAction();
		action.moveTo(xEnd, yEnd);
		action.set(movable, duration, interpolation);
		return action;
	}
	
	public static MoveAction setX(Movable movable, float x) {
		MoveAction action = getAction();
		action.moveXTo(x);
		action.set(movable, 0, null);
		return action;
	}
	
	public static MoveAction setY(Movable movable, float y) {
		MoveAction action = getAction();
		action.moveYTo(y);
		action.set(movable, 0, null);
		return action;
	}
	
	public static MoveAction setPosition(Movable movable, float x, float y) {
		MoveAction action = getAction();
		action.moveTo(x, y);
		action.set(movable, 0, null);
		return action;
	}

	private static final int MOVE_TO = 0;
	private static final int MOVE_BY = 1;

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
	private int xType = -1;
	private int yType = -1;
	
	public void moveXTo(float end) {
		this.xEnd = end;
		xType = MOVE_TO;
	}
	
	public void moveYTo(float end) {
		this.yEnd = end;
		yType = MOVE_TO;
	}
	
	public void moveTo(float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xType = MOVE_TO;
		yType = MOVE_TO;
	}

	public MoveAction moveByAngle(float angle, float amount) {
		angle %= 360;
		xAmount = amount * MathUtils.cosDeg(angle);
		yAmount = amount * MathUtils.sinDeg(angle);
		xType = MOVE_BY;
		yType = MOVE_BY;
		return this;
	}
	
	public MoveAction moveXBy(float amount) {
		xAmount = amount;
		xType = MOVE_BY;
		return this;
	}

	public MoveAction moveYBy(float amount) {
		yAmount = amount;
		yType = MOVE_BY;
		return this;
	}
	
	public MoveAction moveBy(float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xType = MOVE_BY;
		yType = MOVE_BY;
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
		switch(xType) {
			case MOVE_BY:
			case MOVE_TO:
				percentable.setX(MathUtils.lerp(xStart, xEnd, percent));
				break;
		}
		
		switch(yType) {
			case MOVE_BY:
			case MOVE_TO:
				percentable.setY(MathUtils.lerp(yStart, yEnd, percent));
				break;
		}
	}
	
	@Override
	public MoveAction setup() {
		if(setupX) {
			setupX = false;
			
			switch(xType) {
				case MOVE_BY:
					xStart = percentable.getX();
					xEnd = xStart + xAmount;
					break;
				case MOVE_TO:
					xStart = percentable.getX();
					break;
			}
		}
		
		if(setupY) {	
			setupY = false;
			
			switch(yType) {
				case MOVE_BY:
					yStart = percentable.getY();
					yEnd = yStart + yAmount;
					break;
				case MOVE_TO:
					yStart = percentable.getY();
					break;
			}
		}
		return this;
	}

	@Override
	protected void endCycleLogic() {
		super.endCycleLogic();
		setupX = true;
		setupY = true;
		if(xType != MOVE_BY || xType == MOVE_BY && !restartMoveXByFromEnd) setupX = false;
		if(yType != MOVE_BY || yType == MOVE_BY && !restartMoveYByFromEnd) setupY = false;
	}

	@Override
	protected boolean hasConflict(Action action) {
		if(action instanceof MoveAction) {
			MoveAction conflictAction = (MoveAction)action;
			if(conflictAction.xType > -1 && conflictAction.yType > -1) return true;
			
			//only x is being scaled so as long as the other action is not using the x there is no conflict
			if(conflictAction.xType > -1 && xType > -1) return true;
			if(conflictAction.yType > -1 && yType > -1) return true;
		}
		return false;
	}
	
	@Override
	public MoveAction clear() {
		super.clear();
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		yStart = 0;
		xEnd = 0;
		yEnd = 0;
		setupX = true;
		setupY = true;
		restartMoveXByFromEnd = false;
		restartMoveYByFromEnd = false;
		xType = -1;
		yType = -1;
		return this;
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
		xType = -1;
		yType = -1;
	}

}
