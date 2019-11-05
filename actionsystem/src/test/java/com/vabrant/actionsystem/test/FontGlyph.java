package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.vabrant.actionsystem.actions.Colorable;
import com.vabrant.actionsystem.actions.Scalable;

public class FontGlyph implements Scalable, Colorable{
	
	public float xOffset;
	public float yOffset;
	public float width;
	public float height;
	public float originX;
	public float originY;
	public float topOriginX;
	public float topOriginY;
	private float scaleX = 1;
	private float scaleY = 1;
	public char id;
	public TextureRegion baseRegion;
	public TextureRegion topRegion;
	private Color color = new Color(Color.WHITE);
	
	public FontGlyph(float width, char id) {
		this(null, null, id);
		this.width = width;
	}
	
	public FontGlyph(TextureRegion region, char id) {
		this(region, null, id);
	}
	
	public FontGlyph(TextureRegion baseRegion, TextureRegion topRegion, char id) {
		this.id = id;
		if(baseRegion != null) {
			this.baseRegion = baseRegion;
			width = baseRegion.getRegionWidth();
			height = baseRegion.getRegionHeight();
			originX = width / 2;
			originY = height / 2;
		}
		if(topRegion != null) this.topRegion = topRegion;
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
	public void setScaleX(float x) {
		scaleX = x;
	}

	@Override
	public void setScaleY(float y) {
		scaleY = y;
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
		
	}
	
//	public void setOrigin(Origin origin) {
//		switch(origin) {
//			case LEFT:
//				originX = 0;
//				originY = 0;
//				topOriginX = 0;
//				topOriginY = 0;
//				break;
//			case CENTER:
//				originX = width / 2;
//				originY = height / 2;
//				if(topRegion != null) {
//					topOriginX = topRegion.getRegionWidth() / 2;
//					topOriginY = topRegion.getRegionHeight() / 2;
//				}
//				break;
//		}
//	}

}
