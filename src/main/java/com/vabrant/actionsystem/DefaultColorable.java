package com.vabrant.actionsystem;

import com.badlogic.gdx.graphics.Color;

public class DefaultColorable implements Colorable {

	private final Color color;
	
	public DefaultColorable() {
		this(Color.WHITE);
	}
	
	public DefaultColorable(Color color) {
		this.color = new Color(color);
	}
	
	@Override
	public void setColor(Color color) {
		this.color.set(color);
	}

	@Override
	public Color getColor() {
		return color;
	}

}
