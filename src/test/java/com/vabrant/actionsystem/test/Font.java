package com.vabrant.actionsystem.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Keys;
import com.badlogic.gdx.utils.ObjectMap.Values;
import com.vabrant.actionsystem.Movable;

public class Font implements Movable{
	
	private boolean isCustomFont;
	private boolean useFixedWidth;
	private boolean centerWidth;
	private boolean customFixedWidth;
	private float x;
	private float y;
	private float yOffset;
	private float fixedWidth;
	private float scaleFactor = 1f;
	private float charGap;
	private float width;
	private float height;
	private int fixedWidthMaxChars;
	public final Array<FontGlyph> text = new Array<>();
	private final ObjectMap<Character, FontGlyph> glyphs = new ObjectMap<>();
	
	public Font(TextureAtlas atlas, CharSequence regions, boolean isCustomFont) {
		this.isCustomFont = isCustomFont;
		FontGlyph spaceGlyph = new FontGlyph(25, ' ');
		glyphs.put(' ', spaceGlyph);

		for(int i = 0, size = regions.length(); i < size; i++) {
			char glyphChar = regions.charAt(i);
			
			FontGlyph glyph = null;
			if(isCustomFont) {
				glyph = new FontGlyph(atlas.findRegion(glyphChar + "_B"), atlas.findRegion(glyphChar + "_T"), glyphChar);
			}
			else {
				glyph = new FontGlyph(atlas.findRegion(Character.toString(glyphChar)), glyphChar);
			}
			
			glyphs.put(glyphChar, glyph);
		}
	}
	
	public ObjectMap<Character, FontGlyph> getGlyphs(){
		return glyphs;
	}
	
	public void setScaleFactor(float scale) {
		this.scaleFactor = scale;
	}
	
	public void setCharGap(float charGap) {
		this.charGap = charGap;
	}

