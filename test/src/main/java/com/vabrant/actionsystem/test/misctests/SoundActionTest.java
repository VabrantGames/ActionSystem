package com.vabrant.actionsystem.test.misctests;

import com.badlogic.gdx.audio.Sound;
import com.vabrant.actionsystem.actions.SoundAction;
import com.vabrant.actionsystem.desktopaudio.SoundActionDesktopListener;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class SoundActionTest extends ActionSystemTestScreen {

	private final String soundPath = "moveSound.mp3";
	
	public SoundActionTest(TestSelectScreen screen) {
		super(screen);
		application.assetManager.load(soundPath, Sound.class);
		application.assetManager.finishLoading();
		SoundAction.init(new SoundActionDesktopListener());
	}

	@Override
	public void runTest() {
		actionManager.addAction(SoundAction.playSound(application.assetManager.get(soundPath)).setName("SoundAction"));
	}

}
