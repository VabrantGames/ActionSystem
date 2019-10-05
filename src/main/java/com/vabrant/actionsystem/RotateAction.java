package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class RotateAction extends PercentAction<Rotatable, RotateAction> {

	public static RotateAction getAction() {
		return getAction(RotateAction.class);
	}
	
	public static RotateAction rotateTo(Rotatable rotatable, float end, float duration, Interpolation interpolation) {
		RotateAction action = getAction();
		action.rotateTo(end);
		action.set(rotatable, duration, interpolation);
		return action;
	}
	
	public static RotateAction rotateBy(Rotatable rotatable, float amount, float duration, Interpolation interpolation) {
		RotateAction action = getAction();
		action.rotateBy(amount);
		action.set(rotatable, duration, interpolation);
		return action;
	}
	
	public static RotateAction setRotation(Rotatable rotatable, float end) {
		RotateAction action = getAction();
		action.rotateTo(end);
		action.set(rotatable, 0, Interpolation.linear);
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
	private RotationType type = RotationType.NONE;
	
	public RotateAction rotateTo(float end) {
		this.end = end;
		type = RotationType.ROTATE_TO;
		return this;
	}
	
	public RotateAction rotateBy(float amount) {
		byAmount = amount;
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
		percentable.setRotation(MathUtils.lerp(start, end, percent));
	}
	
	private void setup() {
		if(setupRotation) {
			switch(type) {
				case ROTATE_TO:
					start = percentable.getRotation();
					break;
				case ROTATE_BY:
					start = percentable.getRotation();
					end = start + byAmount;
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
		if(!type.equals(RotationType.ROTATE_BY) || type.equals(RotationType.ROTATE_BY) && !restartRotateByFromEnd) setupRotation = false;
		if(cap) percentable.setRotation(percentable.getRotation() % 360f);
	}
	
	@Override
	public void reset() {
		super.reset();
		type = RotationType.NONE;
		setupRotation = true;
		cap = false;
		start = 0;
		end = 0;
		byAmount = 0;
		restartRotateByFromEnd = false;
	}

}
