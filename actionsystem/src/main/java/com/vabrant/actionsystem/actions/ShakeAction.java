package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ShakeAction extends PercentAction<Shakable, ShakeAction> {
	
	public static ShakeAction obtain() {
		return obtain(ShakeAction.class);
	}
	
	public static ShakeAction shakeX(Shakable shakable, float amount, float duration, Interpolation interpolation) {
		ShakeAction action = obtain();
		action.shakeX(amount);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	public static ShakeAction shakeY(Shakable shakable, float amount, float duration,  Interpolation interpolation) {
		ShakeAction action = obtain();
		action.shakeY(amount);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	public static ShakeAction shakeAngle(Shakable shakable, float maxAngle, float duration, Interpolation interpolation) {
		ShakeAction action = obtain();
		action.shakeAngle(maxAngle);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	public static ShakeAction shake(Shakable shakable, float xAmount, float yAmount, float angleAmount, float duration, Interpolation interpolation) {
		ShakeAction action = obtain();
		action.shake(xAmount, yAmount, angleAmount);
		action.set(shakable, duration, interpolation);
		return action;
	}
	
	private boolean usePercent;
	private float x;
	private float y;
	private float angle;
	private boolean shakeX = false;
	private boolean shakeY = false;
	private boolean shakeAngle = false;
	
	public ShakeAction shakeX(float amount) {
		this.x = amount;
		shakeX = true;
		return this;
	}
	
	public ShakeAction shakeY(float amount) {
		this.y = amount;
		shakeY = true;
		return this;
	}
	
	public ShakeAction shakeAngle(float amount) {
		this.angle = amount;
		shakeAngle = true;
		return this;
	}
	
	public ShakeAction shake(float xAmount, float yAmount, float angle) {
		this.x = xAmount;
		this.y = yAmount;
		this.angle = angle;
		shakeX = shakeY = shakeAngle = true;
		return this;
	}
	
	public ShakeAction usePercent(boolean usePercent) {
		this.usePercent = usePercent;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		if(!usePercent) percent = 1;
		if(shakeX) percentable.setShakeX(MathUtils.random(-x, x) * percent);
		if(shakeY) percentable.setShakeY(MathUtils.random(-y, y) * percent);
		if(shakeAngle) percentable.setShakeAngle(MathUtils.random(-angle, angle) * percent);
	}
	
	@Override
	public ShakeAction setup() {
		return this;
	}
	
	@Override
	public void killLogic() {
		super.killLogic();
		percentable.setShakeX(0);
		percentable.setShakeY(0);
		percentable.setShakeAngle(0);
	}
	
	 @Override
	public void endLogic() {
		super.endLogic();
		percentable.setShakeX(0);
		percentable.setShakeY(0);
		percentable.setShakeAngle(0);
	}
	 
	 @Override
	 public boolean hasConflict(Action action) {
		 if(action instanceof ShakeAction) {
			 ShakeAction conflictAction = (ShakeAction)action;
			 if(conflictAction.shakeX && conflictAction.shakeY && conflictAction.shakeAngle) return true;
			 if(conflictAction.shakeX && shakeX) return true;
			 if(conflictAction.shakeY && shakeY) return true;
			 if(conflictAction.shakeAngle && shakeAngle) return true;
		 }
		 return false;
	 }

	@Override
	public void reset() {
		super.reset();
		x = 0;
		y = 0;
		angle = 0;
		usePercent = false;
		shakeX = shakeY = shakeAngle = false;
	}
	
//	public static interface ShakeLogic {
//		public void getX(float percent);
//		public void getY(float percent);
//		public void getAngle(float percent);
//	}
//	
//	private static class DefaultShakeLogic implements ShakeLogic {
//
//		@Override
//		public void getX(float percent) {
//			
//		}
//
//		@Override
//		public void getY(float percent) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void getAngle(float percent) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
}
