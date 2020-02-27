package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.audio.Sound;
import com.vabrant.actionsystem.actions.ActionAdapter;
import com.vabrant.actionsystem.actions.ActionListener;
import com.vabrant.actionsystem.actions.SoundAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.actionsystem.test.ActionSystemTestSelector;

public class SoundActionTest extends ActionSystemTestScreen {

	private final float duration = 1.75f;
	private Sound sound;
	private final String soundPath = "simpleSawMono.mp3";
	
	public SoundActionTest(ActionSystemTestSelector screen) {
		super(screen);
		application.assetManager.load(soundPath, Sound.class);
		application.assetManager.finishLoading();
		sound = application.assetManager.get(soundPath);
	}
	
	public ActionAdapter getListener() {
		return new ActionAdapter() {
		};
	}

	@Override
	public void runTest() {
		SoundAction action = SoundAction.play(sound, duration, 1f, 1, 0);
		actionManager.addAction(action);
	}

}
