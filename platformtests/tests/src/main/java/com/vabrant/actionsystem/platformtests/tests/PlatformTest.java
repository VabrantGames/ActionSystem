
package com.vabrant.actionsystem.platformtests.tests;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionManager;

public class PlatformTest extends InputAdapter implements ApplicationListener {

	protected ActionManager actionManager;

	public void addAction (Action action) {
		actionManager.addAction(action);
	}

	@Override
	public void create () {
		actionManager = new ActionManager();
	}

	@Override
	public void resize (int width, int height) {
	}

	@Override
	public void render () {
		actionManager.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause () {
	}

	@Override
	public void resume () {
	}

	@Override
	public void dispose () {
	}
}
