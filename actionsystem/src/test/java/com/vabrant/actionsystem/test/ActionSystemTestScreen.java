package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.vabrant.actionsystem.actions.ActionManager;
import com.vabrant.testbase.TestScreen;

public abstract class ActionSystemTestScreen extends TestScreen {
	
	public TestObject testObject;
	public TestObject[] testObjects;
	public ActionManager actionManager;
	
	public ActionSystemTestScreen(ActionSystemTestSelector screen) {
		super(screen, ActionSystemTestSelector.viewport);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		actionManager = new ActionManager(5);
	}
	
	public void createTestObject() {
		testObject = new TestObject();
	}
	
	public void createTestObjects(int amount) {
		testObjects = new TestObject[amount];
		for(int i = 0; i < amount; i++) {
			testObjects[i] = new TestObject();
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
