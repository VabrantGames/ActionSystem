package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class RotateAction extends PercentAction<Rotatable, RotateAction> {

	public static RotateAction obtain() {
		return obtain(RotateAction.class);
	}
	
	public static RotateAction rotateTo(Rotatable rotatable, float end, float duration, Interpolation interpolation) {
		RotateAction action = obtain();
		action.rotateTo(end);
		action.set(rotatable, duration, interpolation);
		return action;
	}
	
	public static RotateAction rotateBy(Rotatable rotatable, float amount, float duration, Interpolation interpolation) {
		RotateAction action = obtain();
		action.rotateBy(amount);
		action.set(rotatable, duration, interpolation);
		return action;
	}
	
	public static RotateAction setRotation(Rotatable rotatable, float end) {
		RotateAction action = obtain();
		action.rotateTo(end);
		action.set(rotatable, 0, Interpolation.linear);
		return action;
	}
	
	private static final int ROTATE_TO = 0;
	private static final int ROTATE_BY = 1;
	
	private boolean setup = true;
	private boolean cap;
	private boolean restartRotateByFromEnd;
	private float byAmount;
	private float start;
	private float end;
	private int type = -1;
	
	public RotateAction rotateTo(float end) {
		this.end = end;
		type = ROTATE_TO;
		return this;
	}
	
	public RotateAction rotateBy(float amount) {
		byAmount = amount;
		type = ROTATE_BY;
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
	
	@Override
	public RotateAction setup() {
		if(setup) {
			setup = false;
			
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
		return this;
	}
	
	@Override
	public void endLogic() {
		super.endLogic();
		if(type == ROTATE_BY && restartRotateByFromEnd) setup = true;
		if(cap) percentable.setRotation(percentable.getRotation() % 360f);
	}
	
	@Override
	public boolean hasConflict(Action<?> action) {
		if(action instanceof RotateAction) {
			RotateAction conflictAction = (RotateAction)action;
			if(conflictAction.type > -1) return true; 
		}
		return false;
	}
	
	@Override
	public void reset() {
		super.reset();
		type = -1;
		setup = true;
		cap = false;
		start = 0;
		end = 0;
		byAmount = 0;
		restartRotateByFromEnd = false;
	}

}
