package com.vabrant.actionsystem.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vabrant.testbase.TestBaseApplicationListener;

public class ActionSystemTestApplication extends TestBaseApplicationListener{
	
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
