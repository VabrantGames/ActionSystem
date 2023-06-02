
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vabrant.actionsystem.actions.*;

public class Actionable implements Movable, Scalable, Colorable, Rotatable, Shakable, Zoomable {

	private float moveX;
	private float moveY;
	private float scaleX = 1;
	private float scaleY = 1;
	private float rotation;
	private float shakeX;
	private float shakeY;
	private float shakeAngle;
	private float zoom = 1;
	private Color color;
	private TextureRegion region;

	// public Actionable(Texture tex) {
	// this(new TextureRegion(tex)) ;
	// }

	public Actionable (Texture tex) {
		// this.region = region;
		region = new TextureRegion(tex);
		color = new Color(0xFFFFFFFF);
	}

	public Texture getTexture () {
		return region.getTexture();
	}

	public void draw (Batch batch) {
		batch.setColor(color);
		batch.draw(region, moveX + shakeX, moveY + shakeY, 50, 50, 100, 100, scaleX * zoom, scaleY * zoom, rotation + shakeAngle,
			false);
		batch.setColor(Color.WHITE);
	}

	@Override
	public void setColor (Color c) {
		color.set(c);
	}

	@Override
	public Color getColor () {
		return color;
	}

	public void setPosition (float x, float y) {
		setX(x);
		setY(y);
	}

	@Override
	public void setX (float x) {
		this.moveX = x;
	}

	@Override
	public void setY (float y) {
		this.moveY = y;
	}

	@Override
	public float getX () {
		return moveX;
	}

	@Override
	public float getY () {
		return moveY;
	}

	@Override
	public void setRotation (float rotation) {
		this.rotation = rotation;
	}

	@Override
	public float getRotation () {
		return rotation;
	}

	public void setScale (float scaleX, float scaleY) {
		setScaleX(scaleX);
		setScaleY(scaleY);
	}

	@Override
	public void setScaleX (float scaleX) {
		this.scaleX = scaleX;
	}

	@Override
	public void setScaleY (float scaleY) {
		this.scaleY = scaleY;
	}

	@Override
	public float getScaleX () {
		return scaleX;
	}

	@Override
	public float getScaleY () {
		return scaleY;
	}

	@Override
	public void setShakeX (float x) {
		shakeX = x;
	}

	@Override
	public void setShakeY (float y) {
		shakeY = y;
	}

	@Override
	public void setShakeAngle (float angle) {
		shakeAngle = angle;
	}

	@Override
	public float getShakeX () {
		return shakeX;
	}

	@Override
	public float getShakeY () {
		return shakeY;
	}

	@Override
	public float getShakeAngle () {
		return shakeAngle;
	}

	@Override
	public void setZoom (float zoom) {
		this.zoom = zoom;
	}

	@Override
	public float getZoom () {
		return zoom;
	}
}
