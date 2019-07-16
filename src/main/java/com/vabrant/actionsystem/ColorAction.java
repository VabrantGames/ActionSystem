package com.vabrant.actionsystem;

import com.badlogic.gdx.graphics.Color;

public class ColorAction extends TimeAction {

	private float valueFactor;
	private float startHue;
	private float startSaturation;
	private float startValue;
	private boolean changeAlpha;
	private Color startColor = new Color();
	private Color endColor = new Color();
	public Colorable colorable;
	
	public void changeColor(Colorable colorable, Color endColor) {
		changeColor(colorable, colorable.getColor(), endColor);
	}
	
	public void changeColor(Colorable colorable, Color startColor, Color endColor) {
		changeAlpha = false;
		this.colorable = colorable;
		this.startColor.set(startColor);
		this.endColor.set(endColor);
	}
	
	public void changeColor(Colorable colorable, float h, float s, float v) {
		
	}
	
	public void darker(Colorable colorable, float factor) {
		this.colorable = colorable;
		this.valueFactor = factor;
		this.startColor.set(colorable.getColor());
		this.endColor.set(colorable.getColor());
		endColor.r = Math.max(endColor.r * factor, 0);
		endColor.g = Math.max(endColor.g * factor, 0);
		endColor.b = Math.max(endColor.b * factor, 0);
	}
	
	public void changeAlpha(Colorable colorable, float endAlpha) {
		changeAlpha(colorable, colorable.getColor().a, endAlpha);
	}
	
	public void changeAlpha(Colorable colorable, float startAlpha, float endAlpha) {
		changeAlpha = true;
		this.colorable = colorable;
		startColor.a = startAlpha;
		endColor.a = endAlpha;
	}
	
	@Override
	protected void percent(float percent) {
		if(!changeAlpha) {
			float r = startColor.r + (endColor.r - startColor.r) * percent;
			float g = startColor.g + (endColor.g - startColor.g) * percent;
			float b = startColor.b + (endColor.b - startColor.b) * percent;
			float a = startColor.a + (endColor.a - startColor.a) * percent;
			colorable.getColor().set(r, g, b, a);
		}
		else {
			float a = startColor.a + (endColor.a - startColor.a) * percent;
			colorable.getColor().a = a;
		}
	}
	
	@Override
	public void end() {
		super.end();
		if(!changeAlpha) {
			if(reverseBackToStart) {
				colorable.setColor(startColor);
				colorable.getColor().set(startColor.r, startColor.g, startColor.b, startColor.a);
			}
			else {
				colorable.setColor(endColor);
			}
		}
		else {
			if(reverseBackToStart) {
				colorable.getColor().a = startColor.a;
			}
			else {
				colorable.getColor().a = endColor.a;
			}
		}
	}
	
	@Override
	public void restart() {
		super.restart();
		if(reverseBackToStart) {
			colorable.setColor(startColor);
		}
		else {
			colorable.setColor(endColor);
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		colorable = null;
		changeAlpha = false;
	}

}
