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

public class ScaleAction extends PercentAction<Scalable, ScaleAction> {

	public static ScaleAction obtain () {
		return obtain(ScaleAction.class);
	}

	public static ScaleAction scaleXBy (Scalable scalable, float amount, float duration, Interpolation interpolation) {
		return obtain().scaleXBy(amount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleYBy (Scalable scalable, float amount, float duration, Interpolation interpolation) {
		return obtain().scaleYBy(amount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleBy (Scalable scalable, float xAmount, float yAmount, float duration,
		Interpolation interpolation) {
		return obtain().scaleBy(xAmount, yAmount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleXTo (Scalable scalable, float end, float duration, Interpolation interpolation) {
		return obtain().scaleXTo(end).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleYTo (Scalable scalable, float end, float duration, Interpolation interpolation) {
		return obtain().scaleYTo(end).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleTo (Scalable scalable, float xEnd, float yEnd, float duration, Interpolation interpolation) {
		return obtain().scaleTo(xEnd, yEnd).set(scalable, duration, interpolation);
	}

	public static ScaleAction setScaleX (Scalable scalable, float scaleX) {
		return obtain().scaleXTo(scaleX).set(scalable, 0, Interpolation.linear);
	}

	public static ScaleAction setScaleY (Scalable scalable, float scaleY) {
		return obtain().scaleYTo(scaleY).set(scalable, 0, Interpolation.linear);
	}

	public static ScaleAction setScale (Scalable scalable, float scaleX, float scaleY) {
		return obtain().scaleTo(scaleX, scaleY).set(scalable, 0, Interpolation.linear);
	}

	private static final int SCALE_TO = 0;
	private static final int SCALE_BY = 1;

	private int xType = -1;
	private int yType = -1;

	private boolean solo;
	private boolean setupX = true;
	private boolean setupY = true;
	private boolean startXByFromEnd;
	private boolean startYByFromEnd;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private float xAmount;
	private float yAmount;

	public ScaleAction scaleXBy (float amount) {
		xAmount = amount;
		xType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleYBy (float amount) {
		yAmount = amount;
		yType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleBy (float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xType = SCALE_BY;
		yType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleXTo (float end) {
		this.xEnd = end;
		xType = SCALE_TO;
		return this;
	}

	public ScaleAction scaleYTo (float end) {
		this.yEnd = end;
		yType = SCALE_TO;
		return this;
	}

	public ScaleAction scaleTo (float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xType = SCALE_TO;
		yType = SCALE_TO;
		return this;
	}

	public ScaleAction startXByFromEnd () {
		startXByFromEnd = true;
		return this;
	}

	public ScaleAction startYByFromEnd () {
		startYByFromEnd = true;
		return this;
	}

	public ScaleAction solo (boolean solo) {
		this.solo = solo;
		return this;
	}

	@Override
	protected void percent (float percent) {
		switch (xType) {
		case SCALE_BY:
		case SCALE_TO:
			percentable.setScaleX(MathUtils.lerp(xStart, xEnd, percent));
			break;
		}

		switch (yType) {
		case SCALE_BY:
		case SCALE_TO:
			percentable.setScaleY(MathUtils.lerp(yStart, yEnd, percent));
			break;
		}
	}

	@Override
	public ScaleAction setup () {
		super.setup();

		if (setupX) {
			setupX = false;

			if (xType > -1 && yType == -1) yStart = percentable.getScaleY();

			switch (xType) {
			case SCALE_BY:
				xStart = percentable.getScaleX();
				xEnd = xStart + xAmount;
				break;
			case SCALE_TO:
				xStart = percentable.getScaleX();
				break;
			}
		}

		if (setupY) {
			setupY = false;

			if (xType == -1 && yType > -1) xStart = percentable.getScaleX();

			switch (yType) {
			case SCALE_BY:
				yStart = percentable.getScaleY();
				yEnd = yStart + yAmount;
				break;
			case SCALE_TO:
				yStart = percentable.getScaleY();
				break;
			}
		}
		return this;
	}

	@Override
	protected void startLogic () {
		super.startLogic();
		if (!solo) {
			percentable.setScaleX(xType == -1 ? xStart : (!reverse ? xStart : xEnd));
			percentable.setScaleY(yType == -1 ? yStart : (!reverse ? yStart : yEnd));
		}
	}

	@Override
	public void endLogic () {
		super.endLogic();
		if (xType == SCALE_BY && startXByFromEnd) setupX = true;
		if (yType == SCALE_BY && startYByFromEnd) setupY = true;
	}

	// @Override
	// public boolean hasConflict(Action<?> action) {
	// if(action instanceof ScaleAction) {
	// ScaleAction conflictAction = (ScaleAction)action;
	//
	// //both the x and y are being scaled
	// if(xType > -1 && yType > -1) return true;
	//
	// //only x is being scaled so as long as the other action is not using the x there is no conflict
	// if(conflictAction.xType > -1 && xType > -1) return true;
	// if(conflictAction.yType > -1 && yType > -1) return true;
	// }
	// return false;
	// }

	@Override
	public void clear () {
		super.clear();
		solo = false;
		startXByFromEnd = false;
		startYByFromEnd = false;
		setupX = true;
		setupY = true;
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		xType = -1;
		yType = -1;
	}
}
