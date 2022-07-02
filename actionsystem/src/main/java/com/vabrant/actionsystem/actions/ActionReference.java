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

import com.vabrant.actionsystem.events.ActionEvent;
import com.vabrant.actionsystem.events.ActionListener;

/**
 * Holds a reference to an action
 * @author John Barton
 *
 */
public class ActionReference<T extends Action<T>> {
	
	private T reference;

	private ActionListener cleanupListener = new ActionListener() {
		@Override
		public void onEvent(ActionEvent e) {
			reference = null;
		}
	};
	
	public ActionReference() {}
	
	public ActionReference(T action) {
		if(action == null) throw new IllegalArgumentException();
		setAction(action);
	}

	/**
	 * Sets an action to be used as a reference. Returns the old action reference if set.
	 * @param action
	 * @return Old action
	 */
	public T setAction(T action) {
		T oldAction = null;
		
		//Remove the old action before a new one is set
		if (reference != null) {
			if(action == reference) return null;

			reference.unsubscribeFromEvent(ActionEvent.RESET_EVENT, cleanupListener);
			oldAction = reference;
			reference = null;
		}
		
		if(action == null) return oldAction;
		
		reference = action;
		action.subscribeToEvent(ActionEvent.RESET_EVENT, cleanupListener);

		return oldAction;
	}
	
	public T getAction() {
		return reference;
	}

	public void start() {
		reference.start();
	}
	
	public void restart() {
		reference.restart();
	}
	
	public void end() {
		reference.end();
	}
	
	public void kill() {
		reference.kill();
	}

}
