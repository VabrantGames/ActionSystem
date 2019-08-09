package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ZoomAction extends TimeAction{
	
	public static ZoomAction getAction() {
		return getAction(ZoomAction.class);
	}
	
	public static ZoomAction zoomTo(Zoomable zoomable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ZoomAction action = getAction();
		action.zoomTo(zoomable, end);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ZoomAction zoomBy(Zoomable zoomable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ZoomAction action = getAction();
		action.zoomBy(zoomable, amount);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ZoomAction setZoom(Zoomable zoomable, float zoom) {
		ZoomAction action = getAction();
		action.zoomTo(zoomable, zoom);
		action.set(0, false, null);
		return action;
	}

	private enum ZoomType{
		ZOOM_TO,
		ZOOM_BY,
		NONE
	}
	
	private boolean restartZoomByFromEnd;
	private boolean setupZoom = true;
	private float start;
	private float end;
	private float amount;
	private Zoomable zoomable;
	private ZoomType type = ZoomType.NONE;

	public ZoomAction zoomTo(Zoomable zoomable, float end) {
		this.zoomable = zoomable;
		this.end = end;
		type = ZoomType.ZOOM_TO;
		return this;
	}
	
	public ZoomAction zoomBy(Zoomable zoomable, float amount) {
		this.zoomable = zoomable;
		this.amount = amount;
		type = ZoomType.ZOOM_BY;
		return this;
	}
	
	public ZoomAction restartZoomByFromEnd() {
		this.restartZoomByFromEnd = true;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		float zoom = MathUtils.lerp(start, end, percent);
		zoomable.setZoom(zoom);
	}
	
	@Override
	public void start() {
		super.start();
		
		if(setupZoom) {
			switch(type) {
				case ZOOM_BY:
					start = zoomable.getZoom();
					end = start + amount;
					break;
				case ZOOM_TO:
					start = zoomable.getZoom();
					break;
			}
		}
	}
	
	@Override
	public void end() {
		super.end();
		if(!type.equals(ZoomType.ZOOM_BY) || type.equals(ZoomType.ZOOM_BY) && !restartZoomByFromEnd) setupZoom = false;
	}
	
	@Override
	public void reset() {
		super.reset();
		setupZoom = true;
		start = 0;
		end = 0;
		amount = 0;
		zoomable = null;
		restartZoomByFromEnd = false;
		type = ZoomType.NONE;
	}
}
