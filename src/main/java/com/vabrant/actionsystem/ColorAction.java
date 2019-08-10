package com.vabrant.actionsystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ColorAction extends TimeAction {
	
	public static ColorAction getAction() {
		return getAction(ColorAction.class);
	}
	
	public static ColorAction changeColor(Colorable colorable, Color endColor, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeColor(colorable, endColor);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ColorAction changeColor(Colorable colorable, float hue, float saturation, float brightness, float alpha, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeColor(colorable, hue, saturation, brightness, alpha);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ColorAction changeAlpha(Colorable colorable, float endAlpha, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeAlpha(colorable, endAlpha);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ColorAction changeHue(Colorable colorable, float endHue, float duration, boolean reverseBackToStart, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeHue(colorable, endHue);
		action.set(duration, reverseBackToStart, interpolation);
		return action;
	}
	
	public static ColorAction setColor(Colorable colorable, Color color) {
		ColorAction action = getAction();
		action.changeColor(colorable, color);
		action.set(0, false, Interpolation.linear);
		return action;
	}
	
	public static ColorAction setColor(Colorable colorable, float hue, float saturation, float brightness, float alpha) {
		ColorAction action = getAction();
		action.changeColor(colorable, hue, saturation, brightness, alpha);
		action.set(0, false, Interpolation.linear);
		return action;
	}

	private enum ColorType{
		RGBA,
		HSBA,
		ALPHA,
		HUE,
		NONE
	}
	
	private boolean setupAction = true;
	private float startHue;
	private float endHue;
	private float[] startHSBA = new float[4];
	private float[] endHSBA = new float[4];
	private Color startColor = new Color(Color.WHITE);
	private Color endColor = new Color(Color.WHITE);
	public Colorable colorable;
	private ColorType colorType = ColorType.NONE;
	
	public void changeColor(Colorable colorable, Color endColor) {
		this.colorable = colorable;
		this.endColor.set(endColor);
		colorType = ColorType.RGBA;
	}
	
	public void changeColor(Colorable colorable, float hue, float saturation, float brightness, float alpha) {
		this.colorable = colorable;
		colorType = ColorType.HSBA;
		endHSBA[0] = hue / 360f;
		endHSBA[1] = saturation;
		endHSBA[2] = brightness;
		endHSBA[3] = alpha;
	}
	
	public void changeHue(Colorable colorable, float hue) {
		this.colorable = colorable;
		this.endHue = hue / 360f;
		colorType = ColorType.HUE;
	}
	
//	public void changeSaturation(Colorable colorable, float saturation) {
//		
//	}
//	
//	public void changeBrightness(Colorable colorable, float brightness) {
//		
//	}
	
	public void changeAlpha(Colorable colorable, float endAlpha) {
		this.colorable = colorable;
		endColor.a = endAlpha;
		colorType = ColorType.ALPHA;
	}

	@Override
	protected void percent(float percent) {
		switch(colorType) {
			case RGBA:
				float rgbaR = startColor.r + (endColor.r - startColor.r) * percent;
				float rgbaG = startColor.g + (endColor.g - startColor.g) * percent;
				float rgbaB = startColor.b + (endColor.b - startColor.b) * percent;
				float rgbaA = startColor.a + (endColor.a - startColor.a) * percent;
				colorable.getColor().set(rgbaR, rgbaG, rgbaB, rgbaA);
				break;
			case HSBA:
				float hsbaH = startHSBA[0] + (endHSBA[0] - startHSBA[0]) * percent;
				float hsbaS = startHSBA[1] + (endHSBA[1] - startHSBA[1]) * percent;
				float hsbaB = startHSBA[2] + (endHSBA[2] - startHSBA[2]) * percent;
				float hsbaA = startHSBA[3] + (endHSBA[3] - startHSBA[3]) * percent;
				HSBToRGB(endColor, hsbaH * 360f, hsbaS, hsbaB, hsbaA);
				colorable.getColor().set(endColor);
				break;
			case HUE:
				float hue = startHue + (endHue - startHue) * percent;
				HSBToRGB(endColor, hue * 360, 1f, 1f, 1);
				colorable.getColor().set(endColor);
				break;
			case ALPHA:
				float aaa = startColor.a + (endColor.a - startColor.a) * percent;
				colorable.getColor().a = aaa;
				break;
		}
	}
	
	@Override
	public void start() {
		super.start();
		
		if(setupAction) {
			switch(colorType) {
				case RGBA:
					startColor.set(colorable.getColor());
					break;
				case HSBA:
					startColor.set(colorable.getColor());
					startHSBA[0] = getHue(startColor) / 360f;
					startHSBA[1] = getSaturation(startColor);
					startHSBA[2] = getBrightness(startColor);
					startHSBA[3] = startColor.a;
					break;
				case ALPHA:
					startColor.a = colorable.getColor().a;
					break;
				case HUE:
					startColor.set(colorable.getColor());
					startHue = getHue(startColor) / 360f;
					break;
			}
		}
	}
	
	@Override
	public void end() {
		super.end();
		setupAction = false;
	}

	@Override
	public void reset() {
		super.reset();
		endHue = 0;
		colorable = null;
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
		colorType = ColorType.NONE;
	}
	
	public static void HSBToRGB(Color color, float hue, float saturation, float brightness, float alpha) {
		if(hue >= 360) hue %= 360;
		saturation = MathUtils.clamp(saturation, 0f, 1f);
		brightness = MathUtils.clamp(brightness, 0f, 1f);
		
		float c = brightness * saturation;
		float x = c * (1f - Math.abs((hue / 60f) % 2f - 1f));
		float m = brightness - c;
		
		color.a = alpha;
		
		switch((int)(hue / 60f)) {
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
