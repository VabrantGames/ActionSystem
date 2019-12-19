package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.SoundAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class SoundActionTest extends ActionSystemTestScreen implements ActionListener<SoundAction>{

	private final float duration = 1.75f;
	private Sound sound;
	private final String soundPath = "simpleSawMono.mp3";
	
	public SoundActionTest(TestSelectScreen screen) {
		super(screen);
		application.assetManager.load(soundPath, Sound.class);
		application.assetManager.finishLoading();
		sound = application.assetManager.get(soundPath);
	}

	@Override
	public void runTest() {
		SoundAction action = SoundAction.play(sound, duration, 1f, 1, 0);
		actionManager.addAction(action);
	}

	@Override
	public void actionStart(SoundAction a) {
		System.out.println("SoundAction start");
	}

	@Override
	public void actionEnd(SoundAction a) {
		System.out.println("SoundAction end");
	}

	@Override
	public void actionKill(SoundAction a) {
	}

	@Override
	public void actionRestart(SoundAction a) {
	}

	@Override
	public void actionComplete(SoundAction a) {
	}

}
