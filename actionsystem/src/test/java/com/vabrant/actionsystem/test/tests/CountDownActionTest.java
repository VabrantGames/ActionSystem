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

import com.badlogic.gdx.Input.Keys;
import com.vabrant.actionsystem.actions.CountDownAction;
import com.vabrant.actionsystem.actions.CountDownAction.CountDownActionListener;

/**
 * @author John Barton
 *
 */
public class CountDownActionTest extends ActionSystemTestListener {

	CountDownAction action;
	
	public CountDownActionTest() {
		action = CountDownAction.obtain()
				.unmanage()
				.setDuration(5)
				.addCountDownListener(new CountDownActionListener() {
					@Override
					public void currentCount(int count) {
						System.out.println(count);
					}
				});
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				countDownTest();
				break;
			case Keys.NUMPAD_1:
				action.setDuration(7);
				break;
			}
		return super.keyDown(keycode);
	}
	
	public void countDownTest() {
		actionManager.addAction(action);
	}

}
