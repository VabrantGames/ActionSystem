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

package com.vabrant.actionsystem.actions.coloraction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.vabrant.actionsystem.actions.Colorable;
import com.vabrant.actionsystem.actions.PercentAction;

public class ColorAction extends PercentAction<Colorable, ColorAction> {

	public static ColorAction obtain () {
		return obtain(ColorAction.class);
	}

	public static ColorAction changeAlpha (Colorable colorable, float endAlpha, float duration, Interpolation interpolation) {
		return obtain().changeAlpha(endAlpha).set(colorable, duration, interpolation);
	}

	public static ColorAction changeAlpha (Colorable colorable, float startAlpha, float endAlpha, float duration,
		Interpolation interpolation) {
		return obtain().changeAlpha(startAlpha, endAlpha).set(colorable, duration, interpolation);
	}

	public static int setBit (int num, int bit, int setTo) {
		return (num & ~(1 << bit)) | (setTo & 1) << bit;
	}

	public static boolean isBitOn (int num, int bit) {
		return (num & (1 << (bit))) != 0;
	}

	protected int options;
	protected boolean setupAction = true;
	protected boolean isAlphaStartSet;
	protected Color startColor = new Color(Color.WHITE);
	protected Color endColor = new Color(Color.WHITE);
	protected ColorLogic logic;
	protected ColorLogicData data;

	public ColorAction changeAlpha (float endAlpha) {
		return changeAlpha(-1, endAlpha);
	}

	public ColorAction changeAlpha (float startAlpha, float endAlpha) {
		endColor.a = endAlpha;
		options = setBit(options, 0, 1);

		if (startAlpha != -1) {
			startColor.a = startAlpha;
			isAlphaStartSet = true;
		}
		return this;
	}

	public ColorAction setStartColor (Color color) {
		if (color == null) return this;

		startColor.set(color);
		return this;
	}

	public void setStartAlphaAsSet () {
		isAlphaStartSet = true;
	}

	public ColorAction setStartColor (float red, float green, float blue) {
		startColor.set(red, green, blue, startColor.a);
		return this;
	}

	public int getOptions () {
		return options;
	}

	public void setOptions (int options) {
		this.options = options;
	}

	public Color getStartColor () {
		return startColor;
	}

	public Color getEndColor () {
		return endColor;
	}

	public ColorLogic getLogic () {
		return logic;
	}

	public ColorLogicData getData () {
		return data;
	}

	public ColorAction setLogicAndData (ColorLogic<?> logic, ColorLogicData data) {
		if (logic == null) throw new RuntimeException("Logic is null");
		if (data == null) throw new RuntimeException("Data is null");

		this.logic = logic;
		this.data = data;

		this.data.setAction(this);

		return this;
	}

	@Override
	protected void percent (float percent) {
		if (isBitOn(options, 0)) {
			percentable.getColor().a = MathUtils.lerp(startColor.a, endColor.a, percent);
		}
		if (logic != null) logic.percent(data);
	}

	@Override
	protected void startLogic () {
		super.startLogic();

		if (setupAction) {
			setupAction = false;

			if (isBitOn(options, 0) && !isAlphaStartSet) {
				isAlphaStartSet = true;
				startColor.a = percentable.getColor().a;
			}

			if (data != null) {
				data.onSetup();
			}
		}

		if (data != null) {
			data.onStart();
		}
	}

	@Override
	protected void endLogic () {
		super.endLogic();
		if (data != null) {
			data.onEnd();
		}
	}

	@Override
	public void reset () {
		super.reset();
		if (data != null) data.reset();
		data = null;
		logic = null;
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
		isAlphaStartSet = false;
	}

	public static abstract class ColorLogicData implements Poolable {

		private ColorAction action;

		void setAction (ColorAction action) {
			this.action = action;
		}

		public ColorAction getAction () {
			return action;
		}

		@Override
		public void reset () {
			action = null;
		}

		protected void onSetup () {
		}

		protected void onStart () {
		}

		protected void onEnd () {
		}
	}

	public interface ColorLogic<T extends ColorLogicData> {
		void percent (T data);
	}

}
