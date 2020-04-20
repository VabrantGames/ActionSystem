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
package com.vabrant.actionsystem.test.testlaunchers.lwjgl2;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.vabrant.actionsystem.test.tests.RestartTest;

/**
 * @author John Barton
 *
 */
public class RestartTestLwjgl2Launcher extends Lwjgl2TestLauncher {
	
	static {
		Lwjgl2TestLauncher.CONFIG_CLASS = Config.class;
	}
	
	public static class Config implements Lwjgl2Config {

		@Override
		public LwjglApplicationConfiguration getConfig() {
			return null;
		}

		@Override
		public ApplicationListener getListener() {
			return new RestartTest();
		}
		
	}

}
