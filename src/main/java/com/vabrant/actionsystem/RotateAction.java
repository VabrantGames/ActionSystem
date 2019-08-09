package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class RotateAction extends TimeAction {

	public static RotateAction getAction() {
		return getAction(RotateAction.class);
	}
	
	public static RotateAction rotateTo(Rotatable rotatable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		RotateAction action = getAction();
		action.rotateTo(rotatable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static RotateAction rotateBy(Rotatable rotatable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		RotateAction action = getAction();
		action.rotateBy(rotatable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	private enum RotationType{
		ROTATE_TO,
		ROTATE_BY,
		NONE
	}
	
	private boolean setupRotation = true;
	private boolean cap;
	private boolean restartRotateByFromEnd;
	private float byAmount;
	private float start;
	private float end;
	private Rotatable rotatable;
	private RotationType type = RotationType.NONE;
	
	public RotateAction rotateTo(Rotatable rotatable, float end) {
		this.rotatable = rotatable;
		this.end = end * -1f;
		type = RotationType.ROTATE_TO;
		return this;
	}
	
	public RotateAction rotateBy(Rotatable rotatable, float amount) {
		this.rotatable = rotatable;
		byAmount = amount * -1f;
		type = RotationType.ROTATE_BY;
		return this;
	}
	
	public RotateAction restartRotateByFromEnd() {
		restartRotateByFromEnd = true;
		return this;
	}
	
	public RotateAction capAt360(boolean cap) {
		this.cap = cap;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		rotatable.setRotation(MathUtils.lerp(start, end, percent));
	}
	
	@Override
	public void start() {
		super.start();
		if(setupRotation) {
			switch(type) {
				case ROTATE_TO:
					start = rotatable.getRotation();
					break;
				case ROTATE_BY:
					start = rotatable.getRotation();
					end = start + byAmount;
					break;
			}
		}
	}
	
	@Override
	public void end() {
		super.end();
		if(!type.equals(RotationType.ROTATE_BY) || type.equals(RotationType.ROTATE_BY) && !restartRotateByFromEnd) setupRotation = false;
		if(cap) rotatable.setRotation(rotatable.getRotation() % 360f);
	}
	
	@Override
	public void reset() {
		super.reset();
		type = RotationType.NONE;
		setupRotation = true;
		cap = false;
		rotatable = null;
		start = 0;
		end = 0;
		byAmount = 0;
		restartRotateByFromEnd = false;
	}

}
