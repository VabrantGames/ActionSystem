package com.vabrant.actionsystem.actions;

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
	
	private static final int ROTATE_TO = 0;
	private static final int ROTATE_BY = 1;
	
	private boolean setupRotation = true;
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
		didInitialSetup = true;
		
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
		return this;
	}
	
	@Override
	protected void startLogic() {
		super.startLogic();
		if(!didInitialSetup) setup();
	}
	
	@Override
	public RotateAction restart() {
		super.restart();
		setup();
		return this;
	}
	
	@Override
	public RotateAction end() {
		super.end();
		if(type != ROTATE_BY || type == ROTATE_BY && !restartRotateByFromEnd) setupRotation = false;
		if(cap) percentable.setRotation(percentable.getRotation() % 360f);
		return this;
	}
	
	@Override
	protected boolean hasConflict(Action action) {
		if(action instanceof RotateAction) {
			RotateAction conflictAction = (RotateAction)action;
			if(conflictAction.type > -1) return true; 
		}
		return false;
	}
	
	@Override
	public RotateAction clear() {
		super.clear();
		type = -1;
		setupRotation = true;
		cap = false;
		start = 0;
		end = 0;
		byAmount = 0;
		restartRotateByFromEnd = false;
		return this;
	}
	
	@Override
	public void reset() {
		super.reset();
		type = -1;
		setupRotation = true;
		cap = false;
		start = 0;
		end = 0;
		byAmount = 0;
		restartRotateByFromEnd = false;
	}

}
