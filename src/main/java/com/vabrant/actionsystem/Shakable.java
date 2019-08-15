package com.vabrant.actionsystem;

public interface Shakable extends Percentable{
	public void setShakeX(float x);
	public void setShakeY(float y);
	public void setShakeAngle(float angle);
	public float getShakeX();
	public float getShakeY();
	public float getShakeAngle();
}
