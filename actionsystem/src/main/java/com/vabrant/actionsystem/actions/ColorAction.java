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
			return temp(r / 255f, g / 255f, b / 255f);
		}
		else {
			return temp(r, g, b);
		}
	}
	
	public static float[] rgbaTemp(float r, float g, float b, float a, boolean normalize) {
		if(normalize) {
			return temp(r / 255f, g / 255f, b / 255f, a);
		}
		else {
			return temp(r, g, b, a);
		}
	}
	
	public static float[] temp(float v1, float v2, float v3) {
		V3_TEMP[0] = v1;
		V3_TEMP[1] = v2;
		V3_TEMP[2] = v3;
		return V3_TEMP;
	}
	
	public static float[] temp(float v1, float v2, float v3, float v4) {
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
	private static final int ON = 2;
	
	private int colorModel = -1;
	
	//Red or Hue
	private int channel1 = -1;
	
	//Green or Saturation
	private int channel2 = -1;
	
	//Blue or Brightness
	private int channel3 = -1;
	
	private int alphaChannel = -1;

	private boolean soloChannel;
	private boolean constructEndColor;
	private boolean setupAction = true;
	private float[] startHSB = new float[3];
	private float[] endHSB = new float[3];
	private Color startColor = new Color(Color.WHITE);
	private Color endColor = new Color(Color.WHITE);
	
	public ColorAction changeColor(Color endColor) {
		colorModel = RGB;
		channel1 = channel2 = channel3 = alphaChannel = ON;
		this.endColor.set(endColor);
		return this;
	}
	
	public ColorAction changeColorRGB(float red, float green, float blue) {
		colorModel = RGB;
		channel1 = channel2 = channel3 = ON;
		endColor.r = red;
		endColor.g = green;
		endColor.b = blue;
		return this;
	}
	
	public ColorAction changeColorRGBA(float red, float green, float blue, float alpha) {
		colorModel = RGB;
		channel1 = channel2 = channel3 = alphaChannel = ON;
		endColor.set(red, green, blue, alpha);
		return this;
	}
	
	public ColorAction changeRed(float red) {
		if(colorModel == HSB) return this;
		colorModel = RGB;
		channel1 = ON;
		endColor.r = red;
		return this;
	}
	
	public ColorAction changeGreen(float green) {
		if(colorModel == HSB) return this; 
		colorModel = RGB;
		channel2 = ON;
		endColor.g = green;
		return this;
	}
	
	public ColorAction changeBlue(float blue) {
		if(colorModel == HSB) return this;
		colorModel = RGB;
		channel3 = ON;
		endColor.b = blue;
		return this;
	}
	
	public ColorAction changeColorHSB(float hue, float saturation, float brightness, boolean useHSBValues) {
		colorModel = useHSBValues ? HSB : RGB;
		
		if(colorModel == RGB) {
			//Convert from HSB to RGB
			HSBToRGB(endColor, hue, saturation, brightness);
			changeColorRGB(endColor.r, endColor.g, endColor.b);
		}
		else {
			channel1 = channel2 = channel3 = ON;
			endHSB[0] = hue;
			endHSB[1] = saturation;
			endHSB[2] = brightness;
		}
		return this;
	}
	
	public ColorAction changeColorHSBA(float hue, float saturation, float brightness, float alpha, boolean useHSBValues) {
		changeColorHSB(hue, saturation, brightness, useHSBValues);
		changeAlpha(alpha);
		return this;
	}

	public ColorAction changeHue(float hue, boolean useHSBValues) {
		//If the color model has already been set we can not change it from setting individual channels.

		if(useHSBValues) {
			if(colorModel == RGB) return this;
			colorModel = HSB;
			channel1 = ON;
		}
		else {
			if(colorModel == HSB) return this;
			colorModel = RGB;
			channel1 = ON;
			constructEndColor = true;
		}
		
		endHSB[0] = hue;
		return this;
	}
	
	public ColorAction changeSaturation(float saturation, boolean useHSBValues) {
		if(useHSBValues) {
			if(colorModel == RGB) return this;
			colorModel = HSB;
			channel2 = ON;
		}
		else {
			if(colorModel == HSB) return this;
			colorModel = RGB;
			channel2 = ON;
			constructEndColor = true;
		}
		
		endHSB[1] = saturation;
		return this;
	}
	
	public ColorAction changeBrightness(float brightness, boolean useHSBValues) {
		if(useHSBValues) {
			if(colorModel == RGB) return this;
			colorModel = HSB;
			channel3 = ON;
		}
		else {
			if(colorModel == HSB) return this;
			colorModel = RGB;
			channel3 = ON;
			constructEndColor = true;
		}
		
		endHSB[2] = brightness;
		return this;
	}
	
	public ColorAction changeAlpha(float endAlpha) {
		alphaChannel = ON;
		endColor.a = endAlpha;
		return this;
	}
	
	public ColorAction solo(boolean solo) {
		soloChannel = solo;
		return this;
	}

	@Override
	protected void percent(float percent) {
		Color color = percentable.getColor();
		
		if(alphaChannel != -1) color.a = MathUtils.lerp(startColor.a, endColor.a, percent);
		
		switch(colorModel) {
			case RGB:
				if(channel1 != -1) color.r = MathUtils.lerp(startColor.r, endColor.r, percent);
				if(channel2 != -1) color.g = MathUtils.lerp(startColor.g, endColor.g, percent);
				if(channel3 != -1) color.b = MathUtils.lerp(startColor.b, endColor.b, percent);
				break;
			case HSB:
				float h = channel1 == ON ? MathUtils.lerp(startHSB[0], endHSB[0], percent) : getHue(color);
				float s = channel2 == ON ? MathUtils.lerp(startHSB[1], endHSB[1], percent) : getSaturation(color);
				float b = channel3 == ON ? MathUtils.lerp(startHSB[2], endHSB[2], percent) : getBrightness(color);
				color.set(HSBToRGB(endColor, h, s, b));
				break;
		}
	}
	
	@Override
	public ColorAction setup() {
		if(setupAction) {
			setupAction = false;
			
			startColor.set(percentable.getColor());
			
			switch(colorModel) {
				case RGB:
					if(constructEndColor) {
						float h = channel1 == ON ? endHSB[0] : getHue(startColor);
						float s = channel2 == ON ? endHSB[1] : getSaturation(startColor);
						float b = channel3 == ON ? endHSB[2] : getBrightness(startColor);
						
						HSBToRGB(endColor, h, s, b);
						channel1 = channel2 = channel3 = ON;
					}
					break;
				case HSB:
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
		
		//By default this action is solo by nature. It will only change what you tell it to change. However, 
		//this being the default behavior is very misleading in my opinion. So the default behavior is to 
		//put the action back to its starting position, but it will only change what you told it to change.
		//So if this action started at red and transitioned to green and you repeated it with a RepeatAction,
		//then every time this action is repeated it will start at red every time. So lets use the same example 
		//from above but this time we are doing something with the blue channel. Since we are changing the blue
		//channel elsewhere with another action or some other logic we don't wan't this action to touch anything 
		//about the blue channel. Doing so can ruin what we were trying to do. So what we would do is solo this action.
		//Soloing would force this action to only change the red and green channels and leave the blue channel untouched. Doesn't matter if i restart, move to end,
		//or anything else. This action will never touch the blue channel.
		
		if(!soloChannel) {
			Color c = percentable.getColor();
			
			c.a = alphaChannel == -1 ? startColor.a : (!reverse ? startColor.a : endColor.a);
			
			switch(colorModel) {
				case RGB:
					c.r = channel1 == -1 ? startColor.r : (!reverse ? startColor.r : endColor.r);
					c.g = channel2 == -1 ? startColor.g : (!reverse ? startColor.g : endColor.g);
					c.b = channel3 == -1 ? startColor.b : (!reverse ? startColor.b : endColor.b);
					break;
				case HSB:
					float h = channel1 == -1 ? startHSB[0] : (!reverse ? startHSB[0] : endHSB[0]);
					float s = channel2 == -1 ? startHSB[1] : (!reverse ? startHSB[1] : endHSB[1]);
					float b = channel3 == -1 ? startHSB[2] : (!reverse ? startHSB[2] : endHSB[2]);
					c.set(HSBToRGB(endColor, h, s, b));
					break;
			}
		}
	}
	
//	@Override
//	public boolean hasConflict(Action<?> action) {
//		if(!isRunning) return false;
//		
//		//This action is using all channels so there is always a conflict
//		if(channel1 == ON && channel2 == ON && channel3 == ON && alphaChannel == ON) return true;
//		
//		ColorAction a = (ColorAction)action;
//		
//		if(channel1 == ON && a.channel1 == ON) return true;
//		if(channel2 == ON && a.channel2 == ON) return true;
//		if(channel3 == ON && a.channel3 == ON) return true;
//		if(alphaChannel == ON && a.alphaChannel == ON) return true;
//		return false;
//	}

	@Override
	public void reset() {
		super.reset();
		soloChannel = false;
		channel1 = -1;
		channel2 = -1;
		channel3 = -1;
		alphaChannel = -1;
		constructEndColor = false;
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
		colorModel = -1;
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
