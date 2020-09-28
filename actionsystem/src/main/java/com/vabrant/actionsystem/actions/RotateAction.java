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

public class RotateAction extends PercentAction<Rotatable, RotateAction> {

	public static RotateAction obtain() {
		return obtain(RotateAction.class);
	}
	
	public static RotateAction rotateTo(Rotatable rotatable, float end, float duration, Interpolation interpolation) {
		return obtain()
				.rotateTo(end)
				.set(rotatable, duration, interpolation);
	}
	
	public static RotateAction rotateBy(Rotatable rotatable, float amount, float duration, Interpolation interpolation) {
		return obtain()
				.rotateBy(amount)
				.set(rotatable, duration, interpolation);
	}
	
	public static RotateAction setRotation(Rotatable rotatable, float rotation) {
		return obtain()
				.rotateTo(rotation)
				.set(rotatable, 0, null);
	}
	
	private static final int ROTATE_TO = 0;
	private static final int ROTATE_BY = 1;
	
	private int type = -1;
	
	private boolean setup = true;
	private boolean cap;
	private boolean capDeg;
	private boolean startRotateByFromEnd;
	private float byAmount;
	private float start;
	private float end;
	
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
	
	/**
	 * Every time the action restarts, it will start from its current position instead of its initial start position.
	 * 
	 * @return This action for chaining.
	 */
	public RotateAction startRotateByFromEnd() {
		startRotateByFromEnd = true;
		return this;
	}
	
	/**
	 * Caps the end value between <i> 0 (inclusive)</i> - <i> 360 (exclusive) </i> for degrees
	 * or <i> 0 (inclusive) </i> - <i> 2 * PI (exclusive) </i> for radians.
	 * 
	 * @return This action for chaining. 
	 */
	public RotateAction capEndBetweenRevolution(boolean useDeg) {
		cap = true;
		capDeg = useDeg ? true : false;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		percentable.setRotation(MathUtils.lerp(start, end, percent));
	}
	
	@Override
	public RotateAction setup() {
		if(setup) {
			setup = false;
			
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
	public void endLogic() {
		super.endLogic();
		if(type == ROTATE_BY && startRotateByFromEnd) setup = true;
		
		if(cap) {
			if(capDeg) {
				percentable.setRotation(percentable.getRotation() % 360f);
			}
			else {
				percentable.setRotation(percentable.getRotation() % MathUtils.PI2);
			}
		}
	}

	@Override
	public void clear() {
		super.clear();
		type = -1;
		setup = true;
		cap = false;
		capDeg = false;
		startRotateByFromEnd = false;
		byAmount = 0;
		start = 0;
		end = 0;
	}

}
