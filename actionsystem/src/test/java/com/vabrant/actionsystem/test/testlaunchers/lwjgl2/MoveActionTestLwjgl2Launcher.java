package com.vabrant.actionsystem.test.testlaunchers.lwjgl2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vabrant.actionsystem.test.tests.MoveActionTest;

public class MoveActionTestLwjgl2Launcher extends Lwjgl2TestLauncher{
	
	static {
		Lwjgl2TestLauncher.CONFIG_CLASS = Config.class;
	}
	
	public static class Config implements Lwjgl2Config {

		@Override
		public LwjglApplicationConfiguration getConfig() {
			return null;
		}

		@Override
		public ApplicationListener getListener() {
			return new MoveActionTest();
		}
		
	}

}
