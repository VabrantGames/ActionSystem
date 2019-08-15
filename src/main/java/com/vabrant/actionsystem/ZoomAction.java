package com.vabrant.actionsystem;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ZoomAction extends PercentAction<Zoomable>{
	
	public static ZoomAction getAction() {
		return getAction(ZoomAction.class);
	}
	
	public static ZoomAction zoomTo(Zoomable zoomable, float end, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ZoomAction action = getAction();
		action.zoomTo(end);
		action.set(zoomable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ZoomAction zoomBy(Zoomable zoomable, float amount, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ZoomAction action = getAction();
		action.zoomBy(amount);
		action.set(zoomable, duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ZoomAction setZoom(Zoomable zoomable, float zoom) {
		ZoomAction action = getAction();
		action.zoomTo(zoom);
		action.set(zoomable, 0, false, null);
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
	private ZoomType type = ZoomType.NONE;

	public ZoomAction zoomTo(float end) {
		this.end = end;
		type = ZoomType.ZOOM_TO;
		return this;
	}
	
	public ZoomAction zoomBy(float amount) {
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
		percentable.setZoom(MathUtils.lerp(start, end, percent));
	}
	
	@Override
	public void start() {
		super.start();
		
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
		restartZoomByFromEnd = false;
		type = ZoomType.NONE;
	}
}
