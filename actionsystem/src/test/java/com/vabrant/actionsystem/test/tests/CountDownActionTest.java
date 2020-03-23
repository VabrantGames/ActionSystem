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
	
	public CountDownActionTest() {
	}
	
	@Override
	public boolean keyDown(int keycode) {
		switch(keycode) {
			case Keys.NUMPAD_0:
				countDownTest();
				break;
			}
		return super.keyDown(keycode);
	}
	
	public void countDownTest() {
		CountDownAction action = CountDownAction.countDown(5);
		action.addCountDownListener(new CountDownActionListener() {

			@Override
			public void currentCount(int count) {
				System.out.println(count);
			}
			
		});
		actionManager.addAction(action);
	}

}
