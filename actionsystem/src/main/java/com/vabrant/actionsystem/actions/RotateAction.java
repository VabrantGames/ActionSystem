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
	private boolean capDeg;
	private boolean capRad;
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
	
	public RotateAction startRotateByFromEnd() {
		startRotateByFromEnd = true;
		return this;
	}
	
	/**
	 * Caps the end value between <i> 0 (inclusive)</i> - <i> 360 (exclusive) </i>.
	 * 
	 * @return This action for chaining. 
	 */
	public RotateAction capEndBetweenRevolutionDeg() {
		capDeg = true;
		if(capRad) capRad = false;
		return this;
	}
	
	/**
	 * Caps the end value between <i> 0 (inclusive) </i> - <i> 2 * PI (exclusive) </i>
	 * 
	 * @return
	 */
	public RotateAction capEndBetweenRevolutionRad(){
		capRad = true;
		if(capDeg) capDeg = false;
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
		
		if(capDeg) {
			percentable.setRotation(percentable.getRotation() % 360f);
		}
		else if(capRad) {
			percentable.setRotation(percentable.getRotation() % MathUtils.PI2);
		}
	}
	
	@Override
	public boolean hasConflict(Action<?> action) {
		if(action instanceof RotateAction) {
			RotateAction conflictAction = (RotateAction)action;
			if(conflictAction.type > -1) return true; 
		}
		return false;
	}
	
	@Override
	public void clear() {
		super.clear();
		type = -1;
		setup = true;
		capDeg = false;
		capRad = false;
		start = 0;
		end = 0;
		byAmount = 0;
		startRotateByFromEnd = false;
	}

}
