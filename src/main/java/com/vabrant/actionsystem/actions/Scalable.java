package com.vabrant.actionsystem.actions;

public interface Scalable extends Percentable{
	public void setScaleX(float scaleX);
	public void setScaleY(float scaleY);
	public float getScaleX();
	public float getScaleY();
	public void setScale(float scaleX, float scaleY);
}
