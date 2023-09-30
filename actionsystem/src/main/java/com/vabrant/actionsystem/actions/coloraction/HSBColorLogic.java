
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
		return changeColor(colorable, -1, -1, -1, -1, hue, saturation, brightness, -1, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float hue, float saturation, float brightness, float alpha,
		float duration, Interpolation interpolation) {
		return changeColor(colorable, -1, -1, -1, -1, hue, saturation, brightness, alpha, duration, interpolation);
	}

	public static ColorAction changeColor (Colorable colorable, float startHue, float startSaturation, float startBrightness,
		float startAlpha, float endHue, float endSaturation, float endBrightness, float endAlpha, float duration,
		Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeColor(data, startHue, startSaturation, startBrightness, startAlpha, endHue, endSaturation, endBrightness,
			endAlpha);
		return a;
	}

	public static ColorAction changeHue (Colorable colorable, float hue, float duration, Interpolation interpolation) {
		return changeHue(colorable, -1, hue, duration, interpolation);
	}

	public static ColorAction changeHue (Colorable colorable, float startHue, float endHue, float duration,
		Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeHue(data, startHue, endHue);
		return a;
	}

	public static ColorAction changeSaturation (Colorable colorable, float saturation, float duration,
		Interpolation interpolation) {
		return changeSaturation(colorable, -1, saturation, duration, interpolation);
	}

	public static ColorAction changeSaturation (Colorable colorable, float startSaturation, float endSaturation, float duration,
		Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeSaturation(data, startSaturation, endSaturation);
		return a;
	}

	public static ColorAction changeBrightness (Colorable colorable, float brightness, float duration,
		Interpolation interpolation) {
		return changeBrightness(colorable, -1, brightness, duration, interpolation);
	}

	public static ColorAction changeBrightness (Colorable colorable, float startBrightness, float endBrightness, float duration,
		Interpolation interpolation) {
		HSBColorLogicData data = getData();
		ColorAction a = ColorAction.obtain().set(colorable, duration, interpolation).setLogicAndData(INSTANCE, data);
		INSTANCE.changeBrightness(data, startBrightness, endBrightness);
		return a;
	}

	private HSBColorLogic () {
	}

	public HSBColorLogic changeColor (HSBColorLogicData data, float startHue, float startSaturation, float startBrightness,
		float startAlpha, float endHue, float endSaturation, float endBrightness, float endAlpha) {
		if (endAlpha != -1) {
			data.getAction().changeAlpha(startAlpha, endAlpha);
		}

		if (endHue != -1) {
			changeHue(data, startHue, endHue);
		}

		if (endSaturation != -1) {
			changeSaturation(data, startSaturation, endSaturation);
		}

		if (endBrightness != -1) {
			changeBrightness(data, startBrightness, endBrightness);
		}
		return this;
	}

	public HSBColorLogic changeHue (HSBColorLogicData data, float startHue, float endHue) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 1, 1);
		data.endHSB[0] = endHue;

		if (startHue != -1) {
			data.startHSB[0] = startHue;
			data.isHueStartSet = true;
		}
		return this;
	}

	public HSBColorLogic changeSaturation (HSBColorLogicData data, float startSaturation, float endSaturation) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 2, 1);
		data.endHSB[1] = endSaturation;

		if (startSaturation != -1) {
			data.startHSB[1] = startSaturation;
			data.isSaturationStartSet = true;
		}
		return this;
	}

	public HSBColorLogic changeBrightness (HSBColorLogicData data, float startBrightness, float endBrightness) {
		ColorAction a = data.getAction();
		a.options = ColorAction.setBit(a.options, 3, 1);
		data.endHSB[2] = endBrightness;

		if (startBrightness != -1) {
			data.startHSB[2] = startBrightness;
			data.isBrightnessStartSet = true;
		}
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
		c.r = endColor.r;
		c.g = endColor.g;
		c.b = endColor.b;
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

		protected boolean isHueStartSet;
		protected boolean isSaturationStartSet;
		protected boolean isBrightnessStartSet;

		private float[] startHSB;

		private float[] endHSB;

		public HSBColorLogicData () {
			startHSB = new float[3];
			endHSB = new float[3];
		}

		@Override
		protected void onSetup () {
			ColorAction a = getAction();
			Color c = a.getPercentable().getColor();

			if (ColorAction.isBitOn(a.options, 1) && !isHueStartSet) {
				isHueStartSet = true;
				startHSB[0] = HSBColorLogic.getHue(c);
			}

			if (ColorAction.isBitOn(a.options, 2) && !isSaturationStartSet) {
				isSaturationStartSet = true;
				startHSB[1] = HSBColorLogic.getSaturation(c);
			}

			if (ColorAction.isBitOn(a.options, 3) && !isBrightnessStartSet) {
				isBrightnessStartSet = true;
				startHSB[2] = HSBColorLogic.getBrightness(c);
			}
		}

		@Override
		public void reset () {
			super.reset();
			Arrays.fill(startHSB, 0);
			Arrays.fill(endHSB, 0);
			isHueStartSet = false;
			isSaturationStartSet = false;
			isBrightnessStartSet = false;
		}
	}
}
