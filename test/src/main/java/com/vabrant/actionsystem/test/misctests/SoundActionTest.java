package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.audio.Sound;
import com.vabrant.actionsystem.actions.SoundAction;
import com.vabrant.actionsystem.desktopaudio.SoundActionDesktopListener;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class SoundActionTest extends ActionSystemTestScreen {

	private Sound sound;
	private final String soundPath = "simpleSawMono.mp3";
	
	public SoundActionTest(TestSelectScreen screen) {
		super(screen);
		SoundAction.init(new SoundActionDesktopListener());
		application.assetManager.load(soundPath, Sound.class);
		application.assetManager.finishLoading();
		sound = application.assetManager.get(soundPath);
	}

	@Override
	public void runTest() {
//		actionManager.addAction(SoundAction.playSound(application.assetManager.get(soundPath)).setName("SoundAction"));
		actionManager.addAction(SoundAction.playSound(sound).setPan(-1f));
	}

}
