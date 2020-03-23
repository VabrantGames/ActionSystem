package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ZoomAction extends PercentAction<Zoomable, ZoomAction>{
	
	public static ZoomAction obtain() {
		return obtain(ZoomAction.class);
	}
	
	public static ZoomAction zoomTo(Zoomable zoomable, float end, float duration, Interpolation interpolation) {
		ZoomAction action = obtain();
		action.zoomTo(end);
		action.set(zoomable, duration,  interpolation);
		return action;
	}
	
	public static ZoomAction zoomBy(Zoomable zoomable, float amount, float duration, Interpolation interpolation) {
		ZoomAction action = obtain();
		action.zoomBy(amount);
		action.set(zoomable, duration, interpolation);
		return action;
	}
	
	public static ZoomAction setZoom(Zoomable zoomable, float zoom) {
		ZoomAction action = obtain();
		action.zoomTo(zoom);
		action.set(zoomable, 0, null);
		return action;
	}

	private static final int ZOOM_TO = 0;
	private static final int ZOOM_BY = 1;
	
	private boolean restartZoomByFromEnd;
	private boolean setup = true;
	private float start;
	private float end;
	private float amount;
	private int type = -1;

	public ZoomAction zoomTo(float end) {
		this.end = end;
		type = ZOOM_TO;
		return this;
	}
	
	public ZoomAction zoomBy(float amount) {
		this.amount = amount;
		type = ZOOM_BY;
		return this;
	}
	
	public ZoomAction restartZoomByFromEnd() {
		this.restartZoomByFromEnd = true;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		percentable.setZoom(MathUtils.lerp(start, end, percent));
	}
	
	@Override
	public ZoomAction setup() {
		if(setup) {
			setup = false;
			
			switch(type) {
				case ZOOM_BY:
					start = percentable.getZoom();
					end = start + amount;
					break;
				case ZOOM_TO:
					start = percentable.getZoom();
					break;
			}
		}
		return this;
	}

	@Override
	public void endLogic() {
		super.endLogic();
		if(type == ZOOM_BY && restartZoomByFromEnd) setup = true;
	}
	
	@Override
	public boolean hasConflict(Action<?> action) {
		if(action instanceof ZoomAction) {
			ZoomAction conflictAction = (ZoomAction)action;
			if(conflictAction.type > -1) return true;
		}
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		setup = true;
		start = 0;
		end = 0;
		amount = 0;
		restartZoomByFromEnd = false;
		type = -1;
	}
}
