package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.vabrant.actionsystem.Colorable;
import com.vabrant.actionsystem.Movable;
import com.vabrant.actionsystem.Rotatable;
import com.vabrant.actionsystem.Scalable;
import com.vabrant.actionsystem.Shakable;
import com.vabrant.actionsystem.Zoomable;

public class ActionSystemTestObject implements Movable, Colorable, Zoomable, Shakable, Rotatable, Scalable{
	
	private float scaleX = 1;
	private float scaleY = 1;
	private float rotation;
	private float zoom = 1;
	private float x;
	private float y;
	private float shakeX;
	private float shakeY;
	private float shakeAngle;
	public float width = 50;
	public float height = 50;
	private Color color = new Color(0,0,0,1);
	
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
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setColor(Color color) {
		this.color.set(color);
	}

	@Override
	public Color getColor() {
		return color;
	}
	
	@Override
	public void setZoom(float zoom) {
		this.zoom = zoom;
	}
	
	@Override
	public float getZoom() {
		return zoom;
	}

	@Override
	public void setShakeX(float x) {
		this.shakeX = x;
	}

	@Override
	public void setShakeY(float y) {
		this.shakeY = y;
	}

	@Override
	public void setShakeAngle(float angle) {
		this.shakeAngle = angle;
	}

	@Override
	public float getShakeX() {
		return shakeX;
	}

	@Override
	public float getShakeY() {
		return shakeY;
	}

	@Override
	public float getShakeAngle() {
		return shakeAngle;
	}

	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	@Override
	public float getRotation() {
		return rotation;
	}

	@Override
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	@Override
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}
	
	@Override
	public void setScale(float scaleX, float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
	}
	
	public void draw(ShapeRenderer renderer) {
		if(!renderer.getCurrentType().equals(ShapeType.Filled)) renderer.set(ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(x, y, width / 2, height / 2, width, height, scaleX, scaleY, rotation);
	}

}
