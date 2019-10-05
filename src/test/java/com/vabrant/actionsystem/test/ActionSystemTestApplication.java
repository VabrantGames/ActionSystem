package com.vabrant.actionsystem.test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.vabrant.actionsystem.ActionLogger;
import com.vabrant.testbase.TestApplication;

public class ActionSystemTestApplication extends TestApplication{
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 640;
		config.resizable = false;
		 new LwjglApplication(new ActionSystemTestApplication(), config);
	}
	
	@Override
	public void create() {
		super.create();
		setScreen(new ActionSystemTestSelectScreen(this));
	}
	
}
