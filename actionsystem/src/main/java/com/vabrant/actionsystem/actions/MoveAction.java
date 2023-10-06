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

public class MoveAction extends PercentAction<Movable, MoveAction> {

	public static MoveAction obtain () {
		return obtain(MoveAction.class);
	}

	public static MoveAction moveXBy (Movable movable, float amount, float duration, Interpolation interpolation) {
		return obtain().moveXBy(amount).set(movable, duration, interpolation);
	}

	public static MoveAction moveXBy (Movable movable, float start, float amount, float duration, Interpolation interpolation) {
		return obtain().moveXBy(start, amount).set(movable, duration, interpolation);
	}

	public static MoveAction moveYBy (Movable movable, float amount, float duration, Interpolation interpolation) {
		return obtain().moveYBy(amount).set(movable, duration, interpolation);
	}

	public static MoveAction moveYBy (Movable movable, float start, float amount, float duration, Interpolation interpolation) {
		return obtain().moveYBy(start, amount).set(movable, duration, interpolation);
	}

	public static MoveAction moveBy (Movable movable, float xAmount, float yAmount, float duration, Interpolation interpolation) {
		return obtain().moveBy(xAmount, yAmount).set(movable, duration, interpolation);
	}

	public static MoveAction moveBy (Movable movable, float xStart, float xAmount, float yStart, float yAmount, float duration,
		Interpolation interpolation) {
		return obtain().moveBy(xStart, xAmount, yStart, yAmount).set(movable, duration, interpolation);
	}

	public static MoveAction moveByAngleRad (Movable movable, float radians, float amount, float duration,
		Interpolation interpolation) {
		return moveByAngleDeg(movable, MathUtils.radiansToDegrees * radians, amount, duration, interpolation);
	}

	public static MoveAction moveByAngleDeg (Movable movable, float degrees, float amount, float duration,
		Interpolation interpolation) {
		return obtain().moveByAngle(degrees, amount).set(movable, duration, interpolation);
	}

	public static MoveAction moveXTo (Movable movable, float end, float duration, Interpolation interpolation) {
		return obtain().moveXTo(end).set(movable, duration, interpolation);
	}

	public static MoveAction moveXTo (Movable movable, float start, float end, float duration, Interpolation interpolation) {
		return obtain().moveXTo(start, end).set(movable, duration, interpolation);
	}

	public static MoveAction moveYTo (Movable movable, float end, float duration, Interpolation interpolation) {
		return obtain().moveYTo(end).set(movable, duration, interpolation);
	}

	public static MoveAction moveYTo (Movable movable, float start, float end, float duration, Interpolation interpolation) {
		return obtain().moveYTo(start, end).set(movable, duration, interpolation);
	}

	public static MoveAction moveTo (Movable movable, float xEnd, float yEnd, float duration, Interpolation interpolation) {
		return obtain().moveTo(xEnd, yEnd).set(movable, duration, interpolation);
	}

	public static MoveAction moveTo (Movable movable, float xStart, float xEnd, float yStart, float yEnd, float duration,
		Interpolation interpolation) {
		return obtain().moveTo(xStart, xEnd, yStart, yEnd).set(movable, duration, interpolation);
	}

	public static MoveAction setX (Movable movable, float x) {
		return obtain().moveXTo(x).set(movable, 0, null);
	}

	public static MoveAction setY (Movable movable, float y) {
		return obtain().moveYTo(y).set(movable, 0, null);
	}

	public static MoveAction setPosition (Movable movable, float x, float y) {
		return obtain().moveTo(x, y).set(movable, 0, null);
	}

	public static float offsetX (float angle, float amount) {
		return amount * MathUtils.cosDeg(angle);
	}

	public static float offsetY (float angle, float amount) {
		return amount * MathUtils.sinDeg(angle);
	}

	private static final int MOVE_TO = 0;
	private static final int MOVE_BY = 1;

