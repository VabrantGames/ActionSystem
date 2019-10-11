package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vabrant.actionsystem.ActionManager;
import com.vabrant.testbase.TestScreen;
import com.vabrant.testbase.TestSelectScreen;

public abstract class ActionSystemTestScreen extends TestScreen {
	
	public ActionSystemTestObject testObject;
	public ActionSystemTestObject[] testObjects;
	public ActionManager actionManager;
	
	public ActionSystemTestScreen(TestSelectScreen screen) {
		super(screen);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		actionManager = new ActionManager(5);
	}
	
	public void createTestObject() {
		testObject = new ActionSystemTestObject();
	}
	
	public void createTestObjects(int amount) {
		testObjects = new ActionSystemTestObject[amount];
		for(int i = 0; i < amount; i++) {
			testObjects[i] = new ActionSystemTestObject();
		}
	}
	
	@Override
	public void debug(ShapeRenderer renderer) {
		super.debug(renderer);
		if(testObject != null) testObject.draw(renderer);
		if(testObjects != null) {
			for(int i = 0; i < testObjects.length; i++) {
				testObjects[i].draw(renderer);
			}
		}
	}
	
	@Override
	public void update(float delta) {
		actionManager.update(delta);
	}
	
	@Override
	public void dispose() {
		super.dispose();
		actionManager.freeAll();
	}

}
