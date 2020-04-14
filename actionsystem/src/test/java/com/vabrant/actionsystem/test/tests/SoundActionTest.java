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
import com.badlogic.gdx.audio.Sound;
import com.vabrant.actionsystem.actions.ActionLogger;
import com.vabrant.actionsystem.actions.RepeatAction;
import com.vabrant.actionsystem.actions.SoundAction;

/**
 * @author jaylb
 *
 */
public class SoundActionTest extends ActionSystemTestListener {

	SoundAction action;
	Sound sound;
	
	@Override
	public void create() {
		super.create();

		sound = Gdx.audio.newSound(Gdx.files.internal("testMusic.wav"));
		
		action = SoundAction.obtain()
				.unmanage()
				.setPitch(1f)
				.setSound(sound, 6.857f);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				playTest();
				break;
			case Keys.NUMPAD_1:
				pitchTest();
				break;
			case Keys.NUMPAD_2:
				panTest();
				break;
			case Keys.NUMPAD_3:
				volumeTest();
				break;
		}
		return super.keyDown(keycode);
	}
	
	public void playTest() {
		action.setVolume(1f);
		actionManager.addAction(action);
	}
	
	public void pitchTest() {
		action.setPitch(0.5f);
		actionManager.addAction(action);
	}
	
	public void panTest() {
		action.setPan(1);
		actionManager.addAction(action);
	}
	
	public void volumeTest() {
		action.setVolume(0.2f);
		actionManager.addAction(action);
	}

}
