
package com.vabrant.actionsystem.events;

import com.badlogic.gdx.utils.Pool.Poolable;
import com.vabrant.actionsystem.actions.Action;

public class Event implements Poolable {

	private Action action;

	public void setAction (Action action) {
		this.action = action;
	}

	public Action getAction () {
		return action;
	}

	@Override
	public void reset () {
		action = null;
	}
}
