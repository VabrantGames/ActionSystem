/**
 *	Copyright 2020 See AUTHORS file.
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.test.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.MusicAction;
import com.vabrant.actionsystem.actions.RepeatAction;

/**
 * @author John Barton
 *
 */
public class MusicActionTest extends ActionSystemTestListener {

	MusicAction action;
	Music music;
	
	@Override
	public void create() {
		super.create();
		
		music = Gdx.audio.newMusic(Gdx.files.internal("testMusic.wav"));
		
		action = MusicAction.play(music, 6.857f)
				.unmanage()
				.setLogLevel(ActionLogger.DEBUG);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				playTest();
//				music.setLooping(true);
//				music.play();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void playTest() {
		actionManager.addAction(RepeatAction.continuous(action));
	}

}
