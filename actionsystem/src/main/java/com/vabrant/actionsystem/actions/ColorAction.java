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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public abstract class ColorAction<T extends ColorAction<T>> extends PercentAction<Colorable, T> {

	protected static <T extends ColorAction<T>> T changeAlpha (T action, Colorable colorable, float endAlpha, float duration,
		Interpolation interpolation) {
		return action.changeAlpha(endAlpha).set(colorable, duration, interpolation);
	}

	public static int setBit (int num, int bit, int setTo) {
		return (num & ~(1 << bit)) | (setTo & 1) << bit;
	}

	public static boolean isBitOn (int num, int bit) {
		return (num & (1 << (bit))) != 0;
	}

	protected int alphaChannel = 0;
	protected boolean setupAction = true;
	protected Color startColor = new Color(Color.WHITE);
	protected Color endColor = new Color(Color.WHITE);

	public T changeAlpha (float endAlpha) {
		alphaChannel = 1;
		endColor.a = endAlpha;
		return (T)this;
	}

	@Override
	protected void percent (float percent) {
		if (alphaChannel == 1) percentable.getColor().a = MathUtils.lerp(startColor.a, endColor.a, percent);
	}

	@Override
	public T setup () {
		super.setup();

		if (setupAction) {
			setupAction = false;
			startColor.set(percentable.getColor());
		}
		return (T)this;
	}

	@Override
	public void reset () {
		super.reset();
		alphaChannel = 0;
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
	}
}
