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

/**
 * @author John Barton
 *
 */
public class ActionReference<T extends Action<T>> {
	
	private boolean throwException;
	private T reference;
	
	private CleanupListener<Action<?>> cleanupListener = new CleanupListener<Action<?>>() {
		@Override
		public void cleanup(Action<?> a) {
			reference = null;
		}
	};
	
	public void throwExceptions(boolean throwException) {
		this.throwException = throwException;
	}
	
	private void throwNullPointerException(String message) {
		if(!throwException) return;
		throw new NullPointerException(message);
	}

	public void setAction(T action) {
		if(action != null) {
			reference = action;
			action.addCleanupListener(cleanupListener);
		}
		else {
			throwNullPointerException("Action is null.");
		}
	}
	
	public T getAction() {
		return reference;
	}

	public void start() {
		if(reference != null) {
			reference.start();
		}
		else {
			throwNullPointerException("Reference is null.");
		}
	}
	
	public void restart() {
		if(reference != null) {
			reference.restart();
		}
		else {
			throwNullPointerException("Reference is null.");
		}
	}
	
	public void end() {
		if(reference != null) {
			reference.end();
		}
		else {
			throwNullPointerException("Reference is null.");
		}
	}
	
	public void kill() {
		if(reference != null) {
			reference.kill();
		}
		else {
			throwNullPointerException("Reference is null.");
		}
	}


}
