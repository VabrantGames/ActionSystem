package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ShakeAction extends PercentAction<Shakable, ShakeAction> {
	
	public static ShakeAction getAction() {
		return getAction(ShakeAction.class);
	}
	
	public static ShakeAction shakeX(Shakable shakable, float amount, float duration, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shakeX(amount);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	public static ShakeAction shakeY(Shakable shakable, float amount, float duration,  Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shakeY(amount);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	public static ShakeAction shakeAngle(Shakable shakable, float maxAngle, float duration, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shakeAngle(maxAngle);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	public static ShakeAction shake(Shakable shakable, float xAmount, float yAmount, float angleAmount, float duration, Interpolation interpolation) {
		ShakeAction action = getAction();
		action.shake(xAmount, yAmount, angleAmount);
		action.set(shakable, duration, interpolation);
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
	private ShakeType type = ShakeType.NONE;
	
	public ShakeAction shakeX(float amount) {
		this.x = amount;
		type = ShakeType.SHAKE_X;
		return this;
	}
	
	public ShakeAction shakeY(float amount) {
		this.y = amount;
		type = ShakeType.SHAKE_Y;
		return this;
	}
	
	public ShakeAction shakeAngle(float amount) {
		this.angle = amount;
		type = ShakeType.SHAKE_ANGLE;
		return this;
	}
	
	public ShakeAction shake(float xAmount, float yAmount, float angle) {
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
				percentable.setShakeX(MathUtils.random(-this.x, this.x) * percent);
//				float ranAng = MathUtils.random(360);
//				float ranDist = MathUtils.random(-this.x, this.x);
//				float val = MathUtils.cosDeg(ranAng) * ranDist;
//				percentable.setShakeX(val);
				break;
			case SHAKE_Y:
				percentable.setShakeY(MathUtils.random(-this.y, this.y) * percent);
				break;
			case SHAKE_ANGLE:
				percentable.setShakeAngle(MathUtils.random(-this.angle, this.angle) * percent);
				break;
			case SHAKE_ALL:
				percentable.setShakeX(MathUtils.random(-this.x, this.x) * percent);
				percentable.setShakeY(MathUtils.random(-this.y, this.y) * percent);
				percentable.setShakeAngle(MathUtils.random(-this.angle, this.angle) * percent);
				break;
		}
	}
	
	@Override
	public void kill() {
		super.kill();
		percentable.setShakeX(0);
		percentable.setShakeY(0);
		percentable.setShakeAngle(0);
	}
	
	 @Override
	public void end() {
		super.end();
		percentable.setShakeX(0);
		percentable.setShakeY(0);
		percentable.setShakeAngle(0);
	}
	
	@Override
	public void reset() {
		super.reset();
		x = 0;
		y = 0;
		angle = 0;
		usePercent = false;
		type = ShakeType.NONE;
	}
}
