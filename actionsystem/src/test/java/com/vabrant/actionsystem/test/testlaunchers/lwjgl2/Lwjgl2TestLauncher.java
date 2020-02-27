package com.vabrant.actionsystem.test.testlaunchers.lwjgl2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class Lwjgl2TestLauncher {
	
	public static Class<? extends Lwjgl2Config> CONFIG_CLASS = null;
	
	public interface Lwjgl2Config {
		public LwjglApplicationConfiguration getConfig();
		public ApplicationListener getListener();
	}
	
	public static void main(String[] args) {
		Lwjgl2Config config = null;
		ApplicationListener listener = null;
		LwjglApplicationConfiguration appConfig = null;
		
		try {
			config = ClassReflection.newInstance(CONFIG_CLASS);
			
			appConfig = config.getConfig();
			
			if(appConfig == null) {
				appConfig = new LwjglApplicationConfiguration();
				appConfig.width = 960;
				appConfig.height = 640;
			}
			
			listener = config.getListener();
			
			if(listener == null) throw new NullPointerException("Listener is null.");
		}
		catch(Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
		
		new LwjglApplication(listener, appConfig);
	}

}
