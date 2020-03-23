package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ScaleAction extends PercentAction<Scalable, ScaleAction>{
	
	public static ScaleAction obtain() {
		return obtain(ScaleAction.class);
	}
	
	public static ScaleAction scaleXBy(Scalable scalable, float amount, float duration, Interpolation interpolation) {
		ScaleAction action = obtain();
		action.scaleXBy(amount);
		action.set(scalable, duration, interpolation);
		return action;
	}
	
	public static ScaleAction scaleYBy(Scalable scalable, float amount , float duration, Interpolation interpolation) {
		ScaleAction action = obtain();
		action.scaleYBy(amount);
		action.set(scalable, duration, interpolation);
		return action;
	}
	
	public static ScaleAction scaleBy(Scalable scalable, float xAmount, float yAmount, float duration, Interpolation interpolation) {
		ScaleAction action = obtain();
		action.scaleBy(xAmount, yAmount);
		action.set(scalable, duration, interpolation);
		return action;
	}
	
	public static ScaleAction scaleXTo(Scalable scalable, float end, float duration, Interpolation interpolation) {
		ScaleAction action = obtain();
		action.scaleXTo(end);
		action.set(scalable, duration, interpolation);
		return action;
	}
	
	public static ScaleAction scaleYTo(Scalable scalable, float end, float duration, Interpolation interpolation) {
		ScaleAction action = obtain();
		action.scaleYTo(end);
		action.set(scalable, duration, interpolation);
		return action;
	}
	
	public static ScaleAction scaleTo(Scalable scalable, float xEnd, float yEnd, float duration, Interpolation interpolation) {
		ScaleAction action = obtain();
		action.scaleTo(xEnd, yEnd);
		action.set(scalable, duration, interpolation);
		return action;
	}
	
	public static ScaleAction setScaleX(Scalable scalable, float scaleX) {
		ScaleAction action = obtain();
		action.scaleXTo(scaleX);
		action.set(scalable, 0, Interpolation.linear);
		return action;
	}
	
	public static ScaleAction setScaleY(Scalable scalable, float scaleY) {
		ScaleAction action = obtain();
		action.scaleYTo(scaleY);
		action.set(scalable, 0, Interpolation.linear);
		return action;
	}
	
	public static ScaleAction setScale(Scalable scalable, float scaleX, float scaleY) {
		ScaleAction action = obtain();
		action.scaleTo(scaleX, scaleY);
		action.set(scalable, 0, Interpolation.linear);
		return action;
	}
	
	private static final int SCALE_TO = 0;
	private static final int SCALE_BY = 1;
	
	private boolean restartScaleXByFromEnd;
	private boolean restartScaleYByFromEnd;
	private boolean setupX = true;
	private boolean setupY = true;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private float xAmount;
	private float yAmount;
	private int xType = -1;
	private int yType = -1;
	
	public ScaleAction scaleXBy(float amount) {
		xAmount = amount;
		xType = SCALE_BY;
		return this;
	}
	
	public ScaleAction scaleYBy(float amount) {
		yAmount = amount;
		yType = SCALE_BY;
		return this;
	}
	
	public ScaleAction scaleBy(float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xType = SCALE_BY;
		yType = SCALE_BY;
		return this;
	}
	
	public ScaleAction scaleXTo(float end) {
		this.xEnd = end;
		xType = SCALE_TO;
		return this;
	}
	
	public ScaleAction scaleYTo(float end) {
		this.yEnd = end;
		yType = SCALE_TO;
		return this;
	}
	
	public ScaleAction scaleTo(float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xType = SCALE_TO;
		yType = SCALE_TO;
		return this;
	}

	public ScaleAction restartScaleXByFromEnd() {
		restartScaleXByFromEnd = true;
		return this;
	}
	
	public ScaleAction restartScaleYByFromEnd() {
		restartScaleYByFromEnd = true;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		switch(xType) {
			case SCALE_BY:
			case SCALE_TO:
				percentable.setScaleX(MathUtils.lerp(xStart, xEnd, percent));
				break;
		}
		
		switch(yType) {
			case SCALE_BY:
			case SCALE_TO:
				percentable.setScaleY(MathUtils.lerp(yStart, yEnd, percent));
				break;
		}
	}
	
	@Override
	public ScaleAction setup() {
		if(setupX) {
			setupX = false;
			
			switch(xType) {
				case SCALE_BY:
					xStart = percentable.getScaleX();
					xEnd = xStart + xAmount;
					break;
				case SCALE_TO:
					xStart = percentable.getScaleX();
					break;
			}
		}
		
		if(setupY) {
			setupY = false;
			
			switch(yType) {
				case SCALE_BY:
					yStart = percentable.getScaleY();
					yEnd = yStart + yAmount;
					break;
				case SCALE_TO:
					yStart = percentable.getScaleY();
					break;
			}
		}
		return this;
	}
	
	@Override
	public void endLogic() {
		super.endLogic();
		if(xType == SCALE_BY && restartScaleXByFromEnd) setupX = true;
		if(yType == SCALE_BY && restartScaleYByFromEnd) setupY = true;
//		if(xType != SCALE_BY || xType == SCALE_BY && !restartScaleXByFromEnd) setupX = false;
//		if(yType != SCALE_BY || yType == SCALE_BY && !restartScaleYByFromEnd) setupY = false;
	}
	
	@Override
	public boolean hasConflict(Action action) {
		if(action instanceof ScaleAction) {
			ScaleAction conflictAction = (ScaleAction)action;
			
			//both the x and y are being scaled
			if(conflictAction.xType > -1 && conflictAction.yType > -1) return true;

			//only x is being scaled so as long as the other action is not using the x there is no conflict
			if(conflictAction.xType > -1) {
				if(xType > -1) return true;
			}
			else if(conflictAction.yType > -1) {
				if(yType > -1) return true;
			}
		}
		return false;
	}
	
	@Override
	public void reset() {
		super.reset();
		restartScaleXByFromEnd = false;
		restartScaleYByFromEnd = false;
		setupX = true;
		setupY = true;
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		xType = -1;
		yType = -1;
	}

}
