package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.audio.Music;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.actions.MusicAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class MusicActionTest extends ActionSystemTestScreen {
	
	private final float duration = 6.857f;
	private Music music;
	private final String musicPath = "testMusic.wav";
	private final String actionName = "Music";

	public MusicActionTest(ActionSystemTestSelector screen) {
		super(screen);
		application.assetManager.load(musicPath, Music.class);
		application.assetManager.finishLoading();
		music = application.assetManager.get(musicPath);
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Action action = ActionWatcher.get(actionName);
		if(action != null) {
			if(!action.isPaused()) {
				action.pause();
			}
			else {
				action.resume();
			}
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}
	
	public ActionAdapter<MusicAction> getListener() {
		return new ActionAdapter<MusicAction>() {
			@Override
			public void actionEnd(MusicAction a) {
				log(a.getName(), "Music Action Over");
			}
		};
	}

	@Override
	public void runTest() {
		MusicAction action = MusicAction.play(music, duration);
		action.setName(actionName);
		action.watchAction();
		actionManager.addAction(action);
	}

}
