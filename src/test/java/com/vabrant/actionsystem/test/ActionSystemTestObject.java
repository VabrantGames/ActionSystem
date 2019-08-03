package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.vabrant.actionsystem.Colorable;
import com.vabrant.actionsystem.Movable;

public class ActionSystemTestObject implements Movable, Colorable{
	
	private float x;
	private float y;
	public float width = 50;
	public float height = 50;
	private Color color = new Color(1,1,1,1);
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void setX(float x) {
		this.x = x;
	}
	
	@Override
	public void setY(float y) {
		this.y = y;
	}
	
	@Override
	public float getX() {
		return x;
	
	}
	@Override
	public float getY() {
		return y;
	}

	@Override
	public void setColor(Color color) {
		this.color.set(color);
	}

	@Override
	public Color getColor() {
		return color;
	}
	

}
