package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ScaleAction extends PercentAction{
	
	public static ScaleAction getAction() {
		return getAction(ScaleAction.class);
	}
	
	public static ScaleAction scaleXBy(Scalable scalable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleXBy(scalable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleYBy(Scalable scalable, float amount , float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleYBy(scalable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleBy(Scalable scalable, float xAmount, float yAmount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleBy(scalable, xAmount, yAmount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleXTo(Scalable scalable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleXTo(scalable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleYTo(Scalable scalable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleYTo(scalable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction scaleTo(Scalable scalable, float xEnd, float yEnd, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction();
		action.scaleTo(scalable, xEnd, yEnd);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ScaleAction setScaleX(Scalable scalable, float scaleX) {
		ScaleAction action = getAction();
		action.scaleXTo(scalable, scaleX);
		action.set(0, false, Interpolation.linear);
		return action;
	}
	
	public static ScaleAction setScaleY(Scalable scalable, float scaleY) {
		ScaleAction action = getAction();
		action.scaleYTo(scalable, scaleY);
		action.set(0, false, Interpolation.linear);
		return action;
	}
	
	public static ScaleAction setScale(Scalable scalable, float scaleX, float scaleY) {
		ScaleAction action = getAction();
		action.scaleTo(scalable, scaleX, scaleY);
		action.set(0, false, Interpolation.linear);
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
	private Scalable scalable;
	private XScaleType xScaleType = XScaleType.NONE;
	private YScaleType yScaleType = YScaleType.NONE;
	
	public ScaleAction scaleXBy(Scalable scalable, float amount) {
		this.scalable = scalable;
		xAmount = amount;
		xScaleType = XScaleType.SCALE_X_BY;
		return this;
	}
	
	public ScaleAction scaleYBy(Scalable scalable, float amount) {
		this.scalable = scalable;
		yAmount = amount;
		yScaleType = YScaleType.SCALE_Y_BY;
		return this;
	}
	
	public ScaleAction scaleBy(Scalable scalable, float xAmount, float yAmount) {
		this.scalable = scalable;
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xScaleType = XScaleType.SCALE_X_BY;
		yScaleType = YScaleType.SCALE_Y_BY;
		return this;
	}
	
	public ScaleAction scaleXTo(Scalable scalable, float end) {
		this.scalable = scalable;
		this.xEnd = end;
		xScaleType = XScaleType.SCALE_X_TO;
		return this;
	}
	
	public ScaleAction scaleYTo(Scalable scalable, float end) {
		this.scalable = scalable;
		this.yEnd = end;
		yScaleType = YScaleType.SCALE_Y_TO;
		return this;
	}
	
	public ScaleAction scaleTo(Scalable scalable, float xEnd, float yEnd) {
		this.scalable = scalable;
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
				scalable.setScaleX(MathUtils.lerp(xStart, xEnd, percent));
				break;
		}
		
		switch(yScaleType) {
			case SCALE_Y_BY:
			case SCALE_Y_TO:
				scalable.setScaleY(MathUtils.lerp(yStart, yEnd, percent));
				break;
		}
	}
	
	@Override
	public void start() {
		super.start();
		if(setupX) {
			switch(xScaleType) {
				case SCALE_X_BY:
					xStart = scalable.getScaleX();
					xEnd = xStart + xAmount;
					break;
				case SCALE_X_TO:
					xStart = scalable.getScaleX();
					break;
			}
		}
		
		if(setupY) {
			switch(yScaleType) {
				case SCALE_Y_BY:
					yStart = scalable.getScaleY();
					yEnd = yStart + yAmount;
					break;
				case SCALE_Y_TO:
					yStart = scalable.getScaleY();
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
		scalable = null;
		xScaleType = XScaleType.NONE;
		yScaleType = YScaleType.NONE;
	}

}
