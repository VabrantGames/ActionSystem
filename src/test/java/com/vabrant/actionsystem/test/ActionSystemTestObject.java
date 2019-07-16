package com.vabrant.actionsystem.test;

import com.vabrant.actionsystem.Movable;

public class ActionSystemTestObject implements Movable{
	
	private float x;
	private float y;
	public float width = 50;
	public float height = 50;
	
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
	

}
