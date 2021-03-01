package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.actions.Colorable;
import com.vabrant.actionsystem.actions.Movable;
import com.vabrant.actionsystem.actions.Rotatable;
import com.vabrant.actionsystem.actions.Scalable;

public class NormalActionable extends Actionable implements Movable, Scalable, Rotatable, Colorable {
	
//	private float x;
//	private float y;
//	private float scaleX = 1f;
//	private float scaleY = 1f;
//	private float rotation = 0;
//	private Color color = new Color(Color.WHITE);
	
	public NormalActionable() {
		setSize(10, 10);
	}

	@Override
	public void setColor(Color color) {
		getColor().set(color);
	}

	@Override
	public Color getColor() {
		return getColor();
	}

	@Override
	public void setRotation(float rotation) {
		setRotation(rotation);
	}

	@Override
	public float getRotation() {
		return getRotation();
	}

	@Override
	public void setScaleX(float scaleX) {
		setScaleX(scaleX);
	}

	@Override
	public void setScaleY(float scaleY) {
		setScaleY(scaleY);
	}

	@Override
	public float getScaleX() {
		return getScaleX();
	}

	@Override
	public float getScaleY() {
		return getScaleY();
	}

	@Override
	public void setX(float x) {
		setX(x);
	}

	@Override
	public void setY(float y) {
		setY(y);
	}

	@Override
	public float getX() {
		return getX();
	}

	@Override
	public float getY() {
		return getY();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		switch(drawType) {
			case SHAPE_DRAWER:
				float rotRad = getRotation() * MathUtils.degreesToRadians;
				
				break;
			case TEXTURE_REGION:
				batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight(), 0, 0, getScaleX(), getScaleY(), getRotation());
				break;
		}
	}

}
