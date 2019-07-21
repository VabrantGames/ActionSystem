package com.vabrant.actionsystem;

import com.badlogic.gdx.math.MathUtils;

public class ShakeAction extends TimeAction {
	
	private boolean shakeX;
	private boolean shakeY;
	private boolean shakeAngle;
	private boolean usePercent;
	private float x;
	private float y;
	private float angle;
	private float multiplier;
	private Shakable shakable;

	public void shake(Shakable shakable, float amount, float angle, float multiplier) {
		shake(shakable, amount, amount, angle, multiplier);
	}
	
	public void shakeX(Shakable shakable, float x, float angle, float multiplier) {
		shake(shakable, x, 0, angle, multiplier);
	}
	
	public void shakeY(Shakable shakable, float y, float angle, float multiplier) {
		shake(shakable, 0, y, angle, multiplier);
	}
	
	public void shakeAngle(Shakable shakable, float angle, float multiplier) {
		shake(shakable, 0, 0, angle, multiplier);
	}
	
	public void usePercent(boolean usePercent) {
		this.usePercent = usePercent;
	}
	
	public void shake(Shakable shakable, float xAmount, float yAmount, float angle, float multiplier) {
		this.shakable = shakable;
		this.x = xAmount;
		this.y = yAmount;
		this.angle = angle;
		this.multiplier = multiplier;
	}
	
	@Override
	protected void percent(float percent) {
		if(!usePercent) percent = 1;
		float x = MathUtils.random(-this.x, this.x) * multiplier * percent;
		float y = MathUtils.random(-this.y, this.y) * multiplier * percent;
		float angle = MathUtils.random(-this.angle, this.angle) * multiplier * percent;
		shakable.setShakeX(x);
		shakable.setShakeY(y);
		shakable.setShakeAngle(angle);
	}
	
	@Override
	public void start() {
		super.start();
		if(x == 0) {
			shakeX = false;
		}
		else {
			shakeX = true;
		}
		if(y == 0) {
			shakeY = false;
		}
		else {
			shakeY = true;
		}
		if(angle == 0) {
			shakeAngle = false;
		}
		else {
			shakeAngle = true;
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
	public void restart() {
		super.restart();
		shakable.setShakeX(0);
		shakable.setShakeY(0);
		shakable.setShakeAngle(0);
	}
	
	@Override
	public void reset() {
		super.reset();
		x = 0;
		y = 9;
		angle = 0;
		multiplier = 0;
		shakable = null;
		usePercent = false;
	}
}
