package com.vabrant.actionsystem.actions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ColorAction extends PercentAction<Colorable, ColorAction> {
	
	public static ColorAction obtain() {
		return obtain(ColorAction.class);
	}
	
	public static ColorAction changeColor(Colorable colorable, Color endColor, float duration, Interpolation interpolation) {
		return obtain()
				.changeColor(endColor)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeColorRGB(Colorable colorable, float[] rgb, float duration, Interpolation interpolation) {
		if(rgb.length < 3 || rgb.length > 3) throw new IllegalArgumentException("RGB array length has to be 3.");
		return changeColorRGB(colorable, rgb[0], rgb[1], rgb[2], duration, interpolation);
	}
	
	public static ColorAction changeColorRGB(Colorable colorable, float red, float blue, float green, float duration, Interpolation interpolation) {
		return obtain()
				.changeColorRGB(red, blue, green)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeColorRGBA(Colorable colorable, float[] rgba, float duration, Interpolation interpolation) {
		if(rgba.length < 4 || rgba.length > 4) throw new IllegalArgumentException("RGBA array length has to be 4");
		return changeColorRGBA(colorable, rgba[0], rgba[1], rgba[2], rgba[3], duration, interpolation);
	}
	
	public static ColorAction changeColorRGBA(Colorable colorable, float red, float blue, float green, float alpha, float duration, Interpolation interpolation) {
		return obtain()
				.changeColorRGBA(red, blue, green, alpha)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeRed(Colorable colorable, float red, float duration, Interpolation interpolation) {
		return obtain()
				.changeRed(red)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeGreen(Colorable colorable, float green, float duration, Interpolation interpolation) {
		return obtain()
			.changeGreen(green)
			.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeBlue(Colorable colorable, float blue, float duration, Interpolation interpolation) {
		return obtain()
				.changeBlue(blue)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeColorHSB(Colorable colorable, float[] hsb, float duration, Interpolation interpolation) {
		if(hsb.length < 3 || hsb.length > 3) throw new IllegalArgumentException("HSB array length has to be 3.");
		return changeColorHSB(colorable, hsb[0], hsb[1], hsb[2], duration, interpolation);
	}
	
	public static ColorAction changeColorHSB(Colorable colorable, float hue, float saturation, float brightness, float duration, Interpolation interpolation) {
		return changeColorHSB(colorable, hue, saturation, brightness, duration, interpolation, false);
	}
	
	public static ColorAction changeColorHSB(Colorable colorable, float[] hsb, float duration, Interpolation interpolation, boolean useHSBValues) {
		if(hsb.length < 3 || hsb.length > 3) throw new IllegalArgumentException("HSB array length has to be 3.");
		return changeColorHSB(colorable, hsb[0], hsb[1], hsb[2], duration, interpolation, useHSBValues);
	}
	
	public static ColorAction changeColorHSB(Colorable colorable, float hue, float saturation, float brightness, float duration, Interpolation interpolation, boolean useHSBValues) {
		return obtain()
				.changeColorHSB(hue, saturation, brightness, useHSBValues)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeColorHSBA(Colorable colorable, float[] hsba, float duration, Interpolation interpolation) {
		if(hsba.length < 4 || hsba.length > 4) throw new IllegalArgumentException("HSBA array length has to be 4.");
		return changeColorHSBA(colorable, hsba[0], hsba[1], hsba[2], hsba[3], duration, interpolation);
	}
	
	public static ColorAction changeColorHSBA(Colorable colorable, float hue, float saturation, float brightness, float alpha, float duration, Interpolation interpolation) {
		return changeColorHSBA(colorable, hue, saturation, brightness, alpha, duration, interpolation, false);
	}
	
	public static ColorAction changeColorHSBA(Colorable colorable, float[] hsba, float duration, Interpolation interpolation, boolean useHSBValues) {
		return changeColorHSBA(colorable, hsba[0], hsba[1], hsba[2], hsba[3], duration, interpolation, useHSBValues);
	}
	
	public static ColorAction changeColorHSBA(Colorable colorable, float hue, float saturation, float brightness, float alpha, float duration, Interpolation interpolation, boolean useHSBValues) {
		return obtain()
				.changeColorHSBA(hue, saturation, brightness, alpha, useHSBValues)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeHue(Colorable colorable, float endHue, float duration, Interpolation interpolation) {
		return changeHue(colorable, endHue, duration, interpolation, false);
	}
	
	public static ColorAction changeHue(Colorable colorable, float endHue, float duration, Interpolation interpolation, boolean useHSBValues) {
		return obtain()
				.changeHue(endHue, useHSBValues)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeSaturation(Colorable colorable, float endSaturation, float duration, Interpolation interpolation) {
		return changeSaturation(colorable, endSaturation, duration, interpolation, false);
	}
	
	public static ColorAction changeSaturation(Colorable colorable, float endSaturation, float duration, Interpolation interpolation, boolean useHSBValues) {
		return obtain()
				.changeSaturation(endSaturation, useHSBValues)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeBrightness(Colorable colorable, float endBrightness, float duration, Interpolation interpolation) {
		return changeBrightness(colorable, endBrightness, duration, interpolation, false);
	}
	
	public static ColorAction changeBrightness(Colorable colorable, float endBrightness, float duration, Interpolation interpolation, boolean useHSBValues) {
		return obtain()
				.changeBrightness(endBrightness, useHSBValues)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction changeAlpha(Colorable colorable, float endAlpha, float duration, Interpolation interpolation) {
		return obtain()
				.changeAlpha(endAlpha)
				.set(colorable, duration, interpolation);
	}
	
	public static ColorAction setColor(Colorable colorable, Color color) {
		return obtain()
				.changeColor(color)
				.set(colorable, 0, Interpolation.linear);
	}
	
	public static ColorAction setColorRGB(Colorable colorable, float red, float green, float blue) {
		return obtain() 
				.changeColorRGB(red, green, blue)
				.set(colorable, 0, Interpolation.linear);
	}
	
	public static ColorAction setColorRGBA(Colorable colorable, float red, float green, float blue, float alpha) {
		return obtain()
				.changeColorRGBA(red, green, blue, alpha)
				.set(colorable, 0, Interpolation.linear);
	}
	
	public static ColorAction setColorHSB(Colorable colorable, float hue, float saturation, float brightness) {
		return obtain()
				.changeColorHSB(hue, saturation, brightness, false)
				.set(colorable, 0, Interpolation.linear);
	}
	
	public static ColorAction setColorHSBA(Colorable colorable, float hue, float saturation, float brightness, float alpha) {
		return obtain()
				.changeColorHSBA(hue, saturation, brightness, alpha, false)
				.set(colorable, 0, Interpolation.linear);
	}

	public static float[] rgbTemp(float r, float g, float b, boolean normalize) {
		if(normalize) {
			return v3Temp(r / 255f, g / 255f, b / 255f);
		}
		else {
			return v3Temp(r, g, b);
		}
	}
	
	public static float[] rgbaTemp(float r, float g, float b, float a, boolean normalize) {
		if(normalize) {
			return v4Temp(r / 255f, g / 255f, b / 255f, a);
		}
		else {
			return v4Temp(r, g, b, a);
		}
	}
	
	public static float[] v3Temp(float v1, float v2, float v3) {
		V3_TEMP[0] = v1;
		V3_TEMP[1] = v2;
		V3_TEMP[2] = v3;
		return V3_TEMP;
	}
	
	public static float[] v4Temp(float v1, float v2, float v3, float v4) {
		V4_TEMP[0] = v1;
		V4_TEMP[1] = v2;
		V4_TEMP[2] = v3;
		V4_TEMP[3] = v4;
		return V4_TEMP;
	}
	
	private static final float[] V3_TEMP = new float[3];
	private static final float[] V4_TEMP = new float[4];
	
	private static final int RGB = 0;
	private static final int HSB = 1;
	private static final int RED = 2;
	private static final int GREEN = 3;
	private static final int BLUE = 4;
	private static final int HUE = 5;
	private static final int SATURATION = 6;
	private static final int BRIGHTNESS = 7;
	
	private int valueType = -1;
	private int constructType = -1;

	private boolean soloChannel;
	private boolean useAlpha;
	private boolean constructEndColor;
	private boolean setupAction = true;
	private float[] startHSB = new float[3];
	private float[] endHSB = new float[3];
	private Color startColor = new Color(Color.WHITE);
	private Color endColor = new Color(Color.WHITE);
	
	public ColorAction changeColor(Color endColor) {
		valueType = RGB;
		this.endColor.set(endColor);
		useAlpha = true;
		return this;
	}
	
	public ColorAction changeColorRGB(float red, float green, float blue) {
		valueType = RGB;
		endColor.r = red;
		endColor.g = green;
		endColor.b = blue;
		return this;
	}
	
	public ColorAction changeColorRGBA(float red, float green, float blue, float alpha) {
		valueType = RGB;
		endColor.set(red, green, blue, alpha);
		useAlpha = true;
		return this;
	}
	
	public ColorAction changeRed(float red) {
		valueType = RED;
		endColor.r = red;
		return this;
	}
	
	public ColorAction changeGreen(float green) {
		valueType = GREEN;
		endColor.g = green;
		return this;
	}
	
	public ColorAction changeBlue(float blue) {
		valueType = BLUE;
		endColor.b = blue;
		return this;
	}
	
	public ColorAction changeColorHSB(float hue, float saturation, float brightness, boolean useHSBValues) {
		valueType = useHSBValues ? HSB : RGB;
		
		if(valueType == RGB) {
			HSBToRGB(endColor, hue, saturation, brightness);
		}
		else {
			endHSB[0] = hue;
			endHSB[1] = saturation;
			endHSB[2] = brightness;
		}
		return this;
	}
	
	public ColorAction changeColorHSBA(float hue, float saturation, float brightness, float alpha, boolean useHSBValues) {
		valueType = useHSBValues ? HSB : RGB;

		useAlpha = true;
		
		if(valueType == RGB) {
			HSBToRGB(endColor, hue, saturation, brightness);
			endColor.a = alpha;
		}
		else {
			endHSB[0] = hue;
			endHSB[1] = saturation;
			endHSB[2] = brightness;
		}
		return this;
	}

	public ColorAction changeHue(float hue, boolean useHSBValues) {
		valueType = useHSBValues ? HUE : RGB;
		endHSB[0] = hue;
		
		if(valueType == RGB) {
			constructEndColor = true;
			constructType = HUE;
		}
		return this;
	}
	
	public ColorAction changeSaturation(float saturation, boolean useHSBValues) {
		valueType = useHSBValues ? SATURATION : RGB;
		endHSB[1] = saturation;
		
		if(valueType == RGB) {
			constructEndColor = true;
			constructType = SATURATION;
		}
		return this;
	}
	
	public ColorAction changeBrightness(float brightness, boolean useHSBValues) {
		valueType = useHSBValues ? BRIGHTNESS : RGB;
		endHSB[2] = brightness;
		
		if(valueType == RGB) {
			constructEndColor = true;
			constructType = BRIGHTNESS;
		}
		return this;
	}
	
	public ColorAction changeAlpha(float endAlpha) {
		useAlpha = true;
		endColor.a = endAlpha;
		return this;
	}
	
	public ColorAction soloChannel() {
		soloChannel = true;
		return this;
	}

	@Override
	protected void percent(float percent) {
		Color color = percentable.getColor();
		
		if(useAlpha) color.a = MathUtils.lerp(startColor.a, endColor.a, percent);
		
		switch(valueType) {
			case RGB:
				color.r = MathUtils.lerp(startColor.r, endColor.r, percent);
				color.g = MathUtils.lerp(startColor.g, endColor.g, percent);
				color.b = MathUtils.lerp(startColor.b, endColor.b, percent);
				break;
			case RED:
				color.r = MathUtils.lerp(startColor.r, endColor.r, percent);
				break;
			case GREEN:
				color.g = MathUtils.lerp(startColor.g, endColor.g, percent);
				break;
			case BLUE:
				color.b = MathUtils.lerp(startColor.b, endColor.b, percent);
				break;
			case HSB:
				float h = MathUtils.lerp(startHSB[0], endHSB[0], percent);
				float s = MathUtils.lerp(startHSB[1], endHSB[1], percent);
				float b = MathUtils.lerp(startHSB[2], endHSB[2], percent);
				color.set(HSBToRGB(endColor, h, s, b));
				break;
			case HUE:
				float hh = MathUtils.lerp(startHSB[0], endHSB[0], percent);
				float hS = !soloChannel ? startHSB[1] : getSaturation(color);
				float hB = !soloChannel ? startHSB[2] : getBrightness(color);
				color.set(HSBToRGB(endColor, hh, hS, hB));
				break;
			case SATURATION:
				float sH = !soloChannel ? startHSB[0] : getHue(color);
				float sS = MathUtils.lerp(startHSB[1], endHSB[1], percent);
				float sB = !soloChannel ? startHSB[2] : getSaturation(color);
				color.set(HSBToRGB(endColor, sH, sS, sB));
				break;
			case BRIGHTNESS:
				float bH = !soloChannel ? startHSB[0] : getHue(color);
				float bS = !soloChannel ? startHSB[1] : getSaturation(color);
				float bB = MathUtils.lerp(startHSB[2], endHSB[2], percent);
				color.set(HSBToRGB(endColor, bH, bS, bB));
				break;
		}
	}
	
	@Override
	public ColorAction setup() {
		if(setupAction) {
			setupAction = false;
			
			startColor.set(percentable.getColor());
			
			switch(valueType) {
				case RGB:
					if(constructEndColor) {
						float h = 0;
						float s = 0;
						float b = 0;
						
						switch(constructType) {
							case HUE:
								h = endHSB[0];
								s = getSaturation(startColor);
								b = getBrightness(startColor);
								break;
							case SATURATION:
								h = getHue(startColor);
								s = endHSB[1];
								b = getBrightness(startColor);
								break;
							case BRIGHTNESS:
								h = getHue(startColor);
								s = getSaturation(startColor);
								b = endHSB[2];
								break;
						}
						HSBToRGB(endColor, h, s, b);
					}
					break;
				case HSB:
				case HUE:
				case SATURATION:
				case BRIGHTNESS:
					startHSB[0] = getHue(startColor);
					startHSB[1] = getSaturation(startColor);
					startHSB[2] = getBrightness(startColor);
					break;
			}
		}
		return this;
	}
	
	@Override
	protected void startLogic() {
		super.startLogic();
		
		//By default the color of the action should go back to exactly where it started whether it was changing one channel or
		//multiple channels. When single channels are used they only update their channel and skip the others. This will make
		//sure the other channels will be set to where they were started. They will not be changed when updating just when start 
		//is called. If the channel is being soloed (Allowing you to individually animate each channel) then the other channels won't be set. 
		switch(valueType) {
			case RED:
			case GREEN:
			case BLUE:
			case HUE:
			case SATURATION:
			case BRIGHTNESS:
				if(!soloChannel) {
					percentable.setColor(startColor);
				}
				break;
		}
	}
	
	@Override
	public boolean hasConflict(Action<ColorAction> action) {
		if(action instanceof ColorAction) {
			ColorAction conflictAction = (ColorAction)action;
//			if(conflictAction.type > -1) return true;
		}
		return false;
	}

	@Override
	public void reset() {
		super.reset();
		soloChannel = false;
		useAlpha = false;
		constructEndColor = false;
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
		valueType = -1;
		constructType = -1;
		startHSB[0] = 0;
		startHSB[1] = 0;
		startHSB[2] = 0;
		endHSB[0] = 0;
		endHSB[1] = 0;
		endHSB[2] = 0;
	}
	
	public static Color HSBToRGB(Color color, float hue, float saturation, float brightness) {
		if(hue >= 360) Math.abs(hue %= 360);
		saturation = MathUtils.clamp(saturation, 0f, 1f);
		brightness = MathUtils.clamp(brightness, 0f, 1f);
		
		if(saturation == 0) {
			color.r = color.g = color.b = brightness;
			return color;
		}
		
		float h = hue / 60f;
		float c = brightness * saturation;
		float x = c * (1f - Math.abs(h % 2f - 1f));
		float m = brightness - c;
		
		switch((int)h) {
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
	
	public static float normalize(float value) {
		return value / 255f;
	}
	
	public static float getHue(Color color) {
		float min = Math.min(Math.min(color.r, color.g), color.b);
		float max = Math.max(Math.max(color.r, color.g), color.b);
		float difference = max - min;

		if(difference == 0) {
			return 0f;
		}
		else if(max == color.r) {
			float h = 60f * (((color.g - color.b) / difference) % 6);
			return h < 0 ? h + 360f : h;
		}
		else if(max == color.g) {
			return 60f * (((color.b - color.r) / difference) + 2f);
		}
		else  {
			return 60f * (((color.r - color.g) / difference) + 4f);
		}
	}
	
	public static float getSaturation(Color color) {
		float min = Math.min(Math.min(color.r, color.g), color.b);
		float max = Math.max(Math.max(color.r, color.g), color.b);
		float difference = max - min;
		return max == 0 ? 0 : difference / max;
	}

	public static float getBrightness(Color color) {
		return Math.max(Math.max(color.r, color.g), color.b);
	}

}
