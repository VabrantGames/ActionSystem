package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ZoomAction extends PercentAction<Zoomable, ZoomAction>{
	
	public static ZoomAction getAction() {
		return getAction(ZoomAction.class);
	}
	
	public static ZoomAction zoomTo(Zoomable zoomable, float end, float duration, Interpolation interpolation) {
		ZoomAction action = getAction();
		action.zoomTo(end);
		action.set(zoomable, duration,  interpolation);
		return action;
	}
	
	public static ZoomAction zoomBy(Zoomable zoomable, float amount, float duration, Interpolation interpolation) {
		ZoomAction action = getAction();
		action.zoomBy(amount);
		action.set(zoomable, duration, interpolation);
		return action;
	}
	
	public static ZoomAction setZoom(Zoomable zoomable, float zoom) {
		ZoomAction action = getAction();
		action.zoomTo(zoom);
		action.set(zoomable, 0, null);
		return action;
	}

	private static final int ZOOM_TO = 0;
	private static final int ZOOM_BY = 1;
	
	private boolean restartZoomByFromEnd;
	private boolean setupZoom = true;
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
	
	private void setup() {
		if(setupZoom) {
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
	}
	
	@Override
	public ZoomAction start() {
		super.start();
		setup();
		return this;
	}
	
	@Override
	public ZoomAction restart() {
		super.restart();
		setup();
		return this;
	}
	
	@Override
	public ZoomAction end() {
		super.end();
		if(type != ZOOM_BY || type == ZOOM_BY && !restartZoomByFromEnd) setupZoom = false;
		return this;
	}
	
	@Override
	protected boolean hasConflict(Action action) {
		if(action instanceof ZoomAction) {
			ZoomAction conflictAction = (ZoomAction)action;
			if(conflictAction.type > -1) return true;
		}
		return false;
	}
	
	@Override
	public ZoomAction clear() {
		super.clear();
		setupZoom = true;
		start = 0;
		end = 0;
		amount = 0;
		restartZoomByFromEnd = false;
		type = -1;
		return this;
	}
	
	@Override
	public void reset() {
		super.reset();
		setupZoom = true;
		start = 0;
		end = 0;
		amount = 0;
		restartZoomByFromEnd = false;
		type = -1;
	}
}
