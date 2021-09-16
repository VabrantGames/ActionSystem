package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.Colorable;
import com.vabrant.actionsystem.actions.Movable;
import com.vabrant.actionsystem.actions.Rotatable;
import com.vabrant.actionsystem.actions.Scalable;
import com.vabrant.actionsystem.actions.Shakable;
import com.vabrant.actionsystem.actions.Zoomable;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class TestObject extends ActionAdapter implements Movable, Colorable, Zoomable, Shakable, Rotatable, Scalable{
	
	public boolean isRunning;
	private boolean useDeg;
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
	public float originX;
	private float originY;
	private Color color = new Color(0,0,0,1);
	
	public void useDeg(boolean useDeg) {
		this.useDeg = useDeg;
	}

	public void setSize(float size) {
		width = height = size;
	}
	
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
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
	
	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
	}
	
	public void setOriginX(float originX) {
		this.originX = originX;
	}
	
	public void setOriginY(float originY) {
		this.originY = originY;
	}
	
	public void origin(float originX, float originY) {
		setOriginX(originX);
		setOriginY(originY);
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
	
	private float getActualRotation() {
		return useDeg ? rotation : rotation * MathUtils.degreesToRadians;
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
	
	public void setScale(float scaleX, float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
	}
	
	@Override
	public void actionEnd(Action a) {
		isRunning = false;
	}
	
	public void draw(ShapeRenderer renderer) {
		renderer.set(ShapeType.Filled);
		renderer.setColor(color);
		renderer.rect(x + shakeX, y + shakeY, width / 2, height / 2, width, height, scaleX * zoom, scaleY * zoom, shakeAngle + getActualRotation());
	}
	
	public void draw(ShapeDrawer shapeDrawer) {
		shapeDrawer.setColor(color);
		shapeDrawer.filledRectangle(x + shakeX, y + shakeY, width * scaleX * zoom, height * scaleY * zoom, shakeAngle + getActualRotation());
	}

}
