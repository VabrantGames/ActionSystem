package com.vabrant.actionsystem;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;

public class ColorAction extends PercentAction<Colorable, ColorAction> {
	
	public static ColorAction getAction() {
		return getAction(ColorAction.class);
	}
	
	public static ColorAction changeColor(Colorable colorable, Color endColor, float duration, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeColor(endColor);
		action.set(colorable, duration, interpolation);
		return action;
	}
	
	public static ColorAction changeColor(Colorable colorable, float hue, float saturation, float brightness, float alpha, float duration, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeColor(hue, saturation, brightness, alpha);
		action.set(colorable, duration, interpolation);
		return action;
	}
	
	public static ColorAction changeAlpha(Colorable colorable, float endAlpha, float duration, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeAlpha(endAlpha);
		action.set(colorable, duration, interpolation);
		return action;
	}
	
	public static ColorAction changeHue(Colorable colorable, float endHue, float duration, Interpolation interpolation) {
		ColorAction action = getAction();
		action.changeHue(endHue);
		action.set(colorable, duration, interpolation);
		return action;
	}
	
	public static ColorAction setColor(Colorable colorable, Color color) {
		ColorAction action = getAction();
		action.changeColor(color);
		action.set(colorable, 0, Interpolation.linear);
		return action;
	}
	
	public static ColorAction setColor(Colorable colorable, float hue, float saturation, float brightness, float alpha) {
		ColorAction action = getAction();
		action.changeColor(hue, saturation, brightness, alpha);
		action.set(colorable, 0, Interpolation.linear);
		return action;
	}
	
	private static final int RGBA = 0;
	private static final int HSBA = 1;
	private static final int ALPHA = 2;
	private static final int HUE = 3;
	private int type = -1;
	
	private boolean setupAction = true;
	private float[] startHSBA = new float[4];
	private float[] endHSBA = new float[4];
	private Color startColor = new Color(Color.WHITE);
	private Color endColor = new Color(Color.WHITE);
	
	public void changeColor(Color endColor) {
		type = RGBA;
		this.endColor.set(endColor);
	}
	
	public void changeColor(float hue, float saturation, float brightness, float alpha) {
		type = HSBA;
		endHSBA[0] = hue / 360f;
		endHSBA[1] = saturation;
		endHSBA[2] = brightness;
		endHSBA[3] = alpha;
	}
	
	public void changeHue(float hue) {
		type = HUE;
		endHSBA[0] = hue / 360f;
	}
	
//	public void changeSaturation(float saturation) {
//		
//	}
	
//	public void changeBrightness(float brightness) {
//		
//	}
	
	public void changeAlpha(float endAlpha) {
		type = ALPHA;
		endColor.a = endAlpha;
	}

	@Override
	protected void percent(float percent) {
		switch(type) {
			case RGBA:
				float rgbaR = startColor.r + (endColor.r - startColor.r) * percent;
				float rgbaG = startColor.g + (endColor.g - startColor.g) * percent;
				float rgbaB = startColor.b + (endColor.b - startColor.b) * percent;
				float rgbaA = startColor.a + (endColor.a - startColor.a) * percent;
				percentable.getColor().set(rgbaR, rgbaG, rgbaB, rgbaA);
				break;
			case HSBA:
				float hsbaH = startHSBA[0] + (endHSBA[0] - startHSBA[0]) * percent;
				float hsbaS = startHSBA[1] + (endHSBA[1] - startHSBA[1]) * percent;
				float hsbaB = startHSBA[2] + (endHSBA[2] - startHSBA[2]) * percent;
				float hsbaA = startHSBA[3] + (endHSBA[3] - startHSBA[3]) * percent;
				HSBToRGB(endColor, hsbaH * 360f, hsbaS, hsbaB, hsbaA);
				percentable.getColor().set(endColor);
				break;
			case HUE:
				float hue = startHSBA[0] + (endHSBA[0] - startHSBA[0]) * percent;
				HSBToRGB(endColor, hue * 360, 1f, 1f, 1);
				percentable.getColor().set(endColor);
				break;
			case ALPHA:
				float aaa = startColor.a + (endColor.a - startColor.a) * percent;
				percentable.getColor().a = aaa;
				break;
		}
	}
	
	@Override
	public ColorAction start() {
		super.start();
		
		if(setupAction) {
			switch(type) {
				case RGBA:
					startColor.set(percentable.getColor());
					break;
				case HSBA:
					startColor.set(percentable.getColor());
					startHSBA[0] = getHue(startColor) / 360f;
					startHSBA[1] = getSaturation(startColor);
					startHSBA[2] = getBrightness(startColor);
					startHSBA[3] = startColor.a;
					break;
				case ALPHA:
					startColor.a = percentable.getColor().a;
					break;
				case HUE:
					startColor.set(percentable.getColor());
					startHSBA[0] = getHue(startColor) / 360;
					break;
			}
		}
		return this;
	}
	
	@Override
	public ColorAction end() {
		super.end();
		setupAction = false;
		return this;
	}
	
	@Override
	public ColorAction clear() {
		super.clear();
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
		type = -1;
		startHSBA[0] = 0;
		startHSBA[1] = 0;
		startHSBA[2] = 0;
		startHSBA[3] = 0;
		endHSBA[0] = 0;
		endHSBA[1] = 0;
		endHSBA[2] = 0;
		endHSBA[3] = 0;
		return this;
	}

	@Override
	public void reset() {
		super.reset();
		setupAction = true;
		startColor.set(Color.WHITE);
		endColor.set(Color.WHITE);
		type = -1;
		startHSBA[0] = 0;
		startHSBA[1] = 0;
		startHSBA[2] = 0;
		startHSBA[3] = 0;
		endHSBA[0] = 0;
		endHSBA[1] = 0;
		endHSBA[2] = 0;
		endHSBA[3] = 0;
	}
	
	public static void HSBToRGB(Color color, float hue, float saturation, float brightness, float alpha) {
		if(hue >= 360) Math.abs(hue %= 360);
		saturation = MathUtils.clamp(saturation, 0f, 1f);
		brightness = MathUtils.clamp(brightness, 0f, 1f);
		
		if(saturation == 0) {
			color.r = color.g = color.b = brightness;
			return;
		}
		
		float h = hue / 60f;
		float c = brightness * saturation;
		float x = c * (1f - Math.abs(h % 2f - 1f));
		float m = brightness - c;
		
		color.a = alpha;
		
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
