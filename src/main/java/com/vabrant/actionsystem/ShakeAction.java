package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ShakeAction extends PercentAction {
	
	public static ShakeAction getAction() {
		return getAction(ShakeAction.class);
	}
	
	public static ShakeAction shakeX(Shakable shakable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shakeX(shakable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ShakeAction shakeY(Shakable shakable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shakeY(shakable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ShakeAction shakeAngle(Shakable shakable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shakeAngle(shakable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ShakeAction shake(Shakable shakable, float xAmount, float yAmount, float angleAmount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shake(shakable, xAmount, yAmount, angleAmount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	private enum ShakeType{
		SHAKE_X,
		SHAKE_Y,
		SHAKE_ANGLE,
		SHAKE_ALL,
		NONE
	}
	
	private boolean usePercent;
	private float x;
	private float y;
	private float angle;
	private Shakable shakable;
	private ShakeType type = ShakeType.NONE;
	
	public ShakeAction shakeX(Shakable shakable, float amount) {
		this.shakable = shakable;
		this.x = amount;
		type = ShakeType.SHAKE_X;
		return this;
	}
	
	public ShakeAction shakeY(Shakable shakable, float amount) {
		this.shakable = shakable;
		this.y = amount;
		type = ShakeType.SHAKE_Y;
		return this;
	}
	
	public ShakeAction shakeAngle(Shakable shakable, float amount) {
		this.shakable = shakable;
		this.angle = amount;
		type = ShakeType.SHAKE_ANGLE;
		return this;
	}
	
	public ShakeAction shake(Shakable shakable, float xAmount, float yAmount, float angle) {
		this.shakable = shakable;
		this.x = xAmount;
		this.y = yAmount;
		this.angle = angle;
		type = ShakeType.SHAKE_ALL;
		return this;
	}
	
	public ShakeAction usePercent(boolean usePercent) {
		this.usePercent = usePercent;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		if(!usePercent) percent = 1;
		switch(type) {
			case SHAKE_X:
				shakable.setShakeX(MathUtils.random(-this.x, this.x) * percent);
				break;
			case SHAKE_Y:
				shakable.setShakeY(MathUtils.random(-this.y, this.y) * percent);
				break;
			case SHAKE_ANGLE:
				shakable.setShakeAngle(MathUtils.random(-this.angle, this.angle) * percent);
				break;
			case SHAKE_ALL:
				shakable.setShakeX(MathUtils.random(-this.x, this.x) * percent);
				shakable.setShakeY(MathUtils.random(-this.y, this.y) * percent);
				shakable.setShakeAngle(MathUtils.random(-this.angle, this.angle) * percent);
				break;
		}
	}
	
	 @Override
	public void end() {
		super.end();
		shakable.setShakeX(0);
		shakable.setShakeY(0);
		shakable.setShakeAngle(0);
	}
	
	@Override
	public void reset() {
		super.reset();
		x = 0;
		y = 0;
		angle = 0;
		shakable = null;
		usePercent = false;
		type = ShakeType.NONE;
	}
}