	private int xType = -1;
	private int yType = -1;
	private boolean isXStartSet;
	private boolean isYStartSet;
	private boolean setupX;
	private boolean setupY;
	private boolean startXByFromEnd;
	private boolean startYByFromEnd;
	private float xStart;
	private float xEnd;
	private float yStart;
	private float yEnd;
	private float yAmount;
	private float xAmount;

	public MoveAction moveXTo (float end) {
		this.xEnd = end;
		xType = MOVE_TO;
		return this;
	}

	public MoveAction moveXTo (float start, float end) {
		isXStartSet = true;
		xStart = start;
		moveXTo(end);
		return this;
	}

	public MoveAction moveYTo (float end) {
		this.yEnd = end;
		yType = MOVE_TO;
		return this;
	}

	public MoveAction moveYTo (float start, float end) {
		isYStartSet = true;
		yStart = start;
		moveYTo(end);
		return this;
	}

	public MoveAction moveTo (float xEnd, float yEnd) {
		moveXTo(xEnd);
		moveYTo(yEnd);
		return this;
	}

	public MoveAction moveTo (float xStart, float xEnd, float yStart, float yEnd) {
		moveXTo(xStart, xEnd);
		moveYTo(yStart, yEnd);
		return this;
	}

	public MoveAction moveByAngle (float angle, float amount) {
		angle %= 360;
		xAmount = offsetX(angle, amount);
		yAmount = offsetY(angle, amount);
		xType = MOVE_BY;
		yType = MOVE_BY;
		return this;
	}

	public MoveAction moveXBy (float amount) {
		xAmount = amount;
		xType = MOVE_BY;
		return this;
	}

	public MoveAction moveXBy (float start, float amount) {
		moveXBy(amount);
		xStart = start;
		xEnd = xStart + xAmount;
		isXStartSet = true;
		return this;
	}

	public MoveAction moveYBy (float amount) {
		yAmount = amount;
		yType = MOVE_BY;
		return this;
	}

	public MoveAction moveYBy (float start, float amount) {
		moveYBy(amount);
		yStart = start;
		yEnd = yStart + amount;
		isYStartSet = true;
		return this;
	}

	public MoveAction moveBy (float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xType = MOVE_BY;
		yType = MOVE_BY;
		return this;
	}

	public MoveAction moveBy (float xStart, float xEnd, float yStart, float yEnd) {
		moveXBy(xStart, xEnd);
		moveYBy(yStart, yEnd);
		return this;
	}

	public MoveAction startXByFromEnd () {
		startXByFromEnd = true;
		return this;
	}

	public MoveAction startYByFromEnd () {
		startYByFromEnd = true;
		return this;
	}

	@Override
	protected void percent (float percent) {
		if (xType > -1) percentable.setX(MathUtils.lerp(xStart, xEnd, percent));
		if (yType > -1) percentable.setY(MathUtils.lerp(yStart, yEnd, percent));
	}

	@Override
	public MoveAction setup () {
		super.setup();

		if (setupX || xType > -1 && !isXStartSet) {
			isXStartSet = true;
			xStart = percentable.getX();

			if (xType == MOVE_BY) {
				xEnd = xStart + xAmount;
			}
		}

		if (setupY || yType > -1 && !isYStartSet) {
			isYStartSet = true;
			yStart = percentable.getY();

			if (yType == MOVE_BY) {
				yEnd = yStart + yAmount;
			}
		}
		return this;
	}

	@Override
	protected void endLogic () {
		super.endLogic();
		if (xType == MOVE_BY && startXByFromEnd) setupX = true;
		if (yType == MOVE_BY && startYByFromEnd) setupY = true;
	}

	@Override
	public void clear () {
		super.clear();
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		startXByFromEnd = false;
		startYByFromEnd = false;
		xType = -1;
		yType = -1;
		isXStartSet = false;
		isYStartSet = false;
	}
}
