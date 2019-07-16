package com.vabrant.actionsystem.test;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.utils.ObjectMap;
import com.vabrant.testbase.TestApplicationBase;
import com.vabrant.testbase.TestScreen;

public class ActionSystemTestApplication extends TestApplicationBase{
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 640;
		 new LwjglApplication(new ActionSystemTestApplication(), config);
	}
	
	static final int TEST_WIDTH = 480;
	static final int TEST_HEIGHT = 320;
	
	public enum ActionSystemTests {
		MOVE_TO_TEST,
	}
	
	private final ObjectMap<Class<?>, TestScreen> tests = new ObjectMap<>();
	
	@Override
	public void create() {
		super.create();
//		setScreen(new PauseConditionTest());
//		setScreen(new TestingPlayground());
		setScreen(new MoveByTest());
//		setScreen(new MoveToTest());
	}
	
	public void runTest() {
		
	}
	
	public void endTest() {
	}
	
	

}
