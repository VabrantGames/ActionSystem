package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.audio.Music;
import com.vabrant.actionsystem.actions.Action;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.ActionWatcher;
import com.vabrant.actionsystem.actions.MusicAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class MusicActionTest extends ActionSystemTestScreen implements ActionListener<MusicAction>{
	
	private final float duration = 6.857f;
	private Music music;
	private final String musicPath = "testMusic.wav";
	private final String actionName = "Music";
	ActionWatcher watcher;

	public MusicActionTest(TestSelectScreen screen) {
		super(screen);
		application.assetManager.load(musicPath, Music.class);
		application.assetManager.finishLoading();
		music = application.assetManager.get(musicPath);
		watcher = new ActionWatcher();
	}
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Action action = watcher.getAction(actionName);
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

	@Override
	public void runTest() {
		MusicAction action = MusicAction.play(music, duration);
		action.setName(actionName);
		action.watchAction(watcher);
		actionManager.addAction(action);
	}

	@Override
	public void actionStart(MusicAction a) {
		
	}

	@Override
	public void actionEnd(MusicAction a) {
		System.out.println("Music Action over");
	}

	@Override
	public void actionKill(MusicAction a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionRestart(MusicAction a) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionComplete(MusicAction a) {
		// TODO Auto-generated method stub
		
	}

}