	public void setPosition(float x, float y) {
		setX(x);
		setY(y);
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
	
	public float getMaxHeight() {
		float maxHeight = 0;
		float height = 0;
		Values<FontGlyph> values = glyphs.values();
		FontGlyph glyph = values.next();
		
		while(values.hasNext()) {
			height = glyph.height;
			if(glyph.yOffset < 0) height += (glyph.yOffset * -1);
			if(height > maxHeight) maxHeight = height;
			
			glyph = values.next();
		}
		return maxHeight * scaleFactor;
	}
	
	public float getHeight() {
		return height * scaleFactor;
	}
	
	public float getWidth() {
		return (width + (charGap * (Math.abs(text.size - 1)))) * scaleFactor;
	}
	
	public void centerWidth(boolean center) {
		if(!useFixedWidth) return;
		centerWidth = center;
	}
	
	public float getFixedWidth() {
		if(customFixedWidth) {
			return fixedWidth * scaleFactor;
		}
		else {
			return (fixedWidth + (charGap * (fixedWidthMaxChars - 1))) * scaleFactor;
		}
	}
	
	public void setFixedWidth(float width) {
		customFixedWidth = true;
		fixedWidth = width;
	}
	
	public void setFixedWidth(CharSequence chars, int maxChars) {
		useFixedWidth = true;
		float maxWidth = 0;
		for(int i = 0, length = chars.length(); i < length; i++) {
			FontGlyph glyph = glyphs.get(chars.charAt(i));
			if(glyph == null) continue;
			if(glyph.width > maxWidth) {
				maxWidth = glyph.width;
			}
		}
		fixedWidth = maxWidth * maxChars;
		fixedWidthMaxChars = maxChars;
	}
	
	public void setText(String str) {
		if(text == null) return;
		
		float textWidth = 0;
		float glyphHeight = 0;
		
		text.clear();
		yOffset = 0;
		height = 0;
		
		for(int i = 0; i < str.length(); i++) {
			FontGlyph glyph = glyphs.get(str.charAt(i));
			
			if(glyph == null) continue;
			
			text.add(glyph);
			
			textWidth += glyph.width;
			
			//set the text height and the yOffset
			glyphHeight = glyph.height;
			if(glyph.yOffset != 0) {
				float glyphYOffset = glyph.yOffset;
				
				if(glyph.yOffset < 0) {
					glyphHeight = glyphHeight - (glyph.yOffset * -1);
					
					glyphYOffset *= -1;
					if(glyphYOffset > yOffset) {
						yOffset = glyphYOffset;
					}
				}
				else {
					glyphHeight = glyphHeight + glyph.yOffset;
				}
			}
			
			if(glyphHeight > height) {
				height = glyphHeight;
			}
		}
		
		height += yOffset;
		width = textWidth;
	}
	
//	public void setOrigin(Origin origin) {
//		Keys<Character> keys = glyphs.keys();
//		while(keys.hasNext()) {
//			glyphs.get(keys.next()).setOrigin(origin);
//		}
//	}
	
	public void setColor(Color color) {
		Keys<Character> keys = glyphs.keys();
		while(keys.hasNext()) {
			glyphs.get(keys.next()).setColor(color);
		}
	}
	
	public void draw(Batch batch, float screenXOffset, float screenYOffset) {
		float x = this.x;
		if(centerWidth) x += ((getFixedWidth() / 2) - (getWidth() / 2));

		for(int i = 0, length = text.size; i < length; i++) {
			FontGlyph glyph = text.get(i);
			
			if(!isCustomFont) {
				if(glyph.baseRegion != null) {
					batch.setColor(glyph.getColor());
					batch.draw(glyph.baseRegion, x + screenXOffset, y + (glyph.yOffset * scaleFactor) + screenYOffset, glyph.originX * scaleFactor, glyph.originY * scaleFactor, glyph.width * scaleFactor, glyph.height * scaleFactor, glyph.getScaleX(), glyph.getScaleY(), 0);
					batch.setColor(Color.WHITE);
				}
			}
			else {
				if(glyph.baseRegion != null) batch.draw(glyph.baseRegion, x + screenXOffset, y + screenYOffset, glyph.originX * scaleFactor, glyph.originY * scaleFactor, glyph.width * scaleFactor, glyph.height * scaleFactor, glyph.getScaleX(), glyph.getScaleY(), 0);
				if(glyph.topRegion != null) {
					batch.setColor(glyph.getColor());
					batch.draw(glyph.topRegion, x + (8 * scaleFactor) + screenXOffset, y + (16 * scaleFactor) + screenYOffset, glyph.topOriginX * scaleFactor, glyph.topOriginY * scaleFactor, glyph.topRegion.getRegionWidth() * scaleFactor, glyph.topRegion.getRegionHeight() * scaleFactor, glyph.getScaleX(), glyph.getScaleY(), 0);
					batch.setColor(Color.WHITE);
				}
			}
			
			x += (glyph.width + charGap) * scaleFactor;
		}
	}
	
	public void debugBaseLine(Color color, ShapeRenderer renderer, float screenXOffsest, float screenYOffset) {
		renderer.set(ShapeType.Filled);
		renderer.setColor(color);
		float x = this.x;
		if(centerWidth) x += ((getFixedWidth() / 2) - (getWidth() / 2));
	}

	public void debug(ShapeRenderer renderer, float screenXOffset, float screenYOffset) {
		renderer.set(ShapeType.Line);
		//draw the fixed width
		if(useFixedWidth) {
			float x = this.x;
			renderer.setColor(Color.BLACK);
			renderer.rect(x + screenXOffset, this.y + screenYOffset, getFixedWidth(), getHeight());
		}

		//draw the actual width 
		float x = this.x;
		if(centerWidth) x += ((getFixedWidth() / 2) - (getWidth() / 2));
		renderer.setColor(Color.RED);
		renderer.rect(x + screenXOffset, y - (yOffset * scaleFactor) + screenYOffset, getWidth(), getHeight());
	}


}
