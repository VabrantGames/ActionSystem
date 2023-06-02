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

	public static MoveAction moveYBy (Movable movable, float amount, float duration, Interpolation interpolation) {
		return obtain().moveYBy(amount).set(movable, duration, interpolation);
	}

	public static MoveAction moveBy (Movable movable, float xAmount, float yAmount, float duration, Interpolation interpolation) {
		return obtain().moveBy(xAmount, yAmount).set(movable, duration, interpolation);
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

	public static MoveAction moveYTo (Movable movable, float end, float duration, Interpolation interpolation) {
		return obtain().moveYTo(end).set(movable, duration, interpolation);
	}

	public static MoveAction moveTo (Movable movable, float xEnd, float yEnd, float duration, Interpolation interpolation) {
		return obtain().moveTo(xEnd, yEnd).set(movable, duration, interpolation);
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

	private boolean solo;
	private boolean setupX = true;
	private boolean setupY = true;
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

	public MoveAction moveYTo (float end) {
		this.yEnd = end;
		yType = MOVE_TO;
		return this;
	}

	public MoveAction moveTo (float xEnd, float yEnd) {
		this.xEnd = xEnd;
		this.yEnd = yEnd;
		xType = MOVE_TO;
		yType = MOVE_TO;
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

	public MoveAction moveYBy (float amount) {
		yAmount = amount;
		yType = MOVE_BY;
		return this;
	}

	public MoveAction moveBy (float xAmount, float yAmount) {
		this.xAmount = xAmount;
		this.yAmount = yAmount;
		xType = MOVE_BY;
		yType = MOVE_BY;
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

	/** Solos the direction. Has no use if both x and y are being moved.
	 * @param value
	 * @return */
	public MoveAction solo (boolean value) {
		solo = value;
		return this;
	}

	@Override
	protected void percent (float percent) {
		switch (xType) {
		case MOVE_BY:
		case MOVE_TO:
			percentable.setX(MathUtils.lerp(xStart, xEnd, percent));
			break;
		}

		switch (yType) {
		case MOVE_BY:
		case MOVE_TO:
			percentable.setY(MathUtils.lerp(yStart, yEnd, percent));
			break;
		}
	}

	@Override
	public MoveAction setup () {
		super.setup();

		if (setupX) {
			setupX = false;

			// If move x but not the y save the y
			if (xType > -1 && yType == -1) yStart = percentable.getY();

			switch (xType) {
			case MOVE_BY:
				xStart = percentable.getX();
				xEnd = xStart + xAmount;
				break;
			case MOVE_TO:
				xStart = percentable.getX();
				break;
			}
		}

		if (setupY) {
			setupY = false;

			// If move y but not x save the x
			if (yType > -1 && xType == -1) xStart = percentable.getX();

			switch (yType) {
			case MOVE_BY:
				yStart = percentable.getY();
				yEnd = yStart + yAmount;
				break;
			case MOVE_TO:
				yStart = percentable.getY();
				break;
			}
		}
		return this;
	}

	@Override
	protected void startLogic () {
		super.startLogic();
		if (!solo) {
			percentable.setX(xType == -1 ? xStart : (!reverse ? xStart : xEnd));
			percentable.setY(yType == -1 ? yStart : (!reverse ? yStart : yEnd));
		}
	}

	@Override
	protected void endLogic () {
		super.endLogic();
		if (xType == MOVE_BY && startXByFromEnd) setupX = true;
		if (yType == MOVE_BY && startYByFromEnd) setupY = true;
	}

	// @Override
	// public boolean hasConflict(Action<?> action) {
	// if(action instanceof MoveAction) {
	// MoveAction conflictAction = (MoveAction)action;
	//
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
		xAmount = 0;
		yAmount = 0;
		xStart = 0;
		xEnd = 0;
		yStart = 0;
		yEnd = 0;
		setupX = true;
		setupY = true;
		startXByFromEnd = false;
		startYByFromEnd = false;
		xType = -1;
		yType = -1;
	}
}
