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

/** RGB color action. */
public class RGBColorAction extends ColorAction<RGBColorAction> {

	public static RGBColorAction obtain () {
		return obtain(RGBColorAction.class);
	}

	public static RGBColorAction changeColor (Colorable colorable, Color endColor, float duration, Interpolation interpolation) {
		return obtain().changeColor(endColor).set(colorable, duration, interpolation);
	}

	public static RGBColorAction changeColor (Colorable colorable, float red, float green, float blue, float duration,
		Interpolation interpolation) {
		return obtain().changeColor(red, green, blue).set(colorable, duration, interpolation);
	}

	public static RGBColorAction changeColor (Colorable colorable, float red, float green, float blue, float alpha, float duration,
		Interpolation interpolation) {
		return obtain().changeColor(red, green, blue, alpha).set(colorable, duration, interpolation);
	}

	public static RGBColorAction changeRed (Colorable colorable, float red, float duration, Interpolation interpolation) {
		return obtain().changeRed(red).set(colorable, duration, interpolation);
	}

	public static RGBColorAction changeGreen (Colorable colorable, float green, float duration, Interpolation interpolation) {
		return obtain().changeGreen(green).set(colorable, duration, interpolation);
	}

	public static RGBColorAction changeBlue (Colorable colorable, float blue, float duration, Interpolation interpolation) {
		return obtain().changeBlue(blue).set(colorable, duration, interpolation);
	}

	public static RGBColorAction changeAlpha (Colorable colorable, float endAlpha, float duration, Interpolation interpolation) {
		return ColorAction.changeAlpha(obtain(), colorable, endAlpha, duration, interpolation);
	}

	// Bits
	// soloChannel = 0
	// redChannel = 1
	// greenChannel = 2
	// blueChannel = 3
	private int options;

	public RGBColorAction changeColor (Color endColor) {
		setChannels(1, 1, 1);
		this.endColor.set(endColor);
		return this;
	}

	public RGBColorAction changeColor (float red, float green, float blue) {
		setChannels(1, 1, 1);
		endColor.r = red;
		endColor.g = green;
		endColor.b = blue;
		return this;
	}

	public RGBColorAction changeColor (float red, float green, float blue, float alpha) {
		changeColor(red, green, blue);
		changeAlpha(alpha);
		return this;
	}

	public RGBColorAction changeRed (float red) {
		setChannels(1, 0, 0);
		endColor.r = red;
		return this;
	}

	public RGBColorAction changeGreen (float green) {
		setChannels(0, 1, 0);
		endColor.g = green;
		return this;
	}

	public RGBColorAction changeBlue (float blue) {
		setChannels(0, 0, 1);
		endColor.b = blue;
		return this;
	}

	/** Only change values that were told to change. If {@link #changeRed} is selected then only the red channel will be affected.
	 * The other channels will not be affected, even if the action is restarted.
	 * @param solo
	 * @return */
	public RGBColorAction soloChannels (boolean solo) {
		options = ColorAction.setBit(options, 0, solo ? 1 : 0);
		return this;
	}

	private void setChannels (int red, int green, int blue) {
		options = ColorAction.setBit(options, 1, red);
		options = ColorAction.setBit(options, 2, green);
		options = ColorAction.setBit(options, 3, blue);
	}

	@Override
	protected void percent (float percent) {
		super.percent(percent);

		Color color = percentable.getColor();
		if (ColorAction.isBitOn(options, 1)) color.r = MathUtils.lerp(startColor.r, endColor.r, percent);
		if (ColorAction.isBitOn(options, 2)) color.g = MathUtils.lerp(startColor.g, endColor.g, percent);
		if (ColorAction.isBitOn(options, 3)) color.b = MathUtils.lerp(startColor.b, endColor.b, percent);
	}

	@Override
	protected void startLogic () {
		super.startLogic();

		if (!ColorAction.isBitOn(options, 0)) {
			Color c = percentable.getColor();
			c.a = alphaChannel == 0 ? startColor.a : (!reverse ? startColor.a : endColor.a);
			c.r = !ColorAction.isBitOn(options, 1) ? startColor.r : (!reverse ? startColor.r : endColor.r);
			c.g = !ColorAction.isBitOn(options, 2) ? startColor.g : (!reverse ? startColor.g : endColor.g);
			c.b = !ColorAction.isBitOn(options, 3) ? startColor.b : (!reverse ? startColor.b : endColor.b);
		}
	}

	@Override
	public void reset () {
		super.reset();
		options = 0;
	}
}
