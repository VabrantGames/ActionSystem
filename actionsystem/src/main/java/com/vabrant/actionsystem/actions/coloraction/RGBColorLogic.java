
package com.vabrant.actionsystem.actions.coloraction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.Colorable;
import com.vabrant.actionsystem.actions.coloraction.ColorAction.ColorLogic;
import com.vabrant.actionsystem.actions.coloraction.ColorAction.ColorLogicData;
import com.vabrant.actionsystem.actions.coloraction.RGBColorLogic.RGBColorLogicData;

public class RGBColorLogic implements ColorLogic<RGBColorLogicData> {

	// Bits
	// 1 = red
	// 2 = green
	// 3 = blue

	public static final RGBColorLogic INSTANCE = new RGBColorLogic();

	public static RGBColorLogicData getData () {
		return ActionPools.obtain(RGBColorLogicData.class);
	}

	public static ColorAction changeColor (Colorable colorable, Color endColor, float duration, Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeColor(data, endColor);
		return a;
	}

	public static ColorAction changeColor (Colorable colorable, float red, float green, float blue, float duration,
		Interpolation interpolation) {
		return changeColor(colorable, red, green, blue, -1, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float red, float green, float blue, float alpha, float duration,
		Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeColor(data, red, green, blue, alpha);
		return a;
	}

	public static ColorAction changeRed (Colorable colorable, float red, float duration, Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeRed(data, red);
		return a;
	}

	public static ColorAction changeGreen (Colorable colorable, float green, float duration, Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeGreen(data, green);
		return a;
	}

	public static ColorAction changeBlue (Colorable colorable, float blue, float duration, Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeBlue(data, blue);
		return a;
	}

	private RGBColorLogic () {
	}

	public void changeColor (RGBColorLogicData data, Color endColor) {
		ColorAction a = data.getAction();
		a.options |= 0b1111;
		a.endColor.set(endColor);
	}

	public RGBColorLogic changeColor (RGBColorLogicData data, float red, float green, float blue, float alpha) {
		ColorAction a = data.getAction();
		if (red != -1) a.options = ColorAction.setBit(a.options, 1, 1);
		if (green != -1) a.options = ColorAction.setBit(a.options, 2, 1);
		if (blue != -1) a.options = ColorAction.setBit(a.options, 3, 1);
		if (alpha != -1) a.changeAlpha(alpha);

		Color c = data.getAction().endColor;
		c.r = red;
		c.g = green;
		c.b = blue;
		return this;
	}

	public RGBColorLogic changeRed (RGBColorLogicData data, float red) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 1, 1);
		data.getAction().endColor.r = red;
		return this;
	}

	public RGBColorLogic changeGreen (RGBColorLogicData data, float green) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 2, 1);
		data.getAction().endColor.g = green;
		return this;
	}

	public RGBColorLogic changeBlue (RGBColorLogicData data, float blue) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 3, 1);
		data.getAction().endColor.b = blue;
		return this;
	}

	@Override
	public void percent (RGBColorLogicData data) {
		ColorAction a = data.getAction();

		int options = a.options;
		final float percent = a.getPercent();
		Color startColor = a.startColor;
		Color endColor = a.endColor;
		Color color = a.getPercentable().getColor();

		if (ColorAction.isBitOn(options, 1)) color.r = MathUtils.lerp(startColor.r, endColor.r, percent);
		if (ColorAction.isBitOn(options, 2)) color.g = MathUtils.lerp(startColor.g, endColor.g, percent);
		if (ColorAction.isBitOn(options, 3)) color.b = MathUtils.lerp(startColor.b, endColor.b, percent);
	}

	public static class RGBColorLogicData extends ColorLogicData {
	}

}
