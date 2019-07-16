package com.vabrant.actionsystem;

import com.badlogic.gdx.math.MathUtils;

public class ZoomAction extends TimeAction{
	
	private float start;
	private float end;
	private Zoomable zoomable;

	public void zoomTo(Zoomable zoomable, float end) {
		zoomTo(zoomable, zoomable.getZoom(), end);
	}
	
	public void zoomTo(Zoomable zoomable, float start, float end) {
		this.zoomable = zoomable;
		this.start = start;
		this.end = end;
	}
	
	public void zoomBy(Zoomable zoomable, float amount) {
		zoomBy(zoomable, zoomable.getZoom(), amount);
	}
	
	public void zoomBy(Zoomable zoomable, float start, float amount) {
		this.zoomable = zoomable;
		this.start = start;
		this.end = start + amount;
	}
	
	@Override
	protected void percent(float percent) {
		float zoom = MathUtils.lerp(start, end, percent);
		zoomable.setZoom(zoom);
	}
	
	@Override
	public void end() {
		super.end();
		if(reverseBackToStart) {
			zoomable.setZoom(start);
		}
		else {
			zoomable.setZoom(end);
		}
	}

	@Override
	public void restart() {
		super.restart();
		zoomable.setZoom(start);
	}
	
	@Override
	public void reset() {
		super.reset();
		start = 0;
		end = 0;
		zoomable = null;
	}
}
