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

public class ZoomAction extends PercentAction<Zoomable, ZoomAction>{
	
	public static ZoomAction obtain() {
		return obtain(ZoomAction.class);
	}
	
	public static ZoomAction zoomTo(Zoomable zoomable, float end, float duration, Interpolation interpolation) {
		return obtain()
				.zoomTo(end)
				.set(zoomable, duration,  interpolation);
	}
	
	public static ZoomAction zoomBy(Zoomable zoomable, float amount, float duration, Interpolation interpolation) {
		return obtain()
				.zoomBy(amount)
				.set(zoomable, duration, interpolation);
	}
	
	public static ZoomAction setZoom(Zoomable zoomable, float zoom) {
		return obtain()
				.zoomTo(zoom)
				.set(zoomable, 0, null);
	}

	private static final int ZOOM_TO = 0;
	private static final int ZOOM_BY = 1;
	
	private int type = -1;
	
	private boolean startZoomByFromEnd;
	private boolean setup = true;
	private float start;
	private float end;
	private float amount;

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
	
	public ZoomAction startZoomByFromEnd() {
		this.startZoomByFromEnd = true;
		return this;
	}
	
	@Override
	protected void percent(float percent) {
		percentable.setZoom(MathUtils.lerp(start, end, percent));
	}
	
	@Override
	public ZoomAction setup() {
		super.setup();
		
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
		if(type == ZOOM_BY && startZoomByFromEnd) setup = true;
	}
	
//	@Override
//	public boolean hasConflict(Action<?> action) {
//		if(action instanceof ZoomAction) {
//			ZoomAction conflictAction = (ZoomAction)action;
//			if(conflictAction.type > -1) return true;
//		}
//		return false;
//	}

	@Override
	public void clear() {
		super.clear();
		setup = true;
		start = 0;
		end = 0;
		amount = 0;
		startZoomByFromEnd = false;
		type = -1;
	}
}
