package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class MoveToAction extends TimeAction{

	private boolean moveX;
	private boolean moveY;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private Movable movable;
	
	public void moveXTo(Movable movable, float end) {
		this.movable = movable;
		this.xEnd = end;
		moveX = true;
	}
	
	protected void setupX() {
		this.xStart = movable.getX();
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
		
		moveX = xStart == 0 && xEnd == 0 ? false : true;
		moveY = yStart == 0 && yEnd == 0 ? false : true;
	}
	
	@Override
	public void reset() {
		super.reset();
		moveX = false;
		moveY = false;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		movable = null;
	}
	
	public static MoveToAction moveXTo(Movable movable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		MoveToAction action = getAction(MoveToAction.class);
		action.moveXTo(movable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}

}
