package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ScaleAction extends PercentAction<Scalable>{
	
	public static ScaleAction getAction() {
		return getAction(ScaleAction.class);
	}
	
	public static ScaleAction scaleXBy(Scalable scalable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleXBy(amount);
		action.set(scalable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleYBy(Scalable scalable, float amount , float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleYBy(amount);
		action.set(scalable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleBy(Scalable scalable, float xAmount, float yAmount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleBy(xAmount, yAmount);
		action.set(scalable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleXTo(Scalable scalable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleXTo(end);
		action.set(scalable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleYTo(Scalable scalable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleYTo(end);
		action.set(scalable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleTo(Scalable scalable, float xEnd, float yEnd, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleTo(xEnd, yEnd);
		action.set(scalable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction setScaleX(Scalable scalable, float scaleX) {
		ScaleAction action = getAction();
		action.scaleXTo(scaleX);
		action.set(scalable, 0, false, Interpolation.linear);
		return action;
	}
	
	public static ScaleAction setScaleY(Scalable scalable, float scaleY) {
		ScaleAction action = getAction();
		action.scaleYTo(scaleY);
		action.set(scalable, 0, false, Interpolation.linear);
		return action;
	}
	
	public static ScaleAction setScale(Scalable scalable, float scaleX, float scaleY) {
		ScaleAction action = getAction();
		action.scaleTo(scaleX, scaleY);
		action.set(scalable, 0, false, Interpolation.linear);
		return action;
	}
	
	private enum XScaleType{
		SCALE_X_TO,
		SCALE_X_BY,
		NONE
	}
	
	private enum YScaleType{
		SCALE_Y_TO,
		SCALE_Y_BY,
		NONE
	}
	
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
	private XScaleType xScaleType = XScaleType.NONE;
	private YScaleType yScaleType = YScaleType.NONE;
	
	public ScaleAction scaleXBy(float amount) {
		xAmount = amount;
		xScaleType = XScaleType.SCALE_X_BY;
		return this;
	}
	
	public ScaleAction scaleYBy(float amount) {
		yAmount = amount;
		yScaleType = YScaleType.SCALE_Y_BY;
		return this;
	}
	
	public ScaleAction scaleBy(float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xScaleType = XScaleType.SCALE_X_BY;
		yScaleType = YScaleType.SCALE_Y_BY;
		return this;
	}
	
	public ScaleAction scaleXTo(float end) {
		this.xEnd = end;
		xScaleType = XScaleType.SCALE_X_TO;
		return this;
	}
	
	public ScaleAction scaleYTo(float end) {
		this.yEnd = end;
		yScaleType = YScaleType.SCALE_Y_TO;
		return this;
	}
	
	public ScaleAction scaleTo(float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xScaleType = XScaleType.SCALE_X_TO;
		yScaleType = YScaleType.SCALE_Y_TO;
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
		switch(xScaleType) {
			case SCALE_X_BY:
			case SCALE_X_TO:
				percentable.setScaleX(MathUtils.lerp(xStart, xEnd, percent));
				break;
		}
		
		switch(yScaleType) {
			case SCALE_Y_BY:
			case SCALE_Y_TO:
				percentable.setScaleY(MathUtils.lerp(yStart, yEnd, percent));
				break;
		}
	}
	
	@Override
	public void start() {
		super.start();
		if(setupX) {
			switch(xScaleType) {
				case SCALE_X_BY:
					xStart = percentable.getScaleX();
					xEnd = xStart + xAmount;
					break;
				case SCALE_X_TO:
					xStart = percentable.getScaleX();
					break;
			}
		}
		
		if(setupY) {
			switch(yScaleType) {
				case SCALE_Y_BY:
					yStart = percentable.getScaleY();
					yEnd = yStart + yAmount;
					break;
				case SCALE_Y_TO:
					yStart = percentable.getScaleY();
					break;
			}
		}
	}
	
	@Override
	public void end() {
		super.end();
		if(!xScaleType.equals(XScaleType.SCALE_X_BY) || xScaleType.equals(XScaleType.SCALE_X_BY) && !restartScaleXByFromEnd) setupX = false;
		if(!yScaleType.equals(YScaleType.SCALE_Y_BY) || yScaleType.equals(YScaleType.SCALE_Y_BY) && !restartScaleYByFromEnd) setupY = false;
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
		xScaleType = XScaleType.NONE;
		yScaleType = YScaleType.NONE;
	}

}
