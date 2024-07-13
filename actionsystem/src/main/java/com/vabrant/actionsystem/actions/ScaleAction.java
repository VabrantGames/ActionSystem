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

	public static ScaleAction scaleXBy (Scalable scalable, float start, float amount, float duration,
		Interpolation interpolation) {
		return obtain().scaleXBy(start, amount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleYBy (Scalable scalable, float amount, float duration, Interpolation interpolation) {
		return obtain().scaleYBy(amount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleYBy (Scalable scalable, float start, float amount, float duration,
		Interpolation interpolation) {
		return obtain().scaleYBy(start, amount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleBy (Scalable scalable, float xAmount, float yAmount, float duration,
		Interpolation interpolation) {
		return obtain().scaleBy(xAmount, yAmount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleBy (Scalable scalable, float xStart, float xAmount, float yStart, float yAmount, float duration,
		Interpolation interpolation) {
		return obtain().scaleBy(xStart, xAmount, yStart, yAmount).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleXTo (Scalable scalable, float end, float duration, Interpolation interpolation) {
		return obtain().scaleXTo(end).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleXTo (Scalable scalable, float start, float end, float duration, Interpolation interpolation) {
		return obtain().scaleXTo(start, end).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleYTo (Scalable scalable, float end, float duration, Interpolation interpolation) {
		return obtain().scaleYTo(end).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleYTo (Scalable scalable, float start, float end, float duration, Interpolation interpolation) {
		return obtain().scaleYTo(start, end).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleTo (Scalable scalable, float xEnd, float yEnd, float duration, Interpolation interpolation) {
		return obtain().scaleTo(xEnd, yEnd).set(scalable, duration, interpolation);
	}

	public static ScaleAction scaleTo (Scalable scalable, float xStart, float xEnd, float yStart, float yEnd, float duration,
		Interpolation interpolation) {
		return obtain().scaleTo(xStart, xEnd, yStart, yEnd).set(scalable, duration, interpolation);
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
	private boolean setupX;
	private boolean setupY;
	private boolean startXByFromEnd;
	private boolean startYByFromEnd;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private float xAmount;
	private float yAmount;

	public ScaleAction scaleXBy (float amount) {
		setupX = true;
		xAmount = amount;
		xType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleXBy (float start, float amount) {
		xStart = start;
		xEnd = xStart + amount;
		xAmount = amount;
		xType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleYBy (float amount) {
		setupY = true;
		yAmount = amount;
		yType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleYBy (float start, float amount) {
		yStart = start;
		yEnd = yStart + amount;
		yAmount = amount;
		yType = SCALE_BY;
		return this;
	}

	public ScaleAction scaleBy (float xAmount, float yAmount) {
		scaleXBy(xAmount);
		scaleYBy(yAmount);
		return this;
	}

	public ScaleAction scaleBy (float xStart, float xAmount, float yStart, float yAmount) {
		scaleXBy(xStart, xAmount);
		scaleYBy(yStart, yAmount);
		return this;
	}

	public ScaleAction scaleXTo (float end) {
		setupX = true;
		xEnd = end;
		xType = SCALE_TO;
		return this;
	}

	public ScaleAction scaleXTo (float start, float end) {
		xStart = start;
		xEnd = end;
		xType = SCALE_TO;
		return this;
	}

	public ScaleAction scaleYTo (float end) {
		setupY = true;
		yEnd = end;
		yType = SCALE_TO;
		return this;
	}

	public ScaleAction scaleYTo (float start, float end) {
		yStart = start;
		yEnd = end;
		yType = SCALE_TO;
		return this;
	}

	public ScaleAction scaleTo (float xEnd, float yEnd) {
		scaleXTo(xEnd);
		scaleYTo(yEnd);
		return this;
	}

	public ScaleAction scaleTo (float xStart, float xEnd, float yStart, float yEnd) {
		scaleXTo(xStart, xEnd);
		scaleYTo(yStart, yEnd);
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

	@Override
	protected void percent (float percent) {
		if (xType > -1) percentable.setScaleX(MathUtils.lerp(xStart, xEnd, percent));
		if (yType > -1) percentable.setScaleY(MathUtils.lerp(yStart, yEnd, percent));
	}

	@Override
	protected void startLogic () {
		super.startLogic();

		if (setupX) {
			setupX = false;
			xStart = percentable.getScaleX();

			if (xType == SCALE_BY) {
				xEnd = xStart + xAmount;
			}
		}

		if (setupY) {
			setupY = false;
			yStart = percentable.getScaleY();

			if (yType == SCALE_BY) {
				yEnd = yStart + yAmount;
			}
		}
	}

	@Override
	public void endLogic () {
		super.endLogic();
		if (xType == SCALE_BY && startXByFromEnd) setupX = true;
		if (yType == SCALE_BY && startYByFromEnd) setupY = true;
	}

	@Override
	public void clear () {
		super.clear();
		startXByFromEnd = false;
		startYByFromEnd = false;
		setupX = false;
		setupY = false;
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
