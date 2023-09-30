
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
		return changeColor(colorable, null, endColor, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, Color startColor, Color endColor, float duration,
		Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeColor(data, startColor, endColor);
		return a;
	}

	public static ColorAction changeColor (Colorable colorable, float red, float green, float blue, float duration,
		Interpolation interpolation) {
		return changeColor(colorable, red, green, blue, -1, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float red, float green, float blue, float alpha, float duration,
		Interpolation interpolation) {
		return changeColor(colorable, -1, -1, -1, -1, red, green, blue, alpha, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float startRed, float startGreen, float startBlue, float endRed,
		float endGreen, float endBlue, float duration, Interpolation interpolation) {
		return changeColor(colorable, startRed, startGreen, startBlue, -1, endRed, endGreen, endBlue, -1, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float startRed, float startGreen, float startBlue,
		float startAlpha, float endRed, float endGreen, float endBlue, float endAlpha, float duration,
		Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeColor(data, startRed, startGreen, startBlue, startAlpha, endRed, endGreen, endBlue, endAlpha);
		return a;
	}

	public static ColorAction changeRed (Colorable colorable, float red, float duration, Interpolation interpolation) {
		return changeRed(colorable, -1, red, duration, interpolation);
	}

	public static ColorAction changeRed (Colorable colorable, float startRed, float endRed, float duration,
		Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeRed(data, startRed, endRed);
		return a;
	}

	public static ColorAction changeGreen (Colorable colorable, float endGreen, float duration, Interpolation interpolation) {
		return changeGreen(colorable, -1, endGreen, duration, interpolation);
	}

	public static ColorAction changeGreen (Colorable colorable, float startGreen, float endGreen, float duration,
		Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeGreen(data, startGreen, endGreen);
		return a;
	}

	public static ColorAction changeBlue (Colorable colorable, float endBlue, float duration, Interpolation interpolation) {
		return changeBlue(colorable, -1, endBlue, duration, interpolation);
	}

	public static ColorAction changeBlue (Colorable colorable, float startBlue, float endBlue, float duration,
		Interpolation interpolation) {
		RGBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeBlue(data, startBlue, endBlue);
		return a;
	}

	private RGBColorLogic () {
	}

	public void changeColor (RGBColorLogicData data, Color startColor, Color endColor) {
		ColorAction a = data.getAction();
		a.options |= 0b1111;
		a.endColor.set(endColor);

		if (startColor != null) {
			a.startColor.set(startColor);
			data.isRedStartSet = true;
			data.isGreenStartSet = true;
			data.isBlueStartSet = true;
			a.setStartAlphaAsSet();
		}
	}

	public RGBColorLogic changeColor (RGBColorLogicData data, float startRed, float startGreen, float startBlue, float startAlpha,
		float endRed, float endGreen, float endBlue, float endAlpha) {
		ColorAction a = data.getAction();

		if (endAlpha != -1) {
			a.changeAlpha(startAlpha, endAlpha);
		}

		if (endRed != -1) {
			changeRed(data, startRed, endRed);
		}

		if (endGreen != -1) {
			changeGreen(data, startGreen, endGreen);
		}

		if (endBlue != -1) {
			changeBlue(data, startBlue, endBlue);
		}
		return this;
	}

	public RGBColorLogic changeRed (RGBColorLogicData data, float startRed, float endRed) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 1, 1);
		a.endColor.r = endRed;

		if (startRed != -1) {
			a.startColor.r = startRed;
			data.isRedStartSet = true;
		}
		return this;
	}

	public RGBColorLogic changeGreen (RGBColorLogicData data, float startGreen, float endGreen) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 2, 1);
		a.endColor.g = endGreen;

		if (startGreen != -1) {
			a.startColor.g = startGreen;
			data.isGreenStartSet = true;
		}
		return this;
	}

	public RGBColorLogic changeBlue (RGBColorLogicData data, float startBlue, float endBlue) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 3, 1);
		a.endColor.b = endBlue;

		if (startBlue != -1) {
			a.startColor.b = startBlue;
			data.isBlueStartSet = true;
		}
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

		protected boolean isRedStartSet;
		protected boolean isGreenStartSet;
		protected boolean isBlueStartSet;

		@Override
		protected void onSetup () {
			ColorAction a = getAction();
			Color colorable = getAction().getPercentable().getColor();

			if (ColorAction.isBitOn(a.options, 1) && !isRedStartSet) {
				isRedStartSet = true;
				a.startColor.r = colorable.r;
			}

			if (ColorAction.isBitOn(a.options, 2) && !isGreenStartSet) {
				isGreenStartSet = true;
				a.startColor.g = colorable.g;
			}

			if (ColorAction.isBitOn(a.options, 3) && !isBlueStartSet) {
				isBlueStartSet = true;
				a.startColor.b = colorable.b;
			}
		}

		@Override
		public void reset () {
			super.reset();
			isRedStartSet = false;
			isGreenStartSet = false;
			isBlueStartSet = false;
		}
	}

}
