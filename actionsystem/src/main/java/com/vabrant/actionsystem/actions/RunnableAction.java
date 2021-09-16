/**
 *	Copyright 2019 John Barton
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package com.vabrant.actionsystem.actions;

public class RunnableAction extends Action<RunnableAction>{

	public static RunnableAction runnable(Runnable runnable) {
		return obtain(RunnableAction.class)
				.set(runnable);
	}
	
	private Runnable runnable;
	
	public RunnableAction set(Runnable runnable) {
		this.runnable = runnable;
		return this;
	}
	
	public Runnable getRunnable() {
		return runnable;
	}
	
	@Override
	public boolean update(float delta) {
		if(!isRunning()) return false;
		if(isPaused()) return true;
		runnable.run();
		end();
		return isRunning();
	}
	
	@Override
	public void reset() {
		super.reset();
		runnable = null;
	}
	
}
