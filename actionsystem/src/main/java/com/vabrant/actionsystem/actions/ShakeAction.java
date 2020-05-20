/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ShakeAction extends PercentAction<Shakable, ShakeAction> {
	
	public static ShakeAction obtain() {
		return obtain(ShakeAction.class);
	}
	
	public static ShakeAction shakeX(Shakable shakable, ShakeLogic logic, float amount, float duration, Interpolation interpolation) {
		return obtain()
				.shakeX(amount)
				.set(shakable, duration, interpolation)
				.setLogic(logic);
	}
	
	public static ShakeAction shakeY(Shakable shakable, ShakeLogic logic, float amount, float duration,  Interpolation interpolation) {
		return obtain()
				.shakeY(amount)
				.set(shakable, duration, interpolation)
				.setLogic(logic);
	}
	
	public static ShakeAction shakeAngle(Shakable shakable, ShakeLogic logic, float maxAngle, float duration, Interpolation interpolation) {
		return obtain()
				.shakeAngle(maxAngle)
				.set(shakable, duration, interpolation)
				.setLogic(logic);
	}
	
	public static ShakeAction shake(Shakable shakable, ShakeLogic logic, float xAmount, float yAmount, float angleAmount, float duration, Interpolation interpolation) {
		return obtain()
				.shake(xAmount, yAmount, angleAmount)
				.set(shakable, duration, interpolation)
				.setLogic(logic);
	}

	public static ShakeLogic LOGIC_1 = new DefaultShakeLogic();
	public static ShakeLogic LOGIC_2 = new DefaultShakeLogic2();
	
	private boolean usePercent;
	private float xAmount;
	private float yAmount;
	private float angleAmount;
	private boolean shakeX = false;
	private boolean shakeY = false;
	private boolean shakeAngle = false;
	private ShakeLogic logic;
	
	public ShakeAction shakeX(float amount) {
		this.xAmount = amount;
		shakeX = true;
		return this;
	}
	
	public ShakeAction shakeY(float amount) {
		this.yAmount = amount;
		shakeY = true;
		return this;
	}
	
	public ShakeAction shakeAngle(float amount) {
		this.angleAmount = amount;
		shakeAngle = true;
		return this;
	}
	
	public ShakeAction shake(float xAmount, float yAmount, float angle) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		this.angleAmount = angle;
		shakeX = shakeY = shakeAngle = true;
		return this;
	}
	
	public float getXAmount() {
		return xAmount;
	}
	
	public float getYAmount() {
		return yAmount;
	}
	
	public float getAngleAmount() {
		return angleAmount;
	}
	
	public ShakeAction setLogic(ShakeLogic logic) {
		if(logic == null) {
			this.logic = LOGIC_1;
		}
		else {
			this.logic = logic;
		}
		return this;
	}
	
	public <T extends ShakeLogic> T getLogic() {
		return (T)logic;
	}
	
	public boolean isShakingX() {
		return shakeX;
	}
	
	public boolean isShakingY() {
		return shakeY;
	}
	
	public boolean isShakingAngle() {
		return shakeAngle;
	}
	
	public ShakeAction usePercent(boolean usePercent) {
		this.usePercent = usePercent;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		if(!usePercent) percent = 1;
		if(shakeX) percentable.setShakeX(logic.getX(this, percent));
		if(shakeY) percentable.setShakeY(logic.getY(this, percent));
		if(shakeAngle) percentable.setShakeAngle(logic.getAngle(this, percent));
	}

	@Override
	protected void startLogic() {
		if(logic == null) throw new RuntimeException("Logic has to be set before action is ran.");
		super.startLogic();
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
	 
//	 @Override
//	 public boolean hasConflict(Action<?> action) {
//		 if(action instanceof ShakeAction) {
//			 ShakeAction conflictAction = (ShakeAction)action;
//			 if(shakeX && shakeY && shakeAngle) return true;
//			 if(conflictAction.shakeX && shakeX) return true;
//			 if(conflictAction.shakeY && shakeY) return true;
//			 if(conflictAction.shakeAngle && shakeAngle) return true;
//		 }
//		 return false;
//	 }
	 
	 @Override
	public ShakeAction setup() {
		return this;
	}

	@Override
	public void clear() {
		super.clear();
		xAmount = 0;
		yAmount = 0;
		angleAmount = 0;
		usePercent = false;
		shakeX = false;
		shakeY = false;
		shakeAngle = false;
	}
	
	@Override
	public void reset() {
		super.reset();
		logic = null;
	}

	public interface ShakeLogic {
		public float getX(ShakeAction action, float percent);
		public float getY(ShakeAction action, float percent);
		public float getAngle(ShakeAction action, float percent);
	}
	
	public static class DefaultShakeLogic implements ShakeLogic {

		@Override
		public float getX(ShakeAction action, float percent) {
			return MathUtils.random(-action.xAmount, action.xAmount) * percent;
		}

		@Override
		public float getY(ShakeAction action, float percent) {
			return MathUtils.random(-action.yAmount, action.yAmount) * percent;
		}

		@Override
		public float getAngle(ShakeAction action, float percent) {
			return MathUtils.random(-action.angleAmount, action.angleAmount) * percent;
		}
	}
	
	public static class DefaultShakeLogic2 implements ShakeLogic {
		
		private float xSpeed;
		private float ySpeed;
		private float angleSpeed;
		
		public DefaultShakeLogic2() {
			this(15, 11, 6);
		}
		
		public DefaultShakeLogic2(float xSpeed, float ySpeed, float angleSpeed) {
			set(xSpeed, ySpeed, angleSpeed);
		}
		
		public void set(float xSpeed, float ySpeed, float angleSpeed) {
			this.xSpeed = xSpeed;
			this.ySpeed = ySpeed;
			this.angleSpeed = angleSpeed;
		}

		@Override
		public float getX(ShakeAction action, float percent) {
			return MathUtils.cos(action.getCurrentTime() * MathUtils.PI * xSpeed) * action.xAmount * percent;
		}

		@Override
		public float getY(ShakeAction action, float percent) {
			return MathUtils.sin(action.getCurrentTime() * MathUtils.PI * ySpeed) * action.yAmount * percent;
		}

		@Override
		public float getAngle(ShakeAction action, float percent) {
			return MathUtils.sin(action.getCurrentTime() * MathUtils.PI * angleSpeed) * action.angleAmount * percent;
		}
		
	}
	
}
