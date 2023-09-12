
package com.vabrant.actionsystem.actions.coloraction;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.ActionPools;
import com.vabrant.actionsystem.actions.Colorable;
import com.vabrant.actionsystem.actions.coloraction.ColorAction.ColorLogic;
import com.vabrant.actionsystem.actions.coloraction.ColorAction.ColorLogicData;
import com.vabrant.actionsystem.actions.coloraction.HSBColorLogic.HSBColorLogicData;

import java.util.Arrays;

public class HSBColorLogic implements ColorLogic<HSBColorLogicData> {

	// Bits
	// 0 = hue
	// 1 = saturation
	// 2 = brightness

	public static final HSBColorLogic INSTANCE = new HSBColorLogic();

	public static HSBColorLogicData getData () {
		return ActionPools.obtain(HSBColorLogicData.class);
	}

	public static ColorAction changeColor (Colorable colorable, float hue, float saturation, float brightness, float duration,
		Interpolation interpolation) {
		return changeColor(colorable, hue, saturation, brightness, -1, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float hue, float saturation, float brightness, float alpha,
		float duration, Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeColor(data, hue, saturation, brightness, alpha);
		return a;
	}

	public static ColorAction changeHue (Colorable colorable, float hue, float duration, Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeHue(data, hue);
		return a;
	}

	public static ColorAction changeSaturation (Colorable colorable, float saturation, float duration,
		Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeSaturation(data, saturation);
		return a;
	}

	public static ColorAction changeBrightness (Colorable colorable, float brightness, float duration,
		Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeBrightness(data, brightness);
		return a;
	}

	private HSBColorLogic () {
	}

	public HSBColorLogic changeColor (HSBColorLogicData data, float hue, float saturation, float brightness, float alpha) {
		ColorAction a = data.getAction();

		if (hue != -1) {
			a.options = ColorAction.setBit(a.options, 1, 1);
			data.endHSB[0] = hue;
		}

		if (saturation != -1) {
			a.options = ColorAction.setBit(a.options, 2, 1);
			data.endHSB[1] = saturation;
		}

		if (brightness != -1) {
			a.options = ColorAction.setBit(a.options, 3, 1);
			data.endHSB[2] = brightness;
		}

		if (alpha != -1) {
			a.changeAlpha(alpha);
		}

		return this;
	}

	public HSBColorLogic changeHue (HSBColorLogicData data, float hue) {
// ColorAction a = data.getAction();
// a.options = ColorAction.setBit(a.options, 1, 1);
		setupChannel(data, 1, hue);
		return this;
	}

	public HSBColorLogic changeSaturation (HSBColorLogicData data, float green) {
// ColorAction a = data.getAction();
// a.options = ColorAction.setBit(a.options, 2, 1);
		setupChannel(data, 2, green);
		return this;
	}

	public HSBColorLogic changeBrightness (HSBColorLogicData data, float blue) {
// ColorAction a = data.getAction();
// a.options = ColorAction.setBit(a.options, 3, 1);
		setupChannel(data, 3, blue);
		return this;
	}

	private void setupChannel (HSBColorLogicData data, int idx, float value) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, idx, 1);
		data.endHSB[idx - 1] = value;
	}

	@Override
	public void percent (HSBColorLogicData data) {
		ColorAction a = data.getAction();
		int options = a.options;
		float percent = a.getPercent();
		float[] startHSB = data.startHSB;
		float[] endHSB = data.endHSB;
		Color c = a.getPercentable().getColor();
		Color endColor = a.endColor;

		float h = ColorAction.isBitOn(options, 1) ? MathUtils.lerp(startHSB[0], endHSB[0], percent) : getHue(c);
		float s = ColorAction.isBitOn(options, 2) ? MathUtils.lerp(startHSB[1], endHSB[1], percent) : getSaturation(c);
		float b = ColorAction.isBitOn(options, 3) ? MathUtils.lerp(startHSB[2], endHSB[2], percent) : getBrightness(c);
		HSBToRGB(endColor, h, s, b);
		c.set(endColor.r, endColor.g, endColor.b, c.a);
	}

	public static Color HSBToRGB (Color color, float hue, float saturation, float brightness) {
		if (saturation == 0) {
			color.r = color.g = color.b = brightness;
			return color;
		}

		float h = hue / 60f;
		float c = brightness * saturation;
		float x = c * (1f - Math.abs(h % 2f - 1f));
		float m = brightness - c;

		switch ((int)h) {
		case 0:
			color.r = c + m;
			color.g = x + m;
			color.b = m;
			break;
		case 1:
			color.r = x + m;
			color.g = c + m;
			color.b = m;
			break;
		case 2:
			color.r = m;
			color.g = c + m;
			color.b = x + m;
			break;
		case 3:
			color.r = m;
			color.g = x + m;
			color.b = c + m;
			break;
		case 4:
			color.r = x + m;
			color.g = m;
			color.b = c + m;
			break;
		case 5:
			color.r = c + m;
			color.g = m;
			color.b = x + m;
			break;
		}
		return color;
	}

	public static float getHue (Color color) {
		float min = Math.min(Math.min(color.r, color.g), color.b);
		float max = Math.max(Math.max(color.r, color.g), color.b);
		float difference = max - min;

		if (difference == 0) {
			return 0f;
		} else if (max == color.r) {
			return 60f * ((color.g - color.b) / difference);
		} else if (max == color.g) {
			return 60f * (((color.b - color.r) / difference) + 2f);
		} else {
			return 60f * (((color.r - color.g) / difference) + 4f);
		}
	}

	public static float getSaturation (Color color) {
		float min = Math.min(Math.min(color.r, color.g), color.b);
		float max = Math.max(Math.max(color.r, color.g), color.b);
		float difference = max - min;
		return max == 0 ? 0 : difference / max;
	}

	public static float getBrightness (Color color) {
		return Math.max(Math.max(color.r, color.g), color.b);
	}

	public static class HSBColorLogicData extends ColorLogicData {

		private float[] startHSB;

		private float[] endHSB;

		public HSBColorLogicData () {
			startHSB = new float[3];
			endHSB = new float[3];
		}

		@Override
		protected void onSetup () {
			super.onSetup();
			Color c = getAction().getPercentable().getColor();
			startHSB[0] = HSBColorLogic.getHue(c);
			startHSB[1] = HSBColorLogic.getSaturation(c);
			startHSB[2] = HSBColorLogic.getBrightness(c);
		}

		@Override
		public void reset () {
			super.reset();
			Arrays.fill(startHSB, 0);
			Arrays.fill(endHSB, 0);
		}
	}
}
