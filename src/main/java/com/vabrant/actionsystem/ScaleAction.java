package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ScaleAction extends TimeAction{
	
	private boolean scaleX;
	private boolean scaleY;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private Scalable scalable;
	
	public void scaleXBy(Scalable scalable, float amount) {
		scaleXBy(scalable, scalable.getScaleX(), amount);
	}
	
	public void scaleXBy(Scalable scalable, float start, float amount) {
		this.scalable = scalable;
		this.xStart = start;
		this.xEnd = start + amount; 
	}
	
	public void scaleYBy(Scalable scalable, float amount) {
		scaleYBy(scalable, scalable.getScaleY(), amount);
	}
	
	public void scaleYBy(Scalable scalable, float start, float amount) {
		this.scalable = scalable;
		this.yStart = start;
		this.yEnd = start + amount;
	}
	
	public void scaleXTo(Scalable scalable, float end) {
		scaleXTo(scalable, scalable.getScaleX(), end);
	}
	
	public void scaleXTo(Scalable scalable, float start, float end) {
		this.scalable = scalable;
		this.xStart = start;
		this.xEnd = end;
	}
	
	public void scaleYTo(Scalable scalable, float end) {
		scaleYTo(scalable, scalable.getScaleY(), end);
	}
	
	public void scaleYTo(Scalable scalable, float start, float end) {
		this.scalable = scalable;
		this.yStart = start;
		this.yEnd = end;
	}
	
	@Override
	public void start() {
		super.start();
		if(xStart == xEnd) {
			scaleX = false;
		}
		else {
			scaleX = true;
		}
		if(yStart == yEnd) {
			scaleY = false;
		}
		else {
			scaleY = true;
		}
	}
	
	@Override
	public void end() {
		super.end();
		if(scaleX) {
			if(reverseBackToStart) {
				scalable.setScaleX(xStart);
			}
			else {
				scalable.setScaleX(xEnd);
			}
		}
		if(scaleY) {
			if(reverseBackToStart) {
				scalable.setScaleY(yStart);
			}
			else {
				scalable.setScaleY(yEnd);
			}
		}
	}
	
	@Override
	public void restart() {
		super.restart();
		if(scaleX) {
			scalable.setScaleX(xStart);
		}
		if(scaleY){
			scalable.setScaleY(yStart);
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		scaleX = false;
		scaleY = false;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		scalable = null;
	}
	
	@Override
	protected void percent(float percent) {
		if(scaleX) {
			float x = MathUtils.lerp(xStart, xEnd, percent);
			scalable.setScaleX(x);
		}
		if(scaleY){
			float y = MathUtils.lerp(yStart, yEnd, percent);
			scalable.setScaleY(y);
		}
	}

	public static ScaleAction scaleXBY(Scalable scalable, float start, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction(duration, reverseBackToStart, interpolation);
		action.scaleXBy(scalable, start, amount);
		return action;
	}
	
	public static ScaleAction scaleXBy(Scalable scalable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = getAction(duration, reverseBackToStart, interpolation);
		action.scaleXBy(scalable, amount);
		return action;
	}
	
	public static ScaleAction getAction(float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ScaleAction action = ActionPools.obtain(ScaleAction.class);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}

}
