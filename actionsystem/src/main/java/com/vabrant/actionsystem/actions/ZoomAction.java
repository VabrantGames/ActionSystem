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

public class ZoomAction extends PercentAction<Zoomable, ZoomAction> {

	public static ZoomAction obtain () {
		return obtain(ZoomAction.class);
	}

	public static ZoomAction zoomTo (Zoomable zoomable, float end, float duration, Interpolation interpolation) {
		return obtain().zoomTo(end).set(zoomable, duration, interpolation);
	}

	public static ZoomAction zoomTo (Zoomable zoomable, float start, float end, float duration, Interpolation interpolation) {
		return obtain().zoomTo(start, end).set(zoomable, duration, interpolation);
	}

	public static ZoomAction zoomBy (Zoomable zoomable, float amount, float duration, Interpolation interpolation) {
		return obtain().zoomBy(amount).set(zoomable, duration, interpolation);
	}

	public static ZoomAction zoomBy (Zoomable zoomable, float start, float amount, float duration, Interpolation interpolation) {
		return obtain().zoomBy(start, amount).set(zoomable, duration, interpolation);
	}

	public static ZoomAction setZoom (Zoomable zoomable, float zoom) {
		return obtain().zoomTo(zoom).set(zoomable, 0, null);
	}

	private static final int ZOOM_TO = 0;
	private static final int ZOOM_BY = 1;

	private int type = -1;

	private boolean startZoomByFromEnd;
	private boolean setup;
	private float start;
	private float end;
	private float amount;

	public ZoomAction zoomTo (float end) {
		setup = true;
		this.end = end;
		type = ZOOM_TO;
		return this;
	}

	public ZoomAction zoomTo (float start, float end) {
		this.start = start;
		this.end = end;
		type = ZOOM_TO;
		return this;
	}

	public ZoomAction zoomBy (float amount) {
		setup = true;
		this.amount = amount;
		type = ZOOM_BY;
		return this;
	}

	public ZoomAction zoomBy (float start, float amount) {
		setup = false;
		this.start = start;
		end = start + amount;
		this.amount = amount;
		return this;
	}

	public ZoomAction startZoomByFromEnd () {
		this.startZoomByFromEnd = true;
		return this;
	}

	@Override
	protected void percent (float percent) {
		percentable.setZoom(MathUtils.lerp(start, end, percent));
	}

	@Override
	protected void startLogic () {
		super.startLogic();

		if (setup) {
			setup = false;
			start = percentable.getZoom();

			if (type == ZOOM_BY) {
				end = start + amount;
			}
		}
	}

	@Override
	public void endLogic () {
		super.endLogic();
		if (type == ZOOM_BY && startZoomByFromEnd) setup = true;
	}

	@Override
	public void clear () {
		super.clear();
		setup = false;
		start = 0;
		end = 0;
		amount = 0;
		startZoomByFromEnd = false;
		type = -1;
	}
}
