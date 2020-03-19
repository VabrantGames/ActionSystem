package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MoveAction extends PercentAction<Movable, MoveAction> {
	
	public static MoveAction obtain() {
		return obtain(MoveAction.class);
	}
	
	public static MoveAction moveXBy(Movable movable, float amount, float duration, Interpolation interpolation) {
		return obtain()
				.moveXBy(amount)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction moveYBy(Movable movable, float amount, float duration, Interpolation interpolation) {
		return obtain()
				.moveYBy(amount)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction moveBy(Movable movable, float xAmount, float yAmount, float duration, Interpolation interpolation) {
		return obtain()
				.moveBy(xAmount, yAmount)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction moveByAngleRad(Movable movable, float radians, float amount, float duration, Interpolation interpolation) {
		return moveByAngleDeg(movable, MathUtils.radiansToDegrees * radians, amount, duration, interpolation);
	}
	
	public static MoveAction moveByAngleDeg(Movable movable, float degrees, float amount, float duration, Interpolation interpolation) {
		return obtain()
				.moveByAngle(degrees, amount)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction moveXTo(Movable movable, float end, float duration, Interpolation interpolation) {
		return obtain()
				.moveXTo(end)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction moveYTo(Movable movable, float end, float duration, Interpolation interpolation) {
		return obtain()
				.moveYTo(end)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction moveTo(Movable movable, float xEnd, float yEnd, float duration,  Interpolation interpolation) {
		return obtain()
				.moveTo(xEnd, yEnd)
				.set(movable, duration, interpolation);
	}
	
	public static MoveAction setX(Movable movable, float x) {
		return obtain()
				.moveXTo(x)
				.set(movable, 0, null);
	}
	
	public static MoveAction setY(Movable movable, float y) {
		return obtain()
				.moveYTo(y)
				.set(movable, 0, null);
	}
	
	public static MoveAction setPosition(Movable movable, float x, float y) {
		return obtain()
				.moveTo(x, y)
				.set(movable, 0, null);
	}

	private static final int MOVE_TO = 0;
	private static final int MOVE_BY = 1;

	private boolean useBothStartPositions = true;
	private boolean setupX = true;
	private boolean setupY = true;
	private boolean startXByFromEnd;
	private boolean startYByFromEnd;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private float yAmount;
	private float xAmount;
	private int xType = -1;
	private int yType = -1;
	
	public MoveAction moveXTo(float end) {
		this.xEnd = end;
		xType = MOVE_TO;
		return this;
	}
	
	public MoveAction moveYTo(float end) {
		this.yEnd = end;
		yType = MOVE_TO;
		return this;
	}
	
	public MoveAction moveTo(float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xType = MOVE_TO;
		yType = MOVE_TO;
		return this;
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

	public MoveAction startXByFromEnd() {
		startXByFromEnd = true;
		return this;
	}
	
	public MoveAction startYByFromEnd() {
		startYByFromEnd = true;
		return this;
	}
	
	/**
	 * Whether this action should start at the exact position since {@link #setup} was called. Will only be used when one direction is used. <br><br>
	 * 
	 * e.g <br><br>
	 * 
	 * If this value is false and you're only moving the x value then only the x value will be changed.
	 * Meaning if this action is repeated with a {@link RepeatAction} then every call to {@link Action #start start} after the initial call would only move the x
	 * back to where it was. Leaving the y unchanged. <br><br>
	 * 
	 * If this value is true and you're only moving the x value then the x and y values will be moved to where they started. <br><br>
	 * 
	 * This is useful for when you have 2 move actions, each changing a different direction, that you don't wan't to collide with each other. If one is restarted
	 * it won't change the value of the other.  
	 * 
	 * @param value
	 * @return This action for chaining
	 */
	public MoveAction useBothStartPositions(boolean value) {
		useBothStartPositions = value;
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
			
			//If move x but not the y save the y
			if(xType > -1 && yType == -1) yStart = percentable.getY();
			
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
			
			//If move y but not x save the x
			if(yType > -1 && xType == -1) xStart = percentable.getX();
			
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
	protected void startLogic() {
		super.startLogic();
		
		if(useBothStartPositions) {
			percentable.setX(xStart);
			percentable.setY(yStart);
		}
	}

	@Override
	protected void endLogic() {
		super.endLogic();
		setupX = true;
		setupY = true;
		if(xType != MOVE_BY || xType == MOVE_BY && !startXByFromEnd) setupX = false;
		if(yType != MOVE_BY || yType == MOVE_BY && !startYByFromEnd) setupY = false;
	}

	@Override
	public boolean hasConflict(Action<?> action) {
		if(action instanceof MoveAction) {
			MoveAction conflictAction = (MoveAction)action;
			
			//ConflictAction is moving both x and y
			if(conflictAction.xType > -1 && conflictAction.yType > -1) return true;
			
			//only x is being scaled so as long as the other action is not using the x there is no conflict
			if(conflictAction.xType > -1 && xType > -1) return true;
			if(conflictAction.yType > -1 && yType > -1) return true;
		}
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		useBothStartPositions = true;
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		setupX = true;
		setupY = true;
		startXByFromEnd = false;
		startYByFromEnd = false;
		xType = -1;
		yType = -1;
	}

}
