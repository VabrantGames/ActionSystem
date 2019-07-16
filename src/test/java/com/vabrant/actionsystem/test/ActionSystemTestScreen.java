package com.vabrant.actionsystem.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.vabrant.actionsystem.ActionManager;
import com.vabrant.testbase.TestScreen;

public class ActionSystemTestScreen extends TestScreen implements InputProcessor{
	
	ActionManager actionManager;
	
	public ActionSystemTestScreen() {
		super(ActionSystemTestApplication.TEST_WIDTH, ActionSystemTestApplication.TEST_HEIGHT);
		actionManager = new ActionManager(5);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void update(float delta) {
		actionManager.update(delta);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
