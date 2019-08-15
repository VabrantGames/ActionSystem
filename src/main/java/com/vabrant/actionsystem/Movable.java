package com.vabrant.actionsystem;

public interface Movable extends Percentable{
	public void setX(float x);
	public void setY(float y);
	public float getX();
	public float getY();
	public void setPosition(float x, float y);
}
