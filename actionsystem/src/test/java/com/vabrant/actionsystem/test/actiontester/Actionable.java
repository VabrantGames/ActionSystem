package com.vabrant.actionsystem.test.actiontester;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Actionable extends Actor implements Poolable {
	
	enum DrawType {
		SHAPE_DRAWER,
		SHAPE_RENDERER,
		TEXTURE_REGION,
		NONE
	}
	
	public enum ShapeDrawerOptions {
		RECTANGLE,
		CIRCLE,
		TRIANGLE
	}
	
	public class ShapeDrawerRectangleOption implements Poolable {
		
		public void set() {
			
		}
		
		@Override
		public void reset() {
			
		}
		
	}
	
	ShapeDrawer shapeDrawer;
	TextureRegion textureRegion;
	DrawType drawType = DrawType.NONE;
	ShapeDrawerOptions shapeDrawerOptions;
	
	public void setShapeDrawer(ShapeDrawer shapeDrawer) {
		this.shapeDrawer = shapeDrawer;
	}
	
	public void setTextureRegion(TextureRegion region) {
		this.textureRegion = region;
	}

	@Override
	public void reset() {
		shapeDrawer = null;
		textureRegion = null;
	}

}
