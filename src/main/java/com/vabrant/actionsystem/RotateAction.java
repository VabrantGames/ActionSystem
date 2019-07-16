package com.vabrant.actionsystem;

import com.badlogic.gdx.math.MathUtils;

public class RotateAction extends TimeAction {

	private boolean cap;
	private boolean restartRotateByFromEnd;
	private float rotateByAmount;
	private float start;
	private float end;
	private Rotatable rotatable;
	
	public void rotateTo(Rotatable rotatable, float end) {
		rotateTo(rotatable, rotatable.getRotation(), end);
	}
	
	public void rotateTo(Rotatable rotatable, float start, float end) {
		restartRotateByFromEnd = false;
		this.rotatable = rotatable;
		this.start = start;
		this.end = end;
	}
	
	public void rotateBy(Rotatable rotatable, float amount) {
		rotateBy(rotatable, rotatable.getRotation(), amount);
	}
	
	public void rotateBy(Rotatable rotatable, float start, float amount) {
		rotateByAmount = amount;
		this.rotatable = rotatable;
		this.start = start;
		this.end = start + amount;
	}
	
	public void restartRotateByFromEnd() {
		restartRotateByFromEnd = true;
	}
	
	public void capAt360(boolean cap) {
		this.cap = cap;
	}
	
	@Override
	protected void percent(float percent) {
		float rotation = MathUtils.lerp(start, end, percent);
		rotatable.setRotation(rotation);
	}
	
	@Override
	public void end() {
		super.end();
		if(reverseBackToStart) {
			rotatable.setRotation(start);
		}
		else {
			rotatable.setRotation(end);
		}
		if(cap) rotatable.setRotation(rotatable.getRotation() % 360f);
	}
	
	@Override
	public void restart() {
		super.restart();
		if(restartRotateByFromEnd) rotateBy(rotatable, rotateByAmount);
		rotatable.setRotation(start);
	}
	
	@Override
	public void reset() {
		super.reset();
		cap = false;
		rotatable = null;
		start = 0;
		end = 0;
		rotateByAmount = 0;
		restartRotateByFromEnd = false;
	}

}
